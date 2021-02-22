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

import java.util.ArrayList;
import java.util.List;

public class B1NotesAdapter extends RecyclerView.Adapter<B1NotesAdapter.B1Holder> {
    private List<B1Note> notes = new ArrayList<>();
    private B1NotesAdapter.onItemClickListener listener;
    private B1NotesAdapter.onLongItemClickListener longListener;

    @NonNull
    @Override
    public B1Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.b1_note_items, parent, false);
        return new B1Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull B1Holder holder, int position) {
        B1Note current = notes.get(position);
        holder.noteName.setText(current.getTitle());
        holder.b1Duration.setText(current.getDuration());
        Picasso.get()
                .load(Uri.parse(current.getImageUris().get(0)))
                .into(holder.b1Image);

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<B1Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }


    class B1Holder extends RecyclerView.ViewHolder {

        private TextView noteName;
        private ImageView b1Image;
        private TextView b1Duration;

        public B1Holder(@NonNull View itemView) {
            super(itemView);
            noteName = itemView.findViewById(R.id.b1Name);
            b1Duration = itemView.findViewById(R.id.b1Duration);
            b1Image = itemView.findViewById(R.id.b1_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(notes.get(position));

                    }

                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION && longListener != null) {
                        longListener.onLongItemClick(notes.get(position),b1Image);

                    }
                    return true;
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(B1Note b1Note);
    }

    public void setOnItemClickListener(B1NotesAdapter.onItemClickListener listener) {
        this.listener = listener;
    }

    public interface onLongItemClickListener {
        void onLongItemClick(B1Note b1Note,ImageView b1Image);
    }

    public void setLongOnItemClickListener(B1NotesAdapter.onLongItemClickListener listener) {
        this.longListener = listener;
    }
}
