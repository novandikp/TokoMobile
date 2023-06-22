package com.itbrain.aplikasitoko.TokoKredit;

public class ModelTagihanKredit {

    private int cicilanke, flag_tagihan, idtagihan;
    private String tglbayar, tgltempo;
    private double angsuran;

    public int getCicilanke() {
        return cicilanke;
    }

    public String getTglbayar() {
        return tglbayar;
    }

    public String getTgltempo() {
        return tgltempo;
    }

    public double getAngsuran() {
        return angsuran;
    }

    public int getIdtagihan() {
        return idtagihan;
    }

    public int getFlag_tagihan() {
        return flag_tagihan;
    }

    public ModelTagihanKredit(int idtagihan, int cicilanke, String tglbayar, String tgltempo, double angsuran, int flag_tagihan) {
        this.idtagihan = idtagihan;
        this.cicilanke = cicilanke;
        this.tglbayar = tglbayar;
        this.tgltempo = tgltempo;
        this.angsuran = angsuran;
        this.flag_tagihan = flag_tagihan;
    }
}
