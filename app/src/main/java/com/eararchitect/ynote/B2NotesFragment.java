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


public class B2NotesFragment extends Fragment {

    final B2NoteAdapter b2NotesAdapter = new B2NoteAdapter();
    private String searched;
    private B2NotesViewModel b2NotesViewModel;
    private RecyclerView b2Recycler;

    public B2NotesFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_b2_notes, container, false);

        //Get textView from the parent activity (MainSearchActivity)
        TextView textView = (TextView) getActivity().findViewById(R.id.searched_text);
        //Get text from the textView and save it to string
        searched = (String) textView.getText();
        b2Recycler = view.findViewById(R.id.b2_recycler);
        b2Recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        b2Recycler.setHasFixedSize(true);


        b2Recycler.setAdapter(b2NotesAdapter);

        b2NotesViewModel = new ViewModelProvider(getActivity()).get(B2NotesViewModel.class);
        //Get b2 notes using search Term from parent Activity
        b2NotesViewModel.getAllB2NotesFromSearch(searched).observe(getActivity(), new Observer<List<B2Note>>() {
            @Override
            public void onChanged(List<B2Note> b2Notes) {
                b2NotesAdapter.setNotes(b2Notes);
            }
        });

        b2NotesAdapter.setOnItemClickListener(new B2NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(B2Note b2Note) {
                Intent viewClass = new Intent(getActivity(), B2NoteViewer.class);
                viewClass.putExtra(B2NoteViewer.EXTRA_ID, b2Note.getId());
                startActivity(viewClass);
            }
        });
        return view;
    }
}