package com.example.wittyphotos2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wittyphotos2.KNN.Category;
import com.example.wittyphotos2.KNN.Classifier;
import com.example.wittyphotos2.KNN.Constants;
import com.example.wittyphotos2.KNN.DataPoint;
import com.example.wittyphotos2.KNN.DataPointCanvas;
import com.example.wittyphotos2.KNN.ParameterTuningActivity;
import com.example.wittyphotos2.KNN.distanceAlgorithm.DistanceAlgorithm;
import com.example.wittyphotos2.KNN.distanceAlgorithm.EuclideanDistance;
import com.example.wittyphotos2.KNN.distanceAlgorithm.ManhattenDistance;
import com.example.wittyphotos2.KNN.distanceAlgorithm.MinkowskiDistance;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AnalysisFragment extends Fragment {
    private final int REQUEST_CODE = 101;

    private View root;
    private DistanceAlgorithm[] distanceAlgorithms = {new EuclideanDistance(), new
            ManhattenDistance(), new MinkowskiDistance(0)};
    private Classifier classifier;
    private List<DataPoint> listDataPoint = new ArrayList<>();
    private List<DataPoint> listDataPointOriginal = new ArrayList<>();

    private DataPointCanvas dataPointCanvas;
    private Button buttonTune;
    private Button buttonPredict;
    private Button buttonReset;
    private TextView textViewAccuracy;

    public AnalysisFragment(){ }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        root =  inflater.inflate(R.layout.fragment_analysis, container,false);

        classifier = new Classifier();

        populateList();
        setupUI();
        setupCanvas();
//        setButtonTuneListener();
//        setButtonPredictListener();
//        setButtonResetListener();

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            if (data == null)
                return;
            Bundle bundle = data.getExtras();
            if (bundle == null)
                return;
            int spinnerIndex = bundle.getInt(Constants.DISTANCE_ALGORITHM);
            int K = bundle.getInt(Constants.K);
            double splitRatio = bundle.getDouble(Constants.SPLITE_RATIO);

            DistanceAlgorithm distanceAlgorithm = distanceAlgorithms[spinnerIndex];
            if (distanceAlgorithm instanceof MinkowskiDistance){
                int p = bundle.getInt(Constants.MINKOWSKI_P);
                ((MinkowskiDistance)distanceAlgorithm).setP(p);
            }
            classifier.reset();
            classifier.setDistanceAlgorithm(distanceAlgorithms[spinnerIndex]);
            classifier.setK(K);
            classifier.setSplitRatio(splitRatio);
            classifier.setListDataPoint(listDataPoint);
            classifier.splitData();
            listDataPoint.clear();
            listDataPoint.addAll(classifier.getListTestData());
            listDataPoint.addAll(classifier.getListTrainData());
            dataPointCanvas.invalidate();
            buttonPredict.setEnabled(true);
        }
    }
    private void setupCanvas(){
        dataPointCanvas.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                dataPointCanvas.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                dataPointCanvas.setListDataPoints(listDataPoint);
            }
        });
    }

    private void setupUI(){
        dataPointCanvas = root.findViewById(R.id.dataPointCanvas);
//        buttonTune = root.findViewById(R.id.buttonTune);
//        buttonPredict = root.findViewById(R.id.buttonPredict);
//        buttonReset = root.findViewById(R.id.buttonReset);
//        textViewAccuracy = root.findViewById(R.id.textViewAccuracy);
    }

//    private void setButtonTuneListener(){
//        buttonTune.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), ParameterTuningActivity.class);
//                startActivityForResult(intent, REQUEST_CODE);
//            }
//        });
//    }

//    private void setButtonPredictListener(){
//        buttonPredict.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                classifier.classify();
//                textViewAccuracy.setText("Accuracy = " + classifier.getAccuracy());
//                dataPointCanvas.invalidate();
//            }
//        });
//    }
//    private void setButtonResetListener(){
//        buttonReset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                classifier.reset();
//                listDataPoint.clear();
//                listDataPoint.addAll(listDataPointOriginal);
//                classifier.setListDataPoint(listDataPoint);
//                dataPointCanvas.invalidate();
//                buttonPredict.setEnabled(false);
//            //    textViewAccuracy.setText("");
//            }
//        });
//    }

    private void populateList() {
        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getActivity().getAssets().open
                    ("datas.txt")));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                String[] point = line.split(",");
                double x = Double.parseDouble(point[0])*40;
                double y = Double.parseDouble(point[1]);
                int category = Integer.parseInt(point[2]);
                DataPoint dataPoint = new DataPoint(x, y, Category.values()[category]);
                listDataPointOriginal.add(new DataPoint(dataPoint));
                listDataPoint.add(dataPoint);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}
