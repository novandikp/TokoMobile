package com.itbrain.aplikasitoko;

import android.content.Context;
import android.database.Cursor;

public class ReceiptBuilder {
    private String faktur;
    DatabaseLaundry db;
    public ReceiptBuilder(String idfaktur, Context context){
        this.faktur = idfaktur;
        db = new DatabaseLaundry(context);
    }


    public String getFaktur(){
        Cursor identitas=db.sq(Query.selectwhere("tblidentitas")+Query.sWhere("ididentitas","1"));
        identitas.moveToNext();
        Cursor order=db.sq(Query.selectwhere("qlaundry")+Query.sWhere("faktur",faktur));
        order.moveToNext();
        Cursor cart=db.sq(Query.selectwhere("qlaundry")+Query.sWhere("faktur",faktur));
        Cursor notelppelanggan=db.sq(Query.selectwhere("tblpelanggan")+Query.sWhere("idpelanggan",Modul.getString(order,"idpelanggan")));
        notelppelanggan.moveToFirst();
        String notelp=Modul.getString(notelppelanggan,"notelp");

        String toko = Modul.getString(identitas,"namatoko");
        String alamat = Modul.getString(identitas,"alamattoko");
        String telp = Modul.getString(identitas,"notelptoko");

        String tFaktur = "Faktur : "+faktur;

        String tTglTerima = "Tanggal Terima : "+Modul.dateToNormal(Modul.getString(order,"tgllaundry"));

        String tTglSampai = "Tanggal Selesai : "+Modul.dateToNormal(Modul.getString(order,"tglselesai"));

        String tPegawai = "Pegawai : "+Modul.getString(order,"pegawai");

        String tPelanggan = "Pelanggan : "+Modul.getString(order,"pelanggan");

        String tStatus = "Status : "+Modul.getString(order,"statuslaundry");
        String tBayar = "Pembayaran : "+Modul.getString(order,"statusbayar");


        String header= Modul.setCenter(toko)+"\n"+
                Modul.setCenter(alamat)+"\n"+
                Modul.setCenter(telp)+"\n"+
                Modul.getStrip()+
                tFaktur+"\n"+
                tTglTerima+"\n"+
                tTglSampai+"\n"+
                tPegawai+"\n"+
                tPelanggan+"\n"+
                tStatus+"\n"+
                Modul.getStrip();
        if (Modul.getString(order,"statuslaundry").equals("Selesai")){
            header= Modul.setCenter(toko)+"\n"+
                    Modul.setCenter(alamat)+"\n"+
                    Modul.setCenter(telp)+"\n"+
                    Modul.getStrip()+
                    tFaktur+"\n"+
                    tTglTerima+"\n"+
                    tTglSampai+"\n"+
                    tPegawai+"\n"+
                    tPelanggan+"\n"+
                    tStatus+"\n"+
                    tBayar+"\n"+
                    Modul.getStrip();
        }
        String body="";
        String view="";
        while(cart.moveToNext()){
            String jasa = Modul.getString(cart,"kategori")+" - "+Modul.getString(cart,"jasa");
            String jumlah = Modul.getString(cart,"jumlahlaundry");
            String harga = Modul.removeE(Modul.getString(cart,"biaya"));
            String total = Modul.removeE(Modul.getString(cart,"biayalaundry"));
            String keterangan="";
            if (!Modul.getString(cart,"keterangan").equals("")){
                keterangan="("+Modul.getString(cart,"keterangan")+")\n";
            }

            body+= jasa+"\n"+
                    Modul.removeE(jumlah)+" X "+harga+"\n"+
                    keterangan+
                    Modul.setRight(total)+"\n";

        }

        body+=Modul.getStrip();

        String jumlahbayar = "Total : "+Modul.removeE(Modul.getString(order,"total"));
        String bayar="";
        String kembali="";
        if (Modul.getString(order,"statuslaundry").equals("Selesai")){
            bayar = "Bayar : "+Modul.removeE(Modul.getString(order,"bayar"));
            kembali = "Kembali : "+Modul.removeE(Modul.getString(order,"kembali"));

        }
        String caption =  Modul.getString(identitas,"caption_1") ;
        String caption2 = Modul.getString(identitas,"caption_2") ;
        String caption3 = Modul.getString(identitas,"caption_3") ;


        String footer = Modul.setRight(jumlahbayar)+"\n\n"+
                Modul.setCenter(caption)+"\n"+
                Modul.setCenter(caption2)+"\n"+
                Modul.setCenter(caption3) ;
        if (Modul.getString(order,"statuslaundry").equals("Selesai")){
            footer = Modul.setRight(jumlahbayar)+"\n"+
                    Modul.setRight(bayar)+"\n"+
                    Modul.setRight(kembali)+"\n\n"+
                    Modul.setCenter(caption)+"\n"+
                    Modul.setCenter(caption2)+"\n"+
                    Modul.setCenter(caption3) ;
        }

        return header+body+footer;
    }


}
