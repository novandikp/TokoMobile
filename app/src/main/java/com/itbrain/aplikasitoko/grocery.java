package com.itbrain.aplikasitoko;

public class grocery {
    private String nama;
    private int kuantiti;

    public grocery(String nama, int kuantiti){
        this.nama = nama;
        this.kuantiti = kuantiti;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama){
        this.nama = nama;
    }

    public int getKuantiti(){
        return kuantiti;
    }

    public void setKuantiti(int kuantiti){
        this.kuantiti = kuantiti;
    }
}
