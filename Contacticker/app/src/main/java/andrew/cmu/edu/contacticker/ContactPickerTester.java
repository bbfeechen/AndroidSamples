package andrew.cmu.edu.contacticker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Author  : KAILIANG CHEN
 * Version : 0.1
 * Date    : 1/13/16
 */
public class ContactPickerTester extends Activity {

    public static final int PICK_CONTACT = 1;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    private Uri contactData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactpickertester);

        Button button = (Button) findViewById(R.id.pick_contact_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent((Intent.ACTION_PICK),
                    Uri.parse("content://picker/contacts/"));
                startActivityForResult(intent, PICK_CONTACT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                            checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                        Log.d("xxxxxx", "requestPermissions");
                        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                        //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                    }
                    else {
                        Log.d("xxxxxx", "not requestPermissions");
                        contactData = data.getData();
                        Cursor c = getContentResolver().query(contactData, null, null, null, null);
                        c.moveToFirst();
                        String name = c.getString(c.getColumnIndexOrThrow(
                                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
                        c.close();
                        TextView tv = (TextView) findViewById(R.id.selected_contact_textview);
                        tv.setText(name);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                Log.d("xxxxxx", "onRequestPermissionsResult");
                Cursor c = getContentResolver().query(contactData, null, null, null, null);
                c.moveToFirst();
                String name = c.getString(c.getColumnIndexOrThrow(
                        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
                c.close();
                TextView tv = (TextView) findViewById(R.id.selected_contact_textview);
                tv.setText(name);
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
