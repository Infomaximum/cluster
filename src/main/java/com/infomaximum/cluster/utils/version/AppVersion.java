package com.infomaximum.cluster.utils.version;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by kris on 22.07.16.
 */
public class AppVersion {

    private static Map<Class, String> _hashVersions = new HashMap<Class, String>();

    public static String getVersion(Class clazz) {
        String version = _hashVersions.get(clazz);
        if (version==null) {
            synchronized (_hashVersions) {
                version = _hashVersions.get(clazz);
                if (version == null) {
                    version = clazz.getPackage().getImplementationVersion();
                    if (version==null) {
                        //Возможно это запуск из проекта
                        Path fileBuildGradle = Paths.get("build.gradle");
                        if (Files.exists(fileBuildGradle)) {
                            try {
                                try (Stream<String> stream = Files.lines(fileBuildGradle)) {
                                    String lineWithVersion = stream.filter(s -> s.trim().startsWith("version ")).findFirst().orElse(null);
                                    if (lineWithVersion!=null) {
                                        Pattern pattern = Pattern.compile("'(.+?)'");
                                        Matcher matcher = pattern.matcher(lineWithVersion);
                                        matcher.find();
                                        version=matcher.group(1);
                                    }
                                }
                            } catch (Exception e) {
                                throw new RuntimeException("Exception find version", e);
                            }
                        }
                    }
                    if (version==null) throw new RuntimeException("Not found version app");

                    _hashVersions.put(clazz, version);
                }
            }
        }
        return version;
    }

}
