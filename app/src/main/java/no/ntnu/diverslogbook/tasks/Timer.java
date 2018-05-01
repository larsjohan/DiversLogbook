package no.ntnu.diverslogbook.tasks;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

public class Timer extends CountDownTimer {

    /**
     * Number of milliseconds to wait between each callback
     */
    private static final int CALLBACK_INTERVAL = 500;

    private TextView textView;

    private long remainingMillis;






    public Timer(TextView textView, long millisInFuture) {
        super(millisInFuture, CALLBACK_INTERVAL);
        this.textView = textView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        this.remainingMillis = millisUntilFinished;

        long sec = millisUntilFinished / 1000;
        Log.d("DiverApp", "sec: " + sec);
        long mm = sec / 60;
        Log.d("DiverApp", "mm: " + mm);
        long ss = sec % 60;
        Log.d("DiverApp", "ss: " + ss);
        String time = "Time Remaining: " + mm + ":" + ss;
        this.textView.setText(time);
    }

    @Override
    public void onFinish() {
        Log.d("DiverAppTimer", "Finished");
    }

    public long getRemainingMillis(){
        return this.remainingMillis;
    }

    public TextView getTextView() {
        return textView;
    }
}
