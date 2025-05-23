package harel.nimni.babagim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Login extends AppCompatActivity {
    private TextView moveToSignUp;
    private TextView moveToForgotPassword;
    private Button login;
    private EditText phoneNumber;
    private EditText password;
    private ArrayList<User> usersData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setId();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginListener();
            }
        });
        moveToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });
        moveToForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }
    public int checkExistnace(ArrayList<User> data, String phoneNumber, String password) {
        for (int i = 0; i < data.size(); i++)
        {
            if (password.equals(data.get(i).getPassword()) && phoneNumber.equals(data.get(i).getPhoneNumber())) {
                return i;
            }
        }
        return -1;
    }
    private void setId()
    {
        moveToSignUp = findViewById(R.id.moveToSignup);
        phoneNumber = findViewById(R.id.enterPhoneNumber);
        password = findViewById(R.id.enterPassword);
        login = findViewById(R.id.loginButton);
        moveToForgotPassword = findViewById(R.id.moveToForgotPassword);
        usersData = new ArrayList<>();
    }
    private void loginListener()
    {
        Helpers.insertUsersData(usersData, new Helpers.CallBackUser() {
            @Override
            public void onSuccess(ArrayList<User> users) {
                int userIndex = checkExistnace(usersData, phoneNumber.getText().toString(), password.getText().toString());
                if (userIndex!=-1) {
                    Intent intent = new Intent(Login.this, HomeScreen.class);
                    intent.putExtra("Phone", usersData.get(userIndex).getPhoneNumber());
                    phoneNumber.setText("");
                    password.setText("");
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(Login.this, "number phone or password invalid", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
}