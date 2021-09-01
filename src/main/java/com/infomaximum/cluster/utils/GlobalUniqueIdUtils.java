package com.infomaximum.cluster.utils;

/**
 * Алгоритм кодирования UniqueId основан на следующей идеи:
 * Первый байт всегда ноль (индекс: 0) (что бы получалось положительное число)
 * Следующие 8 байтов отведены для идентификатора ноды (индекс: с 1 по 8 - включительно)
 * Оставшиеся 23 байтов отведены для уникального идинтификатора игрока (индекс: с 9 по 31 - включительно)
 * <!-- Удобный просмотр байтов - python: [n >> i & 1 for i in range(31,-1,-1)] -->
 * <!-- например: [8388607 >> i & 1 for i in range(31,-1,-1)] -->
 */
public class GlobalUniqueIdUtils {

    private static int maskUniqueId = 0b11111111;
    private static int maskLocalId = 0b11111111111111111111111;


    //Возврощаем глобальный идентификатор
    public static int getGlobalUniqueId(byte node, int localId) {
        if (localId > 8388607) {
            throw new RuntimeException("Big localId");//локальный идентификатор не может быть больше этого числа
        }
        return (localId) | (((int) node) << 23);
    }

    //Возврощаем номер ноды по глобальному идентификатору
    public static byte getNode(int globalUniqueId) {
        return (byte) (maskUniqueId & globalUniqueId >>> 23);
    }

    //Возврощаем локальный идентификатор из глобального идентификатора
    public static int getLocalId(int globalUniqueId) {
        return maskLocalId & globalUniqueId;
    }

}
