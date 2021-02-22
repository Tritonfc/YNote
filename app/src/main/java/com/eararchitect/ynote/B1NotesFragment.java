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

public class B1NotesFragment extends Fragment {

    private String searched;
    private B1NotesViewModel b1NotesViewModel;
    private RecyclerView b1Recycler;
    final B1NotesAdapter b1NotesAdapter = new B1NotesAdapter();


    public B1NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_b1_notes, container, false);


        //Get textView from the parent activity (MainSearchActivity)
        TextView textView = (TextView) getActivity().findViewById(R.id.searched_text);
        //Get text from the textView and save it to string
        searched = (String) textView.getText();

        b1Recycler = view.findViewById(R.id.b1_recycler);
        b1Recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        b1Recycler.setHasFixedSize(true);


        b1Recycler.setAdapter(b1NotesAdapter);

        b1NotesViewModel = new ViewModelProvider(getActivity()).get(B1NotesViewModel.class);
        //Get b1 notes using search Term from parent Activity
        b1NotesViewModel.getAllB1NotesFromSearch(searched).observe(getActivity(), new Observer<List<B1Note>>() {
            @Override
            public void onChanged(List<B1Note> b1Notes) {
                b1NotesAdapter.setNotes(b1Notes);
            }
        });

        b1NotesAdapter.setOnItemClickListener(new B1NotesAdapter.onItemClickListener() {
            @Override
            public void onItemClick(B1Note b1Note) {
                Intent viewClass = new Intent(getActivity(), B1NoteViewer.class);
                viewClass.putExtra(B1NoteViewer.EXTRA_ID, b1Note.getId());
                startActivity(viewClass);
            }
        });
        return view;
    }
}