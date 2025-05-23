package harel.nimni.babagim;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class OrdersFragment extends Fragment {
    private ListView savedOrdersList;
    private ArrayList<String> ordersName = new ArrayList<>();
    private TextView emptyMessage;


    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ordersName.clear();
        if (HomeScreen.curUser.getSavedOrders() != null) {
            for (int i = 0; i < HomeScreen.curUser.getSavedOrders().size(); i++) {
                ordersName.add(HomeScreen.curUser.getSavedOrders().get(i).getOrderName());
            }
        }

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        savedOrdersList = view.findViewById(R.id.savedOrders);
        emptyMessage = view.findViewById(R.id.emptyPage);

        if (HomeScreen.curUser.getSavedOrders() != null) {
            emptyMessage.setVisibility(View.INVISIBLE);
            ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, ordersName);
            savedOrdersList.setAdapter(listAdapter);
        }


        // Inflate the layout for this fragment
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        savedOrdersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), AddOrder.class);
                intent.putExtra("selected_order", i);
                startActivity(intent);
            }
        });
    }
}