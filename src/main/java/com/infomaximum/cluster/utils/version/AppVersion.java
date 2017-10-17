package com.infomaximum.cluster.utils.version;

import com.infomaximum.cluster.Version;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by kris on 22.07.16.
 */
public class AppVersion {

    public static Version getVersion(Class clazz) {
        String version = clazz.getPackage().getImplementationVersion();
        if (version != null) {
            return Version.parse(version);
        }

        //Возможно это запуск из проекта
        Path fileBuildGradle = Paths.get("build.gradle");
        if (Files.exists(fileBuildGradle)) {
            try (Stream<String> stream = Files.lines(fileBuildGradle)){
                String lineWithVersion = stream.filter(s -> s.trim().startsWith("version ")).findFirst().orElse(null);
                if (lineWithVersion != null) {
                    Matcher matcher = Pattern.compile("'(.+?)'").matcher(lineWithVersion);
                    matcher.find();
                    version = matcher.group(1);
                }
            } catch (Exception e) {
                throw new RuntimeException("Exception find version", e);
            }
        }

        if (version == null) {
            throw new RuntimeException("Not found version of app.");
        }

        return Version.parse(version);
    }
}
