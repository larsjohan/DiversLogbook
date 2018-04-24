package no.ntnu.diverslogbook;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


/**
 * Manages the adding of security stops.
 */
public class SecurityStops extends AppCompatActivity {


    /**
     * Creates the layout for adding security stops.
     *
     * @param savedInstanceState The saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_stops);
        setTitle(R.string.plan_security_stops_title);

        Button save = findViewById(R.id.saveStops);
        save.setOnClickListener((v) -> goBack(v));
    }


    /**
     * Returns the data from this activity to the previous one.
     *
     * @param view The view
     */
    public void goBack(View view){
        Intent returnIntent = new Intent();

        //returnIntent.putExtra("result", input);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
