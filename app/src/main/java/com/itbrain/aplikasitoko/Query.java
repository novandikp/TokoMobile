package com.itbrain.aplikasitoko;

import com.itbrain.aplikasitoko.restoran.ModulRestoran;

public class Query {
    static String slct="SELECT * FROM ";
    public static String select(String table){
        return slct + table;
    }
    public static String selectwhere(String table){
        return ModulRestoran.slct+table+" WHERE ";
    }
    public static String sWhere(String PrimaryKey,String value){
        return PrimaryKey+" = "+ModulRestoran.quote(value);
    }
    public static String quoteMirip(String w){
        return "\'%" + ModulRestoran.addSlashes(w) + "%\'" ;
    }
    public static String sOrderASC(String key){
        return " ORDER BY "+key+" ASC" ;
    }

    public static String sOrderDESC(String key){
        return " ORDER BY "+key+" DESC" ;
    }

    public static String sLike(String key, String value){
        return key+" LIKE "+quoteMirip(value) ;
    }

    public static String sBetween(String key, String v1, String v2){
        return key+" BETWEEN "+ ModulRestoran.quote(v1)+" AND "+ ModulRestoran.quote(v2) ;
    }

    public static String sCount(String table,String key){
        return "SELECT COUNT("+key+") FROM "+table ;
    }
    public static String sCountWhere(String table,String key,String values){
        return "SELECT COUNT("+key+") FROM "+table+" WHERE "+key+"="+values ;
    }

    public static String sSum(String table,String key){
        return "SELECT SUM("+key+") FROM "+table ;
    }

    public static String sAvg(String table,String key){
        return "SELECT AVG("+key+") FROM "+table ;
    }
    public static String splitParam(String query, String[] p){
        String pecah[] = query.split("\\?") ;
        if(pecah.length > 1){
            String fix = "" ;
            if((p.length+1) != pecah.length){
                return "gagal" ;
            } else {
                int i ;
                for (i = 0; i < p.length; i++) {
                    fix += pecah[i] + ModulRestoran.quote(p[i]) ;
                }
                fix += pecah[i] ;

                return fix ;
            }
        } else {
            return query ;
        }
    }
    public static String splitParam(String query){
        return query ;
    }
}
