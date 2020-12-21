package ru.job4j.okhttp_example;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    private String name = "";
    private String token = "";
    private boolean variant = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Switch changer = findViewById(R.id.method_selector_switch);
        EditText nameField = findViewById(R.id.name_editText);
        EditText passwordField = findViewById(R.id.password_editText);
        Button ok = findViewById(R.id.ok_button);
        changer.setOnCheckedChangeListener((buttonView, isChecked) -> {
            variant = isChecked;
            ok.setEnabled(isChecked || (!nameField.getText().toString().equals("")
                    && passwordField.getText().length() > 4));
            nameField.setEnabled(!isChecked);
            passwordField.setEnabled(!isChecked);
        });
        ok.setEnabled(variant);
        nameField.addTextChangedListener(new TextMaster() {
            @Override
            public void afterTextChanged(Editable s) {
                ok.setEnabled(s.length() > 0 && !token.equals(""));
                name = s.toString();
            }
        });
        passwordField.addTextChangedListener(new TextMaster() {
            @Override
            public void afterTextChanged(Editable s) {
                ok.setEnabled(s.length() > 4 && !name.equals(""));
                token = s.toString();
            }
        });
        ok.setOnClickListener(v -> selector(variant));
    }
    private void selector(boolean variant) {
        int id;
        id = variant ? 1 : 2;
        switch (id) {
            case 1: {
                click();
                break;
            }
            case 2: {
                click2();
                break;
            }
        }
    }
    private void click() {
        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putExtra("variant", variant);
        startActivity(intent);
    }
    private void click2() {
        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("token", token);
        startActivity(intent);
    }
}
