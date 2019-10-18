package com.example.wittyphotos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.UnicodeSetSpanner;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wittyphotos.DB.DBOpenHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String PERMISSION_STORAGE = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private Button albumBtn, analysisBtn;   //앨범 버튼, 분석 버튼
    private FragmentManager fm;
    private Fragment AlbumFrg, AnalysisFrg;
    private DBOpenHelper mDb;
    public static ArrayList<String> allImagePahts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        albumBtn = findViewById(R.id.albumBtn); //앨범 버튼
        analysisBtn = findViewById(R.id.analysisBtn);   //분석 버튼
        AlbumFrg = new AlbumFragment(); //앨범 플래그먼트
        AnalysisFrg = new AnalysisFragment();   //분석 플래그먼트
        mDb = new DBOpenHelper(this);
        mDb.open();
        mDb.create();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //퍼미션 체크
            checkPermissionAndRequestPermission(1);
        }

        getAllImages(); //모든 사진을 가져와서 새로운 사진은 exif추출 후 DB에 저장
        mDb.close();

        //앨범 프래그먼트로 이동
        fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.fragmentContainer, AlbumFrg).commit();

        //앨범 버튼 클릭시 앨범 프래그먼트로 이동
        albumBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(AlbumFrg);
            }
        });

        //분석 버튼 클릭시 분석 플래그먼트로 이동
        analysisBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(AnalysisFrg);
            }
        });
    }

    public boolean checkPermissionAndRequestPermission(final int requestCode) {
        if (!isFinishing()) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]{PERMISSION_STORAGE}, requestCode);
            }
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    checkPermissionAndRequestPermission(1);
                }
                return;
            }
        }
    }

    //프래그먼트 변환 함수
    public void replaceFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentContainer, fragment).commit();
    }

    //기기 내의 모든 사진 경로 불러오기
    private void getAllImages(){
        ArrayList<String> newImgPath = new ArrayList<>();
        ArrayList<Integer> newImgID = new ArrayList<>();

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, MediaStore.MediaColumns._ID + " desc");

        if(cursor.getCount() != 0){
            Cursor dbCur = mDb.select_image();
            while(cursor.moveToNext()){
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                allImagePahts.add(path);
                int cnt = 0;
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                if(dbCur.getCount() != 0){
                    while(dbCur.moveToNext()){
                        if(id == dbCur.getInt(dbCur.getColumnIndex("imageID"))){
                            cnt++;
                        }
                    }
                }
                if(cnt == 0){
                    newImgID.add(id);
                    newImgPath.add(path);
                }
            }
            if(newImgID.size() > 0){    //새로운 사진이 존재하면 exif 추출후 DB에 저
                insertImageData(newImgID, newImgPath);
            }
        }else{
            Toast.makeText(this, "사진이 없습니다.", Toast.LENGTH_LONG).show();
        }
    }

    //새로운 사진 정보 저장
    private void insertImageData(ArrayList<Integer> newImgID, ArrayList<String> newImgPath){
        for(int i = 0; i < newImgID.size(); i++){
            String time = "", gps = "", location = "";
            String times[] = new String[3];
            try{
                ExifInterface exif = new ExifInterface(newImgPath.get(i));
                times = exif.getAttribute(ExifInterface.TAG_DATETIME).split(" ");
                time = times[0].split(":")[0] + "년 " + times[0].split(":")[1] + "월 " + times[0].split(":")[2] + "일 " + times[1];
                GeoDegree geoDegree = new GeoDegree( exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE), exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF),
                        exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE), exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF));
                gps = geoDegree.toString();
//                location = findLocation(gps);
                mDb.insert_image(newImgID.get(i), newImgPath.get(i), time, gps, location);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}