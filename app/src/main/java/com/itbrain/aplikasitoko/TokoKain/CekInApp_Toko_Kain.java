package com.itbrain.aplikasitoko.TokoKain;

import android.content.Context;
import android.database.Cursor;

public class CekInApp_Toko_Kain {


    DatabaseTokoKain db;
    Context context;

    public CekInApp_Toko_Kain(Context context) {
        this.context = context;

        db = new DatabaseTokoKain(context);
    }




    public  int getCount(Cursor c){
        try{
            return c.getCount();
        }catch (Exception e){
            return  0;
        }
    }


    public boolean cekInapp() {
//        if (ActivitySplash.status){
//            return true;
//        }else{
//            try {
//                Cursor c1 = db.sq("SELECT count(*) as jumlah FROM tblkain");
//                Cursor c2= db.sq("SELECT count(*) as jumlah FROM tblpelanggan");
//                Cursor c3 = db.sq("SELECT count(*) as jumlah FROM tblorder");
//
//                c1.moveToFirst();
//                c2.moveToFirst();
//                c3.moveToFirst();
//                return  c1.getInt(c1.getColumnIndex("jumlah")) < 5 && c2.getInt(c2.getColumnIndex("jumlah")) < 5 && c3.getInt(c3.getColumnIndex("jumlah")) < 5;
//            }catch (Exception e){
//                return  false;
//            }
//
//        }

        return true;
    }

}
