package com.itbrain.aplikasitoko.TokoKredit;

import com.opencsv.CSVWriter;

/**
 * Created by KomputerKit on 01/06/2017.
 */

public class FExcelKredit {
    public static Boolean csvNextLine(CSVWriter csvWriter){
        try {
            String[] header = {
                    "",
                    "",
                    "",
            };
            csvWriter.writeNext(header);
            return true ;
        }catch (Exception e){
            return false ;
        }
    }

    public static Boolean csvNextLine(CSVWriter csvWriter, int total){
        try {
            String[] header = {
                    "",
                    "",
                    "",
            };
            for (int i = 0 ; i < total ; i++){
                csvWriter.writeNext(header);
            }
            return true ;
        }catch (Exception e){
            return false ;
        }
    }

    public static Boolean setCenter(CSVWriter csvWriter, int JumlahKolom, String value){
        try {
            String[] item = {} ;
            int baru = JumlahKolom/2 - 1 ;
            int i ;
            for (i = 0 ; i < baru ; i++){
                item[i] = "" ;
            }
            item[i] = value ;
            csvWriter.writeNext(item);
            return true ;
        }catch (Exception e){
            return false ;
        }
    }
}
