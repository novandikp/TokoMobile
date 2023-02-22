package com.itbrain.aplikasitoko.apotek;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;


public class TextWatcherPembelian_Apotek implements TextWatcher {

    private static final String TAG = "NumberTextWatcher";

    private final int numDecimals;
    private String groupingSep;
    private String decimalSep;
    private boolean nonUsFormat;
    private DecimalFormat df;
    private DecimalFormat dfnd;
    private boolean hasFractionalPart;
    private EditText hpp2;
    private EditText hpp1;
    private double harga;
    private double stok;
    private EditText et;
    private boolean stat;
    private String value;
    private EditText etharga;
    private EditText etstok;
    private double nilai;

    private String replicate(char ch, int n) {
        return new String(new char[n]).replace("\0", "" + ch);
    }

    public TextWatcherPembelian_Apotek(EditText et, EditText etharga, EditText etstok, EditText hpp1, EditText hpp2, boolean stat, double harga, double stok, double nilai, Locale locale, int numDecimals) {

//        et.setKeyListener(DigitsKeyListener.getInstance("0123456789.,"));
        this.numDecimals = numDecimals;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);

        char gs = symbols.getGroupingSeparator();
        char ds = symbols.getDecimalSeparator();
        groupingSep = String.valueOf(gs);
        decimalSep = String.valueOf(ds);

        String patternInt = "#,###";
        dfnd = new DecimalFormat(patternInt, symbols);

        String patternDec = patternInt + "." + replicate('#', numDecimals);
        df = new DecimalFormat(patternDec, symbols);
        df.setDecimalSeparatorAlwaysShown(true);
        df.setRoundingMode(RoundingMode.DOWN);

        this.et = et;
        this.etharga=etharga;
        this.etstok=etstok;
        this.hpp1=hpp1;
        this.hpp2=hpp2;
        this.stat=stat;
        this.harga=harga;
        this.stok=stok;
        this.nilai=nilai;


        hasFractionalPart = false;

        nonUsFormat = !decimalSep.equals(".");
        value = null;

    }


    @Override
    public void afterTextChanged(Editable s) {
        Log.d(TAG, "afterTextChanged");
        et.removeTextChangedListener(this);

        try {
            int inilen, endlen;
            inilen = et.getText().length();

            String v = value.replace(groupingSep, "");

            Number n = df.parse(v);

            int cp = et.getSelectionStart();
            if (hasFractionalPart) {
                int decPos = v.indexOf(decimalSep) + 1;
                int decLen = v.length() - decPos;
                if (decLen > numDecimals) {
                    v = v.substring(0, decPos + numDecimals);
                }
                int trz = countTrailingZeros(v);

                StringBuilder fmt = new StringBuilder(df.format(n));
                while (trz-- > 0) {
                    fmt.append("0");
                }
                et.setText(fmt.toString());
            } else {
                et.setText(dfnd.format(n));
            }


            endlen = et.getText().length();
            int sel = (cp + (endlen - inilen));
            if (sel > 0 && sel <= et.getText().length()) {
                et.setSelection(sel);
            } else {
                // place cursor at the end?
                et.setSelection(et.getText().length() - 1);
            }


        } catch (NumberFormatException | ParseException nfe) {
            // do nothing?
        }


        et.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.d(TAG, "beforeTextChanged");
        value = et.getText().toString();
    }

    private int countTrailingZeros(String str) {
        int count = 0;

        for (int i = str.length() - 1; i >= 0; i--) {
            char ch = str.charAt(i);
            if ('0' == ch) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d(TAG, "onTextChanged");

        String newValue = s.toString();
        String change = newValue.substring(start, start + count);
        String prefix = value.substring(0, start);
        String suffix = value.substring(start + before);

        if (".".equals(change) && nonUsFormat) {
            change = decimalSep;
        }

        value = prefix + change + suffix;
        hasFractionalPart = value.contains(decimalSep);

        Log.d(TAG, "VALUE: " + value);


        String a = ModulApotek.unNumberFormat(etharga.getText().toString());
        String jumlah= ModulApotek.unNumberFormat(etstok.getText().toString());
        if (a.equals("0")||jumlah.equals("0")){
            hpp1.setText("0");
            hpp2.setText("0");
        }else if (!a.equals("")&&!jumlah.equals("")){
            double hppsatu= (harga+(ModulApotek.strToDouble(a)* ModulApotek.strToDouble(jumlah)))/(stok+ ModulApotek.strToDouble(jumlah));
            if (stat){
                hpp1.setText(ModulApotek.sederhana(hppsatu*nilai));
                hpp2.setText(ModulApotek.sederhana(hppsatu));

            }else{
                hpp2.setText(ModulApotek.sederhana(hppsatu/nilai));
                hpp1.setText(ModulApotek.sederhana(hppsatu));

            }
        }else{
            hpp1.setText("0");
            hpp2.setText("0");
        }

    }
}