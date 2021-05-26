package com.example.visor2general;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.Preference;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String api="https://lab-mrtecks.com/samsoft/pipcoffe/api/api_movil_general.php";
    Spinner spinner_visor_objeto;
    ArrayList<String> list_visor = new ArrayList<String>();
    ArrayList<String> list_codigo_visor = new ArrayList<String>();
    ArrayList<String> lista_envio = new ArrayList<String>();
    ArrayList<String> lista_recibo = new ArrayList<String>();
    ArrayList<String> lista_tipo = new ArrayList<String>();
    ArrayList<String> lista_nro_publig = new ArrayList<String>();//esta siempre va ser la misma
    ArrayList<String> lista_url = new ArrayList<String>();
    ArrayAdapter<String> adapter_visor;
    public int TIEMPO = 5000;
    Handler handler=new Handler();
Button btn_envio,btn_izquierda,btn_derecha;
ImageView img;
    VideoView vd;
  int grados=0;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ocultar_barras();

        if(PREFERENCES.obtenerBoleean(MainActivity.this,PREFERENCES.PREFERENCE_ESTADO_BUTTON_SECCION)){
            String tp=PREFERENCES.obtenerString(MainActivity.this,PREFERENCES.PREFERENCE_TIPO_ARCHIVO);
            if (tp.equals("foto")){
                Intent intent = new Intent(MainActivity.this,Foto.class);
             startActivity(intent);
            }else{
              Intent intent = new Intent(MainActivity.this,Video.class);
                startActivity(intent);
            }
        }

            getdata_visor();
            lista_envio.add("0");//visor
            lista_envio.add("0");//tipo
            lista_envio.add("0");//numero publicidad   nro_publig
//        lista_envio.add("0");//url
        lista_recibo.add("0");
            ejecutartarea();
            btn_envio=(Button)findViewById(R.id.button_envio);
  //      img=(ImageView)findViewById(R.id.logo);
    //    Picasso.get().load("http://www.lab-mrtecks.com/samsoft/pipcoffe/img/logo.jpg").resize(150,1500).into(img);
            btn_envio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lista_envio.get(0).equals("0")){
                        Toast toast1 = Toast.makeText(getApplicationContext(),"Seleccione un visor",Toast.LENGTH_LONG);
                        toast1.show();
                    }else{
                        verificar();
                    }
                }
            });
//btn_derecha=(Button)findViewById(R.id.btnderecha);
  //      btn_izquierda=(Button)findViewById(R.id.btnizquierda);
    //    img=(ImageView)findViewById(R.id.logo);
     /*   btn_derecha.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

grados=grados+90;
         img.setRotation(grados);
        PREFERENCES.guardarString(MainActivity.this,PREFERENCES.PREFERENCE_ROTACION,String.valueOf(grados));
         }
});
btn_izquierda.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        grados=grados-90;
        img.setRotation(grados);
        PREFERENCES.guardarString(MainActivity.this,PREFERENCES.PREFERENCE_ROTACION,String.valueOf(grados));
    }
});*/

    }
    private void verificar(){
        Log.d("Pagina", lista_envio.get(1).toLowerCase());
        PREFERENCES.guardarbolean(MainActivity.this,PREFERENCES.PREFERENCE_ESTADO_BUTTON_SECCION,true);
        PREFERENCES.guardarString(MainActivity.this,PREFERENCES.PREFERENCE_TV,lista_envio.get(0));
        PREFERENCES.guardarString(MainActivity.this,PREFERENCES.PREFERENCE_TIPO_ARCHIVO,lista_envio.get(1));
        PREFERENCES.guardarString(MainActivity.this,PREFERENCES.PREFERENCE_PUBLICIDAD,lista_envio.get(2));
        if ((lista_envio.get(1).equals("foto") )){
            Intent intent = new Intent(MainActivity.this,Foto.class);
            startActivity(intent);

        }else{
            Intent intent = new Intent(MainActivity.this,Video.class);
            startActivity(intent);

        }

    }
    public void   ejecutartarea(){

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("cantidad", String.valueOf(get_actualizacion()));
if (get_actualizacion()>0){

list_codigo_visor.clear();
list_visor.clear();
lista_envio.clear();
lista_recibo.clear();
lista_tipo.clear();
lista_nro_publig.clear();
    Log.d("actualizar", set_actualizacion());
      if(set_actualizacion().equals("true")){
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
}
}
                handler.postDelayed(this,TIEMPO);
            }
        },TIEMPO);


    }
    private  void getdata_visor(){

        adapter_visor = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list_visor);

        String sql = api+"?dato=visor";
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
                list_visor.add(jsonObject.optString("n"));
                list_codigo_visor.add(jsonObject.optString("i"));
                lista_tipo.add(jsonObject.optString("tipo"));
                lista_nro_publig.add(jsonObject.optString("nro_publig"));
lista_url.add(jsonObject.optString("url"));
            }
            adapter_visor.notifyDataSetChanged();
            spinner_visor_objeto=(Spinner)findViewById(R.id.spinner_visor);
            spinner_visor_objeto.setAdapter(adapter_visor);
            spinner_visor_objeto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    lista_envio.set(0,list_codigo_visor.get(position));
                    lista_envio.set(1,lista_tipo.get(position));
                    lista_envio.set(2,lista_nro_publig.get(position));
       /*             img=(ImageView)findViewById(R.id.logo);

if (lista_tipo.get(position).equals("foto")) {
    Picasso.get().load(lista_url.get(position)).resize(150, 1500).into(img);
}else{
    video_view_objeto.setVideoPath(archivo );
    video_view_objeto.start();

}*/

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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