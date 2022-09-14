package com.itbrain.aplikasitoko.Model;

public class IdentitasLaundry {
    private int idnama;
    private String nama,alamat,telp,caption_1,caption_2,caption_3;

    public IdentitasLaundry(int idnama, String nama, String alamat, String telp) {
        this.idnama = idnama;
        this.nama = nama;
        this.alamat = alamat;
        this.telp = telp;
    }

    public int getIdnama() {
        return idnama;
    }

    public void setIdnama(int idnama) {
        this.idnama = idnama;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getCaption_1() {
        return caption_1;
    }

    public void setCaption_1(String caption_1) {
        this.caption_1 = caption_1;
    }

    public String getCaption_2() {
        return caption_2;
    }

    public void setCaption_2(String caption_2) {
        this.caption_2 = caption_2;
    }

    public String getCaption_3() {
        return caption_3;
    }

    public void setCaption_3(String caption_3) {
        this.caption_3 = caption_3;
    }
}
