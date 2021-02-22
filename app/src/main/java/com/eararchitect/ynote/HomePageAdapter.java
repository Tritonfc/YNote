package com.eararchitect.ynote;

// RecyclerView Adapter or HomePage MenuItems

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.PlaceViewHolder> {

    List<HomePageModel> items;
    private HomePageAdapter.onItemClickListener listener;

    public HomePageAdapter(List<HomePageModel> items) {

        this.items = items;


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {

        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_items, parent, false);
        PlaceViewHolder pvh = new PlaceViewHolder(view);

        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        holder.menuPic.setImageResource(items.get(position).menuPic);


        holder.menuText.setText(items.get(position).menuText);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView menuText;
        ImageView menuPic;


        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);


            menuPic = itemView.findViewById(R.id.menu_pic);
            menuText = itemView.findViewById(R.id.menu_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(position, menuPic);
                    }

                }
            });

        }
    }

    public interface onItemClickListener {
        void onItemClick(int position, ImageView v);

    }

    public void setOnItemClickListener(HomePageAdapter.onItemClickListener listener) {
        this.listener = listener;
    }
}




