package com.infomaximum.cluster.utils.version;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kris on 07.04.16.
 */
public class VersionUtils {

    /**
     * Компанатор сравнивания версий
     * @param version1
     * @param version2
     * @return ("5.1.1", "5.0.1") = 1
     * @return ("4.8.9", "5.0.1") = -1
     */
    public static int compareVersion(String version1, String version2) {
        String[] arr1 = version1.split("\\.");
        String[] arr2 = version2.split("\\.");

        int i=0;
        while(i<arr1.length || i<arr2.length){
            if(i<arr1.length && i<arr2.length){
                if(Integer.parseInt(arr1[i]) < Integer.parseInt(arr2[i])){
                    return -1;
                }else if(Integer.parseInt(arr1[i]) > Integer.parseInt(arr2[i])){
                    return 1;
                }
            } else if(i<arr1.length){
                if(Integer.parseInt(arr1[i]) != 0){
                    return 1;
                }
            } else if(i<arr2.length){
                if(Integer.parseInt(arr2[i]) != 0){
                    return -1;
                }
            }

            i++;
        }

        return 0;
    }

    /** Возврощаем одинаковые ли это версии */
    public static boolean isEquals(String version1, String version2){
        return (compareVersion(version1, version2)==0);
    }

    /** Сортируем версии в порядке возростания */
    public static void collectionsSortAsc(ArrayList<String> items) {
        Collections.sort(items, new Comparator<String>() {
            @Override
            public int compare(final String version1, final String version2) {
                return compareVersion(version1, version2);
            }
        });
    }

    /** валидируем версию */
    public static void validateVersion(String codeVersion) {
        //Сначала валидируем на допустимые символы
        Matcher m = Pattern.compile("^[0-9.]+$").matcher(codeVersion);
        if (!m.matches()) throw new RuntimeException("Ошибка валидации версии(Недопустимые символы): " + codeVersion);

        //Теперь уже анализируем символы
        String[] versions = codeVersion.split("\\.");
        if (versions.length!=3) throw new RuntimeException("Ошибка валидации версии: " + codeVersion);
        for (String siVersion: versions){
            if (siVersion.isEmpty()) throw new RuntimeException("Ошибка валидации версии: " + codeVersion);
            Integer iVersion;
            try {
                iVersion = Integer.parseInt(siVersion);
            } catch (Exception e) {
                throw new RuntimeException("Ошибка валидации версии: " + codeVersion);
            }
            if (iVersion<0) throw new RuntimeException("Ошибка валидации версии: " + codeVersion);
        }
    }

}
