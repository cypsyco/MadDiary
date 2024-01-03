package com.example.tab;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;


import android.widget.GridView;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import android.widget.GridView;


public class GalleryFragment extends Fragment {
    public int q = 0;
    String x = "";
    String img_name = "osz.png";
    Button btn_insert;
    GridView gv;
    public GalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_gallery, container, false);
        btn_insert = v.findViewById(R.id.btn_insert);
        View a = inflater.inflate(R.layout.gallery_expanded, container, false);
        gv = v.findViewById(R.id.gridView);
        SharedPreferences pref = this.getActivity().getSharedPreferences("pref", android.app.Activity.MODE_PRIVATE);
        if ((pref != null) && (pref.contains("x"))) {  // pref가 비어있지 않고 x가 있으면 실행
            x = pref.getString("x", "");    // x 받아오기
            q = pref.getInt("i", 0);    // i 받아오기
        }
        MyGridAdapter gAdapter = new MyGridAdapter(getActivity());
        gv.setAdapter(gAdapter);
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 101);
            }
        });
        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { // 갤러리
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == android.app.Activity.RESULT_OK) {
                Uri fileUri = data.getData();
                ContentResolver resolver = getActivity().getContentResolver();
                try {
                    InputStream instream = resolver.openInputStream(fileUri);
                    Bitmap imgBitmap = BitmapFactory.decodeStream(instream);  // 선택한 이미지 이미지뷰에 셋
                    instream.close();   // 스트림 닫아주기
                    saveBitmapToJpeg(imgBitmap);    // 내부 저장소에 저장
                } catch (Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(), "파일 불러오기 실패", Toast.LENGTH_SHORT).show();
                }
                MyGridAdapter gAdapter = new MyGridAdapter(getActivity());
                gv.setAdapter(gAdapter);
            }
        }
    }

    public void saveBitmapToJpeg(Bitmap bitmap) {   // 선택한 이미지 내부 저장소에 저장
        File tempFile = new File(getActivity().getCacheDir(), img_name.concat(Integer.toString(q)));    // 파일 경로와 이름 넣기
        q++;
        x = "OSZ";
        SharedPreferences pref = getActivity().getSharedPreferences("pref", android.app.Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("x", x);   // x에 x 저장하기
        editor.putInt("i", q);  // i에 i 저장하기
        editor.commit();    // 입력했으면 이걸 필수로 해줘야 함 없을시 저장 안됨
        try {
            tempFile.createNewFile();   // 자동으로 빈 파일을 생성하기
            FileOutputStream out = new FileOutputStream(tempFile);  // 파일을 쓸 수 있는 스트림을 준비하기
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);   // compress 함수를 사용해 스트림에 비트맵을 저장하기
            out.close();    // 스트림 닫아주기
        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "파일 저장 실패", Toast.LENGTH_SHORT).show();
        }
    }
    public class MyGridAdapter extends BaseAdapter{
        Context context;
        public MyGridAdapter(Context c){
            context = c;
        }

        File file = getActivity().getCacheDir();
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
            imageView.setLayoutParams(new ViewGroup.LayoutParams(250, 250));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(5, 400, 5, 5);
            String ask = img_name.concat(Integer.toString(i));
            try {
                String img_path = getActivity().getCacheDir() + "/" + ask;   // 내부 저장소에 저장되어 있는 이미지 경로
                Bitmap bm = BitmapFactory.decodeFile(img_path);
                while(bm == null){
                    i++;
                    ask = img_name.concat(Integer.toString(i));
                    img_path = getActivity().getCacheDir() + "/" + ask;
                    bm = BitmapFactory.decodeFile(img_path);
                }
                imageView.setImageBitmap(bm);   // 내부 저장소에 저장된 이미지를 이미지뷰에 셋

            } catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), "파일 로드 실패", Toast.LENGTH_SHORT).show();
            }


            final int pos = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                int prev = pos-1;
                @Override
                public void onClick(View view) {
                    View dialogView = View.inflate(getActivity(), R.layout.gallery_expanded, null);
                    AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                    ImageView ivPic = dialogView.findViewById(R.id.ivPic);
                    try {
                        String imgpath = getActivity().getCacheDir() + "/" + "osz.png".concat(Integer.toString(pos));   // 내부 저장소에 저장되어 있는 이미지 경로
                        Bitmap bm = BitmapFactory.decodeFile(imgpath);
                        ivPic.setImageBitmap(bm);   // 내부 저장소에 저장된 이미지를 이미지뷰에 셋
                    } catch (Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), "파일 로드 실패", Toast.LENGTH_SHORT).show();
                    }
                    dlg.setView(dialogView);
                    dlg.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            File file = getActivity().getCacheDir();
                            File[] flist = file.listFiles();
                            for(int i = 0; i<flist.length; i++){
                                if(flist[i].getName().equals("osz.png".concat(Integer.toString(pos)))){
                                    flist[i].delete();
                                }
                            }
                            MyGridAdapter gAdapter = new MyGridAdapter(getActivity());
                            gv.setAdapter(gAdapter);
                        }
                    });
                    dlg.setPositiveButton("닫기", null);
                    dlg.show();
                }
            });


            return imageView;
        }

    }


}