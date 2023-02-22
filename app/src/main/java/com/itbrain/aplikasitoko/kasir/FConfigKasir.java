package com.itbrain.aplikasitoko.kasir;

import android.content.SharedPreferences;
import android.os.Environment;

/**
 * Created by KomputerKit on 31/05/2017.
 */

public class FConfigKasir {
    static Boolean inAppPurchase = true ;

    static String type = "sqlite";
    static String host = "";
    static String user = "";
    static String pass = "";
    static String db   = "kasirsupermudah" ;
    static String dirBackup = "kasirsupermudah" ;
    static int version = 1 ;
    static SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;

    public FConfigKasir(SharedPreferences sharedPreferences){
        this.sharedPreferences = sharedPreferences ;
        editor = sharedPreferences.edit() ;
    }

    public String getType(){
        return sharedPreferences.getString("type",type) ;
    }
    public void setType(String type){
        editor.putString("type",type) ;
        editor.apply();
    }

    public String getHost(){
        return sharedPreferences.getString("host",host) ;
    }
    public void setHost(String host){
        editor.putString("host",host) ;
        editor.apply();
    }

    public String getUser(){
        return sharedPreferences.getString("user",user) ;
    }
    public void setUser(String user){
        editor.putString("user",user) ;
        editor.apply();
    }

    public String getPass(){
        return sharedPreferences.getString("password",pass) ;
    }
    public void setPass(String pass){
        editor.putString("password",pass) ;
        editor.apply();
    }

    public static String getDb(){
        return sharedPreferences.getString("database",db) ;
    }
    public void setDb(String db){
        editor.putString("database",db) ;
        editor.apply();
    }

    public int getVersion(){
        return sharedPreferences.getInt("version",1) ;
    }
    public void setVersion(int version){
        editor.putInt("version",version) ;
        editor.apply();
    }

    public String getDirBackup(){
        return sharedPreferences.getString("dirBackup", Environment.getExternalStorageDirectory()+"/"+dirBackup+"/") ;
    }
    public void setDirBackup(String path){
        editor.putString("dirBackup",path) ;
        editor.apply();
    }

    public String getCustom(String key,String def){
        return sharedPreferences.getString(key, def) ;
    }
    public void setCustom(String key,String value){
        editor.putString(key,value) ;
        editor.apply();
    }
}
