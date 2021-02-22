package com.eararchitect.ynote;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class PeopleAdapter extends FirestoreRecyclerAdapter<SearchModel, PeopleAdapter.PeopleHolder> {

    private PeopleAdapter.onItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PeopleAdapter(@NonNull FirestoreRecyclerOptions<SearchModel> options) {
        super(options);
    }

    @NonNull
    @Override
    public PeopleHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.people_items, viewGroup, false);


        return new PeopleHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull PeopleHolder peopleHolder, int i, @NonNull SearchModel searchModel) {
        peopleHolder.name.setText(searchModel.getUserName());

        Picasso.get()
                .load(Uri.parse(searchModel.getUserPic()))
                .into(peopleHolder.userPic);
    }


    class PeopleHolder extends RecyclerView.ViewHolder {
        ImageView userPic;


        TextView name;


        public PeopleHolder(@NonNull View itemView) {
            super(itemView);
            userPic = itemView.findViewById(R.id.people_image);
            name = itemView.findViewById(R.id.people_user);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }


                }
            });


        }
    }

    public interface onItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);


    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

}
