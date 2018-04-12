package no.ntnu.diverslogbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.b_testButton);

        button.setOnClickListener(view -> {
            Intent testLogin = new Intent(this, LoginActivity.class);
            startActivity(testLogin);
        });

    }
}
