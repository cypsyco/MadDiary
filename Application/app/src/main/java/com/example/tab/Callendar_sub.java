package com.example.tab;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Callendar_sub extends AppCompatActivity {
    public String readDay = null;
    public String readDay2 = null;
    public String str = null;
    public String str2 = null;
    public Button cha_Btn, del_Btn, save_Btn, image_add, address_add;
    public TextView diaryTextView, textView, textView2, image_text, address_text;
    public EditText contextEditText, contextEditText2;

    RecyclerView recyclerView;
    PhoneBookAdapter adapter;
    PhoneBookDB db;
    ArrayList<PhoneBook> phoneList = new ArrayList<>();

    public ImageView imageView;
    String prev_class = "kkk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callendar_sub);
        Intent intent = getIntent();
        int year = intent.getIntExtra("year", 0);
        int month = intent.getIntExtra("month", 0);
        int dayOfMonth = intent.getIntExtra("dayOfMonth", 0);
        if(intent.hasExtra("prev_class")) {
            prev_class = intent.getStringExtra("prev_class");
        } else {
            prev_class = "DefaultPrevClass";
        }

        imageView = findViewById(R.id.imageView2);
        diaryTextView = findViewById(R.id.diaryTextView);
        save_Btn = findViewById(R.id.save_Btn);
        del_Btn = findViewById(R.id.del_Btn);
        cha_Btn = findViewById(R.id.cha_Btn);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        contextEditText = findViewById(R.id.contextEditText);
        contextEditText2 = findViewById(R.id.contextEditText2);
        image_add = findViewById(R.id.image_add);
        address_add = findViewById(R.id.address_add);
        image_text = findViewById(R.id.image_text);
        address_text = findViewById(R.id.address_text);
        diaryTextView.setVisibility(View.VISIBLE);
        save_Btn.setVisibility(View.VISIBLE);
        contextEditText.setVisibility(View.INVISIBLE);
        contextEditText2.setVisibility(View.VISIBLE);
        textView.setVisibility(View.INVISIBLE);
        textView2.setVisibility(View.VISIBLE);
        cha_Btn.setVisibility(View.INVISIBLE);
        del_Btn.setVisibility(View.INVISIBLE);
        address_add.setVisibility(View.INVISIBLE);
        image_add.setVisibility(View.VISIBLE);
        address_text.setVisibility(View.INVISIBLE);
        image_text.setVisibility(View.VISIBLE);
        diaryTextView.setText(String.format("%d / %d / %d", year, month + 1, dayOfMonth));
        contextEditText.setText("");
        contextEditText2.setText("");

        checkDay(year, month, dayOfMonth);
        checkDay2(year, month, dayOfMonth);

        if(prev_class.equals("gallery")){
            byte[] arr = getIntent().getByteArrayExtra("bm");
            Bitmap bm = android.graphics.BitmapFactory.decodeByteArray(arr, 0, arr.length);
            imageView.setImageBitmap(bm);
            saveImage(readDay, bm);
            address_add.setVisibility(View.VISIBLE);
            address_text.setVisibility(View.VISIBLE);
        }

        image_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Callendar_sub.this, Callendar_Gallery.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("dayOfMonth", dayOfMonth);
                startActivity(intent);
            }
        });

        save_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDiary2(readDay2);

                textView2.setText("");

                // Save text data
                str2 = contextEditText2.getText().toString();
                textView2.setText(str2);

                // Save image data
                save_Btn.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.VISIBLE);
                del_Btn.setVisibility(View.VISIBLE);
                contextEditText2.setVisibility(View.INVISIBLE);
            }
        });

        cha_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_Btn.setVisibility(View.VISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                contextEditText2.setVisibility(View.VISIBLE);
                contextEditText2.setText(str2);
                textView2.setText(contextEditText2.getText());
            }
        });

        del_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contextEditText2.setText("");
                contextEditText2.setVisibility(View.VISIBLE);

                save_Btn.setVisibility(View.VISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);

                removeDiary2(readDay2);
            }
        });


        address_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AddressFragment.class activity here
                Intent intent = new Intent(Callendar_sub.this, Callendar_Address.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("dayOfMonth", dayOfMonth);
                startActivity(intent);
            }
        });

        String name = "";

        if (intent.hasExtra("id")) {
            int id = intent.getIntExtra("id", 0);
        }
        if (intent.hasExtra("name")) {
            name = intent.getStringExtra("name");
            contextEditText.setText(textView.getText() + name + ",");
            saveDiary(readDay);
            address_add.setVisibility(View.VISIBLE);
            address_text.setVisibility(View.VISIBLE);
        }
        if (intent.hasExtra("phone_number")) {
            String phoneNumber = intent.getStringExtra("phone_number");
        }
        if (intent.hasExtra("image")) {
        }

        //태그된 친구들 RecyclerView
        recyclerView = findViewById(R.id.recyclerView);

        adapter = new PhoneBookAdapter(this, Callendar_sub.class, year, month, dayOfMonth);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = new PhoneBookDB(this);

        CharSequence charSequence = textView.getText();
        String stringValue = charSequence.toString();
        List<String> stringList = createListFromString(stringValue);

        for (String element : stringList) {
            storeDataInArray(element);
        }
        storeDataInArray(name);
    }

    private void storeDataInArray(String targetName) {
        Cursor cursor = db.readDataByName(targetName);

        if (cursor.getCount() == 0) {
        } else {
            while (cursor.moveToNext()) {
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

    public static List<String> createListFromString(String inputString) {
        // 쉼표(,)를 기준으로 문자열을 분할하여 리스트 생성
        String[] stringArray = inputString.split(",");

        // 배열을 리스트로 변환
        List<String> stringList = Arrays.asList(stringArray);

        return stringList;
    }

    public void checkDay(int cYear, int cMonth, int cDay)
    {
        readDay = "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt";
        FileInputStream fis;
        FileInputStream imageFis;

        try
        {
            imageFis = openFileInput(readDay + "_image.png");
            byte[] fileData1 = new byte[imageFis.available()];
            imageFis.read(fileData1);
            Bitmap loadedBitmap = BitmapFactory.decodeByteArray(fileData1, 0, fileData1.length);
            imageView.setImageBitmap(loadedBitmap);
            imageFis.close();

            //이미지 불러온 상태에서는 친구추가 버튼 활성화
            if (imageView.getDrawable() != null) {
                address_add.setVisibility(View.VISIBLE);
                address_text.setVisibility(View.VISIBLE);
            }

            fis = openFileInput(readDay);

            byte[] fileData = new byte[fis.available()];
            fis.read(fileData);
            fis.close();

            str = new String(fileData);
            textView.setText(str);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public void saveDiary(String readDay) {
        FileOutputStream fos;
        try {
            fos = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS);

            // Save text content
            String content = contextEditText.getText().toString();
            fos.write(content.getBytes());


            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void saveImage(String fileName, Bitmap bitmap) {
        try {
            FileOutputStream fos = openFileOutput(fileName + "_image.png", MODE_PRIVATE);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            fos.write(byteArray);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //텍스트를 위한 저장 메소드
    public void checkDay2(int cYear, int cMonth, int cDay)
    {
        readDay2 = "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + "text" + ".txt";
        FileInputStream fis2;
        FileInputStream imageFis2;

        try
        {
            fis2 = openFileInput(readDay2);

            byte[] fileData = new byte[fis2.available()];
            fis2.read(fileData);
            fis2.close();

            str2 = new String(fileData);

            contextEditText2.setVisibility(View.INVISIBLE);
            textView2.setText(str2);

            save_Btn.setVisibility(View.INVISIBLE);
            cha_Btn.setVisibility(View.VISIBLE);
            del_Btn.setVisibility(View.VISIBLE);

            cha_Btn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    contextEditText2.setVisibility(View.VISIBLE);
                    contextEditText2.setText(str2);

                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    textView2.setText(contextEditText2.getText());
                    contextEditText2.setText(str2);
                    textView2.setText(contextEditText2.getText());
                }

            });
            del_Btn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    contextEditText2.setText("");
                    contextEditText2.setVisibility(View.VISIBLE);
                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    removeDiary2(readDay2);
                }
            });
            if (textView2.getText() == null)
            {
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                contextEditText2.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public void removeDiary2(String readDay2)
    {
        FileOutputStream fos;
        try
        {
            fos = openFileOutput(readDay2, MODE_NO_LOCALIZED_COLLATORS);
            String content = "";
            fos.write((content).getBytes());
            fos.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public void saveDiary2(String readDay2) {
        FileOutputStream fos;
        try {
            fos = openFileOutput(readDay2, MODE_NO_LOCALIZED_COLLATORS);

            // Save text content
            String content = contextEditText2.getText().toString();
            fos.write(content.getBytes());

            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}