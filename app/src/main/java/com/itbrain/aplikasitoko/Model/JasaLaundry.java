package com.itbrain.aplikasitoko.Model;

public class JasaLaundry {
    private int idjasa,idkategori;
    private String jasa,satuan,kategori,biaya;


    public JasaLaundry(int idjasa, int idkategori, String kategori, String jasa, String biaya, String satuan) {
        this.idjasa = idjasa;
        this.idkategori = idkategori;
        this.kategori = kategori;
        this.jasa = jasa;
        this.biaya = biaya;
        this.satuan = satuan;
    }

    public int getIdjasa() {
        return idjasa;
    }

    public int getIdkategori() {
        return idkategori;
    }

    public void setIdkategori(int idkategori) {
        this.idkategori = idkategori;
    }

    public int getIdJasa() {
        return idjasa;
    }

    public void setIdjasa(int idjasa) {
        this.idjasa = idjasa;
    }

    public String getJasa() {
        return jasa;
    }

    public void setJasa(String jasa) {
        this.jasa = jasa;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

}
