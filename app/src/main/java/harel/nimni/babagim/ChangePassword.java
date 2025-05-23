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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChangePassword extends AppCompatActivity {
    private EditText newPassword;
    private EditText confirmedNewPassword;
    private Button setBtn;
    private TextView moveToLogin;
    private TextView moveToSignUp;
    private ArrayList<User> usersData;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        setId();

        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBtnListener();
            }
        });
        moveToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePassword.this, Login.class);
                startActivity(intent);
            }
        });
        moveToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePassword.this, SignUp.class);
                startActivity(intent);
            }
        });
    }
    public boolean checkPasswordExistnace (ArrayList<User> data, String password)
    {
        for(int i=0;i<data.size();i++)
        {
            if(password.equals(data.get(i).getPassword()))
            {
                return true;
            }
        }
        return false;
    }
    public void updatePassword()
    {
        String userPhone = getIntent().getStringExtra("USER_PHONE");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    if(snapshot.getRef().getKey().equals(userPhone))
                    {
                        User newUser = snapshot.getValue(User.class);
                        newUser.setPassword(newPassword.getText().toString());
                        snapshot.getRef().setValue(newUser);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("שגיאה: " + databaseError.getMessage());
            }
        });
    }
    private void setId()
    {
        newPassword = findViewById(R.id.newPassword);
        confirmedNewPassword = findViewById(R.id.confirmNewPassword);
        setBtn = findViewById(R.id.setPasswordBtn);
        moveToLogin = findViewById(R.id.returnLogin2);
        moveToSignUp = findViewById(R.id.returnSignUp2);
        usersData = new ArrayList<>();
    }
    private void setBtnListener()
    {
        if(newPassword.getText().toString().equals(confirmedNewPassword.getText().toString()))
        {
            if(newPassword.getText().toString().length()>=6)
            {
                Helpers.insertUsersData(usersData, new Helpers.CallBackUser() {
                    @Override
                    public void onSuccess(ArrayList<User> users) {
                        if(!checkPasswordExistnace(usersData, newPassword.getText().toString()))
                        {
                            updatePassword();
                            Toast.makeText(ChangePassword.this, "הסיסמה שונתה בהצלחה", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ChangePassword.this, Login.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(ChangePassword.this, "הסיסמה כבר קיימת במערכת", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {

                    }
                });
            }
            else
            {
                Toast.makeText(ChangePassword.this, "וודא שסיסמתך מכילה לפחות 6 תווים", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(ChangePassword.this, "הסיסמאות אינן תואמות!", Toast.LENGTH_LONG).show();
        }
    }
}