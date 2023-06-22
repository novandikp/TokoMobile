package com.itbrain.aplikasitoko.TokoKredit;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;

/**
 * Created by msaifa on 05/05/2018.
 */

public class CekInAppKredit {

    Context context;
    FConfigKredit config, temp;
    FKoneksiKredit db;
    SharedPreferences getPrefs;

    public CekInAppKredit(Context context) {
        config = new FConfigKredit(context.getSharedPreferences("config", Context.MODE_PRIVATE));
        temp = new FConfigKredit(context.getSharedPreferences("temp", Context.MODE_PRIVATE));
        db = new FKoneksiKredit(context, config);
        getPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean masihTrial() {
        String barang = "Select * from tblbarang";
        String bayar = "Select * from tblbayar";
        String kredit = "select * from tblkredit";

        return getCount(barang) < 5 && getCount(bayar) < 5 && getCount(kredit) < 5;
    }

    public int getCount(String sql) {
        try {
            Cursor c = db.sq(sql);

            return c.getCount() > 0 ? c.getCount() : 0;
        } catch (Exception ee) {
            return 0;
        }
    }

}
