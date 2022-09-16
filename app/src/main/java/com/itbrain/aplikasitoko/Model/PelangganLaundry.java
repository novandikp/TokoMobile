package com.itbrain.aplikasitoko.Model;

public class PelangganLaundry {
    private int idpelanggan;
    private String pelanggan;
    private String alamatpelanggan;
    private String notelppelanggan;
    private double hutang;

    public PelangganLaundry(int idpelanggan, String pelanggan, String alamatpelanggan, String notelppelanggan, double hutang) {
        this.idpelanggan = idpelanggan;
        this.pelanggan = pelanggan;
        this.alamatpelanggan = alamatpelanggan;
        this.notelppelanggan = notelppelanggan;
        this.hutang = hutang;
    }

    public PelangganLaundry(int idpelanggan, String pegawai, String alamatpegawai, String notelppegawai) {
    }

    public static int size() {
        return 0;
    }

    public static void remove(int Position) {
    }

    public int getIdpelanggan() {
        return idpelanggan;
    }
    public void setIdpelanggan(int idpelanggan) {
        this.idpelanggan = idpelanggan;
    }
    public String getPelanggan() { return pelanggan; }
    public void setPelanggan(String pelanggan) {
        this.pelanggan = pelanggan;
    }
    public String getAlamatpelanggan() { return alamatpelanggan; }
    public void setAlamatpelanggan(String alamatpelanggan) { this.alamatpelanggan = alamatpelanggan; }
    public String getNotelppelanggan() { return notelppelanggan; }
    public void setNotelppelanggan(String notelppelanggan) { this.notelppelanggan = notelppelanggan; }
    public double getHutang() { return hutang; }
    public void setHutang(double hutang) { this.hutang = hutang; }
}
