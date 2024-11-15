package com.example.fitnessapp.Fragments;

//import static com.example.fitnessapp.classes.NotificationReceiver.CHANNEL_ID;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.fitnessapp.R;
import com.example.fitnessapp.classes.NotificationService;

import java.util.Calendar;

public class FragmentNotification extends Fragment {

    private TimePicker timePicker;
    private DatePicker datePicker;
    private Button btnScheduleNotification;
    private Button btnSkip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timePicker = view.findViewById(R.id.timePicker);
        datePicker = view.findViewById(R.id.datePicker);
        btnScheduleNotification = view.findViewById(R.id.btn_schedule_notification);
       btnSkip= view.findViewById(R.id.btnSkip);


        // Check if notification permission is granted
        if (!NotificationManagerCompat.from(requireContext()).areNotificationsEnabled()) {
            // If notifications are not enabled, show a dialog prompting the user to enable them
            askToEnableNotifications();
        } else {
            // If notifications are enabled, proceed with setting the notification
            btnScheduleNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setNotification();
                    Navigation.findNavController(view).navigate(R.id.action_fragmentNotification_to_fragmentHome22);

                }
            });
        }

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), "Notification not scheduled", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.action_fragmentNotification_to_fragmentHome22);

            }
        });
    }

    private void setNotification() {
        int hour;
        int minute;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
        } else {
            hour = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
        }

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        long timeInMillis = calendar.getTimeInMillis();

        // Schedule the notification
        NotificationService.scheduleNotification(requireContext(), timeInMillis);
        Toast.makeText(requireContext(), "Notification scheduled", Toast.LENGTH_SHORT).show();
    }

    // Open app notification settings
    private void askToEnableNotifications() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Enable Notifications")
                .setMessage("To receive workout reminders, please enable notifications for this app.")
                .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Open app notification settings
                        openNotificationSettings();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle cancelation, maybe show a message or dismiss the fragment
                    }
                })
                .show();
    }

    // Open app notification settings
    private void openNotificationSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().getPackageName());
        startActivity(intent);
    }
}
