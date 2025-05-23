package harel.nimni.babagim;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PaymentScreen extends AppCompatActivity {
    private SeekBar pointsSeekBar;
    private Button payBtn;
    private TextView maxValuePoints;
    private TextView redeemedPoints;
    private RecyclerView orderList;
    private RecyclerCustomAdapterOrder orderAdapter;
    public static int newPrice = HomeScreen.order.calcPrice();
    private Button closeBtn;
    private TextView itemDetails;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_screen);

        setId();
        payBtn.setText("שלם: " + HomeScreen.order.calcPrice());
        initList();
        initSeekBar();
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payBtnListener();
            }
        });
    }
    protected void onResume()
    {
        super.onResume();
        newPrice = HomeScreen.order.calcPrice();
    }
    private void initSeekBar()
    {
        if (HomeScreen.order.calcPrice() < HomeScreen.curUser.getPoints())
        {
            pointsSeekBar.setMax(HomeScreen.order.calcPrice());
        }
        else
        {
            pointsSeekBar.setMax(HomeScreen.curUser.getPoints());
        }

        maxValuePoints.setText(String.valueOf(pointsSeekBar.getMax()));


        pointsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                newPrice = HomeScreen.order.calcPrice()-pointsSeekBar.getProgress();
                payBtn.setText("שלם: " + newPrice);
                redeemedPoints.setText("נקודות למימוש: " + seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    private void initList()
    {
        orderAdapter = new RecyclerCustomAdapterOrder(HomeScreen.order.getUserItems(), HomeScreen.order.getOrderImages(), new RecyclerCustomAdapterOrder.OnOrderClickListener() {
            @Override
            public void onItemClick(int position) {
                orderAdapterListener(position);
            }
        });
        orderList.setAdapter(orderAdapter);
        orderList.setLayoutManager(new LinearLayoutManager(PaymentScreen.this, LinearLayoutManager.VERTICAL, false));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false; // We are not handling move actions
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Get the position of the swiped item
                int position = viewHolder.getAdapterPosition();

                // Remove item from dataset
                HomeScreen.order.removeItem(position);
                newPrice = HomeScreen.order.calcPrice()-pointsSeekBar.getProgress();
                payBtn.setText("שלם: "+newPrice);
                Toast.makeText(PaymentScreen.this, "מוצר נמחק", Toast.LENGTH_SHORT).show();

                // Notify adapter about item removal
                orderAdapter.notifyItemRemoved(position);
            }
        });
        itemTouchHelper.attachToRecyclerView(orderList);
    }
    private void payBtnListener()
    {
        if(HomeScreen.order.calcPrice() > 0)
        {
            HomeScreen.curUser.addPoints(newPrice/10);
            HomeScreen.curUser.removePoints(pointsSeekBar.getProgress());
            if(ContextCompat.checkSelfPermission(PaymentScreen.this, android.Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED)
            {
                //-- there is a limit of 160 digits per message --//
                SmsManager smsManager = SmsManager.getDefault();
                ArrayList<String> messageParts = smsManager.divideMessage(HomeScreen.order.toString());
                smsManager.sendMultipartTextMessage(HomeScreen.curUser.getPhoneNumber(), null, messageParts, null, null);
            }
            else
            {
                ActivityCompat.requestPermissions(PaymentScreen.this, new String[]{Manifest.permission.SEND_SMS}, 100);
            }
            FirebaseDatabase.getInstance().getReference("Users").child(HomeScreen.curUser.getPhoneNumber()).setValue(HomeScreen.curUser);
            Intent intent = new Intent(PaymentScreen.this, EndScreen.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(PaymentScreen.this, "ההזמנה שלך ריקה", Toast.LENGTH_SHORT).show();
        }
    }
    private void orderAdapterListener(int position)
    {
        Dialog dialog = new Dialog(PaymentScreen.this);
        dialog.setContentView(R.layout.item_details_dialog);
        dialog.setCancelable(true);
        dialog.show();

        closeBtn = dialog.findViewById(R.id.closeDialogBtn);
        itemDetails = dialog.findViewById(R.id.itemDetails);

        itemDetails.setText(HomeScreen.order.getUserItems().get(position).toString());
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
    private void setId()
    {
        pointsSeekBar = findViewById(R.id.seekBarPoints);
        payBtn = findViewById(R.id.payBtn);
        maxValuePoints = findViewById(R.id.maxValue);
        redeemedPoints = findViewById(R.id.redeemedPoints);
        orderList = findViewById(R.id.orderList);
    }
}