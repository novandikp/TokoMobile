package com.itbrain.aplikasitoko.bengkel;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;



public class ModulBengkel {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    static String db = "dbtoko";
    static String dirBackup = "dbtoko";
    static int version = 1;


    ModulBengkel(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        editor = sharedPreferences.edit();
    }

    public void setCustom(String key,String value){
        editor.putString(key,value) ;
        editor.apply();
    }

    public String getCustom(String key,String def){
        return sharedPreferences.getString(key, def) ;
    }


    public int getVersion() {
        return sharedPreferences.getInt("version", 1);
    }

    public String getDb() {
        return sharedPreferences.getString("database", db);
    }
    public static String setDatePickerNormal(int year , int month, int day) {
        String bln,thn,hri ;
        if(month < 10){
            bln = "0"+ ModulBengkel.intToStr(month) ;
        } else {
            bln = ModulBengkel.intToStr(month) ;
        }

        if(day < 10){
            hri = "0"+ ModulBengkel.intToStr(day) ;
        } else {
            hri = ModulBengkel.intToStr(day) ;
        }

        return hri+"/"+bln+"/"+ModulBengkel.intToStr(year);
    }

    public static String addSlashes(String s) {
        try{
            s = s.replaceAll("\\\\", "\\\\\\\\");
            s = s.replaceAll("\\n", "\\\\n");
            s = s.replaceAll("\\r", "\\\\r");
            s = s.replaceAll("\\00", "\\\\0");
            s = s.replaceAll("'", "\"");
            return s;
        }catch (Exception e){
            return "";
        }

    }
    public static String unNumberFormat(String number){
        try {
            String a = number.replace(",","-") ;
            String b = a.replace(".","") ;
//            String c = b.replace("-",".") ;
            return b;
        }catch (Exception e){
            return "" ;
        }
    }

    public static String upperCaseFirst(String value) {

        // Convert String to char array.
        char[] array = value.toCharArray();
        // Modify first element in array.
        array[0] = Character.toUpperCase(array[0]);
        // Return string.
        return new String(array);
    }

    public  static  String toUppercase(String kata){
        kata=kata.toUpperCase();
        return  kata;
    }

    public static Boolean copyFile(String pIn, String pOut, String name){
        InputStream in ;
        OutputStream out ;

        try{
            File file = new File(pOut) ;
            if(!file.exists()){
                file.mkdirs() ;
            }

            in = new FileInputStream(pIn+name) ;
            out = new FileOutputStream(pOut+name) ;

            byte[] buffer = new byte[1024] ;
            int read ;
            while((read = in.read(buffer)) != -1){
                out.write(buffer,0,read) ;
            }

            in.close();
            in = null ;

            out.flush();
            out.close();
            out = null ;

            return true ;
        }catch (Exception e){
            return false ;
        }
    }

    public static Boolean deleteFile(String path){
        try {
            File del = new File(path) ;
            del.delete() ;
            return true ;
        }catch (Exception e){
            return false ;
        }
    }

    public static Boolean renameFile(String path, String namaLama, String namaBaru){
        try{
            File a = new File(path+namaLama) ;
            File b = new File(path+namaBaru) ;
            a.renameTo(b) ;

            return true ;
        }catch (Exception e){
            return false ;
        }
    }

    public static String intToStr(int a){
        return String.valueOf(a) ;
    }

    public static int strToInt(String v){
        try {
            return Integer.parseInt(v) ;
        } catch (Exception e){
            return 0 ;
        }
    }

    public static double strToDouble(String v){
        try {
            return Double.parseDouble(v) ;
        }catch (Exception e){
            return 0 ;
        }
    }

    public static String doubleToStr(double d){
        try {
            return String.valueOf(d) ;
        }catch (Exception e){
            return "" ;
        }
    }

    public static String setCenter(String item){
        int leng = item.length() ;
        String hasil = "" ;
        for(int i=0 ; i<32-leng;i++){
            if((32-leng)/2+1 == i){
                hasil += item ;
            } else {
                hasil += " " ;
            }
        }
        return hasil ;
    }
    public static String getStrip(){
        String a = "" ;
        for(int i = 0 ; i < 32 ; i++){
            a+="-" ;
        }
        return a+"\n" ;
    }
    public static String setRight(String item){
        int leng = item.length() ;
        String hasil = "" ;
        for(int i=0 ; i<32-leng;i++){
            if((31-leng) == i){
                hasil += item ;
            } else {
                hasil += " " ;
            }
        }
        return hasil ;
    }
    public static String setRight(String item,int min){
        int leng = item.length() ;
        int total = 32-min ;
        String hasil = "" ;
        for(int i=0 ; i<total-leng;i++){
            if((total-leng) == i){
                hasil += item ;
            } else {
                hasil += " " ;
            }
        }
        return hasil ;
    }
    public static String getDate(String type){ //Random time type : HHmmssddMMyy
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(type);
        String formattedDate = df.format(c.getTime());
        return formattedDate ;
    }

