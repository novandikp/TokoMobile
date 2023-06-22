package com.itbrain.aplikasitoko.TokoKredit;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ModuleKredit {

    AppCompatActivity appCompatActivity;

    public ModuleKredit(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    public static void info(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    public static void info(Context ctx, String msg, boolean longToast) {
        if (longToast) {
            Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static void goToActivity(Context ctx, Class<?> cls, HashMap<String, String> extras) {
        Intent i = new Intent(ctx, cls);
        for (HashMap.Entry<String, String> extra : extras.entrySet()) {
            i.putExtra(extra.getKey(), extra.getValue());
        }
        ctx.startActivity(i);
    }

    public static void goToActivity(Context ctx, Class<?> cls, Boolean clearTop) {
        Intent i = new Intent(ctx, cls);
        if (clearTop) i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ctx.startActivity(i);
    }

    public static void goToActivity(Context ctx, Class<?> cls) {
        Intent i = new Intent(ctx, cls);
        ctx.startActivity(i);
    }

//    public static void implementBackButton(AppCompatActivity appCompatActivity) {
//        appCompatActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
//        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//    }

    public static void removeFocus(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static void fillSpinner(Context ctx, int resource, Spinner target) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ctx, resource, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        target.setAdapter(adapter);
    }

    public static void setText(AppCompatActivity appCompatActivity, int id, String txt) {
        TextView t = appCompatActivity.findViewById(id);
        try {
            t.setText(txt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean notEmpty(EditText[] editTexts) {
        boolean status = true;
        for (EditText e : editTexts) {
            if (TextUtils.isEmpty(e.getText().toString()))
                status = false;
        }
        return status;
    }

    private static String formatNumber(String nominal) { // 1,000,000.00
        String number = nominal;
        try {
            String hasil = "";
            String[] b = number.split("\\.");

            // Jika tidak ada koma (bulat)
            String[] a = b[0].split("(?!^)");
            int c = 0;
            for (int i = a.length - 1; i >= 0; i--) {
                if (c == 3 && !a[i].equals("-")) {
                    hasil = a[i] + "." + hasil;
                    c = 1;
                } else {
                    hasil = a[i] + hasil;
                    c++;
                }
            }

            if (b.length > 1) {
                // Jika ada koma (desimal)
                hasil += !b[1].equals("00") ? "," + b[1] : "";
            }
            return hasil;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String numFormat(double value) {
        try {
            // Old way
            // Decimal Format declaration
//            DecimalFormat decimalFormatter = new DecimalFormat("#");
//            decimalFormatter.setMaximumFractionDigits(8);
//            // Replace "," with "."
//            String fixDecimal = decimalFormatter.format(value).replace(",", ".");
//            Double hasil = Double.parseDouble(fixDecimal);

            // New way
            String valueAsString = String.format("%.2f", value).replace(",", ".");
            // Format Number
            return formatNumber(valueAsString);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String removeE(double value) {
        try {
            // Format Number
            String valueAsString = String.format("%.2f", value).replace(",", ".");
            String[] b = valueAsString.split("\\.");
            String result = "";

            result = b[1].equals("00") ? b[0] : valueAsString;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String formatPhone(String num) {
        String[] splittedNum = num.split("(?!^)");
        String result = "";
        for (int i = 0; i < splittedNum.length; i++) {
            // Jika index kelipatan empat dan bukan index terakhir
            result += ((i + 1) % 4 == 0 && i != splittedNum.length - 1) ? splittedNum[i] + "-" : splittedNum[i];
        }
        return result;
    }

    public static void addTextChangedListener(EditText e, TextView tvCharLeft) {
        e.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int charLeft = 30 - e.length();
                tvCharLeft.setText(charLeft + "/30");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public static void simpleDialog(Context ctx, String title, String msg, final DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title).setMessage(msg);
        builder.setNeutralButton("ok", listener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void yesNoDialog(Context ctx, String title, String msg, final DialogInterface.OnClickListener positiveListener, final DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ctx);
        alertBuilder.setTitle(title).setMessage(msg);
        alertBuilder.setPositiveButton("Ya", positiveListener).setNegativeButton("Tidak", negativeListener);
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    public static int diffBetween(String beginDate, String endDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date beginObj = formatter.parse(beginDate);
            Date endObj = formatter.parse(endDate);
            long diff = endObj.getTime() - beginObj.getTime();
            return (int) (diff / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }

    public static String getDateString(String tanggal, String pattern) {
        try {
            Date dateObj = new SimpleDateFormat("yyyy-MM-dd").parse(tanggal);
            return new SimpleDateFormat(pattern).format(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
            return tanggal;
        }
    }

    public static String trim(String text) {
        String[] split = text.split("\\s+");
        if (split.length > 4) {
            String result = "";
            for (int i = 0; i <= 3; i++) {
                result += split[i] + " ";
            }
            result+="...";
            return result;
        } else {
            return text;
        }
    }

    public static String trim(String text, int wordCount) {
        String[] split = text.split("\\s+");
        if (split.length > wordCount) {
            String result = "";
            for (int i = 0; i <= wordCount - 1; i++) {
                result += split[i] + " ";
            }
            result+="...";
            return result;
        } else {
            return text;
        }
    }

}
