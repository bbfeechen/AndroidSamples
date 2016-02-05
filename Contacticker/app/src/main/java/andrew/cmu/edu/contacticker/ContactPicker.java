package andrew.cmu.edu.contacticker;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ContactPicker extends Activity {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_picker);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("xxx", "requestPermissions");
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            Log.d("xxx", "not requestPermissions");
            final Cursor c = getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            String[] from = new String[]{ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};
            int[] to = new int[]{R.id.itemTextView};
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                    R.layout.listitemlayout, c, from, to);
            ListView lv = (ListView) findViewById(R.id.contactListView);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new ListView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    c.moveToPosition(position);
                    int rowId = c.getInt(c.getColumnIndexOrThrow("_id"));
                    Uri outURI = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                            rowId);
                    Intent outData = new Intent();
                    outData.setData(outURI);
                    setResult(Activity.RESULT_OK, outData);
                    finish();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                final Cursor c = getContentResolver().query(
                        ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                String[] from = new String[]{ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};
                int[] to = new int[]{R.id.itemTextView};
                Log.d("xxx", "onRequestPermissionsResult");
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                        R.layout.listitemlayout, c, from, to);
                ListView lv = (ListView) findViewById(R.id.contactListView);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        c.moveToPosition(position);
                        int rowId = c.getInt(c.getColumnIndexOrThrow("_id"));
                        Uri outURI = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                                rowId);
                        Intent outData = new Intent();
                        outData.setData(outURI);
                        setResult(Activity.RESULT_OK, outData);
                        finish();
                    }
                });
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