    public static String getEnter(){
        return "\n" ;
    }

    public static String removeE(double value){
        NumberFormat nf = NumberFormat.getInstance(new Locale("id", "id"));
        return nf.format(value);
    }
    public static String removeE(String value){
        double number = strToDouble(value);
        NumberFormat nf = NumberFormat.getInstance(new Locale("id", "id"));
        return nf.format(number);
    }


    public static String numberFormat(String number){ // Rp. 1,000,000.00
        try{
            String hasil = "";
            String[] b = number.split("\\.") ;

            if(b.length == 1){
                String[] a = number.split("") ;
                int c=0 ;
                for(int i=a.length-1;i>=0;i--){
                    if(c == 3 && !TextUtils.isEmpty(a[i])){
                        hasil = a[i] + "." + hasil ;
                        c=1;
                    } else {
                        hasil = a[i] + hasil ;
                        c++ ;
                    }
                }
            } else {
                String[] a = b[0].split("") ;
                int c=0 ;
                for(int i=a.length-1;i>=0;i--){
                    if(c == 3 && !TextUtils.isEmpty(a[i])){
                        hasil = a[i] + "." + hasil ;
                        c=1;
                    } else {
                        hasil = a[i] + hasil ;
                        c++ ;
                    }
                }
                hasil+=","+b[1] ;
            }
            return  hasil ;
        }catch (Exception e){
            return  "" ;
        }
    }

    public static String quote(String w){
        return "\'" + addSlashes(w) + "\'" ;
    }

    public static String getText(View v, int id){ //cara instal //FFunction.getText(this.findViewById(android.R.id.content), R.id.jumlah) ;
        TextView a = (TextView) v.findViewById(id) ;
        return a.getText().toString() ;
    }
    public static void showToast(Context mContext, String message){
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
    public static Boolean setText(View v, int id,String t){ //cara instal //FFunction.setText(this.findViewById(android.R.id.content), R.id.jumlah,"ini String") ;
        TextView a = (TextView) v.findViewById(id) ;
        try {
            a.setText(t);
            return true ;
        }catch (Exception e){
            return false;
        }
    }


    /////////////////////////////QUERY???????????????????
    static String slct = "SELECT * FROM " ;

    public static String quoteForLike(String w){
        return "\'%" + addSlashes(w) + "%\'" ;
    }

    public static String select(String table){
        return slct + table;
    }

    public static String selectwhere(String table){
        return slct + table + " WHERE ";
    }

    public static String sOrderASC(String key){
        return " ORDER BY "+key+" ASC" ;
    }

    public static String sOrderDESC(String key){
        return " ORDER BY "+key+" DESC" ;
    }

    public static String sWhere(String key, String value){
        return key+"="+ quote(value) ;
    }

    public static String sLike(String key, String value){
        return key+" LIKE "+quoteForLike(value) ;
    }

    public static String sBetween(String key, String v1, String v2){
        return toDateNormal(key)+" BETWEEN "+ toDateNormal(quote(v1)) +" AND "+ toDateNormal(quote(v2));
    }

    public static String toDateNormal(String key){
        return "strftime('%Y%m%d',datetime(substr("+key+",7,4) || '-' || substr("+key+",4,2) || '-' || substr("+key+",1,2)))";
    }

    public static String sCount(String table,String key){
        return "SELECT COUNT("+key+") FROM "+table ;
    }

    public static String sSum(String table,String key){
        return "SELECT SUM("+key+") FROM "+table ;
    }

    public static String sAvg(String table,String key){
        return "SELECT AVG("+key+") FROM "+table ;
    }
    public static String getString(Cursor c, String name){
        try {
            return c.getString(c.getColumnIndex(name));
        }catch (Exception e){
            return "";
        }
    }

    public static int getCount(Cursor c){
        try{
            return c.getCount();
        }catch (Exception e){
            return 0;
        }
    }



    public static String splitParam(String query, String[] p){
        String pecah[] = query.split("\\?") ;
        if(pecah.length > 1){
            String fix = "" ;
            if((p.length+1) != pecah.length){
                return "gagal" ;
            } else {
                int i ;
                for (i = 0; i < p.length; i++) {
                    fix += pecah[i] + quote(p[i]) ;
                }
                fix += pecah[i] ;

                return fix ;
            }
        } else {
            return query ;
        }
    }

    public static String slimit(String tabel,String keyCari, String pencarian){
        String hasil = "" ;
        if(TextUtils.isEmpty(pencarian)){
            hasil = ModulBengkel.select(tabel) +" LIMIT 30"  ;
        } else {
            hasil = ModulBengkel.selectwhere(tabel) +ModulBengkel.sLike(keyCari,pencarian) ;
        }
        return hasil ;
    }

    public static String splitParam(String query){
        return query ;
    }



    public static String getEncrypt(String input) {
        // This is base64 encoding, which is not an encryption
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    public static String getDecrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }

}


