package com.itbrain.aplikasitoko.Model;

public class Kategori {
    private Integer idkategori;
    private String kategori;

    public Kategori(Integer idkategori, String kategori) {
        this.idkategori = idkategori;
        this.kategori = kategori;
    }

    public Integer getIdkategori() {
        return idkategori;
    }

    public void setIdkategori(Integer idkategori) {
        this.idkategori = idkategori;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
}

