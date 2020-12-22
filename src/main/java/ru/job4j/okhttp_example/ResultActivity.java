package ru.job4j.okhttp_example;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import java.io.IOException;
import java.util.Objects;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ResultActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView information;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
        toolbar = findViewById(R.id.toolbar);
        setToolbar();
        this.information = findViewById(R.id.result_textView);
        OkHttpMaster master = new OkHttpMaster();
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getBooleanExtra("variant", false)) {
                master.loadPost();
            } else {
                master.loadInfo(intent.getStringExtra("name"),
                        intent.getStringExtra("token"));
            }
        }
    }
    private void setToolbar() {
        Intent intent = getIntent();
        if (toolbar != null) {
            if (intent != null) {
                toolbar.setTitle("Information");
            }
            setSupportActionBar(toolbar);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    private class OkHttpMaster {
        private String result;
        void loadPost() {
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
                        result = Objects.requireNonNull(response.body()).string();
                        information.post(() -> information.setText(result));
                    }
                }
            });
        }
        void loadInfo(String name, String token) {
            OkHttpClient client = new OkHttpClient.Builder().authenticator((route, response) -> {
                String credentials = Credentials.basic(
                        name, token);
                return response.request().newBuilder()
                        .addHeader("Authorization", credentials).build();
            }).build();
            String url = "https://api.github.com/users/" + name;
            Request request = new Request.Builder().url(url).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response)
                        throws IOException {
                    if (response.isSuccessful() && response.body() != null) {
                        result = response.body().string();
                        information.post(() -> information.setText(result));
                    }
                }
            });
        }
    }
}
