/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.List;

/**
 *
 * @author aurea
 */
public class ListUtil {

    public static int sumIntegerList(List<Integer> list) {
        if (list == null || list.size() < 1) {
            return 0;
        }

        int sum = 0;
        for (Integer i : list) {
            sum += i;
        }

        return sum;
    }

    public static Double sumDoubleList(List<Double> list) {
        if (list == null || list.size() < 1) {
            return 0.0;
        }

        Double sum = 0.0;
        for (Double i : list) {
            sum += i;
        }

        return sum;
    }
    
    public static Float sumFloatList(List<Float> list) {
        if (list == null || list.size() < 1) {
            return 0.f;
        }

        Float sum = 0.f;
        for (Float i : list) {
            sum += i;
        }

        return sum;
    }
}
