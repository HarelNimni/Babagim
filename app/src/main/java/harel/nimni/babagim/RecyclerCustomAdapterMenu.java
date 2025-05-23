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

public class RecyclerCustomAdapterMenu extends RecyclerView.Adapter<RecyclerCustomAdapterMenu.ViewHolder>
{
    public interface OnMenuClickListener {
        void onItemClick(int position);
    }

    private ArrayList<Item> itemsData;
    private OnMenuClickListener listener;
    private ArrayList<ArrayList<Integer>> images;

    public RecyclerCustomAdapterMenu(ArrayList<Item> itemsData) {
        this.itemsData = itemsData;
    }
    public RecyclerCustomAdapterMenu(ArrayList<Item> itemsData, OnMenuClickListener listener) {
        this.itemsData = itemsData;
        this.listener = listener;
    }

    public RecyclerCustomAdapterMenu(ArrayList<Item> itemsData, ArrayList<ArrayList<Integer>> images) {
        this.itemsData = itemsData;
        this.images = images;
    }

    public RecyclerCustomAdapterMenu(ArrayList<Item> itemsData, ArrayList<ArrayList<Integer>> images, OnMenuClickListener listener) {
        this.itemsData = itemsData;
        this.listener = listener;
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.custom_design_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(v -> listener.onItemClick(position));
        if(itemsData.get(position).getType().equals("Shawarma"))
        {
            holder.itemImage.setImageResource(images.get(Helpers.SHAWARMA_KEY).get(position));
        }
        else if(itemsData.get(position).getType().equals("Falafel"))
        {
            holder.itemImage.setImageResource(images.get(Helpers.FALAFEL_KEY).get(position));
        }
        else if(itemsData.get(position).getType().equals("Grill"))
        {
            holder.itemImage.setImageResource(images.get(Helpers.GRILL_KEY).get(position));
        }
        else if(itemsData.get(position).getType().equals("Deal"))
        {
            holder.itemImage.setImageResource(images.get(Helpers.DEALS_KEY).get(position));
        }
        else if(itemsData.get(position).getType().equals("Side"))
        {
            holder.itemImage.setImageResource(images.get(Helpers.SIDES_KEY).get(position));
        }
        else if(itemsData.get(position).getType().equals("Drink"))
        {
            holder.itemImage.setImageResource(images.get(Helpers.DRINKS_KEY).get(position));
        }
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

    @Override
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