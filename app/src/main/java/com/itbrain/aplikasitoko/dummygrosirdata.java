package com.itbrain.aplikasitoko;

import java.util.ArrayList;
import java.util.List;

public class dummygrosirdata {
    public static List<grocery> groserilist(){
        grocery telur = new grocery("telur", 10);
        grocery sabun = new grocery("sabun", 2);
        grocery kopi = new grocery("kopi", 5);
        grocery teh = new grocery("teh", 2);

        List<grocery> groserilist = new ArrayList<>();

        groserilist.add(telur);
        groserilist.add(sabun);
        groserilist.add(kopi);
        groserilist.add(teh);
        return groserilist;
    }
}
