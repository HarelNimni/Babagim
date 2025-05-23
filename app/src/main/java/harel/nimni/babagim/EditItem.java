package harel.nimni.babagim;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditItem extends AppCompatActivity {
    public interface ToppingsCallBack
    {
        void onSuccess(ArrayList<String> toppingsOptions);
        void onFailure(String errorMessage);
    }
    public interface ItemCallBack
    {
        void onSuccess(Item curItem);
        void onFailure(String errorMessage);
    }
    private ImageView itemImage;
    private LinearLayout toppingsButtons;
    private ArrayList<String> toppings = new ArrayList<>();
    private Switch moreMeatSwitch;
    private RadioGroup meatTypes;
    private TextView chooseMeatMessage;
    private Button addItemBtn;
    private Item curItem = new Item();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        setId();

        setToppingsData(toppings, new ToppingsCallBack() {
            @Override
            public void onSuccess(ArrayList<String> toppingsOptions) {
                for(int i=0; i<toppings.size(); i++)
                {
                    CheckBox top = new CheckBox(EditItem.this);
                    top.setText(toppings.get(i));
                    toppingsButtons.addView(top);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(EditItem.this, "connection error", Toast.LENGTH_SHORT).show();
            }
        });

        String itemName = getIntent().getStringExtra("itemName");
        findItemByName(curItem, itemName, new ItemCallBack() {
            @Override
            public void onSuccess(Item curItem) {
                itemImage.setImageResource(getIntent().getIntExtra("itemImage", 0));
                if(curItem.getType().equals("Falafel"))
                {
                    meatTypes.setVisibility(View.GONE);
                    moreMeatSwitch.setVisibility(View.GONE);
                    chooseMeatMessage.setVisibility(View.GONE);
                }
                else if (curItem.getType().equals("Grill"))
                {
                    meatTypes.setVisibility(View.GONE);
                    chooseMeatMessage.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(EditItem.this, "connection error", Toast.LENGTH_SHORT).show();
            }
        });


        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemBtnListener();
            }
        });
    }
    public static void setToppingsData(ArrayList<String> toppingsOptions, ToppingsCallBack toppingsCallBack)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Toppings");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                toppingsOptions.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    toppingsOptions.add(snapshot1.getValue(String.class));
                }
                toppingsCallBack.onSuccess(toppingsOptions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                toppingsCallBack.onFailure("שגיאה");
            }
        });
    }
    public static void findItemByName(Item curItem, String itemName, ItemCallBack callBack)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Items");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    if(snapshot1.getValue(Item.class).getName().equals(itemName))
                    {
                        curItem.setName(snapshot1.child("name").getValue(String.class));
                        curItem.setCountOfPepole(snapshot1.child("count").getValue(Integer.class));
                        curItem.setPrice(snapshot1.child("price").getValue(Integer.class));
                        curItem.setVeggie(snapshot1.child("isVeggie").getValue(Boolean.class));
                        curItem.setType(snapshot1.child("type").getValue(String.class));
                        curItem.setChicken(snapshot1.child("Chicken").getValue(Boolean.class));
                        curItem.setVeal(snapshot1.child("Veal").getValue(Boolean.class));
                        curItem.setHodu(snapshot1.child("Hodu").getValue(Boolean.class));
                        curItem.setChicken(snapshot1.child("Mix").getValue(Boolean.class));
                        curItem.setMoreMeat(snapshot1.child("moreMeat").getValue(Boolean.class));
                    }
                }
                callBack.onSuccess(curItem);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callBack.onFailure("error");
            }
        });
    }
    private void addItemBtnListener()
    {
        HomeScreen.order.addImage(getIntent().getIntExtra("itemImage", 0));
        if(moreMeatSwitch.isChecked())
        {
            curItem.addMeat();
        }
        for(int i=0; i<toppingsButtons.getChildCount(); i++)
        {
            if(((CheckBox) toppingsButtons.getChildAt(i)).isChecked())
            {
                curItem.addTopping(((CheckBox) toppingsButtons.getChildAt(i)).getText().toString());
            }
        }
        int chosenMeatKey = meatTypes.getCheckedRadioButtonId();
        RadioButton selectedMeat = findViewById(chosenMeatKey);
        if(chosenMeatKey != -1)
        {
            if (selectedMeat.getText().toString().contains("פרגית"))
            {
                curItem.setChicken(true);
                HomeScreen.order.addItem(curItem);
                Toast.makeText(EditItem.this, "המוצר נוסף לעגלה", Toast.LENGTH_SHORT).show();
                finish();
            }
            else if (selectedMeat.getText().toString().contains("עגל"))
            {
                curItem.addVeal();
                HomeScreen.order.addItem(curItem);
                Toast.makeText(EditItem.this, "המוצר נוסף לעגלה", Toast.LENGTH_SHORT).show();
                finish();
            }
            else if (selectedMeat.getText().toString().contains("הודו"))
            {
                curItem.addHodu();
                HomeScreen.order.addItem(curItem);
                Toast.makeText(EditItem.this, "המוצר נוסף לעגלה", Toast.LENGTH_SHORT).show();
                finish();
            }
            else if (selectedMeat.getText().toString().contains("מיקס"))
            {
                curItem.addMix();
                HomeScreen.order.addItem(curItem);
                Toast.makeText(EditItem.this, "המוצר נוסף לעגלה", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        else if(curItem.getType().equals("Falafel") || curItem.getType().equals("Grill"))
        {
            HomeScreen.order.addItem(curItem);
            Toast.makeText(EditItem.this, "המוצר נוסף לעגלה", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            Toast.makeText(EditItem.this, "בחר סוג בשר", Toast.LENGTH_LONG).show();
        }
    }
    private void setId()
    {
        itemImage = findViewById(R.id.currentItemPhoto);
        toppingsButtons = findViewById(R.id.toppingsList);
        meatTypes = findViewById(R.id.meatType);
        moreMeatSwitch = findViewById(R.id.moreMeat);
        chooseMeatMessage = findViewById(R.id.chooseMeatType);
        addItemBtn = findViewById(R.id.addBtn);
    }
}