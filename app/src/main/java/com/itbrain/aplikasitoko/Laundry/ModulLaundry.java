package com.itbrain.aplikasitoko.Laundry;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

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
import java.util.Locale;


public class ModulLaundry {
    static String slct = "SELECT * FROM " ;
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


    public static int getIntFromColumn(Cursor c, int col){
        try{
            return c.getInt(col);
        }catch (Exception e){
            return 0;
        }
    }

    public static String getStringFromColumn(Cursor c, int col){
        try{
            return c.getString(col);
        }catch (Exception e){
            return "";
        }
    }

    public static String quote(String word){
        try{
            return "\'" + addSlashes(word) + "\'" ;
        }catch (Exception e){
            return "";
        }

    }
    public static String getText(View v, int id){ //cara instal //FFunction.getText(this.findViewById(android.R.id.content), R.id.jumlah) ;
        TextView a = v.findViewById(id);
        return a.getText().toString() ;
    }
    public static Boolean setText(View v, int id,String t){ //cara instal //FFunction.setText(this.findViewById(android.R.id.content), R.id.jumlah,"ini String") ;
        TextView a = v.findViewById(id);
        try {
            a.setText(t);
            return true ;
        }catch (Exception e){
            return false;
        }
    }
    public static void setTextVisibility(View v,int id,int value){
        TextView a= v.findViewById(id);
        a.setVisibility(value);
    }
    public static String getString(Cursor c, String name){
        try{
            return c.getString(c.getColumnIndex(name)) ;
        }catch (Exception e){
            return "";
        }

    }

    public static double getDouble(Cursor c, String name){
        try{
            return c.getDouble(c.getColumnIndex(name)) ;
        }catch (Exception e){
            return 0;
        }

    }
    public static int getInt(Cursor c, String name){
        try{
            return c.getInt(c.getColumnIndex(name)) ;
        }catch (Exception e){
            return 0 ;
        }
    }
    public static String intToStr(int integer){
        try{
            return String.valueOf(integer) ;
        }catch (Exception e){
            return "";
        }

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
            bln = "0"+ ModulLaundry.intToStr(month) ;
        } else {
            bln = ModulLaundry.intToStr(month) ;
        }

        if(day < 10){
            hri = "0"+ ModulLaundry.intToStr(day) ;
        } else {
            hri = ModulLaundry.intToStr(day) ;
        }

        return hri+"/"+bln+"/"+ ModulLaundry.intToStr(year);
    }
    public static String getDate(String type){ //Random time type : HHmmssddMMyy
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(type);
        String formattedDate = df.format(c.getTime());
        return formattedDate ;
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
            String a = number.replace(",","^") ;
            String b = a.replace(".","") ;
            String c = b.replace("^",".") ;
            return c ;
        }catch (Exception e){
            return "" ;
        }
    }
    public static String removeMinus(String number){
        try{
            String a = number.replace("-","");
            return a;
        }catch (Exception e){
            return "";
        }
    }
    public static String removeCurrencySymbol(String number){
        try{
            String a=number.replace(ModulLaundry.getCurrencySymbol(),"");
            String b=a.replaceAll("\\s","");
            return b;
        }catch (Exception e){
            return "";
        }
    }
    //    public static String removeE(double value){
//        DecimalFormat df = new DecimalFormat("#");
//        df.setMaximumFractionDigits(8);
//        return Modul.numberFormat(df.format(value)) ;
//    }
//    public static String removeE(String value) {
//        double hasil = Modul.strToDouble(value);
//        DecimalFormat df = new DecimalFormat("#");
//        df.setMaximumFractionDigits(8);
//        return Modul.numberFormat(df.format(hasil));
//    }
    public static String removeE(double value){
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        return ModulLaundry.numFormat(df.format(value)) ;
    }
    public static String removeE(String value) {
        double hasil = ModulLaundry.strToDouble(value);
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        return ModulLaundry.numFormat(df.format(hasil));
    }
    public static String justRemoveE(String value) {
        double hasil = ModulLaundry.strToDouble(value);
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        return df.format(hasil);
    }

    public static String removeComma(String value){
        try{
            String a=value.replace(",0","");
//            String b=a.replace(".0","");
            return a;
        }catch (Exception e){
            return "";
        }
    }
    public static String convertDate(String date) {
        String[] a = date.split("/");
        return a[2] + a[1] + a[0];
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
        NumberFormat nf=NumberFormat.getCurrencyInstance(locale);
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
        Currency currency=Currency.getInstance(locale);
        return currency.getSymbol();
    }

    public static Boolean cekLimit(Context contex, String type){
        Boolean free=true;
        DatabaseLaundry db=new DatabaseLaundry(contex);
        Cursor pegawai=db.sq(QueryLaundry.select("tblpegawai"));
        Cursor pelanggan=db.sq(QueryLaundry.select("tblpelanggan"));
        Cursor kategori=db.sq(QueryLaundry.select("tblkategori"));
        Cursor jasa=db.sq(QueryLaundry.select("tbljasa"));
        Cursor transaksi=db.sq(QueryLaundry.select("tbllaundry"));

        if (type.equals("pegawai")){
            if (pegawai.getCount()>10){
                free=false;
            }
        }else if (type.equals("pelanggan")){
            if (pelanggan.getCount()>10){
                free=false;
            }
        }else if (type.equals("kategori")){
            if (kategori.getCount()>10){
                free=false;
            }
        }else if (type.equals("jasa")){
            if (jasa.getCount()>10){
                free=false;
            }
        }else if (type.equals("transaksi")){
            if (transaksi.getCount()>10){
                free=false;
            }
        }
        return free;
    }
}
