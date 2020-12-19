package ru.job4j.okhttp_example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class OkHttpMaster {
    private void authorizationRequest(String name, String token) {
        String path = "https://api.github.com";
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(path).openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setConnectTimeout(400);
            connection.setReadTimeout(400);
            connection.connect();
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                System.out.println("<<< ЗАПРОС ВЫПОЛНЕН УСПЕШНО >>>");
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }
                System.out.println(builder.toString());
            } else {
                System.out.println("СЛУЧИЛОСЬ ТАКОЕ ДЕРЬМО: "
                        + connection.getResponseCode() + ", " + connection.getResponseMessage());
            }
        }catch (Throwable trouble) {
            trouble.printStackTrace();
        } finally {
            if (connection != null) {
                System.out.println("<<< ПОДКЛЮЧЕНИЕ ЗАВЕРШЕНО >>>");
                connection.disconnect();
            }
        }
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
