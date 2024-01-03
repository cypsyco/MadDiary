package com.example.tab;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import android.widget.GridView;
import android.content.Intent;
import android.widget.Toast;

import java.io.File;


public class CalendarGallery extends AppCompatActivity {
    String img_name = "osz.png";
    int year, month, dayOfMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_gallery);
        Intent intent = getIntent();
        year = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month", 0);
        dayOfMonth = intent.getIntExtra("dayOfMonth", 0);
        final GridView gv = findViewById(R.id.gridView);
        MyGridAdapter gAdapter = new MyGridAdapter(CalendarGallery.this);
        gv.setAdapter(gAdapter);
    }

    public class MyGridAdapter extends BaseAdapter{
        Context context;
        public MyGridAdapter(Context c){
            context = c;
        }

        File file = getCacheDir();
        File[] file_list = file.listFiles();

        @Override
        public int getCount() {
            return file_list.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(357, 238));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(5, 5, 5, 5);
            String ask = img_name.concat(Integer.toString(i));
            try {
                String img_path = getCacheDir() + "/" + ask;   // 내부 저장소에 저장되어 있는 이미지 경로
                Bitmap bm = BitmapFactory.decodeFile(img_path);
                while(bm == null){
                    i++;
                    ask = img_name.concat(Integer.toString(i));
                    img_path = getCacheDir() + "/" + ask;
                    bm = BitmapFactory.decodeFile(img_path);
                }
                imageView.setImageBitmap(bm);   // 내부 저장소에 저장된 이미지를 이미지뷰에 셋

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "파일 로드 실패", Toast.LENGTH_SHORT).show();
            }


            final int pos = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String imgpath = getCacheDir() + "/" + "osz.png".concat(Integer.toString(pos));   // 내부 저장소에 저장되어 있는 이미지 경로
                        Bitmap bm = BitmapFactory.decodeFile(imgpath);
                        java.io.ByteArrayOutputStream stream = new java.io.ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        String prev_class = "gallery";
                        Intent intent = new Intent(CalendarGallery.this, CalendarDate.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("year", year);
                        intent.putExtra("month", month);
                        intent.putExtra("dayOfMonth", dayOfMonth);
                        intent.putExtra("bm", byteArray);
                        intent.putExtra("prev_class", prev_class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "파일 로드 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            return imageView;
        }

    }
}