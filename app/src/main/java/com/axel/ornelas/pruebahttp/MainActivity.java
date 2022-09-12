package com.axel.ornelas.pruebahttp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {

    private final String URL = "http://192.168.100.5:3000/error";
    private Button botonError;
    private Button botonCorrecto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        botonError = findViewById(R.id.error);
        botonCorrecto = findViewById(R.id.correcto);
        botonCorrecto.setOnClickListener(view -> {
            realizarPeticion(false);
        });
        botonError.setOnClickListener(view -> {
            realizarPeticion(true);
        });
    }


    public void realizarPeticion(boolean error) {
        final int timeOut = 120;
        MediaType JSONMediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(timeOut, TimeUnit.SECONDS);
        builder.readTimeout(timeOut, TimeUnit.SECONDS);
        builder.writeTimeout(timeOut, TimeUnit.SECONDS);
        OkHttpClient client = builder.build();
        String json = obtenerError(error).toString();
        System.out.println(json);
        okhttp3.RequestBody body = RequestBody.create(json, JSONMediaType);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(URL)
                .post(body)
                .build();
        try {
            okhttp3.Response response = client.newCall(request).execute();
            String result = response.body() != null ? response.body().string() : "ERROR";
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private JSONObject obtenerError(boolean error) {
        try {
            if (!error)
                return crearJson("No hay errores");
            int numero = Integer.parseInt("Hola");
            return null;
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return crearJson(sw.toString());
        }
    }
    private JSONObject crearJson(String descripcion) {
        try {
            JSONObject contenido = new JSONObject();
            contenido.put("descripcion", descripcion);
            contenido.put("contrasena", 1234);
            return contenido;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
