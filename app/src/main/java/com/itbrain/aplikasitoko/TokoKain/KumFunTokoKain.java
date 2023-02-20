package com.itbrain.aplikasitoko.TokoKain;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.Settings;
import androidx.appcompat.app.ActionBar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class KumFunTokoKain {
    static String slct = "SELECT * FROM ";

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
    public static String getString(Cursor c, String name) {
        return c.getString(c.getColumnIndex(name));
    }

    public static String getCampur(String satu, String dua, String tiga, String empat, String lima) {
        return satu + "__" + dua + "__" + tiga + "__" + empat + "__" + lima;
    }

    public static int getInt(Cursor c, String name) {
        return c.getInt(c.getColumnIndex(name));
    }

    public static String intToStr(int integer) {
        return String.valueOf(integer);
    }

    public static int strToInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (Exception e) {
            return 0;
        }
    }

    public static double strToDouble(String v) {
        try {
            return Double.parseDouble(v);
        } catch (Exception e) {
            return 0;
        }
    }

    public static String doubleToStr(double d) {
        try {
            return String.valueOf(d);
        } catch (Exception e) {
            return "";
        }
    }

    public static String setCenter(String item) {
        int leng = item.length();
        String hasil = "";
        for (int i = 0; i < 32 - leng; i++) {
            if ((32 - leng) / 2 + 1 == i) {
                hasil += item;
            } else {
                hasil += " ";
            }
        }
        return hasil;
    }

    public static Boolean renameFile(String path, String namaLama, String namaBaru) {
        try {
            File a = new File(path + namaLama);
            File b = new File(path + namaBaru);
            a.renameTo(b);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean copyFile(String pIn, String pOut, String name) {
        InputStream in;
        OutputStream out;

        try {
            File file = new File(pOut);
            if (!file.exists()) {
                file.mkdirs();
            }

            in = new FileInputStream(pIn + name);
            out = new FileOutputStream(pOut + name);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            in.close();
            in = null;

            out.flush();
            out.close();
            out = null;

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getStrip() {
        String a = "";
        for (int i = 0; i < 32; i++) {
            a += "-";
        }
        return a + "\n";
    }

    public static String numberFormat(String number) { // Rp. 1,000,000.00
        try {
            String hasil = "";
            String[] b = number.split("\\.");

            if (b.length == 1) {
                String[] a = number.split("");
                int c = 0;
                for (int i = a.length - 1; i >= 0; i--) {
                    if (c == 3 && !TextUtils.isEmpty(a[i])) {
                        hasil = a[i] + "." + hasil;
                        c = 1;
                    } else {
                        hasil = a[i] + hasil;
                        c++;
                    }
                }
            } else {
                String[] a = b[0].split("");
                int c = 0;
                for (int i = a.length - 1; i >= 0; i--) {
                    if (c == 3 && !TextUtils.isEmpty(a[i])) {
                        hasil = a[i] + "." + hasil;
                        c = 1;
                    } else {
                        hasil = a[i] + hasil;
                        c++;
                    }
                }
                hasil += "," + b[1];
            }
            return hasil;
        } catch (Exception e) {
            return "";
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

    public static Boolean deleteFile(String path) {
        try {
            File del = new File(path);
            del.delete();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String removeE(double value) {
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        return KumFunTokoKain.numberFormat(df.format(value));
    }

    public static String removeE(String value) {
        double hasil = KumFunTokoKain.strToDouble(value);
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        return KumFunTokoKain.numberFormat(df.format(hasil));
    }

    public static String justRemoveE(String value) {
        double hasil = KumFunTokoKain.strToDouble(value);
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        return df.format(hasil);
    }

    public static String unNumberFormat(String number) {
        try {
            String a = number.replace(",", "^");
            String b = a.replace(".", "");
            String c = b.replace("^", ".");
            return c;
        } catch (Exception e) {
            return "";
        }
    }

    public static String convertDate(String date) {
        String[] a = date.split("/");
        return a[2] + a[1] + a[0];
    }

    public static String dateToNormal(String date) {
        try {
            String b1 = date.substring(4);
            String b2 = b1.substring(2);

            String m = b1.substring(0, 2);
            String d = b2.substring(0, 2);
            String y = date.substring(0, 4);
            return d + "/" + m + "/" + y;
        } catch (Exception e) {
            return "ini tanggal";
        }
    }

    public static String setDatePickerNormal(int year, int month, int day) {
        String bln, thn, hri;
        if (month < 10) {
            bln = "0" + KumFunTokoKain.intToStr(month);
        } else {
            bln = KumFunTokoKain.intToStr(month);
        }

        if (day < 10) {
            hri = "0" + KumFunTokoKain.intToStr(day);
        } else {
            hri = KumFunTokoKain.intToStr(day);
        }

        return hri + "/" + bln + "/" + KumFunTokoKain.intToStr(year);
    }

    public static String getDate(String type) { //Random time type : HHmmssddMMyy
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(type);
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static String setRight(String item) {
        int leng = item.length();
        String hasil = "";
        for (int i = 0; i < 32 - leng; i++) {
            if ((31 - leng) == i) {
                hasil += item;
            } else {
                hasil += " ";
            }
        }
        return hasil;
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
    public static String getDeviceID(ContentResolver c) {
        return Settings.Secure.getString(c, Settings.Secure.ANDROID_ID);
    }

    //    public static String getDecrypt(String teks){
//        try {
//            String key = "KomputerKit";
//            String salt = "KomputerKit";
//            byte[] iv = new byte[16];
//
//
//
//            Encryption encryption = Encryption.getDefault(key, salt, iv);
//
//            String hasil = encryption.decryptOrNull(teks);
//            return hasil ;
//        } catch (Exception e){
//            return "" ;
//        }
//    }
    public static String encrypt(String input) {
        // This is base64 encoding, which is not an encryption
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    public static String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }

    public static void btnBack(String cari_pelanggan, ActionBar supportActionBar) {
    }

    public static String getText(View v, int id){ //cara instal //FFunction.getText(this.findViewById(android.R.id.content), R.id.jumlah) ;
        TextView a = v.findViewById(id) ;
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

    public static String getDecrypt(String deviceid) {
        return deviceid;
    }

    public static String getEncrypt(String deviceid) {
        return deviceid;
    }
}
