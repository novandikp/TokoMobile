package com.itbrain.aplikasitoko.Model;

public class JasaLaundry{
    private int idJasa,idKategori;
    private String jasa,satuan,kategori,biaya;

    public int getIdJasa() {
        return idJasa;
    }

    public void setIdJasa(int idJasa) {
        this.idJasa = idJasa;
    }

    public int getIdKategori() {
        return idKategori;
    }

    public void setIdKategori(int idKategori) {
        this.idKategori = idKategori;
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

    public String getBiaya() {
        return biaya;
    }

    public void setBiaya(String biaya) {
        this.biaya = biaya;
    }

    public JasaLaundry(int idJasa, int idKategori, String string, String kategori, String jasa, String biaya) {
        this.idJasa = idJasa;
        this.idKategori = idKategori;
        this.kategori = kategori;
        this.jasa = jasa;
        this.biaya = biaya;
        this.satuan = this.satuan;
    }
}

//public class JasaLaundry {
//    private int idjasa,idkategori;
//    private String jasa,satuan,kategori,biaya;
//
//
//    public JasaLaundry(int idjasa, int idkategori, String kategori, String jasa, String biaya) {
//        this.idjasa = idjasa;
//        this.idkategori = idkategori;
//        this.jasa = jasa;
//        this.biaya = biaya;
//        this.satuan = satuan;
//    }
//
////    public JasaLaundry(int idjasa, int idkategori, String jasa, String biaya, String satuan) {
////
////    }
//
//    public int getIdjasa() {
//        return idjasa;
//    }
//
//    public int getIdkategori() {
//        return idkategori;
//    }
//
//    public void setIdkategori(int idkategori) {
//        this.idkategori = idkategori;
//    }
//
//    public int getIdJasa() {
//        return idjasa;
//    }
//
//    public void setIdjasa(int idjasa) {
//        this.idjasa = idjasa;
//    }
//
//    public String getJasa() {
//        return jasa;
//    }
//
//    public void setJasa(String jasa) {
//        this.jasa = jasa;
//    }
//
//    public String getSatuan() {
//        return satuan;
//    }
//
//    public void setSatuan(String satuan) {
//        this.satuan = satuan;
//    }
//
//    public String getKategori() {
//        return kategori;
//    }
//
//    public void setKategori(String kategori) {
//        this.kategori = kategori;
//    }
//
//    public String getBiaya() {
//        return kategori;
//    }
//
//    public void setBiaya(String biaya) {
//        this.biaya = biaya;
//    }
//
//}
