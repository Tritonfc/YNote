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

public class AnnouncementAdapter extends FirestoreRecyclerAdapter<Announcement, AnnouncementAdapter.AnnouncementHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AnnouncementAdapter(@NonNull FirestoreRecyclerOptions<Announcement> options) {
        super(options);
    }

    @NonNull
    @Override
    public AnnouncementHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.announcement_items, viewGroup, false);


        return new AnnouncementHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull AnnouncementHolder announcementHolder, int i, @NonNull Announcement announcement) {
        announcementHolder.name.setText(announcement.getUserName());

        Picasso.get()
                .load(Uri.parse(announcement.getUserPic()))
                .into(announcementHolder.userPic);

        announcementHolder.announcement.setText(announcement.getAnnouncement());

    }

    class AnnouncementHolder extends RecyclerView.ViewHolder {
        ImageView userPic;
        TextView name;
        TextView announcement;


        public AnnouncementHolder(@NonNull View itemView) {
            super(itemView);
            userPic = itemView.findViewById(R.id.announcement_image);
            name = itemView.findViewById(R.id.people_user);
            announcement = itemView.findViewById(R.id.announcement);

        }
    }



}
