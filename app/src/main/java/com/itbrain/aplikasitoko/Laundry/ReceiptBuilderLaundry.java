package com.itbrain.aplikasitoko.Laundry;

import android.content.Context;
import android.database.Cursor;

public class ReceiptBuilderLaundry {
    private String faktur;
    DatabaseLaundry db;
    public ReceiptBuilderLaundry(String idfaktur, Context context){
        this.faktur = idfaktur;
        db = new DatabaseLaundry(context);
    }


    public String getFaktur(){
        Cursor identitas=db.sq(QueryLaundry.selectwhere("tblidentitas")+ QueryLaundry.sWhere("ididentitas","1"));
        identitas.moveToNext();
        Cursor order=db.sq(QueryLaundry.selectwhere("qlaundry")+ QueryLaundry.sWhere("faktur",faktur));
        order.moveToNext();
        Cursor cart=db.sq(QueryLaundry.selectwhere("qlaundry")+ QueryLaundry.sWhere("faktur",faktur));
        Cursor notelppelanggan=db.sq(QueryLaundry.selectwhere("tblpelanggan")+ QueryLaundry.sWhere("idpelanggan", ModulLaundry.getString(order,"idpelanggan")));
        notelppelanggan.moveToFirst();
        String notelp= ModulLaundry.getString(notelppelanggan,"notelp");

        String toko = ModulLaundry.getString(identitas,"namatoko");
        String alamat = ModulLaundry.getString(identitas,"alamattoko");
        String telp = ModulLaundry.getString(identitas,"notelptoko");

        String tFaktur = "Faktur : "+faktur;

        String tTglTerima = "Tanggal Terima : "+ ModulLaundry.dateToNormal(ModulLaundry.getString(order,"tgllaundry"));

        String tTglSampai = "Tanggal Selesai : "+ ModulLaundry.dateToNormal(ModulLaundry.getString(order,"tglselesai"));

        String tPegawai = "Pegawai : "+ ModulLaundry.getString(order,"pegawai");

        String tPelanggan = "Pelanggan : "+ ModulLaundry.getString(order,"pelanggan");

        String tStatus = "Status : "+ ModulLaundry.getString(order,"statuslaundry");
        String tBayar = "Pembayaran : "+ ModulLaundry.getString(order,"statusbayar");


        String header= ModulLaundry.setCenter(toko)+"\n"+
                ModulLaundry.setCenter(alamat)+"\n"+
                ModulLaundry.setCenter(telp)+"\n"+
                ModulLaundry.getStrip()+
                tFaktur+"\n"+
                tTglTerima+"\n"+
                tTglSampai+"\n"+
                tPegawai+"\n"+
                tPelanggan+"\n"+
                tStatus+"\n"+
                ModulLaundry.getStrip();
        if (ModulLaundry.getString(order,"statuslaundry").equals("Selesai")){
            header= ModulLaundry.setCenter(toko)+"\n"+
                    ModulLaundry.setCenter(alamat)+"\n"+
                    ModulLaundry.setCenter(telp)+"\n"+
                    ModulLaundry.getStrip()+
                    tFaktur+"\n"+
                    tTglTerima+"\n"+
                    tTglSampai+"\n"+
                    tPegawai+"\n"+
                    tPelanggan+"\n"+
                    tStatus+"\n"+
                    tBayar+"\n"+
                    ModulLaundry.getStrip();
        }
        String body="";
        String view="";
        while(cart.moveToNext()){
            String jasa = ModulLaundry.getString(cart,"kategori")+" - "+ ModulLaundry.getString(cart,"jasa");
            String jumlah = ModulLaundry.getString(cart,"jumlahlaundry");
            String harga = ModulLaundry.removeE(ModulLaundry.getString(cart,"biaya"));
            String total = ModulLaundry.removeE(ModulLaundry.getString(cart,"biayalaundry"));
            String keterangan="";
            if (!ModulLaundry.getString(cart,"keterangan").equals("")){
                keterangan="("+ ModulLaundry.getString(cart,"keterangan")+")\n";
            }

            body+= jasa+"\n"+
                    ModulLaundry.removeE(jumlah)+" X "+harga+"\n"+
                    keterangan+
                    ModulLaundry.setRight(total)+"\n";

        }

        body+= ModulLaundry.getStrip();

        String jumlahbayar = "Total : "+ ModulLaundry.removeE(ModulLaundry.getString(order,"total"));
        String bayar="";
        String kembali="";
        if (ModulLaundry.getString(order,"statuslaundry").equals("Selesai")){
            bayar = "Bayar : "+ ModulLaundry.removeE(ModulLaundry.getString(order,"bayar"));
            kembali = "Kembali : "+ ModulLaundry.removeE(ModulLaundry.getString(order,"kembali"));

        }
        String caption =  ModulLaundry.getString(identitas,"caption_1") ;
        String caption2 = ModulLaundry.getString(identitas,"caption_2") ;
        String caption3 = ModulLaundry.getString(identitas,"caption_3") ;


        String footer = ModulLaundry.setRight(jumlahbayar)+"\n\n"+
                ModulLaundry.setCenter(caption)+"\n"+
                ModulLaundry.setCenter(caption2)+"\n"+
                ModulLaundry.setCenter(caption3) ;
        if (ModulLaundry.getString(order,"statuslaundry").equals("Selesai")){
            footer = ModulLaundry.setRight(jumlahbayar)+"\n"+
                    ModulLaundry.setRight(bayar)+"\n"+
                    ModulLaundry.setRight(kembali)+"\n\n"+
                    ModulLaundry.setCenter(caption)+"\n"+
                    ModulLaundry.setCenter(caption2)+"\n"+
                    ModulLaundry.setCenter(caption3) ;
        }

        return header+body+footer;
    }


}
