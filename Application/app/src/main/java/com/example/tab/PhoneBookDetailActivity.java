package com.example.tab;

import static com.example.tab.PhoneBookAddActivity.imageViewToByte;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.io.FileNotFoundException;
import java.io.IOException;

public class PhoneBookDetailActivity extends AppCompatActivity {

    EditText updateNameEdit, updatePhoneEdit;
    ImageView updateImageView;
    String id, name, phoneNumber;
    byte[] image;
    ImageView imageView;
    Uri uri;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_detail);

        updateNameEdit = findViewById(R.id.update_name_edit);
        updatePhoneEdit = findViewById(R.id.update_phone_edit);
        updateImageView = findViewById(R.id.update_image_view);

        updateImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activityResultSelect.launch(intent);
            }
        });

        updatePhoneEdit.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        getAndSetIntentData();

        Button saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = updateNameEdit.getText().toString();
                String phoneNumber = updatePhoneEdit.getText().toString();
                byte[] data = imageViewToByte(updateImageView);

                PhoneBookDB db = new PhoneBookDB(PhoneBookDetailActivity.this);
                db.updateData(id, name, phoneNumber, data);

                TabLayout tab;
                ViewPager viewPager;

                setContentView(R.layout.activity_main);

                tab=findViewById(R.id.tab);
                viewPager=findViewById(R.id.viewpager);

                ViewPagerAdapter ad =new ViewPagerAdapter(getSupportFragmentManager());
                viewPager.setAdapter(ad);

                tab.setupWithViewPager(viewPager);
                viewPager.setCurrentItem(1);
            }
        });

        Button deleteBtn = findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PhoneBookDB db = new PhoneBookDB(PhoneBookDetailActivity.this);
                db.deleteData(id);

                TabLayout tab;
                ViewPager viewPager;

                setContentView(R.layout.activity_main);

                tab=findViewById(R.id.tab);
                viewPager=findViewById(R.id.viewpager);

                ViewPagerAdapter ad =new ViewPagerAdapter(getSupportFragmentManager());
                viewPager.setAdapter(ad);

                tab.setupWithViewPager(viewPager);
                viewPager.setCurrentItem(1);
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

    ActivityResultLauncher<Intent> activityResultSelect = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null){
                        try{
                            uri = result.getData().getData();
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            updateImageView.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
}
