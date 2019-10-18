package com.example.wittyphotos;

import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.wittyphotos.DB.DBOpenHelper;

public class ImageDetailFragment extends Fragment {
    private ImageView backBtn, tagBtn, favoriteBtn, shareBtn, imageDetail;
    private TextView imageName, time, place;
    private DBOpenHelper mDb;

    public ImageDetailFragment(){ }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root =  inflater.inflate(R.layout.fragment_image_detail, container,false);

        String imagePath = getArguments().getString("imagePath");

        backBtn = root.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).replaceFragment(new AlbumFragment());
            }
        });

        imageName = root.findViewById(R.id.imageName);
        imageName.setText(last(imagePath.split("/")));

        tagBtn = root.findViewById(R.id.tagBtn);
        tagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        favoriteBtn = root.findViewById(R.id.favoriteBtn);
        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        shareBtn = root.findViewById(R.id.shareBtn);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        imageDetail = root.findViewById(R.id.imageDetail);
        try {
            imageDetail.setImageURI(Uri.parse(imagePath));
        }catch (Exception e){
            e.printStackTrace();
        }

        mDb = new DBOpenHelper(getContext());
        mDb.open();
        time = root.findViewById(R.id.time);
        place = root.findViewById(R.id.place);

        Cursor cursor = mDb.select_image();
        while(cursor.moveToNext()){
            if(cursor.getString(cursor.getColumnIndex("imagePath")).equals(imagePath)){
                time.setText(cursor.getString(cursor.getColumnIndex("imageTime")));
                place.setText(cursor.getString(cursor.getColumnIndex("location")));
            }
        }
        mDb.close();
        return root;
    }

    public static <T> T last(T[] array){
        return array[array.length - 1];
    }
}
