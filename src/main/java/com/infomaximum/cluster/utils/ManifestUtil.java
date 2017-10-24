package com.infomaximum.cluster.utils;

import com.infomaximum.cluster.Version;
import com.infomaximum.cluster.exception.runtime.VersionNotFoundException;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ManifestUtil {

    public static Version getVersion(Class clazz) {
        String version = clazz.getPackage().getImplementationVersion();
        if (version == null) {
            try {
                version = getAssignedGradleValue("version", Paths.get("build.gradle"));
            } catch (IOException e) {
                throw new VersionNotFoundException(e);
            }
        }

        return Version.parse(version);
    }

    public static Version getEnvironmentVersion(Class clazz) {
        try {
            String version = getManifestValue("Environment-Version", clazz);
            if (version == null) {
                version = getDefinedGradleValue("environment_version", Paths.get("build.gradle"));
            }
            return Version.parse(version);
        } catch (IOException e) {
            throw new VersionNotFoundException(e);
        }
    }

    public static String getComponentUUID(Class clazz) {
        try {
            String uuid = getManifestValue("Component-UUID", clazz);
            if (uuid != null) {
                return uuid;
            }

            String group = getAssignedGradleValue("group", Paths.get("build.gradle"));
            if (group == null) {
                return null;
            }

            String rootProject_name = getAssignedGradleValue("rootProject.name", Paths.get("settings.gradle"));
            if (rootProject_name == null) {
                return null;
            }

            return group + '.' + rootProject_name;
        } catch (IOException e) {
            throw new VersionNotFoundException(e);
        }
    }

    public static String getManifestValue(String variableName, Class clazz) throws IOException {
        URLClassLoader cl = (URLClassLoader) clazz.getClassLoader();
        URL url = cl.findResource("META-INF/MANIFEST.MF");
        if (url == null) {
            return null;
        }

        Manifest manifest = new Manifest(url.openStream());
        return manifest.getMainAttributes().getValue(new Attributes.Name(variableName));
    }

    public static String getAssignedGradleValue(String variableName, Path gradlePath) throws IOException {
        return getGradleValue("^ *" + variableName + "(?: +| *= *)'(.+?)'", gradlePath);
    }

    public static String getDefinedGradleValue(String variableName, Path gradlePath) throws IOException {
        return getGradleValue("^ *def +" + variableName + " *= *'(.+?)'", gradlePath);
    }

    private static String getGradleValue(String patternValue, Path gradlePath) throws IOException {
        if (!Files.exists(gradlePath)) {
            return null;
        }

        try (Stream<String> stream = Files.lines(gradlePath)) {
            Pattern pattern = Pattern.compile(patternValue);
            Iterator<String> i = stream.iterator();
            while (i.hasNext()) {
                Matcher matcher = pattern.matcher(i.next());
                if (matcher.find()) {
                    return matcher.group(1);
                }
            }
        }

        return null;
    }
}
