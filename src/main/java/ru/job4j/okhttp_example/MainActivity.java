package ru.job4j.okhttp_example;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import java.io.IOException;
import java.util.Objects;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private String url = "https://api.github.com/users/octocat";
    private String name = "";
    private String password = "";
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
                ok.setEnabled(s.length() > 0 && !password.equals(""));
                name = s.toString();
            }
        });
        passwordField.addTextChangedListener(new TextMaster() {
            @Override
            public void afterTextChanged(Editable s) {
                ok.setEnabled(s.length() > 4 && !name.equals(""));
                password = s.toString();
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
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(
                "https://jsonplaceholder.typicode.com/posts/1").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(@NonNull Call call,
                                   @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String strResponse = Objects.requireNonNull(response.body()).string();
                    MainActivity.this.runOnUiThread(() -> {
                        Intent intent = new Intent(
                                MainActivity.this, ResultActivity.class);
                        intent.putExtra("result", strResponse);
                        startActivity(intent);
                    });
                }
            }
        });
    }
    private void click2() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.authenticator((route, response) -> {
            String nameAndPassword = Credentials.basic(name, password);
            Request request = response.request().newBuilder().header(url, nameAndPassword).build();
            String strResult = request.toString();
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.putExtra("result", strResult);
            startActivity(intent);
            return request;
        });
    }
 /*   private void click3() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().header(name, password).url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(@NonNull Call call,
                                   @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String strResponse = Objects.requireNonNull(response.body()).string();
                    MainActivity.this.runOnUiThread(() -> {
                        Intent intent = new Intent(
                                MainActivity.this, ResultActivity.class);
                        intent.putExtra("result", strResponse);
                        startActivity(intent);
                    });
                }
            }
        });

    }*/
}
