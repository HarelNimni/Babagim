package harel.nimni.babagim;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerCustomAdapterOrder extends RecyclerView.Adapter<RecyclerCustomAdapterOrder.ViewHolder> {
    public interface OnOrderClickListener {
        void onItemClick(int position);
    }
    private ArrayList<Item> itemsData;
    private ArrayList<Integer> images;
    private OnOrderClickListener listener;

    public RecyclerCustomAdapterOrder(ArrayList<Item> itemsData) {
        this.itemsData = itemsData;
    }

    public RecyclerCustomAdapterOrder(ArrayList<Item> itemsData, ArrayList<Integer> images) {
        this.itemsData = itemsData;
        this.images = images;
    }

    public RecyclerCustomAdapterOrder(ArrayList<Item> itemsData, ArrayList<Integer> images, OnOrderClickListener listener) {
        this.itemsData = itemsData;
        this.images = images;
        this.listener = listener;
    }

    @NonNull
    public RecyclerCustomAdapterOrder.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.custom_design_list, parent, false);
        RecyclerCustomAdapterOrder.ViewHolder viewHolder = new RecyclerCustomAdapterOrder.ViewHolder(contactView);
        return viewHolder;
    }

    public void onBindViewHolder(@NonNull RecyclerCustomAdapterOrder.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(v -> listener.onItemClick(position));
        holder.itemImage.setImageResource(images.get(position));
        holder.itemName.setText(itemsData.get(position).getName());
        holder.price.setText(String.valueOf(itemsData.get(position).getPrice()));
        holder.countOfPepole.setText(String.valueOf(itemsData.get(position).getCountOfPepole()));
        if(itemsData.get(position).isVeggie())
        {
            holder.veganSign.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.veganSign.setVisibility(View.INVISIBLE);
        }
    }

    public int getItemCount() {
        return itemsData.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView itemImage;
        private TextView itemName;
        private TextView countOfPepole;
        private TextView price;
        private ImageView veganSign;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            countOfPepole = itemView.findViewById(R.id.countOfPepole);
            price = itemView.findViewById(R.id.price);
            veganSign = itemView.findViewById(R.id.vegan);
        }
    }
}
