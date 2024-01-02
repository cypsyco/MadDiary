package com.example.tab;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class CallendarAddressDetail extends AppCompatActivity {

    EditText updateNameEdit, updatePhoneEdit;
    ImageView updateImageView;
    String id, name, phoneNumber;
    byte[] image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        updateNameEdit = findViewById(R.id.update_name_edit);
        updatePhoneEdit = findViewById(R.id.update_phone_edit);
        updateImageView = findViewById(R.id.update_image_view);

        updatePhoneEdit.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        getAndSetIntentData();

        Button editBtn = findViewById(R.id.edit_btn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = updateNameEdit.getText().toString();
                String phoneNumber = updatePhoneEdit.getText().toString();

                PhoneBookDB db = new PhoneBookDB(CallendarAddressDetail.this);
                db.updateData(id, name, phoneNumber);

                TabLayout tab;
                ViewPager viewPager;

                setContentView(R.layout.activity_main);

                tab=findViewById(R.id.tab);
                viewPager=findViewById(R.id.viewpager);

                viewPagerAdapter ad =new viewPagerAdapter(getSupportFragmentManager());
                viewPager.setAdapter(ad);

                tab.setupWithViewPager(viewPager);
            }
        });

        Button deleteBtn = findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PhoneBookDB db = new PhoneBookDB(CallendarAddressDetail.this);
                db.deleteData(id);

                TabLayout tab;
                ViewPager viewPager;

                setContentView(R.layout.activity_main);

                tab=findViewById(R.id.tab);
                viewPager=findViewById(R.id.viewpager);

                viewPagerAdapter ad =new viewPagerAdapter(getSupportFragmentManager());
                viewPager.setAdapter(ad);

                tab.setupWithViewPager(viewPager);
            }
        });
    }

    private void getAndSetIntentData() {
        if(getIntent().hasExtra("id")
                && getIntent().hasExtra("name")
                && getIntent().hasExtra("phone_number")
                && getIntent().hasExtra("image")){

            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            phoneNumber = getIntent().getStringExtra("phone_number");
            image = getIntent().getByteArrayExtra("image");

            updateNameEdit.setText(name);
            updatePhoneEdit.setText(phoneNumber);

            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

            updateImageView.setImageBitmap(bitmap);
        }
    }
}
