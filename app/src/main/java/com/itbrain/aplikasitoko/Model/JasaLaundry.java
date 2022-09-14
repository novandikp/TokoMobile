package com.itbrain.aplikasitoko.Model;

public class JasaLaundry {
    private int idjasa,idkategori;
    private String jasa,satuan;


    public JasaLaundry(int idjasa, int idkategori, String jasa, String satuan) {
        this.idjasa = idjasa;
        this.idkategori = idkategori;
        this.jasa = jasa;
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
}
