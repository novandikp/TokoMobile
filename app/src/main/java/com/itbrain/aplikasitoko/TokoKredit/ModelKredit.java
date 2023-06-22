package com.itbrain.aplikasitoko.TokoKredit;

public class ModelKredit {

    private String faktur, tglkredit, pelanggan;
    private Double angsuran, lamakredit, hargaBarang;

    public String getFaktur() {
        return faktur;
    }

    public String getTglkredit() {
        return tglkredit;
    }

    public Double getHargaBarang() {
        return hargaBarang;
    }

    public String getPelanggan() {
        return pelanggan;
    }

    public Double getJumlahkredit() {
        return angsuran * lamakredit;
    }

    public ModelKredit(String faktur, String tglkredit, String pelanggan, Double angsuran, Double lamakredit, Double hargaBarang) {
        this.faktur = faktur;
        this.tglkredit = tglkredit;
        this.pelanggan = pelanggan;
        this.angsuran = angsuran;
        this.lamakredit = lamakredit;
        this.hargaBarang = hargaBarang;
    }
}
