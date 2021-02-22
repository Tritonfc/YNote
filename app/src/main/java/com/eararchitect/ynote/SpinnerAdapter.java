package com.eararchitect.ynote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

//Adapter class for populating spinners with already created classes
public class SpinnerAdapter extends BaseAdapter {

    Context context;
    List<NewClass> classes;

    public SpinnerAdapter(Context applicationContext, List<NewClass> item) {
        this.context = applicationContext;
        this.classes = item;
    }

    @Override
    public int getCount() {
        return classes.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void setClasses(List<NewClass> classes) {
        this.classes = classes;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        NewClass model = classes.get(i);
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.spinner_items, null);
        TextView names = (TextView) view.findViewById(R.id.spin_text);
        names.setText(model.getClassName());

        return view;
    }
}
