package harel.nimni.babagim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {
    public interface CallBackItems
    {
        void onSuccess(ArrayList<Item> items);
        void onFailure(String errorMessage);
    }
    private TextView name;
    private TextView points;
    public static Button payment;
    private ImageButton tiktok;
    private ImageButton instegram;
    private ImageButton facebook;
    private ArrayList<User> usersData = new ArrayList<>();
    public static User curUser;
    public static Order order = new Order();
    private ViewPagerAdapter fragmentAdapter;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        setId();
        setFragments();

        payment.setText("לתשלום: "+order.calcPrice());
        String phone = getIntent().getStringExtra("Phone");

        Helpers.insertUsersData(usersData, new Helpers.CallBackUser() {
            @Override
            public void onSuccess(ArrayList<User> users) {
                curUser = findUserByPhone(phone, users);
                name.setText("ברוך הבא, " + curUser.getUsername());
                points.setText(""+curUser.getPoints());
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(order.calcPrice() != 0)
                {
                    Intent intent = new Intent(HomeScreen.this, PaymentScreen.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(HomeScreen.this, "ההזמנה שלך ריקה", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tiktok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tiktok.com/@babagim_modiin"));
                startActivity(intent);
            }
        });
        instegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/babagim_modiin/"));
                startActivity(intent);
            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/babajim.mamajim.modiin/?locale=he_IL"));
                startActivity(intent);
            }
        });
    }
    protected void onResume()
    {
        super.onResume();
        payment.setText("לתשלום: "+order.calcPrice());
    }
    public User findUserByPhone(String phone, ArrayList<User> usersData)
    {
        for(int i=0; i<usersData.size(); i++)
        {
            if(phone.equals(usersData.get(i).getPhoneNumber()))
            {
                return usersData.get(i);
            }
        }
        return null;
    }
    private void setId()
    {
        name = findViewById(R.id.helloMessage);
        points = findViewById(R.id.points);
        payment = findViewById(R.id.paymentBtn);
        tiktok = findViewById(R.id.tiktok);
        instegram = findViewById(R.id.instegram);
        facebook = findViewById(R.id.facebook);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.sideRuler);
        order = new Order();
    }
    private void setFragments()
    {

        fragmentAdapter = new ViewPagerAdapter(this);
        fragmentAdapter.addFragment(new HomeFragment());
        fragmentAdapter.addFragment(new PersonalizeFragment());
        fragmentAdapter.addFragment(new OrdersFragment());

        viewPager.setAdapter(fragmentAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position)
            {
                case 0:
                    tab.setText("בית");
                    tab.setIcon(R.drawable.home_ic);
                    break;
                case 1:
                    tab.setText("התאמה אישית");
                    tab.setIcon(R.drawable.ic_personalize);
                    break;
                case 2:
                    tab.setText("הזמנות שמורות");
                    tab.setIcon(R.drawable.ic_saved);
                    break;
            }
        }).attach();
    }
}