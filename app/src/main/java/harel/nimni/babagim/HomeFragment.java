package harel.nimni.babagim;

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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ArrayList<Item> shawarmaData = new ArrayList<>();
    private RecyclerView shawarmaList;
    private ArrayList<Item> falafelData = new ArrayList<>();
    private RecyclerView falafelList;
    private ArrayList<Item> grillData = new ArrayList<>();
    private RecyclerView grillList;
    private ArrayList<Item> dealsData = new ArrayList<>();
    private RecyclerView dealsList;
    private ArrayList<Item> sidesData = new ArrayList<>();
    private RecyclerView sidesList;
    private ArrayList<Item> drinksData = new ArrayList<>();
    private RecyclerView drinksList;
    private RecyclerCustomAdapterMenu RecyclerAdapter;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        shawarmaList = view.findViewById(R.id.shawarmaList);
        falafelList = view.findViewById(R.id.falafelList);
        grillList = view.findViewById(R.id.grillList);
        dealsList = view.findViewById(R.id.dealsList);
        sidesList = view.findViewById(R.id.sidesList);
        drinksList = view.findViewById(R.id.drinksList);


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createList(shawarmaData, FirebaseDatabase.getInstance().getReference("Items"), "Shawarma", new HomeScreen.CallBackItems() {
            @Override
            public void onSuccess(ArrayList<Item> items) {
                shawarmaListListener();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), "connection error", Toast.LENGTH_SHORT).show();
            }
        });
        createList(falafelData, FirebaseDatabase.getInstance().getReference("Items"), "Falafel", new HomeScreen.CallBackItems() {
            @Override
            public void onSuccess(ArrayList<Item> items) {
                falafelListListener();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), "connection error", Toast.LENGTH_SHORT).show();
            }
        });
        createList(grillData, FirebaseDatabase.getInstance().getReference("Items"), "Grill", new HomeScreen.CallBackItems() {
            @Override
            public void onSuccess(ArrayList<Item> items) {
                grillListListener();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), "connection error", Toast.LENGTH_SHORT).show();
            }
        });
        createList(dealsData, FirebaseDatabase.getInstance().getReference("Items"), "Deal", new HomeScreen.CallBackItems() {
            @Override
            public void onSuccess(ArrayList<Item> items) {
                dealsListListener();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), "connection error", Toast.LENGTH_SHORT).show();
            }
        });
        createList(sidesData, FirebaseDatabase.getInstance().getReference("Items"), "Side", new HomeScreen.CallBackItems() {
            @Override
            public void onSuccess(ArrayList<Item> items) {
                sidesListListener();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), "connection error", Toast.LENGTH_SHORT).show();
            }
        });
        createList(drinksData, FirebaseDatabase.getInstance().getReference("Items"), "Drink", new HomeScreen.CallBackItems() {
            @Override
            public void onSuccess(ArrayList<Item> items) {
                drinksListListener();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), "connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createList(ArrayList<Item> list, DatabaseReference ref, String type, HomeScreen.CallBackItems callBack) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.child("type").getValue(String.class).equals(type)) {
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

                        list.add(item);

                    }
                }
                callBack.onSuccess(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callBack.onFailure("error");
            }
        });
    }
    private void shawarmaListListener()
    {
        RecyclerAdapter = new RecyclerCustomAdapterMenu(shawarmaData, Helpers.menuImages, new RecyclerCustomAdapterMenu.OnMenuClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), EditItem.class);
                intent.putExtra("itemName", shawarmaData.get(position).getName());
                intent.putExtra("itemImage", Helpers.menuImages.get(Helpers.SHAWARMA_KEY).get(position));
                intent.putExtra("itemType", shawarmaData.get(position).getType());
                startActivity(intent);
            }
        });
        shawarmaList.setAdapter(RecyclerAdapter);
        shawarmaList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }
    private void falafelListListener()
    {
        RecyclerAdapter = new RecyclerCustomAdapterMenu(falafelData, Helpers.menuImages, new RecyclerCustomAdapterMenu.OnMenuClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), EditItem.class);
                intent.putExtra("itemName", falafelData.get(position).getName());
                intent.putExtra("itemImage", Helpers.menuImages.get(Helpers.FALAFEL_KEY).get(position));
                intent.putExtra("itemType", falafelData.get(position).getType());
                startActivity(intent);
            }
        });
        falafelList.setAdapter(RecyclerAdapter);
        falafelList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }
    private void grillListListener()
    {
        RecyclerAdapter = new RecyclerCustomAdapterMenu(grillData, Helpers.menuImages, new RecyclerCustomAdapterMenu.OnMenuClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), EditItem.class);
                intent.putExtra("itemName", grillData.get(position).getName());
                intent.putExtra("itemImage", Helpers.menuImages.get(Helpers.GRILL_KEY).get(position));
                intent.putExtra("itemType", grillData.get(position).getType());
                startActivity(intent);
            }
        });
        grillList.setAdapter(RecyclerAdapter);
        grillList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }
    private void dealsListListener()
    {
        RecyclerAdapter = new RecyclerCustomAdapterMenu(dealsData, Helpers.menuImages, new RecyclerCustomAdapterMenu.OnMenuClickListener() {
            @Override
            public void onItemClick(int position) {
                HomeScreen.order.addItem(dealsData.get(position));
                HomeScreen.order.addImage(Helpers.menuImages.get(Helpers.DEALS_KEY).get(position));
                Toast.makeText(getContext(), "המוצר נוסף לעגלה", Toast.LENGTH_SHORT).show();
                HomeScreen.payment.setText("לתשלום: "+HomeScreen.order.calcPrice());
            }
        });
        dealsList.setAdapter(RecyclerAdapter);
        dealsList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }
    private void sidesListListener()
    {
        RecyclerAdapter = new RecyclerCustomAdapterMenu(sidesData, Helpers.menuImages, new RecyclerCustomAdapterMenu.OnMenuClickListener() {
            @Override
            public void onItemClick(int position) {
                HomeScreen.order.addItem(sidesData.get(position));
                HomeScreen.order.addImage(Helpers.menuImages.get(Helpers.SIDES_KEY).get(position));
                Toast.makeText(getContext(), "המוצר נוסף לעגלה", Toast.LENGTH_SHORT).show();
                HomeScreen.payment.setText("לתשלום: "+HomeScreen.order.calcPrice());
            }
        });
        sidesList.setAdapter(RecyclerAdapter);
        sidesList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }
    private void drinksListListener()
    {
        RecyclerAdapter = new RecyclerCustomAdapterMenu(drinksData, Helpers.menuImages, new RecyclerCustomAdapterMenu.OnMenuClickListener() {
            @Override
            public void onItemClick(int position) {
                HomeScreen.order.addItem(drinksData.get(position));
                HomeScreen.order.addImage(Helpers.menuImages.get(Helpers.DRINKS_KEY).get(position));
                Toast.makeText(getContext(), "מוצר נוסף לעגלה", Toast.LENGTH_SHORT).show();
                HomeScreen.payment.setText("לתשלום: "+HomeScreen.order.calcPrice());
            }
        });
        drinksList.setAdapter(RecyclerAdapter);
        drinksList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }
}