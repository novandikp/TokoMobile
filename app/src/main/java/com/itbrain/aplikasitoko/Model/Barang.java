package com.itbrain.aplikasitoko.Model;

public class Barang {
    private Integer idbarang;
    private String  barang, hargabeli, stok, harga, idkategori;

    public Barang(Integer idbarang, String barang, String hargabeli, String stok, String harga, String idkategori) {
        this.idbarang = idbarang;
        this.barang = barang;
        this.hargabeli = hargabeli;
        this.stok = stok;
        this.harga = harga;
        this.idkategori = idkategori;
    }

    public Barang(String barang) {
        this.barang = barang;
    }

    public Integer getIdbarang() {
        return idbarang;
    }

    public String getIdkategori() {
        return idkategori;
    }

    public void setIdkategori(String idkategori) {
        this.idkategori = idkategori;
    }

    public void setIdbarang(Integer idbrang) {
        this.idbarang = idbrang;
    }

    public String getStok() {
        return stok;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getHargabeli() {
        return hargabeli;
    }

    public void setHargabeli(String hargabeli) {
        this.hargabeli = hargabeli;
    }

//    public String getIdkategori() {
//        return idkategori;
//    }

//    public void setIdkategori(String idkategori) {
//        this.idkategori = idkategori;
//    }

    public String getBarang() {
        return barang;
    }

    public void setBarang(String barang) {
        this.barang = barang;
    }

    public Barang(Integer idbarang, String stok, String harga, String barang, String hargabeli) {
        this.idbarang = idbarang;
        this.stok = stok;
        this.harga = harga;
        this.hargabeli = hargabeli;
//        this.idkategori = idkategori;
        this.barang = barang;
    }
}
