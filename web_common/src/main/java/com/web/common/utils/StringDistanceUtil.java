package com.web.common.utils;

/**
 *
 *
 * @description 字符串编辑距离工具类
 * 编辑距离，指的是两个字符串之间，由一个转换成另一个所需的最少编辑操作次数
 * 用以计算两个字符串的相似度
 */
public class StringDistanceUtil {


    private static int compare(String source, String target) {
        // 矩阵
        int d[][];
        int n = source.length();
        int m = target.length();
        // 遍历source的
        int i;
        // 遍历target的
        int j;
        char ch1;
        char ch2;
        // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
        int temp;
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        d = new int[n + 1][m + 1];
        // 初始化第一列
        for (i = 0; i <= n; i++) {
            d[i][0] = i;
        }
        // 初始化第一行
        for (j = 0; j <= m; j++) {
            d[0][j] = j;
        }
        for (i = 1; i <= n; i++) {
            // 遍历str
            ch1 = source.charAt(i - 1);
            // 去匹配target
            for (j = 1; j <= m; j++) {
                ch2 = target.charAt(j - 1);
                if (ch1 == ch2 || ch1 == ch2 + 32 || ch1 + 32 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                // 左边+1,上边+1, 左上角+temp取最小
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }
        return d[n][m];
    }


    /**
     * 获取最小的值
     */
    private static int min(int one, int two, int three) {
        return (one = one < two ? one : two) < three ? one : three;
    }

    /**
     * 获取两字符串的相似度
     * 值越大,相似度越高
     */
    public static float getSimilarityRatio(String source, String target) {
        int max = Math.max(source.length(), target.length());
        return 1 - (float) compare(source, target) / max;
    }

    public static void main(String[] args) {
        float ratio = getSimilarityRatio("糖衣片(片芯重0.3g)", "片剂(糖衣,薄膜衣)");
        float ratio1 = getSimilarityRatio("糖衣片(片芯重0.3g)", "糖衣片(片芯重0.3g);薄膜衣片  每片重0.3g");

        System.out.println(ratio);
        System.out.println(ratio1);
    }
}
