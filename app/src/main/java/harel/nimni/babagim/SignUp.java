package harel.nimni.babagim;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {
    private TextView moveToLogin;
    private EditText username;
    private EditText password;
    private EditText phoneNumber;
    private EditText confirmedPassword;
    private Button signUp;
    private ArrayList<User> usersData;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setId();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpListener();
            }
        });

        moveToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });
    }
    public boolean checkUserExistnace (ArrayList<User> data, String phone, String password)
    {
        for(int i=0;i<data.size();i++)
        {
            if(password.equals(data.get(i).getPassword()) || phone.equals(data.get(i).getPhoneNumber()))
            {
                return true;
            }
        }
        return false;
    }
    public boolean isValidPhone(String phoneNumber)
    {
        if(!phoneNumber.startsWith("05"))
        {
            return false;
        }
        if(!(phoneNumber.length() == 10))
        {
            return false;
        }
        //--check if contains only numbers--//
        if(!phoneNumber.matches("\\d+"))
        {
            return false;
        }
        return true;
    }
    public boolean checkUser(ArrayList<User> usersData, User user, String confirmedPassword)
    {
        if(!checkUserExistnace(usersData, user.getPhoneNumber(), user.getPassword()))
        {
            if (isValidPhone(user.getPhoneNumber()))
            {
                if(user.getPassword().length()>=6)
                {
                    if(!user.getPassword().contains(" "))
                    {
                        if(!user.getUsername().equals(""))
                        {
                            if(user.getPassword().equals(confirmedPassword))
                            {
                                return true;
                            }
                            else
                            {
                                Toast.makeText(SignUp.this, "הסיסמאות שלך אינן תואמות", Toast.LENGTH_LONG).show();
                                return false;
                            }
                        }
                        else
                        {
                            Toast.makeText(SignUp.this, "שם המשתמש שלך לא חוקי", Toast.LENGTH_LONG).show();
                            return false;
                        }
                    }
                    else
                    {
                        Toast.makeText(SignUp.this, "וודא שסיסמתך לא מכילה רווח", Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
                else
                {
                    Toast.makeText(SignUp.this, "וודא שסיסמתך מכילה לפחות 6 תווים", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
            else
            {
                Toast.makeText(SignUp.this, "מספר הטלפון שלך לא חוקי", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        else
        {
            Toast.makeText(SignUp.this, "הטלפון או הסיסמה כבר קיימים במערכת", Toast.LENGTH_LONG).show();
            return false;
        }
    }
    private void signUpListener()
    {
        Helpers.insertUsersData(usersData, new Helpers.CallBackUser() {
            @Override
            public void onSuccess(ArrayList<User> users) {
                if(checkUser(usersData, new User(username.getText().toString(), password.getText().toString(), phoneNumber.getText().toString()), confirmedPassword.getText().toString()))
                {
                    User user = new User(username.getText().toString(), password.getText().toString(), phoneNumber.getText().toString(), 0);
                    Toast.makeText(SignUp.this, "המשתמש נוצר בהצלחה!", Toast.LENGTH_LONG).show();

                    FirebaseDatabase.getInstance().getReference("Users").child(user.getPhoneNumber()).setValue(user);

                    Intent intent = new Intent(SignUp.this, HomeScreen.class);
                    intent.putExtra("Phone", user.getPhoneNumber());

                    phoneNumber.setText("");
                    username.setText("");
                    password.setText("");
                    confirmedPassword.setText("");

                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
    private void setId()
    {
        moveToLogin = findViewById(R.id.moveToLogin);
        username = findViewById(R.id.enterUsername);
        password = findViewById(R.id.enterPassword);
        phoneNumber = findViewById(R.id.phoneNumber);
        confirmedPassword = findViewById(R.id.confirmedPassword);
        signUp = findViewById(R.id.SignUpButton);
        usersData = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Users");
    }
}