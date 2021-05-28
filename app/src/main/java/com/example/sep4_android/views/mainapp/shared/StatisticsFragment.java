package com.example.sep4_android.views.mainapp.shared;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Cartesian3d;
import com.anychart.core.gauge.pointers.Bar;
import com.example.sep4_android.R;
import com.example.sep4_android.models.FrequencyTypes;
import com.example.sep4_android.models.Measurement;
import com.example.sep4_android.models.MeasurementTypes;
import com.example.sep4_android.util.DateValueFormatter;
import com.example.sep4_android.viewmodels.shared.PlantOverviewViewModel;
import com.example.sep4_android.viewmodels.shared.StatisticsViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class StatisticsFragment extends Fragment {

    private View view;
    private StatisticsViewModel viewModel;

    private BarChart barChart;
    private BarData barData;
    private BarDataSet barDataSet;
    private ArrayList barEntries;
    private Button co2;
    private Button temp;
    private Button hum;
    private Button light;
    private AnyChartView anyChartView;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_statistics, container, false);
        viewModel = new ViewModelProvider(this).get(StatisticsViewModel.class);
        prepareUI();
        prepareOnClickEvents();
        loadData();
        return view;
    }

    private void prepareUI() {
        anyChartView = view.findViewById(R.id.any_chart_view);
        co2 = view.findViewById(R.id.button_statisctis_co2);
        light = view.findViewById(R.id.button_statisctis_light);
        hum = view.findViewById(R.id.button_statisctis_hum);
        temp = view.findViewById(R.id.button_statisctis_temp);
    }

    private void prepareOnClickEvents() {
        co2.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            viewModel.loadMeasurements(720, FrequencyTypes.HISTORY, MeasurementTypes.CO2);
            progressBar.setVisibility(View.GONE);
        });
        temp.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            viewModel.loadMeasurements(720, FrequencyTypes.HISTORY, MeasurementTypes.TEMP);
            progressBar.setVisibility(View.GONE);
        });
        hum.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            viewModel.loadMeasurements(720, FrequencyTypes.HISTORY, MeasurementTypes.HUM);
            progressBar.setVisibility(View.GONE);
        });
        light.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            viewModel.loadMeasurements(720, FrequencyTypes.HISTORY, MeasurementTypes.LIGHT);
            progressBar.setVisibility(View.GONE);
        });
    }

    private void loadData() {
        viewModel.getLoadedMeasurements().observe(getViewLifecycleOwner(), measurements -> {
            Cartesian bar = AnyChart.column();
            List<DataEntry> data = new ArrayList<>();
            for (Measurement measurement : measurements) {
                System.out.println(" XXXXXXXXXXXXXXXXXXX" + measurement.getDate());
                data.add(new ValueDataEntry(measurement.getDate(), measurement.getMeasurementValue()));
            }
            bar.column(data);
            anyChartView.setChart(bar);
        });
    }
}
