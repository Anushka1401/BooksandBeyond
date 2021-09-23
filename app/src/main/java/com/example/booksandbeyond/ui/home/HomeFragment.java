package com.example.booksandbeyond.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booksandbeyond.BookAdapter;
import com.example.booksandbeyond.BookItem;
import com.example.booksandbeyond.MainActivity;
import com.example.booksandbeyond.R;
import com.example.booksandbeyond.ReadingStory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ArrayList<BookItem> bookItems = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new BookAdapter(bookItems, getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        bookItems.add(new BookItem(R.drawable.beginner, "The Big Decision","0","0"));
        bookItems.add(new BookItem(R.drawable.intermediate, "House on Maple Street","1","0"));
        bookItems.add(new BookItem(R.drawable.advanced, "Brush with History","2","0"));

        return root;
    }
}