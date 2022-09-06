package com.itbrain.aplikasitoko.Model;

public class IdentitasLaundry {
    private int ididentitas;
    private String namatoko,alamattoko,notelptoko;

    public IdentitasLaundry(int ididentitas, String namatoko,String notelptoko) {
    }

    public int getIdidentitas() {
        return ididentitas;
    }

    public void setIdpegawai(int idpegawai) {
        this.ididentitas = ididentitas;
    }

    public String getNamatoko() { return namatoko; }
    public void setNamatoko(String namatoko) {
        this.namatoko = namatoko;
    }
    public String getAlamattoko() { return alamattoko; }
    public void setAlamattoko(String alamattoko) { this.alamattoko = alamattoko; }
    public String getNotelptoko() { return notelptoko; }
    public void setNotelptoko(String notelptoko) { this.notelptoko = notelptoko; }
    }
