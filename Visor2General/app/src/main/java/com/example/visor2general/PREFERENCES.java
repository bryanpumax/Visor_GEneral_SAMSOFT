package com.example.visor2general;

import android.content.Context;
import android.content.SharedPreferences;

public class PREFERENCES {
    public static final  String STRING_PREFERENCE="com.example.visor2general";
    public  static final  String PREFERENCE_ESTADO_BUTTON_SECCION="estado.button.session";
    public  static final  String PREFERENCE_PUBLICIDAD="main.cod_publicidad";
    public  static final  String PREFERENCE_TV="main.tv";
    public  static final  String PREFERENCE_TIPO_ARCHIVO="main.tipo";
    public  static final  String PREFERENCE_ROTACION="0";
    public  static  void guardarString(Context c, String key, String valor){
        SharedPreferences preferences =c.getSharedPreferences(STRING_PREFERENCE,c.MODE_PRIVATE);
        preferences.edit().putString(key,valor).apply();
    }
    public  static  void guardarbolean(Context c, String key, Boolean valor){
        SharedPreferences preferences =c.getSharedPreferences(STRING_PREFERENCE,c.MODE_PRIVATE);
        preferences.edit().putBoolean(key,valor).apply();
    }
    public static boolean obtenerBoleean(Context c, String key){
        SharedPreferences preferences=c.getSharedPreferences(STRING_PREFERENCE,c.MODE_PRIVATE);
        return preferences.getBoolean(key,false);
    }
    public  static String obtenerString(Context c, String key){
        SharedPreferences preferences=c.getSharedPreferences(STRING_PREFERENCE,c.MODE_PRIVATE);
        return preferences.getString(key,"");
    }

}
