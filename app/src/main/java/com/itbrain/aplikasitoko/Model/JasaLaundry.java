package com.itbrain.aplikasitoko.Model;

import androidx.recyclerview.widget.RecyclerView;

public abstract class JasaLaundry extends RecyclerView.Adapter {
    private int idkategori;
    private String jasa;
    private double biaya;
    private String satuan;

    public JasaLaundry(int idjasa, String jasa, double biaya, String satuan) {
        this.idkategori = idjasa;
        this.jasa = jasa;
        this.biaya = biaya;
        this.satuan = satuan;
    }

    public JasaLaundry(int idjasa, String jasa, double biaya) {
    }

    public int getIdjasa() {
        return idkategori;
    }

    public void setIdjasa(int idjasa) {
        this.idkategori = idjasa;
    }

    public String getJasa() {
        return jasa;
    }
    public double getBiaya() {
        return biaya;
    }
    public String getSatuan() {
        return satuan;
    }

    public void setJasa(String jasa) {
        this.jasa = jasa;
    }
    public void setBiaya(double biaya) {
        this.biaya = biaya;
    }
    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }


}
