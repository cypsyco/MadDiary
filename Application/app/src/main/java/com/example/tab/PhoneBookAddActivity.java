package com.example.tab;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PhoneBookAddActivity extends AppCompatActivity {

    Uri uri;
    ImageView imageView;
    EditText addNameEdit, addPhoneEdit;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonebook_add);

        addNameEdit = findViewById(R.id.add_name_edit);
        addPhoneEdit = findViewById(R.id.add_phone_edit);
        imageView = findViewById(R.id.imageView);

        addPhoneEdit.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        Button pictureBtn = findViewById(R.id.picture_btn);
        Button selectBtn = findViewById(R.id.select_btn);

        pictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultPicture.launch(intent);
            }
        });

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activityResultSelect.launch(intent);
            }
        });

        Button addBtn = findViewById(R.id.add_btn);
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String name = addNameEdit.getText().toString();
                String phone = addPhoneEdit.getText().toString();

                byte[] data = imageViewToByte(imageView);

                PhoneBookDB addressDB = new PhoneBookDB(PhoneBookAddActivity.this);
                addressDB.addPhoneNumber(name, phone, data);

                // 다음 코드는 두 번째 탭으로 이동합니다.
                TabLayout tab;
                ViewPager viewPager;

                setContentView(R.layout.activity_main);

                tab = findViewById(R.id.tab);
                viewPager = findViewById(R.id.viewpager);

                ViewPagerAdapter ad = new ViewPagerAdapter(getSupportFragmentManager());
                viewPager.setAdapter(ad);

                tab.setupWithViewPager(viewPager);

                // 두 번째 탭으로 자동 전환
                viewPager.setCurrentItem(1);
            }
        }
        );
    }

    //사진 찍기
    ActivityResultLauncher<Intent> activityResultPicture = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null){
                        Bundle extras = result.getData().getExtras();
                        bitmap = (Bitmap) extras.get("data");
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
    );

    //사진 선택
    ActivityResultLauncher<Intent> activityResultSelect = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null){
                        try{
                            uri = result.getData().getData();
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imageView.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    public static byte[] imageViewToByte(ImageView image){
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

}