package com.itbrain.aplikasitoko.TokoKredit;

public class ModelKeuanganKredit {

    private String ket, faktur, tgl;
    private Double masuk, keluar;
    private int noTransaksi;

    public ModelKeuanganKredit(String ket, String faktur, String tgl, Double masuk, Double keluar, int noTransaksi) {
        this.ket = ket;
        this.tgl = tgl;
        this.faktur = faktur;
        this.masuk = masuk;
        this.keluar = keluar;
        this.noTransaksi = noTransaksi;
    }

    public int getNoTransaksi() {
        return noTransaksi;
    }

    public String getTgl() {
        return tgl;
    }

    public String getKet() {
        return ket;
    }

    public String getFaktur() {
        return faktur;
    }

    public Double getMasuk() {
        return masuk;
    }

    public Double getKeluar() {
        return keluar;
    }

}
