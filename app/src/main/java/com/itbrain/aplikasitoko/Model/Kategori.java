package com.itbrain.aplikasitoko.Model;

public class Kategori {
    private int idkategori;
    private String kategori;

    public Kategori(int idkategori, String kategori) {
        this.idkategori = idkategori;
        this.kategori = kategori;
    }

    public int getIdkategori() {
        return idkategori;
    }

    public void setIdkategori(int idkategori) {
        this.idkategori = idkategori;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
}
