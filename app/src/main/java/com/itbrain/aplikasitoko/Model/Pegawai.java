package com.itbrain.aplikasitoko.Model;

public class Pegawai {
    private int idpegawai;
    private String pegawai,alamatpegawai,notelppegawai;

    public Pegawai(int idpegawai, String pegawai, String alamatpegawai, String notelppegawai) {
        this.idpegawai = idpegawai;
        this.pegawai = pegawai;
        this.alamatpegawai = alamatpegawai;
        this.notelppegawai = notelppegawai;
    }

    public int getIdpegawai() {
        return idpegawai;
    }

    public void setIdpegawai(int idpegawai) {
        this.idpegawai = idpegawai;
    }

    public String getPegawai() { return pegawai; }
    public void setPegawai(String pegawai) {
        this.pegawai = pegawai;
    }
    public String getAlamatpegawai() { return alamatpegawai; }
    public void setAlamatpegawai(String alamatpegawai) {
        this.alamatpegawai = alamatpegawai;
    }
    public String getNotelppegawai() { return notelppegawai; }
    public void setNotelppegawai(String notelppegawai) {
        this.notelppegawai = notelppegawai;
    }
}
