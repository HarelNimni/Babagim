package harel.nimni.babagim;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AddOrder extends AppCompatActivity {
    private RecyclerView savedOrderList;
    private RecyclerCustomAdapterOrder adapter;
    private Button addToOrder;
    private TextView itemDetails;
    private Button closeBtn;
    private ArrayList<Item> curOrderItems = new ArrayList<>();
    private ArrayList<Integer> curOrderImages = new ArrayList<>();
    private Order curOrder = new Order();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_order);

        setId();
        initOrder();

        adapter = new RecyclerCustomAdapterOrder(curOrderItems, curOrderImages, new RecyclerCustomAdapterOrder.OnOrderClickListener() {
            @Override
            public void onItemClick(int position) {
                itemClickListener(position);
            }
        });
        savedOrderList.setAdapter(adapter);
        savedOrderList.setLayoutManager(new LinearLayoutManager(AddOrder.this, LinearLayoutManager.VERTICAL, false));

        addToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBtnListener();
            }
        });
    }
    private void setId()
    {
        savedOrderList = findViewById(R.id.savedOrderList);
        addToOrder = findViewById(R.id.addOrder);
    }
    private void initOrder()
    {
        curOrder = HomeScreen.curUser.getSavedOrder(getIntent().getIntExtra("selected_order", 0));
        for(int i=0; i<curOrder.getSize(); i++)
        {
            curOrderItems.add(curOrder.getItem(i));
            curOrderImages.add(curOrder.getImage(i));
        }
    }
    private void itemClickListener(int position)
    {
        Dialog dialog = new Dialog(AddOrder.this);
        dialog.setContentView(R.layout.item_details_dialog);
        dialog.setCancelable(true);
        dialog.show();

        closeBtn = dialog.findViewById(R.id.closeDialogBtn);
        itemDetails = dialog.findViewById(R.id.itemDetails);

        itemDetails.setText(curOrder.getItem(position).toString());
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
    private void addBtnListener()
    {
        for(int i=0; i<curOrder.getUserItems().size(); i++)
        {
            HomeScreen.order.addItem(curOrder.getItem(i));
            HomeScreen.order.addImage(curOrder.getImage(i));
        }
        Toast.makeText(AddOrder.this, "הזמנה נוספה", Toast.LENGTH_SHORT).show();
        finish();
    }
}