package com.example.mrh.musicplayer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MR.H on 2016/12/29 0029.
 */

public class Test {
    private static final String TAG = "Test";
    public static void main(String[] args){
        HashMap<String, ArrayList<String>> hashMap = new HashMap<>();
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        hashMap.put("a", list);
        ArrayList<String> list1 = hashMap.get("a");
        list1.remove(2);
        ArrayList<String> list2 = hashMap.get("a");
        System.out.println("s000 ++ "+list2.size());
    }
}
