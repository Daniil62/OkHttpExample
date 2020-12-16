package ru.job4j.okhttp_example;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
        TextView result = findViewById(R.id.result_textView);
        Intent intent = getIntent();
        if (intent != null) {
            result.setText(intent.getStringExtra("result"));
        }
    }
}
