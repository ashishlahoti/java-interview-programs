package com.example.core;

import java.text.DecimalFormat;

public class LongTest {
    public static void main(String[] args) {
        primitiveToString();
        longObjToString();
        nullObjToString();
    }

    public static void primitiveToString(){
        String s1 = Long.toString(1L); // "1"
        String s2 = String.valueOf(1L);  // "1"
        String s3 = "" + 1L; // "1"
        String s4 = new StringBuilder().append(1L).toString(); // "1"
        String s5 = String.format("%d", 1L); // "1"
        String s6 = new DecimalFormat("#").format(1L);

        System.out.println(s1 + " " + s2 + " " + s3 + " " + s4 + " " + s5 + " " + s6);
    }

    public static void longObjToString(){
        Long longObj = 1L;

        String s1 = longObj.toString();  // "1"
        String s2 = Long.toString(longObj); // "1"
        String s3 = String.valueOf(longObj);  // "1"
        String s4 = "" + longObj; // "1"
        String s5 = new StringBuilder().append(longObj).toString(); // "1"
        String s6 = String.format("%d", longObj); // "1"
        String s7 = new DecimalFormat("#").format(longObj);

        System.out.println(s1 + " " + s2 + " " + s3 + " " + s4 + " " + s5 + " " + s6 + " " + s7);
    }

    public static void nullObjToString(){
        Long longObj = null;

        String s1 = longObj.toString();  // throw "NullPointerException"
        String s2 = Long.toString(longObj); // throw "NullPointerException"
        String s3 = String.valueOf(longObj);  // throw "IllegalArgumentException"
        String s4 = "" + longObj; // "null"
        String s5 = new StringBuilder().append(longObj).toString(); // "null"
        String s6 = String.format("%d", longObj); // "null"
        String s7 = new DecimalFormat("#").format(longObj); // throw "IllegalArgumentException"

    }
}
