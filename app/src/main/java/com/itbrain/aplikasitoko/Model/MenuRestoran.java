package com.itbrain.aplikasitoko.Model;

public class MenuRestoran {
   private Integer idmakanan;
   private String makanan, harga, idkategori, kategori;

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public MenuRestoran(String kategori) {
        this.kategori = kategori;
    }

    public Integer getIdmakanan() {
        return idmakanan;
    }

    public void setIdmakanan(Integer idmakanan) {
        this.idmakanan = idmakanan;
    }

    public String getMakanan() {
        return makanan;
    }

    public void setMakanan(String makanan) {
        this.makanan = makanan;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getIdkategori() {
        return idkategori;
    }

    public void setIdkategori(String idkategori) {
        this.idkategori = idkategori;
    }

    public String getMenuRestoran() {
        return makanan;
    }

    public void setMenuRestoran(String makanan) {
        this.makanan = makanan;
    }

    public MenuRestoran(Integer idmakanan, String makanan, String harga, String idkategori, String kategori) {
        this.idmakanan = idmakanan;
        this.makanan = makanan;
        this.harga = harga;
        this.idkategori = idkategori;
        this.kategori = kategori;
    }
}
