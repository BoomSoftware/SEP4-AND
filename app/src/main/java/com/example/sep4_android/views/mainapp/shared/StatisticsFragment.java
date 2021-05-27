package com.example.sep4_android.views.mainapp.shared;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sep4_android.R;
import com.example.sep4_android.models.FrequencyTypes;
import com.example.sep4_android.models.Measurement;
import com.example.sep4_android.models.MeasurementTypes;
import com.example.sep4_android.viewmodels.shared.PlantOverviewViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;


public class StatisticsFragment extends Fragment {

    private View view;
    private LineChart lineChart;

    private LineData lineDataTemp;
    private LineData lineDataC02;
    private LineData lineDataLight;
    private LineData lineDataHum;

    private LineDataSet lineDataSetTemp;
    private LineDataSet lineDataSetC02;
    private LineDataSet lineDataSetLight;
    private LineDataSet lineDataSetHum;

    private ArrayList<Entry> lineEntriesTemp;
    private ArrayList<Entry> lineEntriesC02;
    private ArrayList<Entry> lineEntriesLight;
    private ArrayList<Entry> lineEntriesHum;

    private Measurement measurement;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_statistics, container, false);
        prepareUI();

        setLineChartTemperature();
        getEntriesTemp();

        setLineChartC02();
        getEntriesC02();

        setLineChartLight();
        getEntriesLight();

        setLineChartHumidity();
        getEntriesHum();

        return view;
    }

    private void prepareUI(){
        lineChart = view.findViewById(R.id.line_chart);
    }

    private void setLineChartTemperature(){
        lineDataSetTemp = new LineDataSet(lineEntriesTemp, "Temperature");
        lineDataTemp = new LineData(lineDataSetTemp);
        lineChart.setData(lineDataTemp);
        lineDataSetTemp.setColors(ColorTemplate.PASTEL_COLORS);
        lineDataSetTemp.setValueTextColor(Color.RED);
        lineDataSetTemp.setValueTextSize(18f);
    }

    private void setLineChartC02(){
        lineDataSetC02 = new LineDataSet(lineEntriesC02, "C02");
        lineDataC02 = new LineData(lineDataSetC02);
        lineChart.setData(lineDataC02);
        lineDataSetC02.setColors(ColorTemplate.PASTEL_COLORS);
        lineDataSetC02.setValueTextColor(Color.GRAY);
        lineDataSetC02.setValueTextSize(18f);
    }

    private void setLineChartLight(){
        lineDataSetLight = new LineDataSet(lineEntriesLight, "Light");
        lineDataLight = new LineData(lineDataSetLight);
        lineChart.setData(lineDataLight);
        lineDataSetLight.setColors(ColorTemplate.PASTEL_COLORS);
        lineDataSetLight.setValueTextColor(Color.YELLOW);
        lineDataSetLight.setValueTextSize(18f);
    }

    private void setLineChartHumidity(){
        lineDataSetHum = new LineDataSet(lineEntriesHum, "Humidity");
        lineDataHum = new LineData(lineDataSetHum);
        lineChart.setData(lineDataHum);
        lineDataSetHum.setColors(ColorTemplate.PASTEL_COLORS);
        lineDataSetHum.setValueTextColor(Color.BLUE);
        lineDataSetHum.setValueTextSize(18f);
    }

    private void getEntriesTemp() {
        lineEntriesTemp = new ArrayList<>();
        if(measurement.getMeasurementType().equals(MeasurementTypes.TEMP.toString())){
            String value = measurement.getMeasurementValue() + " \u00B0C";
            lineEntriesTemp.add(new Entry(2f, 0));
            lineEntriesTemp.add(new Entry(4f, 1));
            lineEntriesTemp.add(new Entry(6f, 1));
            lineEntriesTemp.add(new Entry(8f, 3));
            lineEntriesTemp.add(new Entry(7f, 4));
            lineEntriesTemp.add(new Entry(3f, 3));
        }
    }

    private void getEntriesC02() {
        if(measurement.getMeasurementType().equals(MeasurementTypes.CO2.toString())){
            String value = measurement.getMeasurementValue() + " PPM";
            lineEntriesC02 = new ArrayList<>();
            lineEntriesC02.add(new Entry(2f, 0));
            lineEntriesC02.add(new Entry(4f, 1));
            lineEntriesC02.add(new Entry(6f, 1));
            lineEntriesC02.add(new Entry(8f, 3));
            lineEntriesC02.add(new Entry(7f, 4));
            lineEntriesC02.add(new Entry(3f, 3));
        }
    }

    private void getEntriesLight() {
        if(measurement.getMeasurementType().equals(MeasurementTypes.LIGHT.toString())){
            String value = measurement.getMeasurementValue() + " lux";
            lineEntriesLight = new ArrayList<>();
            lineEntriesLight.add(new Entry(2f, 0));
            lineEntriesLight.add(new Entry(4f, 1));
            lineEntriesLight.add(new Entry(6f, 1));
            lineEntriesLight.add(new Entry(8f, 3));
            lineEntriesLight.add(new Entry(7f, 4));
            lineEntriesLight.add(new Entry(3f, 3));
        }
    }

    private void getEntriesHum() {
        if(measurement.getMeasurementType().equals(MeasurementTypes.HUM.toString())){
            String value = measurement.getMeasurementValue() + " %";
            lineEntriesHum = new ArrayList<>();
            lineEntriesHum.add(new Entry(2f, 0));
            lineEntriesHum.add(new Entry(4f, 1));
            lineEntriesHum.add(new Entry(6f, 1));
            lineEntriesHum.add(new Entry(8f, 3));
            lineEntriesHum.add(new Entry(7f, 4));
            lineEntriesHum.add(new Entry(3f, 3));
        }
    }
}
