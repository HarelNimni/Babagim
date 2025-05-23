package harel.nimni.babagim;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EndScreen extends AppCompatActivity {

    private TextView earnedPoints;
    private Button restartBtn;
    private Button shareBtn;
    private Button saveBtn;
    private EditText orderName;
    private Button verifyBtn;
    private ActivityResultLauncher<Intent> contactPickerLauncher;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_end_screen);

        setId();
        earnedPoints.setText("צברת: "+PaymentScreen.newPrice/10+" נקודות!");
        initActivityLauncher();

        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartBtnListener();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBtnListener();
            }
        });
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareBtnListener();
            }
        });
    }
    public boolean checkNameExistence(String name, ArrayList<Order> orders)
    {
        if(orders != null)
        {
            for(int i=0; i<orders.size(); i++)
            {
                if(orders.get(i).getOrderName().equals(name))
                {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private void initActivityLauncher() {
        contactPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri contactUri = data.getData();

                            // Define the projection to get the phone number
                            String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

                            // Query the contact's phone number
                            Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);

                            if (cursor != null && cursor.moveToFirst()) {
                                // Get the phone number from the cursor
                                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                                String phoneNumber = cursor.getString(numberIndex);
                                cursor.close();

                                // Your generated message
                                String message = "היי! הנה מה שהזמנתי מבאבאג'ים:\n" + HomeScreen.order.toString();

                                // Create SMS intent
                                Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                                smsIntent.setData(Uri.parse("smsto:" + phoneNumber));
                                smsIntent.putExtra("sms_body", message);
                                startActivity(smsIntent);
                            }

                        }
                    }
                });
    }
    private void saveBtnListener()
    {
        Dialog dialog = new Dialog(EndScreen.this);
        dialog.setContentView(R.layout.save_order_dialog);
        dialog.setCancelable(true);
        dialog.show();

        orderName = dialog.findViewById(R.id.orderName);
        verifyBtn = dialog.findViewById(R.id.verifyBtn);

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!orderName.getText().toString().equals(""))
                {
                    if(!checkNameExistence(orderName.getText().toString(), HomeScreen.curUser.getSavedOrders()))
                    {
                        HomeScreen.order.setOrderName(orderName.getText().toString());
                        HomeScreen.curUser.addOrder(HomeScreen.order);
                        FirebaseDatabase.getInstance().getReference("Users").child(HomeScreen.curUser.getPhoneNumber()).setValue(HomeScreen.curUser);
                        dialog.cancel();
                        saveBtn.setEnabled(false);
                        Toast.makeText(EndScreen.this, "נשמר!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(EndScreen.this, "כבר שמרת הזמנה על שם זה", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(EndScreen.this, "הכנס שם!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void shareBtnListener()
    {
        if(ContextCompat.checkSelfPermission(EndScreen.this, android.Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED)
        {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            contactPickerLauncher.launch(intent);
        }
        else
        {
            ActivityCompat.requestPermissions(EndScreen.this, new String[]{Manifest.permission.SEND_SMS}, 100);
        }
    }
    private void restartBtnListener()
    {
        Intent intent = new Intent(EndScreen.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);   //prevents returning to previous page
        startActivity(intent);
    }
    private void setId()
    {
        earnedPoints = findViewById(R.id.earnedPoints);
        restartBtn = findViewById(R.id.restartBtn);
        shareBtn = findViewById(R.id.shareBtn);
        saveBtn = findViewById(R.id.saveBtn);
    }
}