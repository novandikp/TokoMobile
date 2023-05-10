package com.itbrain.aplikasitoko.tabungan;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

//import se.simbio.encryption.Encryption;

public class ModulTabungan {
    Context context;

    public ModulTabungan(Context context) {
        this.context = context;
    }

    static String slct = "SELECT * FROM " ;
    public static String addSlashes(String s) {
        s = s.replaceAll("\\\\", "\\\\\\\\");
        s = s.replaceAll("\\n", "\\\\n");
        s = s.replaceAll("\\r", "\\\\r");
        s = s.replaceAll("\\00", "\\\\0");
        s = s.replaceAll("'", "\"");
        return s;
    }
    public static String quote(String word){
        return "\'" + addSlashes(word) + "\'" ;
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
    @SuppressLint("Range")
    public static String getString(Cursor c, String name){
        try{
            return c.getString(c.getColumnIndex(name)) ;
        }catch (Exception e){
            return "";
        }
    }
    @SuppressLint("Range")
    public static int getInt(Cursor c, String name){
        try {
            return c.getInt(c.getColumnIndex(name)) ;
        }catch (Exception e){
            return 0;
        }
    }
    @SuppressLint("Range")
    public static double getDouble(Cursor c, String name){
        return c.getDouble(c.getColumnIndex(name)) ;
    }
    public static String intToStr(int integer){
        return String.valueOf(integer) ;
    }

    public static int strToInt(String string){
        try {
            return Integer.parseInt(string) ;
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
    public static String setDatePickerNormal(int year , int month, int day) {
        String bln,thn,hri ;
        if(month < 10){
            bln = "0"+ ModulTabungan.intToStr(month) ;
        } else {
            bln = ModulTabungan.intToStr(month) ;
        }

        if(day < 10){
            hri = "0"+ ModulTabungan.intToStr(day) ;
        } else {
            hri = ModulTabungan.intToStr(day) ;
        }

        return hri+"/"+bln+"/"+ModulTabungan.intToStr(year);
    }
    public static String getDate(String type){ //Random time type : HHmmssddMMyy
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(type);
        String formattedDate = df.format(c.getTime());
        return formattedDate ;
    }
    public static Integer getDate(Calendar c,String type){ //Random time type : HHmmssddMMyy
        SimpleDateFormat df = new SimpleDateFormat(type);
        String formattedDate = df.format(c.getTime());
        return Integer.valueOf(formattedDate) ;
    }
    public static String getDate2(Calendar c,String type){ //Random time type : HHmmssddMMyy
        SimpleDateFormat df = new SimpleDateFormat(type);
        String formattedDate = df.format(c.getTime());
        return String.valueOf(formattedDate) ;
    }
    public static String convertDate(String date) {
        String[] a = date.split("/");
        return a[2] + a[1] + a[0];
    }
    public static Date dateToDate(String yyyyMMdd){
        try {
            String b1 = yyyyMMdd.substring(4) ;
            String b2 = b1.substring(2) ;

            String m = b1.substring(0,2) ;
            String d = b2.substring(0,2) ;
            String y = yyyyMMdd.substring(0,4) ;
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.valueOf(y),Integer.valueOf(m)-1,Integer.valueOf(d));
            return calendar.getTime();
        }catch (Exception e){
            return Calendar.getInstance().getTime() ;
        }
    }

    public static String removeE(double value){
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        return ModulTabungan.numFormat(df.format(value)) ;
    }
    public static String removeE(String value) {
        double hasil = ModulTabungan.strToDouble(value);
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        return ModulTabungan.numFormat(df.format(hasil));
    }
    public static String removeE2(double value){
        DecimalFormat df = new DecimalFormat("0");
        df.setMaximumFractionDigits(340);
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        return ModulTabungan.numFormat(df.format(value));
    }
    public static String justRemoveE(String value) {
        double hasil = ModulTabungan.strToDouble(value);
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        return df.format(hasil);
    }
    public static String minimalizingDecimal(String value){
        String finalValue=value;
        if (value.contains(".")){
            String[] a=value.split("\\.");
            finalValue=a[0]+"."+a[1];
            if (a[1].length()>2){
                finalValue=a[0]+"."+a[1].substring(0,2);
            }
        }
        return finalValue;
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
    public static Long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
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

    public static String numFormat(String number){
        String[] part;
        Locale locale=new Locale("in","ID");
        NumberFormat nf= NumberFormat.getCurrencyInstance(locale);
        DecimalFormatSymbols decimalFormatSymbols=((DecimalFormat)nf).getDecimalFormatSymbols();
        char ds=decimalFormatSymbols.getDecimalSeparator();
        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat)nf).setDecimalFormatSymbols(decimalFormatSymbols);
        String output="0";
        try {
            if (number.contains(".")){
                part=number.split("\\.");
                double a=Double.valueOf(part[0]);
                String b=nf.format(a);
                String dec=part[1];
                if (part[1].equals("0")){
                    output=b;
                }else {
                    if (part[1].length()>1){
                        dec=part[1].substring(0,2);
                    }
                    output=b+String.valueOf(ds)+dec;
                }
            }else {
                double a=Double.valueOf(number);
                String b=nf.format(a);
                output=b;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return output;
    }
    public static String parseDF(String df){
        Locale locale=new Locale("in","ID");
        DecimalFormat format=(DecimalFormat) DecimalFormat.getInstance(locale);
        DecimalFormatSymbols symbols=format.getDecimalFormatSymbols();
        char separator=symbols.getDecimalSeparator();
        char grouping=symbols.getGroupingSeparator();

        String a=df.replace(String.valueOf(grouping),"");
        String b=a.replace(separator,'.');

        return b;
    }

    public static ActionBar btnBack(String title, ActionBar bar){
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setElevation(0);
        bar.setTitle(title);
        return bar;
    }
    public static ActionBar btnBack(int resTitle, ActionBar bar){
//        bar.setDisplayShowHomeEnabled(true);
//        bar.setDisplayHomeAsUpEnabled(true);
//        bar.setElevation(0);
//        bar.setTitle(resTitle);
        return bar;
    }

    public static void edtRequestFocus(View view,int id){
        EditText editText=view.findViewById(id);
        editText.requestFocus();
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
    public static String getDeviceID(ContentResolver c){
        return Settings.Secure.getString(c, Settings.Secure.ANDROID_ID);
    }

    public static int getCurrentCountryCode(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String countryIso = telephonyManager.getSimCountryIso().toUpperCase();
        return PhoneNumberUtil.getInstance().getCountryCodeForRegion(countryIso);
    }
    public static String getCurrencySymbol(){
        Locale locale=Locale.getDefault();
        Currency currency= Currency.getInstance(locale);
        return currency.getSymbol();
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
//    public static String getDecrypt(String teks){
//        try {
//            String key = "KomputerKit";
//            String salt = "KomputerKit";
//            byte[] iv = new byte[16];
//            Encryption encryption = Encryption.getDefault(key, salt, iv);
//
//            String hasil = encryption.decryptOrNull(teks);
//            return hasil ;
//        } catch (Exception e){
//            return "" ;
//        }
//    }
    public static String getResString(Context context,@StringRes int string){
        return context.getResources().getString(string);
    }

    public static Boolean cekLimit(Context contex,String type){
        Boolean free=true;
        DatabaseTabungan db=new DatabaseTabungan(contex);
        Cursor anggota=db.sq(QueryTabungan.select("tblanggota"));
        Cursor jenis=db.sq(QueryTabungan.select("tbljenissimpanan"));
        Cursor simpanan=db.sq(QueryTabungan.select("tblsimpanan"));
        Cursor transaksi=db.sq(QueryTabungan.select("tbltransaksi"));

        if (type.equals("anggota")){
            if (anggota.getCount()>30){
                free=false;
            }
        }else if (type.equals("jenis")){
            if (jenis.getCount()>30){
                free=false;
            }
        }else if (type.equals("simpanan")){
            if (simpanan.getCount()>30){
                free=false;
            }
        }else if (type.equals("transaksi")){
            if (transaksi.getCount()>30){
                free=false;
            }
        }
        return free;
    }
    public static Boolean cekTransaksi(Context contex,String idsimpanan){
        Boolean free=true;
        DatabaseTabungan db=new DatabaseTabungan(contex);
        Cursor transaksi=db.sq(QueryTabungan.selectwhere("tbltransaksi")+QueryTabungan.sWhere("idsimpanan",idsimpanan));

        if (transaksi.getCount()>30){
            free=false;
        }
        return free;
    }

    public void inApp(){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Beli Aplikasi Versi Pro")
                .setMessage("Pemakaian anda sudah melebihi batas trial, untuk menggunakan lagi beli versi pro")
                .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivity(new Intent(context,MenuUtilitasTabungan.class));
                    }
                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog buat = alert.create();
        buat.show();
    }
}
