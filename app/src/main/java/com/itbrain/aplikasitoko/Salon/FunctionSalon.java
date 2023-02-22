package com.itbrain.aplikasitoko.Salon;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;



public class FunctionSalon {
    static String slct = "SELECT * FROM ";
    static String base64Encode = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAg2zAfKTk1shNHVPqOmfoCheRp+OYT4jaPOvUaZASeXIRI6Bjvi5C+H9MqNU5N7VODZEtqliJaubljI2RFwCSWWmFcVSthhLNcEuTszv/rxgic8u7KI0EhvpFv9u2mAlbgOa3L9aMyUxThn1w89TWOarFtAiYktkMhGLZaWO5GQwnvq8Ip7HN/KNBZbsqfQI2YBhXnAphfMy1YaFs1shy3kD4QV027R9iaZ+htpqwQo6v/4qzmz6ltiKQ6EH987GQOO85+DGZgf5XSKyXIr4UXI4Mn/45JuPmfyHPwQrqvqOELutfRvpCpfliEi8PXCAQvgJKyiWYwNvCVXP3E34lswIDAQAB" ;

    public static String addSlashes(String s) {
        try {
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

    public static String getText(View v, int id){ //cara instal //FFunctionSalon.getText(this.findViewById(android.R.id.content), R.id.jumlah) ;
        TextView a = (TextView) v.findViewById(id) ;
        return a.getText().toString() ;
    }
    public static Boolean setText(View v, int id,String t){ //cara instal //FFunctionSalon.setText(this.findViewById(android.R.id.content), R.id.jumlah,"ini String") ;
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

    public static Boolean clearFocus(View v, int id){ //cara instal //FFunctionSalon.setText(this.findViewById(android.R.id.content), R.id.jumlah,"ini String") ;
        TextView a = (TextView) v.findViewById(id) ;
        try {
            a.clearFocus();
            return true ;
        }catch (Exception e){
            return false;
        }
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

    public static int getInt(Cursor c, String name){
        try {
            return c.getInt(c.getColumnIndex(name));
        }catch (Exception e){
            return 0;
        }
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
            bln = "0"+ FunctionSalon.intToStr(month) ;
        } else {
            bln = FunctionSalon.intToStr(month) ;
        }

        if(day < 10){
            hri = "0"+ FunctionSalon.intToStr(day) ;
        } else {
            hri = FunctionSalon.intToStr(day) ;
        }

        return FunctionSalon.intToStr(year) + bln+hri;
    }

    public static String setDatePickerNormal(int year , int month, int day) {
        String bln,thn,hri ;
        if(month < 10){
            bln = "0"+ FunctionSalon.intToStr(month) ;
        } else {
            bln = FunctionSalon.intToStr(month) ;
        }

        if(day < 10){
            hri = "0"+ FunctionSalon.intToStr(day) ;
        } else {
            hri = FunctionSalon.intToStr(day) ;
        }

        return hri+"/"+bln+"/"+FunctionSalon.intToStr(year);
    }

    public static String setTimePickerNormal(int hour , int minute) {
        String jam,menit ;
        if(hour < 10){
            jam = "0"+ FunctionSalon.intToStr(hour) ;
        } else {
            jam = FunctionSalon.intToStr(hour) ;
        }

        if(minute < 10){
            menit = "0"+ FunctionSalon.intToStr(minute) ;
        } else {
            menit = FunctionSalon.intToStr(minute) ;
        }

        return jam+":"+menit;
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

    public static String convertDate(String date){
        String[] a = date.split("/") ;
        return a[2]+a[1]+a[0];
    }

    public static String unNumberFormat(String number){
        try {
            String a = number.replace(",","-") ;
            String b = a.replace(".","") ;
            String c = b.replace("-",".") ;
            return c ;
        }catch (Exception e){
            return "" ;
        }
    }

    public static String changeComa(String number){
        try {
            String a = number.replace(",",".") ;
            return a ;
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

    public static String getYear(String date){
        try {
            String y = date.substring(0,4) ;
            return y ;
        }catch (Exception e){
            return "ini tahun" ;
        }
    }

    public static String getMonth(String date){
        try {
            String b1 = date.substring(4) ;

            String m = b1.substring(0,2) ;
            return m ;
        }catch (Exception e){
            return "ini bulan" ;
        }
    }

    public static String getDay(String date){
        try {
            String b1 = date.substring(6) ;

            String d = b1.substring(0,2) ;
            return d ;
        }catch (Exception e){
            return "ini hari" ;
        }
    }

    public static String timeToNormal(String time){
        try {
            String b1 = time.substring(2) ;

            String m = b1.substring(0,2) ;
            String h = time.substring(0,2) ;
            return h+":"+m ;
        }catch (Exception e){
            return "ini waktu" ;
        }
    }

    public static String getHour(String time){
        try {
            String h = time.substring(0,2) ;
            return h ;
        }catch (Exception e){
            return "ini jam" ;
        }
    }

    public static String getMinute(String time){
        try {
            String b1 = time.substring(2) ;

            String m = b1.substring(0,2) ;
            return m ;
        }catch (Exception e){
            return "ini menit" ;
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

    public static ActionBar btnBack(String title, ActionBar bar){
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle(title);
        return bar;
    }
    public static AlertDialog.Builder alertDialog(Context ctx, String title, String message, String pBtn,
                                                  DialogInterface.OnClickListener positive, String nBtn, DialogInterface.OnClickListener negative){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.create();
        builder.setTitle(title).setMessage(message)
                .setPositiveButton(pBtn,positive)
                .setNegativeButton(nBtn,negative);
        return builder;
    }
    public static AlertDialog.Builder alertDialog(Context ctx, String message, String pBtn,
                                                  DialogInterface.OnClickListener positive,String nBtn, DialogInterface.OnClickListener negative){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.create();
        builder.setPositiveButton(pBtn,positive)
                .setNegativeButton(nBtn,negative);
        return builder;
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

    public static String removeEE(double value){
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(3);
        return FunctionSalon.numberFormat(df.format(value)) ;
    }

    public static String removeE(double value){
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        return FunctionSalon.numberFormat(df.format(value)) ;
    }
    public static String removeE(String value){
        double hasil = FunctionSalon.strToDouble(value) ;
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        return FunctionSalon.numberFormat(df.format(hasil)) ;
    }

    public static String removeComma(String value){
        try{
            String a=value.replace(",0","");
            return a;
        }catch (Exception e){
            return "";
        }
    }

    public static Boolean copyFile(String pIn, String pOut, String name){
        try{
            File file = new File(pOut);
            if (!file.exists()) {
                file.mkdirs();
            }
            InputStream in = new FileInputStream(pIn + name);
            OutputStream out = new FileOutputStream(pOut + name);
            byte[] buffer = new byte[1024];
            while (true) {
                int read = in.read(buffer);
                if (read != -1) {
                    out.write(buffer, 0, read);
                } else {
                    in.close();
                    out.flush();
                    out.close();
                    return Boolean.valueOf(true);
                }
            }
        } catch (Exception e) {
            return Boolean.valueOf(false);
        }

    }

    public static Boolean deleteFile(String path){
        try {
            new File(path).delete();
            return Boolean.valueOf(true);
        } catch (Exception e) {
            return Boolean.valueOf(false);
        }
    }


    public static Boolean renameFile(String path, String namaLama, String namaBaru){
        try {
            new File(path + namaLama).renameTo(new File(path + namaBaru));
            return Boolean.valueOf(true);
        } catch (Exception e) {
            return Boolean.valueOf(false);
        }

    }

//    public static String getEncrypt(String teks) {
//        try {
//            return Encryption.getDefault("KomputerKit", "KomputerKit", new byte[16]).encryptOrNull(teks);
//        } catch (Exception e) {
//            return "";
//        }
//    }
//
//    public static String getDecrypt(String teks) {
//        try {
//            return Encryption.getDefault("KomputerKit", "KomputerKit", new byte[16]).decryptOrNull(teks);
//        } catch (Exception e) {
//            return "";
//        }
//    }

    public static String getDeviceID(ContentResolver c){
        return Settings.Secure.getString(c, Settings.Secure.ANDROID_ID);
    }

    public static String getBase64Code(){
        return base64Encode ;
    }
}
