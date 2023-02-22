package com.itbrain.aplikasitoko.kasir;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;

/**
 * Created by msaifa on 05/05/2018.
 */

public class CekInAppKasir {

    Context context;
    FConfigKasir config, temp;
    DatabaseKasir db;
    SharedPreferences getPrefs;

    public CekInAppKasir(Context context) {
        config = new FConfigKasir(context.getSharedPreferences("config", Context.MODE_PRIVATE));
        temp = new FConfigKasir(context.getSharedPreferences("temp", Context.MODE_PRIVATE));
        db = new DatabaseKasir(context, config);
        getPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean cek() {
        String sql1 = "Select * from tblbarang";
        String sql2 = "Select * from tblpenjualan";

        return getCount(sql1) < 5 && getCount(sql2) < 5;
    }

    public int getCount(String sql) {
        try {
            Cursor c = db.sq(sql);

            if (c.getCount() > 0) {
                return c.getCount();
            } else {
                return 0;
            }
        } catch (Exception ee) {
            return 0;
        }
    }

}
