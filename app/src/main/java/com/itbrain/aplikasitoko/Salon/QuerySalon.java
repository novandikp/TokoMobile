package com.itbrain.aplikasitoko.Salon;

import android.text.TextUtils;

public class QuerySalon {
    static String slct = "SELECT * FROM ";

    public static String quoteForLike(String w){
        return "\'%" + FunctionSalon.addSlashes(w) + "%\'" ;
    }

    public static String select(String table){
        return slct + table;
    }

    public static String selectwhere(String table){
        return FunctionSalon.slct + table + " WHERE ";
    }

    public static String sOrderASC(String key){
        return " ORDER BY "+key+" ASC" ;
    }

    public static String sOrderDESC(String key){
        return " ORDER BY "+key+" DESC" ;
    }

    public static String sWhere(String key, String value){
        return key+"="+ FunctionSalon.quote(value) ;
    }

    public static String sLike(String key, String value){
        return key+" LIKE "+quoteForLike(value) ;
    }

    public static String sBetween(String key, String v1, String v2){
        return key+" BETWEEN "+ FunctionSalon.quote(v1)+" AND "+ FunctionSalon.quote(v2) ;
    }

    public static String sCount(String table,String key){
        return "SELECT COUNT("+key+") FROM "+table ;
    }

    public static String sSum(String table,String key){
        return "SELECT SUM("+key+") FROM "+table ;
    }

    public static String sAvg(String table,String key){
        return "SELECT AVG("+key+") FROM "+table ;
    }

    public static String splitParam(String QuerySalon, String[] p){
        String pecah[] = QuerySalon.split("\\?") ;
        if(pecah.length > 1){
            String fix = "" ;
            if((p.length+1) != pecah.length){
                return "gagal" ;
            } else {
                int i ;
                for (i = 0; i < p.length; i++) {
                    fix += pecah[i] + FunctionSalon.quote(p[i]) ;
                }
                fix += pecah[i] ;

                return fix ;
            }
        } else {
            return QuerySalon ;
        }
    }

    public static String slimit(String tabel,String keyCari, String pencarian){
        String hasil = "" ;
        if(TextUtils.isEmpty(pencarian)){
            hasil = QuerySalon.select(tabel) +" LIMIT 30"  ;
        } else {
            hasil = QuerySalon.selectwhere(tabel) +QuerySalon.sLike(keyCari,pencarian) ;
        }
        return hasil ;
    }

    public static String splitParam(String QuerySalon){
        return QuerySalon ;
    }
}
