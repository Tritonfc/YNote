package com.eararchitect.ynote;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.ClassesHolder> {
    private List<NewClass> classes = new ArrayList<>();
    private ClassesAdapter.onItemClickListener listener;

    @NonNull
    @Override
    public ClassesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.classes_items, parent, false);
        return new ClassesHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassesHolder holder, int position) {
        NewClass current = classes.get(position);
        holder.className.setText(current.getClassName());
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    class ClassesHolder extends RecyclerView.ViewHolder {

        private TextView className;

        public ClassesHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.class_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(classes.get(position));

                    }

                }
            });
        }
    }

    public void setClasses(List<NewClass> classes) {
        this.classes = classes;
        notifyDataSetChanged();
    }


    public interface onItemClickListener {
        void onItemClick(NewClass newClass);
    }


    public void setOnItemClickListener(ClassesAdapter.onItemClickListener listener) {
        this.listener = listener;
    }


}
