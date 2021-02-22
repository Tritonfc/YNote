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

public class B2NoteAdapter extends RecyclerView.Adapter<B2NoteAdapter.B2Holder> {
    private List<B2Note> notes = new ArrayList<>();
    private B2NoteAdapter.onItemClickListener listener;
    private B2NoteAdapter.onLongItemClickListener longListener;
    @NonNull
    @Override
    public B2Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.b2_note_items, parent, false);
        return new B2Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull B2Holder holder, int position) {
        B2Note current = notes.get(position);
        holder.noteName.setText(current.getTitle());
        holder.b2Duration.setText(current.getDuration());

        Picasso.get()
                .load(Uri.parse(current.getImageUri()))
                .into(holder.b2Image);


    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<B2Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }


    class B2Holder extends RecyclerView.ViewHolder {

        private TextView noteName;
        private ImageView b2Image;
        private TextView b2Duration;

        public B2Holder(@NonNull View itemView) {
            super(itemView);
            noteName = itemView.findViewById(R.id.b2Name);
            b2Duration = itemView.findViewById(R.id.b2Duration);
            b2Image = itemView.findViewById(R.id.b2_image);
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
                        longListener.onLongItemClick(notes.get(position),b2Image);

                    }
                    return true;
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(B2Note b2Note);
    }

    public void setOnItemClickListener(B2NoteAdapter.onItemClickListener listener) {
        this.listener = listener;
    }

    public interface onLongItemClickListener {
        void onLongItemClick(B2Note b2Note,ImageView b2Image);
    }

    public void setLongOnItemClickListener(B2NoteAdapter.onLongItemClickListener listener) {
        this.longListener = listener;
    }


}
