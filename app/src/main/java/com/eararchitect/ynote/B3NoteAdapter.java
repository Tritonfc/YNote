package com.eararchitect.ynote;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class B3NoteAdapter extends RecyclerView.Adapter<B3NoteAdapter.B3Holder> {
    private List<B3Note> notes = new ArrayList<>();
    private B3NoteAdapter.onItemClickListener listener;
    private B3NoteAdapter.onLongItemClickListener longListener;

    @NonNull
    @Override
    public B3Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.b3_note_items, parent, false);
        return new B3Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull B3Holder holder, int position) {

        B3Note current = notes.get(position);
        holder.noteName.setText(current.getTitle());
        holder.b3Duration.setText(current.getDuration());



            Picasso.get()
                    .load(Uri.parse(current.getImageUri()))
                    .into(holder.b3Image);



    }


    public void setNotes(List<B3Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class B3Holder extends RecyclerView.ViewHolder {

        private TextView noteName;
        private ImageView b3Image;
        private TextView b3Duration;

        public B3Holder(@NonNull View itemView) {
            super(itemView);
            noteName = itemView.findViewById(R.id.b3Name);
            b3Duration = itemView.findViewById(R.id.b3Duration);
            b3Image = itemView.findViewById(R.id.b3_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(notes.get(position));

                    }

                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(B3Note b3Note);
    }

    public void setOnItemClickListener(B3NoteAdapter.onItemClickListener listener) {
        this.listener = listener;
    }

    public interface onLongItemClickListener {
        void onLongItemClick(B3Note b3Note,ImageView b3Image);
    }

    public void setLongOnItemClickListener(B3NoteAdapter.onLongItemClickListener listener) {
        this.longListener = listener;
    }
}
