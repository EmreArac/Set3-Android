package com.lab.set3;

// Name: Emre ARACI
// Student ID: 56905
// Lab: SET 3 - Calculator

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    public static ArrayList<String> history = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        TextView textViewHistory = findViewById(R.id.textViewHistory);
        Button btnBack = findViewById(R.id.btnBack);

        if (history.isEmpty()) {
            textViewHistory.setText("No history yet.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (String entry : history) {
                sb.append(entry).append("\n");
            }
            textViewHistory.setText(sb.toString());
        }

        btnBack.setOnClickListener(v -> finish());
    }
}