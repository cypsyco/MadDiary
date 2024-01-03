package com.example.tab;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PhoneBookFragment extends Fragment {

    public PhoneBookFragment() {
        // Required empty public constructor
    }

    PhoneBookDB db;
    ArrayList<PhoneBook> phoneList = new ArrayList<>();
    RecyclerView recyclerView;
    PhoneBookAdapter adapter;
    TextView noDataText;
    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_phonebook, container, false);

        noDataText = v.findViewById(R.id.noData_text);
        recyclerView = v.findViewById(R.id.recyclerView);

        adapter = new PhoneBookAdapter(getContext(), PhoneBookFragment.class, 0, 0, 0);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = new PhoneBookDB(getContext());

        storeDataInArray();

        searchView = v.findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });

        FloatingActionButton addBtn = v.findViewById(R.id.add_btn);
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PhoneBookAddActivity.class);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    private void filterList(String text) {
        ArrayList<PhoneBook> filteredList = new ArrayList<>();

        for (PhoneBook phone : phoneList) {
            // You can modify the condition based on your search requirements
            if (phone.getPhone_name().toLowerCase().contains(text.toLowerCase())
                    || phone.getPhone_number().contains(text.toLowerCase())) {
                filteredList.add(phone);
            }
        }
        adapter.filterList(filteredList);
    }



    private void storeDataInArray() {

        Cursor cursor = db.readAllData();

        if(cursor.getCount() == 0){
            noDataText.setVisibility(noDataText.VISIBLE);
        }
        else{
            noDataText.setVisibility(noDataText.GONE);

            while(cursor.moveToNext()){
                PhoneBook phone = new PhoneBook(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getBlob(3));

                phoneList.add(phone);
                adapter.addItem(phone);

                adapter.notifyDataSetChanged();
            }
        }
    }

}