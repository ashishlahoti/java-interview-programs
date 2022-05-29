package com.example.bits;

import org.junit.Test;

public class BitDivide {
    public static void main(String[] args) {
        System.out.println(divide(46, 3));
        //System.out.println(divide(13, -3));
        //System.out.println(divide(-13, 3));
        //System.out.println(divide(-13, -3));
        //System.out.println(divide(2147483647, 1));
        //System.out.println(divide(2147483647, 2));
        //System.out.println(divide(-2147483647, 1));
        //System.out.println("Answer:" + divide(-2147483648, -1)); //2147483648
        //System.out.println("Answer:" + divide(-2147483648, 2)); //-1073741824
        //System.out.println("Answer:" + divide(-2147483648, -3)); //715827882
        //System.out.println("Answer:" + divide(-1010369383, -2147483648));
    }

    public static int divide(int dividend, int divisor) {
        boolean isNegative = dividend < 0 ^ divisor < 0;
        long dividendMod = dividend < 0 ? ((long) dividend)*-1 : dividend;
        long divisorMod = divisor < 0 ? ((long) divisor)*-1 : divisor;

        if(dividendMod == 0 || dividendMod < divisorMod) return 0;
        if(dividend == Integer.MIN_VALUE && divisor == -1) return Integer.MAX_VALUE;
        if(divisorMod == 1) return (int) (isNegative ? 0-dividendMod : dividendMod);

        // Assume dividend = 46 and divisor = 3
        int count = 0;
        long num = divisorMod; //3
        // compare the dividend value against divisor*2^n to find the value which is just greater then dividend
        while (num < dividendMod) {
            num = num << 1; // num   = 6, 12, 24, 48
            count++;        // count = 1, 2, 3, 4
        }
        // at this point num = 48 is just greater than dividend 46
        int multiplier = 1<<count; // multiplier is 2^4 = 16 to reach 48, then what is the multiplier for divident 46?
        while(num > dividendMod){
            num = num - divisorMod; // 48 - 3
            multiplier--;               // 16-1 = 15 <-- answer
        }
        return isNegative ? 0-multiplier : multiplier;
    }
}
