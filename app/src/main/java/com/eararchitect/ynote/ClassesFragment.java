package com.eararchitect.ynote;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class ClassesFragment extends Fragment {

    final ClassesAdapter adapts = new ClassesAdapter();
    private RecyclerView classesRecycler;
    private String searched;
    private ClassViewModel classViewModel;


    public ClassesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_classes, container, false);


        //Get textView from the parent activity (MainSearchActivity)
        TextView gol = (TextView) getActivity().findViewById(R.id.searched_text);
        //Get text from the textView and save it to string
        classesRecycler = view.findViewById(R.id.classes_recycler);

        classesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        classesRecycler.setHasFixedSize(true);

        classesRecycler.setAdapter(adapts);

        searched = (String) gol.getText();

        classViewModel = new ViewModelProvider(getActivity()).get(ClassViewModel.class);
        //Get classes using search Term from parent Activity
        classViewModel.getAllClassesFromSearch(searched).observe(getActivity(), new Observer<List<NewClass>>() {
            @Override
            public void onChanged(List<NewClass> newClasses) {
                adapts.setClasses(newClasses);
            }
        });

        adapts.setOnItemClickListener(new ClassesAdapter.onItemClickListener() {
            @Override
            public void onItemClick(NewClass newClass) {
                Intent viewClass = new Intent(getActivity(), ViewClassActivity.class);
                viewClass.putExtra(ViewClassActivity.EXTRA_TITLE, newClass.getClassName());
                viewClass.putExtra(ViewClassActivity.EXTRA_ID, newClass.getId());
                viewClass.putExtra(ViewClassActivity.EXTRA_B1, newClass.getB1Notes());
                viewClass.putExtra(ViewClassActivity.EXTRA_B2, newClass.getB2Notes());
                viewClass.putExtra(ViewClassActivity.EXTRA_B3, newClass.getB3Notes());
                startActivity(viewClass);
            }
        });

        return view;
    }
}