package com.example.sep4_android.views.mainapp.gardener;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.example.sep4_android.R;
import com.example.sep4_android.models.Plant;
import com.example.sep4_android.util.AlertReceiver;
import com.example.sep4_android.viewmodels.shared.SettingsViewModel;

import java.util.Calendar;

public class SettingsFragment extends PreferenceFragmentCompat {
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private SettingsViewModel viewModel;
    private Preference synchronizeButton;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        preparePreferences();
        preparePreferencesOnClick();

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this.requireContext());

        EditTextPreference notificationText = findPreference("notification_text");
        notificationText.setOnBindEditTextListener(p -> {
            notificationText.setText("insert text");
        });

        EditTextPreference notificationsTime = findPreference("notifications_time");
        notificationsTime.setOnPreferenceClickListener(p -> {
            TimePickerDialog timeDialog = new TimePickerDialog(getContext(), (timePicker, hour, minute) -> {
                notificationsTime.setText(convertDayMonthToString(hour) + ":" + convertDayMonthToString(minute));
            }, Integer.parseInt(notificationsTime.getText().split(":")[0]), Integer.parseInt(notificationsTime.getText().split(":")[1]), true);
            timeDialog.show();
            return false;
        });

        sharedPreferences.registerOnSharedPreferenceChangeListener((sp, key) -> {
            if (key.equals("notifications")) {
                boolean isShowNotifications = sp.getBoolean(key, false);
                Log.i("pref", "Changed " + isShowNotifications);
                if (isShowNotifications) {
                    int hour = Integer.parseInt(sp.getString("notifications_time", "").split(":")[0]);
                    int minute = Integer.parseInt(sp.getString("notifications_time", "").split(":")[1]);
                    String text = sp.getString("notification_text", "");
                    startAlarm(hour, minute, text);
                    Log.i("pref", "Started alarm at " + hour + " " + minute);
                } else {
                    setUp();
                    alarmManager.cancel(pendingIntent);
                }
            } else if (key.equals("notifications_time")) {
                if (sp.getBoolean("notifications", false)) {
                    int hour = Integer.parseInt(sp.getString("notifications_time", "").split(":")[0]);
                    int minute = Integer.parseInt(sp.getString("notifications_time", "").split(":")[1]);
                    String text = sp.getString("notification_text", "");
                    startAlarm(hour, minute, text);
                    Log.i("pref", "Started alarm at " + hour + " " + minute);
                }
            }
        });
    }


    private void preparePreferences(){
        synchronizeButton = findPreference(getString(R.string.settings_synchronize));
    }

    private void preparePreferencesOnClick(){
        synchronizeButton.setOnPreferenceClickListener(v -> {
            viewModel.synchronizeGarden();
            viewModel.getSynchronizedGardenName().observe(getViewLifecycleOwner(), name -> {
                if(name != null){
                    viewModel.loadPlantsForGardenLive(name);
                    viewModel.getPlantsForGardenLive().observe(getViewLifecycleOwner(), plants  -> {
                        for(Plant plant : plants){
                            viewModel.addPlant(plant);
                        }
                    });
                }
            });
            return true;
        });

    }


    private void startAlarm(int hour, int minute, String text) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        setUp();
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void setUp() {
        alarmManager = (AlarmManager) this.requireContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private String convertDayMonthToString(int value) {
        return value < 10 ? "0" + value : "" + value;
    }
}
