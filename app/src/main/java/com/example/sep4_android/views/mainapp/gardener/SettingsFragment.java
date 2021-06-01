package com.example.sep4_android.views.mainapp.gardener;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import com.example.sep4_android.R;
import com.example.sep4_android.models.Plant;
import com.example.sep4_android.util.AlertReceiver;
import com.example.sep4_android.viewmodels.gardener.SettingsViewModel;

import java.util.Calendar;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragemnt_settings_with_style, container, false);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings_content, new SettingsPreferences())
                .commit();
        return view;
    }

    public static class SettingsPreferences extends PreferenceFragmentCompat {
        private AlarmManager alarmManager;
        private PendingIntent pendingIntent;
        private SettingsViewModel viewModel;
        private EditTextPreference notificationText;
        private EditTextPreference notificationsTime;
        private Preference synchronizeButton;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
            preparePreferences();
            preparePreferencesOnClick();
            registerAndPrepareListeners();
        }

        private void registerAndPrepareListeners() {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.requireContext());
            sharedPreferences.registerOnSharedPreferenceChangeListener((sp, key) -> {
                if (key.equals("notifications")) {
                    boolean isShowNotifications = sp.getBoolean(key, false);
                    if (isShowNotifications) {
                        int hour = Integer.parseInt(sp.getString("notifications_time", "").split(":")[0]);
                        int minute = Integer.parseInt(sp.getString("notifications_time", "").split(":")[1]);
                        String text = sp.getString("notification_text", "");
                        startAlarm(hour, minute, text);
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
                    }
                }
            });
            notificationText.setOnBindEditTextListener(p -> {
                notificationText.setText("");
            });
        }

        private void preparePreferences() {
            synchronizeButton = findPreference(getString(R.string.settings_synchronize));
            notificationText = findPreference("notification_text");
            notificationsTime = findPreference("notifications_time");
        }

        private void preparePreferencesOnClick() {
            synchronizeButton.setOnPreferenceClickListener(v -> {
                viewModel.synchronizeGarden();
                viewModel.getSynchronizedGardenName().observe(getViewLifecycleOwner(), name -> {
                    if (name != null) {
                        viewModel.loadPlantsForGardenLive(name);
                        viewModel.getPlantsForGardenLive().observe(getViewLifecycleOwner(), plants -> {
                            for (Plant plant : plants) {
                                viewModel.addPlant(plant);
                            }
                        });
                    }
                });
                return true;
            });

            notificationsTime.setOnPreferenceClickListener(p -> {
                TimePickerDialog timeDialog = new TimePickerDialog(getContext(), (timePicker, hour, minute) -> {
                    notificationsTime.setText(convertDayMonthToString(hour) + ":" + convertDayMonthToString(minute));
                }, Integer.parseInt(notificationsTime.getText().split(":")[0]), Integer.parseInt(notificationsTime.getText().split(":")[1]), true);
                timeDialog.show();
                return false;
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
}


