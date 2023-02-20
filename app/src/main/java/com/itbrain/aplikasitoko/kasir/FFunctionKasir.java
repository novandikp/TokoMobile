package com.itbrain.aplikasitoko.kasir;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// import se.simbio.encryption.Encryption;

/**
 * Created by KomputerKit on 31/05/2017.
 */

public class FFunctionKasir {
    static String slct = "SELECT * FROM " ;
    static String base64Encode = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4NtN23cfmquUGNNC/svuU8phwaIUXZbO78uAM7FG5GQM96TxR4WiciRmpDtPfcVtSsg9DyL7mmCxhuwyS3KYJPAT/1IasDOnNRHtoyHM2fNbn3QqysseCyAaKA67s2YBMGl95G4TU1b7F2sdkKQrETfciYtmOl3GqYD3IB+UoerE+hs/PmOVjovfYjUTgGRR4KQz9nKArslFZgtGQWfBQfBXciBXkyhAl1xFJL+FLFjE/F14UZiRy599K9H3QQfMXYU6c0/TGTF9NoI4UIKwTxSKsjn7IOtKrYeEsCP+kBmX/jITEqXxmv1zsi32axqTv9/Ox1eHfjAMlfx8L4ZO6wIDAQAB" ;
    Context context ;

    public FFunctionKasir (Context context) {
        this.context = context;
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

    public static int getCount(Cursor c){
        try{
         return c.getCount();
        }catch (Exception e){
            return 0;
        }
    }

    public static String DateFormatChanger(String FormatAsal, String FormatTujuan, String Tgl){
        SimpleDateFormat sdf = new SimpleDateFormat(FormatAsal);
        try {
            Date dEnd = sdf.parse(Tgl);
            sdf = new SimpleDateFormat(FormatTujuan);
            Tgl = sdf.format(dEnd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Tgl;
    }

    public static String quote(String w){
        return "\'" + addSlashes(w) + "\'" ;
    }

    public static String intToStr(int a){
        try {
            return String.valueOf(a);
        }catch (Exception e){
            return "";
        }
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

    public static String getText(View v, int id){ //cara instal //FFunction.getText(this.findViewById(android.R.id.content), R.id.jumlah) ;
        TextView a = (TextView) v.findViewById(id) ;
        return a.getText().toString() ;
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
    public static String getSpinnerItem(View v,int idSpinner){
        try {
            Spinner s = (Spinner) v.findViewById(idSpinner) ;
            return s.getSelectedItem().toString() ;
        }catch (Exception e){
            return "" ;
        }
    }

    public static Boolean clearFocus(View v, int id){ //cara instal //FFunction.setText(this.findViewById(android.R.id.content), R.id.jumlah,"ini String") ;
        TextView a = (TextView) v.findViewById(id) ;
        try {
            a.clearFocus();
            return true ;
        }catch (Exception e){
            return false;
        }
    }

    @SuppressLint("Range")
    public static String getString(Cursor c, String name){
        try{
            return c.getString(c.getColumnIndex(name)) ;
        } catch (Exception e){
            return "" ;
        }
//        return c.getString(c.getColumnIndex(name)) ;
    }

    @SuppressLint("Range")
    public static int getInt(Cursor c, String name){
        return c.getInt(c.getColumnIndex(name)) ;
    }

    public static String getDate(String type){ //Random time type : HHmmssddMMyy
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(type);
        String formattedDate = df.format(c.getTime());
        return formattedDate ;
    }

    public static String getPerfectTime(){
        Calendar c = Calendar.getInstance();
        return c.getTime().toString() ;
    }

    public static String setDatePicker(int year , int month, int day) {
        String bln,thn,hri ;
        if(month < 10){
            bln = "0"+ FFunctionKasir.intToStr(month) ;
        } else {
            bln = FFunctionKasir.intToStr(month) ;
        }

        if(day < 10){
            hri = "0"+ FFunctionKasir.intToStr(day) ;
        } else {
            hri = FFunctionKasir.intToStr(day) ;
        }

        return FFunctionKasir.intToStr(year) + bln+hri;
    }

    public static String setDatePickerNormal(int year , int month, int day) {
        String bln,thn,hri ;
        if(month < 10){
            bln = "0"+ FFunctionKasir.intToStr(month) ;
        } else {
            bln = FFunctionKasir.intToStr(month) ;
        }

        if(day < 10){
            hri = "0"+ FFunctionKasir.intToStr(day) ;
        } else {
            hri = FFunctionKasir.intToStr(day) ;
        }

        return hri+"/"+bln+"/"+FFunctionKasir.intToStr(year);
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

    public static String unNumberFormat(String number){
        try {
            String a = number.replace(",","-") ;
            String b = a.replace("\\.","") ;
            String c = b.replace("-",".") ;
            return c ;
        }catch (Exception e){
            return "" ;
        }
    }

    public static String dateToNormal(String date){
        try {
            String b1 = date.substring(4) ;
            String b2 = b1.substring(2) ;

            String m = b1.substring(0,2) ;
            String d = b2.substring(0,2) ;
            String y = date.substring(0,4) ;
            return d+"/"+m+"/"+y ;
        }catch (Exception e){
            return "ini tanggal" ;
        }
    }

    public static String getCampur(String satu,String dua){
        return satu+"__"+dua ;
    }
    public static String getCampur(String satu,String dua,String tiga){
        return satu+"__"+dua+"__"+tiga ;
    }
    public static String getCampur(String satu,String dua,String tiga, String empat){
        return satu+"__"+dua+"__"+tiga+"__"+empat ;
    }
    public static String getCampur(String satu,String dua,String tiga, String empat,String lima){
        return satu+"__"+dua+"__"+tiga+"__"+empat+"__"+lima ;
    }
    public static String getCampur(String satu,String dua,String tiga, String empat,String lima,String enam){
        return satu+"__"+dua+"__"+tiga+"__"+empat+"__"+lima+"__"+enam ;
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

    public static String getEnter(){
        return "\n" ;
    }

    public static String removeE(double value){
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        return FFunctionKasir.numberFormat(df.format(value)) ;
    }
    public static String removeE(String value){
        double hasil = FFunctionKasir.strToDouble(value) ;
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        return FFunctionKasir.numberFormat(df.format(hasil)) ;
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

//    public static String getEncrypt(String teks){
//        try {
//            String key = "KomputerKit";
//            String salt = "KomputerKit";
//            byte[] iv = new byte[16];
//            Encryption encryption = Encryption.getDefault(key, salt, iv);
//
//            String hasil = encryption.encryptOrNull(teks);
//            return hasil ;
//        } catch (Exception e){
//            return "" ;
//        }
//    }
//
//
//
//    public static String getDecrypt(String teks){
//        try {
//            String key = "KomputerKit";
//            String salt = "KomputerKit";
//            byte[] iv = new byte[16];
//           // Encryption encryption = Encryption.getDefault(key, salt, iv);
//
//            String hasil = encryption.decryptOrNull(teks);
//            return hasil ;
//        } catch (Exception e){
//            return "" ;
//        }
//    }

    public static String getDeviceID(ContentResolver c){
        return Secure.getString(c, Secure.ANDROID_ID);
    }

    public static String getBase64Code(){
        return base64Encode ;
    }

    public void showAlert(String teks, final DialogInterface.OnClickListener listener){
        AlertDialog.Builder alertDialog ;
        AlertDialog alert ;
        alertDialog = new AlertDialog.Builder(context) ;
        alertDialog.setMessage(teks)
                .setCancelable(false)
                .setPositiveButton("OK", listener) ;

        alert = alertDialog.create() ;
        alert.show() ;
    }
}

