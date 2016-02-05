package ezremote.client.liveview;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.util.Log;

public class UnchunkedInputStream extends InputStream {
	private InputStream inputStream = null;
	private int remainOfThisChunk = 0;
	private boolean responseHeaderIsSkipped = false;
	private boolean bof = true;
	private byte[] readTmpBuf = null;
	ByteArrayOutputStream baos = null;
	private boolean eof = false;
	private static final String TAG = "Liveview-Unchunk";
	
	public UnchunkedInputStream(InputStream in) {
		inputStream = in;
		readTmpBuf = new byte[1];
		baos = new ByteArrayOutputStream();
	}

	public boolean isEof() {
		return eof;
	}
	
	@Override
	public int read() throws IOException {
		int readSize = read(readTmpBuf, 0, 1);
		if (readSize != 1) {
			Log.i(TAG, "read(, 0, 1) = " + String.valueOf(readSize));
			return -1;
		}
		return readTmpBuf[0];
	}

	public int read(byte[] b, int offset, int len) throws IOException {
		//String msgRead = "read called, len = " + String.valueOf(len);
		//Log.i(TAG, msgRead);
		if (eof == true) {
			return -1;
		}
		
		if (responseHeaderIsSkipped == false) {
			skipResponseHeader();
		}
		
		int o = offset;
		int remainLen = len;
		while (remainLen > 0) {
			if (remainOfThisChunk < 1) {
				goNextChunk();
				if (eof == true) {
					Log.i(TAG, "No more chunk.");
					return len - remainLen;
				}
			}
			int readSize;
			if (remainLen <= remainOfThisChunk) {
				readSize = inputStream.read(b, o, remainLen);
			} else {
				readSize = inputStream.read(b, o, remainOfThisChunk);
			}
			
			if(readSize < 1){
				String msgReadSize = "read() = " + String.valueOf(readSize);
				Log.i(TAG, msgReadSize);
				return len - remainLen;
			}
			remainLen -= readSize;
			remainOfThisChunk -= readSize;
			o += readSize;
		}
		
		return len;
	}

	public int available() throws IOException {
		if (eof == true) {
			return 0;
		}

		int avail = inputStream.available();
		if (responseHeaderIsSkipped == false & avail > 0) {
			skipResponseHeader();
			avail = inputStream.available();
		}
		if (remainOfThisChunk < 1 & avail > 0) {
			goNextChunk();
			avail =  inputStream.available();
		}

		return avail;
	}
	
	public void close() throws IOException {
		inputStream.close();
	}
	
	public boolean markSupported() {
		return false;
	}
	
	private void skipResponseHeader() throws IOException {
		Log.i(TAG, "Start skipping HTTP response header.");
		ByteArrayOutputStream headerOs = new ByteArrayOutputStream();
		int d1 = 0;
		int d2 = 0;
		int d3 = 0;
		int d4 = 0;
		int count = 0;
		while (! (d1 == '\r' && d2 == '\n' && d3 == '\r' && d4 == '\n') ){
			d1 = d2;
			d2 = d3;
			d3 = d4;
			d4 = inputStream.read();
			if (d4 < 0) {
				Log.i(TAG, "Skipping HTTP response header failed.");
				eof = true;
				return;
			}
			headerOs.write(d4);
			count++;
		}
		responseHeaderIsSkipped = true;

		Log.i(TAG, "End skipping " + String.valueOf(count) + " bytes.");

		byte headerByte[] = headerOs.toByteArray();
		ByteArrayInputStream headerIs = new ByteArrayInputStream(headerByte);
    	BufferedReader headerReader = new BufferedReader(new InputStreamReader(headerIs));
    	String headerStr = null;
		try {
			while ((headerStr = headerReader.readLine()) != null) {
				Log.i(TAG, headerStr);
			}
    	} catch (IOException e) {
    		e.printStackTrace();
    		Log.i(TAG, "Print HTTP response header, IOException");
    	}
		Log.i(TAG, "available = " + String.valueOf(inputStream.available()));
	}
	
	private void goNextChunk() throws IOException {
        if (!bof) {
            readCRLF();
        }
        bof = false;
        remainOfThisChunk = getChunkSize();
        if( remainOfThisChunk < 1) {
        	Log.i(TAG, "Detect end chunk.");
        	eof = true;

        	/*
        	Log.i(TAG, "Data after end chunk.");
        	BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        	String str = null;
    		try {
    			while ((str = reader.readLine()) != null) {
    				Log.i(TAG, str);
    			}
        	} catch (IOException e) {
        		e.printStackTrace();
        		Log.i(TAG, "Print data after end chunk, IOException");
        	}
        	*/
        }
	}

    private void readCRLF() throws IOException {
        int cr = inputStream.read();
        int lf = inputStream.read();
        if ((cr != '\r') || (lf != '\n')) {
        	Log.i(TAG, "readCRLF failed.");
            throw new IOException(
                "CRLF expected at end of chunk: " + cr + "/" + lf);
        }
    }

    private int getChunkSize()
    		throws IOException {
    	baos.reset();
    	
    	// States: 0=normal, 1=\r was scanned, 2=inside quoted string, -1=end
    	int state = 0;
    	while (state != -1) {
    		int b = inputStream.read();
    		if (b == -1) {
    			throw new IOException("chunked stream ended unexpectedly");
    		}
    		//String msgB = "getChunkSize read = " + String.valueOf(b);
    		//Log.i(TAG, msgB);
    		switch (state) {
    		case 0:
    			switch (b) {
    			case '\r':
    				state = 1;
    				break;
    			case '\"':
    				state = 2;
    				/* fall through */
    			default:
    				baos.write(b);
    			}
    			break;

    		case 1:
    			if (b == '\n') {
    				state = -1;
    			} else {
    				// this was not CRLF
    				throw new IOException("Protocol violation: Unexpected"
    						+ " single newline character in chunk size");
    			}
    			break;

    		case 2:
    			switch (b) {
    			case '\\':
    				b = inputStream.read();
    				baos.write(b);
    				break;
    			case '\"':
    				state = 0;
    				/* fall through */
    			default:
    				baos.write(b);
    			}
    			break;
    		default: throw new RuntimeException("assertion failed");
    		}
    	}

    	String dataString = baos.toString();

    	//skip chunk extension
    	int separator = dataString.indexOf(';');
    	dataString = (separator > 0)
    			? dataString.substring(0, separator).trim()
    					: dataString.trim();

    	int result;
    	try {
    		result = Integer.parseInt(dataString.trim(), 16);
    	} catch (NumberFormatException e) {
    		throw new IOException ("Bad chunk size: " + dataString);
    	}
    	return result;
    }
}
