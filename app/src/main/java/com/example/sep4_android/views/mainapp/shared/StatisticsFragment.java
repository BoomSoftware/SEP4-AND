package com.example.sep4_android.views.mainapp.shared;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.example.sep4_android.R;
import com.example.sep4_android.models.ConnectionStatus;
import com.example.sep4_android.models.FrequencyTypes;
import com.example.sep4_android.models.Measurement;
import com.example.sep4_android.models.MeasurementTypes;
import com.example.sep4_android.viewmodels.shared.StatisticsViewModel;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class StatisticsFragment extends Fragment {

    private View view;
    private StatisticsViewModel viewModel;
    private Button co2;
    private Button temp;
    private Button hum;
    private TextView emptyChart;
    private Button light;
    private AnyChartView anyChartView;
    private Cartesian bar;
    private int plantId;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_statistics, container, false);
        viewModel = new ViewModelProvider(this).get(StatisticsViewModel.class);
        viewModel.clearHistoricalMeasurements();
        plantId = getArguments().getInt("plantID");
        prepareUI();
        prepareOnClickEvents();
        loadData();
        checkConnectionStatus();
        bar = AnyChart.column();
        prepareChart();
        return view;
    }

    private void prepareUI() {
        anyChartView = view.findViewById(R.id.any_chart_view);
        co2 = view.findViewById(R.id.button_statisctis_co2);
        light = view.findViewById(R.id.button_statisctis_light);
        hum = view.findViewById(R.id.button_statisctis_hum);
        temp = view.findViewById(R.id.button_statisctis_temp);
        progressBar = view.findViewById(R.id.progress);
        emptyChart = view.findViewById(R.id.empty_chart);
        anyChartView.setBackgroundColor("#22483E");
    }

    private void prepareChart(){
        bar.xScroller(true);
        bar.xScroller().enabled();
        anyChartView.setZoomEnabled(true);
        anyChartView.setChart(bar);
        bar.background().fill("#22483E");
        bar.xScroller().fill("#739F62");

        bar.xScroller().selectedFill("#739F62", 2);
        bar.xScroller().thumbs().autoHide(true);
        bar.xScroller().thumbs().fill("#081C15");
        bar.xScroller().thumbs().stroke("#739F62", 0, "solid", "0", "0");
        bar.xScroller().autoHide(true);
        bar.xAxis(false);
    }

    private void prepareOnClickEvents() {
        co2.setOnClickListener(v -> {
            emptyChart.setVisibility(View.GONE);
            viewModel.clearHistoricalMeasurements();
            progressBar.setVisibility(View.VISIBLE);
            viewModel.loadMeasurements(plantId, FrequencyTypes.HISTORY, MeasurementTypes.CO2);
        });
        temp.setOnClickListener(v -> {
            emptyChart.setVisibility(View.GONE);
            viewModel.clearHistoricalMeasurements();
            progressBar.setVisibility(View.VISIBLE);
            viewModel.loadMeasurements(plantId, FrequencyTypes.HISTORY, MeasurementTypes.TEMP);
        });
        hum.setOnClickListener(v -> {
            emptyChart.setVisibility(View.GONE);
            viewModel.clearHistoricalMeasurements();
            progressBar.setVisibility(View.VISIBLE);
            viewModel.loadMeasurements(plantId, FrequencyTypes.HISTORY, MeasurementTypes.HUM);
        });
        light.setOnClickListener(v -> {
            emptyChart.setVisibility(View.GONE);
            viewModel.clearHistoricalMeasurements();
            progressBar.setVisibility(View.VISIBLE);
            viewModel.loadMeasurements(plantId, FrequencyTypes.HISTORY, MeasurementTypes.LIGHT);
        });
    }

    private void loadData() {
        viewModel.getHistoricalMeasurements().observe(getViewLifecycleOwner(), measurements -> {
            if (measurements != null) {
                List<DataEntry> data = new ArrayList<>();
                bar.removeAllSeries();
                for (Measurement measurement : measurements) {
                    data.add(new ValueDataEntry(measurement.getDate() +
                            " " +
                            measurement.getTime().replace(".0000000", ""),
                            measurement.getMeasurementValue()));
                }
                bar.column(data);
                bar.getSeries(0).normal().fill("#739F62");
                bar.getSeries(0).normal().stroke("#739F62", 0, "solid", "0", "0");
                if (measurements.get(0) != null) {
                    bar.title(measurements.get(0).getMeasurementType());
                }
                anyChartView.setVisibility(View.VISIBLE);
                emptyChart.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void checkConnectionStatus() {
        viewModel.getConnectionStatus().observe(getViewLifecycleOwner(), status -> {
            if (status.equals(ConnectionStatus.ERROR)) {
                Toasty.error(view.getContext(), view.getContext().getString(R.string.measurements_error), Toast.LENGTH_SHORT, true).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
