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

public class B3NotesFragment extends Fragment {

    final B3NoteAdapter b3NotesAdapter = new B3NoteAdapter();
    private B3NotesViewModel b3NotesViewModel;
    private RecyclerView b3Recycler;
    private String searched;



    public B3NotesFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_b3_notes, container, false);


        //Get textView from the parent activity (MainSearchActivity)
        TextView textView = (TextView) getActivity().findViewById(R.id.searched_text);
        //Get text from the textView and save it to string
        searched = (String) textView.getText();

        b3Recycler = view.findViewById(R.id.b3_recycler);
        b3Recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        b3Recycler.setHasFixedSize(true);


        b3Recycler.setAdapter(b3NotesAdapter);

        b3NotesViewModel = new ViewModelProvider(getActivity()).get(B3NotesViewModel.class);
        //Get b3 notes using search Term from parent Activity
        b3NotesViewModel.getAllB3NotesFromSearch(searched).observe(getActivity(), new Observer<List<B3Note>>() {
            @Override
            public void onChanged(List<B3Note> b3Notes) {
                b3NotesAdapter.setNotes(b3Notes);
            }
        });

        b3NotesAdapter.setOnItemClickListener(new B3NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(B3Note b3Note) {
                Intent viewClass = new Intent(getActivity(), B3NoteViewer.class);
                viewClass.putExtra(B3NoteViewer.EXTRA_ID, b3Note.getId());
                startActivity(viewClass);
            }
        });
        return view;
    }
}