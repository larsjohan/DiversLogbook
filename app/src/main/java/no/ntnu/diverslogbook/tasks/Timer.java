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

    private long totalTime;






    public Timer(TextView textView, long millisInFuture) {
        super(millisInFuture, CALLBACK_INTERVAL);
        this.textView = textView;
        this.totalTime = millisInFuture;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        this.remainingMillis = millisUntilFinished;

        long sec = millisUntilFinished / 1000;
        long mm = sec / 60;
        long ss = sec % 60;
        String time = mm + ":" + ss;
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

    public long getTotalTime() {
        return totalTime;
    }
}
