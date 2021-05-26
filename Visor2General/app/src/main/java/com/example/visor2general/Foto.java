package com.example.visor2general;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import android.os.StrictMode;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
public class Foto extends AppCompatActivity {
    String api="https://lab-mrtecks.com/samsoft/pipcoffe/api/api_movil_general.php";
    public int TIEMPO = 1000;
    Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);
        ocultar_barras();
        String tv=PREFERENCES.obtenerString(Foto.this,PREFERENCES.PREFERENCE_TV);
        String publicidad=PREFERENCES.obtenerString(Foto.this,PREFERENCES.PREFERENCE_PUBLICIDAD);
        String tipo=PREFERENCES.obtenerString(Foto.this,PREFERENCES.PREFERENCE_TIPO_ARCHIVO);

        Log.d("pagina", tipo);
        getimagen(tv,publicidad,tipo);
        ejecutartarea();
    }
    public  void getimagen( String tv,String publicidad,String tipo){

        String sql = api+"?dato=obtener_archivo_publicidad_visor_imagen&visor="+tv+"&publicidad="+publicidad+"&tipo="+tipo;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Log.d("url2", sql);
        URL url = null;
        HttpURLConnection conn;
        try {
            url = new URL(sql);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            String json = "";
            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            json = response.toString();

            JSONArray jsonArr = null;

            jsonArr = new JSONArray(json);
            String archivo = "";
            String estado="";
            String registro="";
            ImageView image_View_objeto = (ImageView)findViewById(R.id.imageView);

            for(int i = 0;i<jsonArr.length();i++){
                JSONObject jsonObject = jsonArr.getJSONObject(i);
                archivo=jsonObject.optString("archivo");
                estado=jsonObject.optString("estado_consulta");
                registro=jsonObject.optString("registro");
            }
            Log.d("estado", estado);
            Log.d("registro",  registro  );
            if (registro.equals("no")){
                PREFERENCES.guardarbolean(Foto.this,PREFERENCES.PREFERENCE_ESTADO_BUTTON_SECCION,false);
                Intent intent = new Intent(Foto.this,MainActivity.class);
                startActivity(intent);
            }
if ("no" != archivo){
      Picasso.get().load(archivo).resize(800,800).into(image_View_objeto);
}else{
    if (registro.equals("no")){
    PREFERENCES.guardarbolean(Foto.this,PREFERENCES.PREFERENCE_ESTADO_BUTTON_SECCION,false);
        PREFERENCES.guardarString(Foto.this,PREFERENCES.PREFERENCE_TV,"");
        PREFERENCES.guardarString(Foto.this,PREFERENCES.PREFERENCE_TIPO_ARCHIVO,"");
        PREFERENCES.guardarString(Foto.this,PREFERENCES.PREFERENCE_PUBLICIDAD,"");

        Intent intent = new Intent(Foto.this,MainActivity.class);
    startActivity(intent);

    }
}
            Log.d("archivo", archivo);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void   ejecutartarea(){

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (get_actualizacion()>0){
                    Intent intent = new Intent(Foto.this,Foto.class);
                    startActivity(intent);
                    set_actualizacion().equals("true");
                }
                handler.postDelayed(this,TIEMPO);
            }
        },TIEMPO);
    }
    public int get_actualizacion(){
        int mensaje =2000;
        String sql = api+"?dato=ver_actualizacion";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = null;

        HttpURLConnection conn;
        try {
            url = new URL(sql);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            String json = "";
            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            json = response.toString();

            JSONArray jsonArr = null;

            jsonArr = new JSONArray(json);

            for(int i = 0;i<jsonArr.length();i++){
                JSONObject jsonObject = jsonArr.getJSONObject(i);
                mensaje=Integer.parseInt(jsonObject.optString("actualizacion"));
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mensaje;
    }

    public String set_actualizacion(){
        String mensaje="" ;
        String sql = api+"?dato=actualizar_publicidad";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        URL url = null;
        HttpURLConnection conn;
        try {
            url = new URL(sql);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            String json = "";
            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            json = response.toString();

            JSONArray jsonArr = null;

            jsonArr = new JSONArray(json);

            for(int i = 0;i<jsonArr.length();i++){
                JSONObject jsonObject = jsonArr.getJSONObject(i);
                mensaje= jsonObject.optString("estado");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mensaje;
    }
    private void ocultar_barras() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }



}