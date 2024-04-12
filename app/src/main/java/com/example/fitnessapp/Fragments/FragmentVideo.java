package com.example.fitnessapp.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.fitnessapp.R;

public class FragmentVideo extends Fragment {

    private VideoView videoView;
    private Button btnReplay;
    private Button btnDone;
    private Button btnStartStop;
    private Button btnStartStopVideo;

    private TextView textViewDescription;
    private TextView textViewTimer;

    private CountDownTimer countDownTimer;
    private boolean timerRunning;
    private long timeLeftInMillis;

    public FragmentVideo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        videoView = view.findViewById(R.id.videoView5);
        btnReplay = view.findViewById(R.id.btnReplay);
        btnDone = view.findViewById(R.id.btnDone);
        btnStartStop = view.findViewById(R.id.btnStartStop);
        textViewDescription = view.findViewById(R.id.textViewDescription);
        textViewTimer = view.findViewById(R.id.textViewTimer);

        btnStartStopVideo = view.findViewById(R.id.btnStartStopVideo); // Initialize the new button


        btnReplay.setOnClickListener(v -> replayVideo());
        btnDone.setOnClickListener(v -> goBackToTraining());
        btnStartStop.setOnClickListener(v -> {
            if (btnStartStop.getText().toString().equals("Start")) {
                startTimer();
            } else {
                pauseTimer();
            }
        });


        // New button click listener to control video playback
        btnStartStopVideo.setOnClickListener(v -> {
            if (videoView.isPlaying()) {
                videoView.pause();
                btnStartStopVideo.setText("Start Video");
            } else {
                videoView.start();
                btnStartStopVideo.setText("Pause Video");
            }
        });






        // Retrieve video URL and description passed as arguments
        Bundle arguments = getArguments();
        if (arguments != null) {
            String videoUrl = arguments.getString("videoUrl");
            String description = arguments.getString("description");
            int minutes = arguments.getInt("minutes", 15); // Default to 15 minutes if not provided
            timeLeftInMillis = minutes * 60 * 1000; // Convert minutes to milliseconds
            if (videoUrl != null) {
                playVideo(videoUrl);
            }
            if (description != null) {
                textViewDescription.setText(description);
            }
            updateCountDownText(); // Update the timer text based on the initial time
        }
    }

    private void playVideo(String videoUrl) {
        MediaController mediaController = new MediaController(requireContext());
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse(videoUrl));
        videoView.requestFocus();
        videoView.start();
    }

    private void replayVideo() {
        videoView.seekTo(0);
        videoView.start();
        startTimer();
    }

    private void goBackToTraining() {
        stopTimer();
        Navigation.findNavController(requireView()).popBackStack();
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                btnStartStop.setText("Start");
                textViewTimer.setText("00:00");
                // Perform any actions you want when the timer finishes
            }
        }.start();

        timerRunning = true;
        btnStartStop.setText("Pause");
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        btnStartStop.setText("Start");
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        textViewTimer.setText(timeLeftFormatted);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopTimer();
    }
}
