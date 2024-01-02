package com.example.tab;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Callendar_Address extends AppCompatActivity {

    PhoneBookDB db;
    ArrayList<PhoneBook> phoneList = new ArrayList<>();
    RecyclerView recyclerView;
    PhoneBookAdapter adapter;
    TextView noDataText;
    SearchView searchView;

    int year = 0;
    int month = 0;
    int dayOfMonth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callendar_address);

        noDataText = findViewById(R.id.noData_text);
        recyclerView = findViewById(R.id.recyclerView);

        //callendar intent
        Intent intent = getIntent();
        if (intent != null) {
            // 받은 데이터 처리
            year = intent.getIntExtra("year", 0);
            month = intent.getIntExtra("month", 0);
            dayOfMonth = intent.getIntExtra("dayOfMonth", 0);
        }

        adapter = new PhoneBookAdapter(this, Callendar_Address.class, year, month, dayOfMonth);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = new PhoneBookDB(this);

        storeDataInArray();

        searchView = findViewById(R.id.searchView);
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

        FloatingActionButton addBtn = findViewById(R.id.add_btn);
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Callendar_Address.this, AddActivity.class);
                startActivity(intent);
            }
        });
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