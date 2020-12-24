package com.web.common.utils;

import org.junit.Test;

public class Gcdlcm {

    //最大公约数
    public static int get_gcd(int a, int b) {
        int max, min;
        max = (a > b) ? a : b;
        min = (a < b) ? a : b;

        if (max % min != 0) {
            return get_gcd(min, max % min);
        } else {
            return min;
        }

    }

    // 最小公倍数
    public static int get_lcm(int a, int b) {
        return a * b / get_gcd(a, b);
    }

    @Test
    public void test2(){
        int n1 = 5;
        int n2 = 10;
        System.out.println("(" + n1 + "," + n2 + ")" + "=" + get_gcd(n1, n2));
        System.out.println("[" + n1 + "," + n2 + "]" + "=" + get_lcm(n1, n2));

    }

}
