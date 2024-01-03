package com.example.tab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PhoneBookAdapter extends RecyclerView.Adapter<PhoneBookAdapter.MyViewHolder> {

    Context context;
    ArrayList<PhoneBook> phoneList = new ArrayList<>();

    private Class<?> callerClass; // 추가: 호출한 클래스를 저장할 변수

    private int year, month, dayOfMonth; //callendar 관련 정보

    PhoneBookAdapter(Context context, Class<?> callerClass, int year, int month, int dayOfMonth) {
        this.context = context;
        this.callerClass = callerClass;
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.phonebook_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PhoneBook phone = phoneList.get(position);

        holder.id_text.setText(phone.getPhone_id());
        holder.name_text.setText(phone.getPhone_name());

        byte[] phoneImage = phone.getUser_image();
        Bitmap bitmap = BitmapFactory.decodeByteArray(phoneImage, 0, phoneImage.length);
        holder.imageView.setImageBitmap(bitmap);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent;
                if (callerClass == PhoneBookFragment.class) {
                    // AddressFragment에서 호출했을 때의 동작
                    intent = new Intent(context, PhoneBookDetailActivity.class);
                    intent.putExtra("id", phone.getPhone_id());
                    intent.putExtra("name", phone.getPhone_name());
                    intent.putExtra("phone_number", phone.getPhone_number());
                    intent.putExtra("image", phoneImage);
                } else if (callerClass == CalendarAddress.class) {
                    // CalendarAddress에서 호출했을 때의 동작
                    intent = new Intent(context, CalendarDate.class);
                    intent.putExtra("id", phone.getPhone_id());
                    intent.putExtra("name", phone.getPhone_name());
                    intent.putExtra("phone_number", phone.getPhone_number());
                    intent.putExtra("image", phoneImage);
                    intent.putExtra("year", year);
                    intent.putExtra("month", month);
                    intent.putExtra("dayOfMonth", dayOfMonth);
                } else {
                    // 기본 동작
                    intent = new Intent(context, PhoneBookDetailActivity.class);
                    intent.putExtra("id", phone.getPhone_id());
                    intent.putExtra("name", phone.getPhone_name());
                    intent.putExtra("phone_number", phone.getPhone_number());
                    intent.putExtra("image", phoneImage);
                }
                context.startActivity(intent);
            }
        });
    }

    public void filterList(ArrayList<PhoneBook> filteredList) {
        phoneList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return phoneList.size();
    }

    public void removeItem(int position){
        phoneList.remove(position);
    }

    public void addItem(PhoneBook item){
        phoneList.add(item);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView id_text, name_text;
        ImageView imageView;
        LinearLayout mainLayout;

        public MyViewHolder (@NonNull View itemView) {
            super(itemView);

            id_text = itemView.findViewById(R.id.id_text);
            name_text = itemView.findViewById(R.id.name_text);
            imageView = itemView.findViewById(R.id.user_image);
            mainLayout = itemView.findViewById(R.id.main_layout);
        }
    }
}
