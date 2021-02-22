package com.eararchitect.ynote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends ArrayAdapter<SearchModel> {
    private Context context;
    private List<SearchModel> list;

    public SearchAdapter(Context context, List<SearchModel> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_items, parent, false);

        TextView searched = view.findViewById(R.id.people_user);
        CircularImageView userPic = view.findViewById(R.id.people_image);

        SearchModel model = getItem(position);
        searched.setText(model.getUserName());

        Picasso.get()
                .load(model.getUserPic())
                .into(userPic);


        return view;
    }
}
