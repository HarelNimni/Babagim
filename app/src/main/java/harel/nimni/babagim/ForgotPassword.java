package harel.nimni.babagim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class ForgotPassword extends AppCompatActivity {
    private EditText phoneNumber;
    private EditText enterCode;
    private Button sendSms;
    private TextView moveToLogin;
    private TextView moveToSignUp;
    private ArrayList<User> usersData;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        setId();

        sendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSmsListener();
            }
        });
        moveToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this, Login.class);
                startActivity(intent);
            }
        });
        moveToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this, SignUp.class);
                startActivity(intent);
            }
        });
    }
    public boolean phoneIsExist(String phoneNumber, ArrayList<User> data)
    {
        for(int i=0; i< data.size(); i++)
        {
            if(phoneNumber.equals(data.get(i).getPhoneNumber()))
            {
                return true;
            }
        }
        return false;
    }
    public String sendCodeToSms()
    {
        String phone = phoneNumber.getText().toString();
        SmsManager smsManager = SmsManager.getDefault();
        Random rnd = new Random();
        String code = String.valueOf(rnd.nextInt(899999)+100000);
        smsManager.sendTextMessage(phone, null, "Your verification code is: "+code, null, null);
        Toast.makeText(this, "סמס עם קוד נשלח אלייך בהצלחה", Toast.LENGTH_LONG).show();
        return code;
    }
    public void checkCode()
    {
        String code = sendCodeToSms();
        enterCode.setVisibility(View.VISIBLE);
        sendSms.setText("הזן");
        sendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(enterCode.getText().toString().equals(code))
                {
                    Intent intent = new Intent(ForgotPassword.this, ChangePassword.class);
                    intent.putExtra("USER_PHONE", phoneNumber.getText().toString());
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(ForgotPassword.this, "קוד שגוי! נסה שוב!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100 && grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            checkCode();
        }
        else
        {
            Toast.makeText(ForgotPassword.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
        }
    }
    private void sendSmsListener()
    {
        Helpers.insertUsersData(usersData, new Helpers.CallBackUser() {
            @Override
            public void onSuccess(ArrayList<User> users) {
                if(phoneIsExist(phoneNumber.getText().toString(), usersData))
                {
                    if(ContextCompat.checkSelfPermission(ForgotPassword.this, Manifest.permission.SEND_SMS)
                            == PackageManager.PERMISSION_GRANTED)
                    {
                        checkCode();
                    }
                    else
                    {
                        ActivityCompat.requestPermissions(ForgotPassword.this, new String[]{Manifest.permission.SEND_SMS}, 100);
                    }
                }
                else
                {
                    Toast.makeText(ForgotPassword.this, "מספר הטלפון שלך לא קיים. הירשם או נסה שוב!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ForgotPassword.this, "connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setId()
    {
        phoneNumber = findViewById(R.id.enterPhone);
        enterCode = findViewById(R.id.enterCode);
        sendSms = findViewById(R.id.sendSMS);
        moveToLogin = findViewById(R.id.returnLogin);
        moveToSignUp = findViewById(R.id.returnSignUp);
        usersData = new ArrayList<>();
    }
}