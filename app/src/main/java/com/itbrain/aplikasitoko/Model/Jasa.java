package com.itbrain.aplikasitoko.Model;

public class Jasa {
   private Integer idbarang;
   private String barang, harga, hargabeli;

    public Integer getIdbarang() {
        return idbarang;
    }

    public void setIdbarang(Integer idbarang) {
        this.idbarang = idbarang;
    }

    public String getBarang() {
        return barang;
    }

    public void setBarang(String barang) {
        this.barang = barang;
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

    public Jasa(Integer idbarang, String barang, String harga, String hargabeli) {
        this.idbarang = idbarang;
        this.barang = barang;
        this.harga = harga;
        this.hargabeli = hargabeli;
    }
}
