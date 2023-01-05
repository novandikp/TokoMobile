package com.itbrain.aplikasitoko.Model;

public class Teknisi {
    private Integer idteknisi;

    public Integer getIdteknisi() {
        return idteknisi;
    }

    public void setIdteknisi(Integer idteknisi) {
        this.idteknisi = idteknisi;
    }

    public String getTeknisi() {
        return teknisi;
    }

    public void setTeknisi(String teknisi) {
        this.teknisi = teknisi;
    }

    public String getAlamatteknisi() {
        return alamatteknisi;
    }

    public void setAlamatteknisi(String alamatteknisi) {
        this.alamatteknisi = alamatteknisi;
    }

    public String getNoteknisi() {
        return noteknisi;
    }

    public void setNoteknisi(String noteknisi) {
        this.noteknisi = noteknisi;
    }

    public Teknisi(Integer idteknisi, String teknisi, String alamatteknisi, String noteknisi) {
        this.idteknisi = idteknisi;
        this.teknisi = teknisi;
        this.alamatteknisi = alamatteknisi;
        this.noteknisi = noteknisi;
    }

    private String teknisi, alamatteknisi, noteknisi;
}
