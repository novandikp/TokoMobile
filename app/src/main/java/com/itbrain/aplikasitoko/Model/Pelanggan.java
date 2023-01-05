package com.itbrain.aplikasitoko.Model;

public class Pelanggan {
    private Integer idpelanggan;
    private String pelanggan, alamat, notelp;

    public Integer getIdpelanggan() {
        return idpelanggan;
    }

    public void setIdpelanggan(Integer idpelanggan) {
        this.idpelanggan = idpelanggan;
    }

    public String getNotelp() {
        return notelp;
    }

    public void setNotelp(String notelp) {
        this.notelp = notelp;
    }

    public String getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(String pelanggan) {
        this.pelanggan = pelanggan;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public Pelanggan(Integer idpelanggan, String notelp, String pelanggan, String alamat) {
        this.idpelanggan = idpelanggan;
        this.notelp = notelp;
        this.pelanggan = pelanggan;
        this.alamat = alamat;
    }
}
