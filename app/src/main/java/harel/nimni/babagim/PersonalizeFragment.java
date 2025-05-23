package harel.nimni.babagim;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PersonalizeFragment extends Fragment {

    private Spinner countOfPepoleInOrder;
    private String[] options = {"1", "2", "3", "4", "5", "6", "7", "8+"};
    private SeekBar pricePerPerson;
    private TextView chosenPrice;
    private ArrayList<Item> filteredItems = new ArrayList<>();
    private ArrayList<Integer> filteredImages = new ArrayList<>();
    private Button generateBtn;
    private CheckBox veganOption;
    private CheckBox sharingOption;
    private RecyclerView filteredList;
    private ArrayList<Item> menu = new ArrayList<>();

    public PersonalizeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personalize, container, false);

        countOfPepoleInOrder = view.findViewById(R.id.countOfPepoleInOrder);
        pricePerPerson = view.findViewById(R.id.pricePerPersonBar);
        chosenPrice = view.findViewById(R.id.chosenPrice);
        generateBtn = view.findViewById(R.id.generateBtn);
        veganOption = view.findViewById(R.id.veganOption);
        sharingOption = view.findViewById(R.id.sharingOption);
        filteredList = view.findViewById(R.id.filteredList);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, options);
        countOfPepoleInOrder.setAdapter(spinnerAdapter);

        // Inflate the layout for this fragment
        return view;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        pricePerPerson.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                chosenPrice.setText("מחיר לאדם: "+pricePerPerson.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateBtnListener();
            }
        });
    }
    private void generateBtnListener()
    {
        if(pricePerPerson.getProgress() > 0)
        {
            insertMenuData(menu, new HomeScreen.CallBackItems() {
                @Override
                public void onSuccess(ArrayList<Item> items) {
                    int chosenCount;
                    if(countOfPepoleInOrder.getSelectedItem().toString().equals("8+"))
                    {
                        chosenCount = 8;
                    }
                    else
                    {
                        chosenCount = Integer.parseInt(countOfPepoleInOrder.getSelectedItem().toString());
                    }
                    Boolean vegan = false;
                    Boolean sharing = false;
                    int price = pricePerPerson.getProgress();
                    if(veganOption.isChecked())
                    {
                        vegan = true;
                    }
                    if(sharingOption.isChecked())
                    {
                        sharing = true;
                    }

                    filterData(menu, filteredItems, filteredImages, vegan, sharing, price, chosenCount);
                    RecyclerCustomAdapterOrder adapter = new RecyclerCustomAdapterOrder(filteredItems, filteredImages, new RecyclerCustomAdapterOrder.OnOrderClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Intent intent;
                            if(filteredItems.get(position).getType().equals("Shawarma") || filteredItems.get(position).getType().equals("Falafel") || filteredItems.get(position).getType().equals("Grill"))
                            {
                                intent = new Intent(getContext(), EditItem.class);
                                intent.putExtra("itemName", filteredItems.get(position).getName());
                                intent.putExtra("itemImage", filteredImages.get(position));
                                intent.putExtra("itemType", filteredItems.get(position).getType());
                                startActivity(intent);
                            }
                            else
                            {
                                HomeScreen.order.addItem(filteredItems.get(position));
                                HomeScreen.order.addImage(filteredImages.get(position));
                                Toast.makeText(getContext(), "מוצר נוסף לעגלה", Toast.LENGTH_SHORT).show();
                                HomeScreen.payment.setText("לתשלום: "+HomeScreen.order.calcPrice());
                            }
                        }
                    });
                    filteredList.setAdapter(adapter);
                    filteredList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                }

                @Override
                public void onFailure(String errorMessage) {

                }
            });

        }
        else
        {
            Toast.makeText(getContext(), "הזן מחיר לאדם", Toast.LENGTH_SHORT).show();
        }
    }
    public void insertMenuData(ArrayList<Item> menu, HomeScreen.CallBackItems callBack)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Items");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menu.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    Item item = new Item();
                    item.setName(snapshot1.child("name").getValue(String.class));
                    item.setCountOfPepole(snapshot1.child("count").getValue(Integer.class));
                    item.setPrice(snapshot1.child("price").getValue(Integer.class));
                    item.setVeggie(snapshot1.child("isVeggie").getValue(Boolean.class));
                    item.setType(snapshot1.child("type").getValue(String.class));
                    item.setChicken(snapshot1.child("Chicken").getValue(Boolean.class));
                    item.setVeal(snapshot1.child("Veal").getValue(Boolean.class));
                    item.setHodu(snapshot1.child("Hodu").getValue(Boolean.class));
                    item.setMix(snapshot1.child("Mix").getValue(Boolean.class));
                    item.setMoreMeat(snapshot1.child("moreMeat").getValue(Boolean.class));

                    menu.add(item);
                }
                callBack.onSuccess(menu);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callBack.onFailure("error");
            }
        });
    }

    public void filterData(ArrayList<Item> menu, ArrayList<Item> items, ArrayList<Integer> images, Boolean vegan, Boolean sharing, int price, int countOfPepole)
    {
        items.clear();
        images.clear();
        for(int i=0; i< menu.size(); i++)
        {
            Item item = menu.get(i);
            if(sharing)
            {
                if(countOfPepole == 1)
                {
                    if(item.getCountOfPepole() == 1 && item.getPrice() <= price && (vegan == item.isVeggie() || !vegan))
                    {
                        items.add(item);
                        images.add(findImage(item.getType(), i));
                    }
                }
                else
                {
                    if(item.getCountOfPepole() > 1 && item.getCountOfPepole() <= countOfPepole)
                    {
                        if(item.getPrice()/item.getCountOfPepole() <= price && (vegan == item.isVeggie() || !vegan))
                        {
                            items.add(item);
                            images.add(findImage(item.getType(), i));
                        }
                    }
                }
            }
            else
            {
                if(item.getCountOfPepole() == 1 && item.getPrice() <= price && (vegan == item.isVeggie() || !vegan))
                {
                    items.add(item);
                    images.add(findImage(item.getType(), i));
                }
            }
        }
    }

    public int findImage(String type, int pos)
    {
        if(type.equals("Shawarma"))
        {
            return Helpers.menuImages.get(Helpers.SHAWARMA_KEY).get(pos);
        }
        else if(type.equals("Falafel"))
        {
            return Helpers.menuImages.get(Helpers.FALAFEL_KEY).get(pos-Helpers.menuImages.get(Helpers.SHAWARMA_KEY).size());
        }
        else if(type.equals("Grill"))
        {
            return Helpers.menuImages.get(Helpers.GRILL_KEY).get(pos-(Helpers.menuImages.get(Helpers.SHAWARMA_KEY).size()+Helpers.menuImages.get(Helpers.FALAFEL_KEY).size()));
        }
        else
        {
            return Helpers.menuImages.get(Helpers.DEALS_KEY).get(pos-(Helpers.menuImages.get(Helpers.SHAWARMA_KEY).size()+Helpers.menuImages.get(Helpers.FALAFEL_KEY).size()+Helpers.menuImages.get(Helpers.GRILL_KEY).size()));
        }
    }
}