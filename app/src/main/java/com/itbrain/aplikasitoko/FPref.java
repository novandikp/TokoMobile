package com.itbrain.aplikasitoko;

import android.content.SharedPreferences;
import android.os.Environment;

public class FPref {
    public static String type = "sqlite";
    public  static String host = "";
    public   static String user = "";
    public  static String pass = "";
    public static String db   = "db_restaurant" ;
    public    static String dirBackup = "POS Restaurant" ;
    static String licenseKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoNrkRcDe/5cwfdVDT4vcdvPSl1FExbD9x5MSot43zpwHi4Kfcql6vfi9ru2F/CzDWewge2ON0Laf8buScfXg8HdE06AGojfolMvjUFvlcZ9j9TbSZqveo8MTkCUvjzf4YUfMirrwuf03++urTWmNUl/8IR9K6xjH++3Oy9Misc3CEPlZSG73UvjlaXs+JLQUZ0P+dXagIicvSdakgeJ52EkBYVD8P/VBGpC+QfNe3h4riUT5j+HdC3Mxdqa0p3jKcdAbfpo5MKuEoWB1Tf/mEd6mxA/z2cliEAHA/SWhsYi4nen5cbJ9iraQYBE7Xg0ptkgxyByw0KnNzGU+4w+IvQIDAQAB";
    static String productID="versipro_posrestaurant";
    //    static String productID="android.test.purchased";
    static String produkid2="com.komputerkit.posrestaurant.fullver";
//


    public static int version = 1 ;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;

    public FPref(SharedPreferences sharedPreferences){
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

    public String getDb(){
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
