package com.example.wittyphotos;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.wittyphotos.DB.DBOpenHelper;

import java.io.IOException;
import java.util.ArrayList;

public class AlbumFragment extends Fragment {

    private EditText editSearch;    //검색창
    private GridView gridView;
    private ImageAdapter imageAdapter;
    private DisplayMetrics mMetrics;
    private DBOpenHelper mDB;
    public AlbumFragment(){ }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root =  inflater.inflate(R.layout.fragment_album, container,false);
        editSearch = root.findViewById(R.id.editSearch);
        gridView = root.findViewById(R.id.gv);

        imageAdapter = new ImageAdapter(getContext());
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putString("imagePath",MainActivity.allImagePahts.get(i));
                ImageDetailFragment imageDetailFragment = new ImageDetailFragment();
                imageDetailFragment.setArguments(bundle);
                ((MainActivity)getActivity()).replaceFragment(imageDetailFragment);
            }
        });

        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
        return root;
    }
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        public ImageAdapter(Context c) {
            mContext = c;
        }
        public int getCount() {
            return MainActivity.allImagePahts.size();
        }

        public String getItem(int position) {
            return MainActivity.allImagePahts.get(position);
        }
        public long getItemId(int position) {
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            int rowWidth = (mMetrics.widthPixels) / 3 - 30;
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(rowWidth,rowWidth));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(10, 10, 10, 10);
            } else {
                imageView = (ImageView) convertView;
            }
            try {
                imageView.setImageURI(Uri.parse(MainActivity.allImagePahts.get(position)));
            }catch (Exception e){
                e.printStackTrace();
            }
            return imageView;
        }
    }

}