package ru.job4j.okhttp_example;

import android.support.annotation.NonNull;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpMaster {
    private void authorizationRequest(String name, String login) {
        String path = "https://api.github.com/users/whatever? client_id="
                + name + "&client_secret=" + login;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(path
                ).build();
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
                    System.out.println(strResponse);
                }
            }
        });
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        OkHttpMaster master = new OkHttpMaster();
        System.out.print("LOGIN: ");
        String name = scanner.next();
        System.out.print("TOKEN: ");
        String token = scanner.next();
        master.authorizationRequest(name, token);
    }
}
