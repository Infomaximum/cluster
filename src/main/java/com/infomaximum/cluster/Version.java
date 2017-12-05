package com.infomaximum.cluster;

public class Version {

    public final int major;
    public final int minor;
    public final int build;

    public Version(int major, int minor, int build) {
        if (major < 0 || minor < 0 || build < 0) {
            throw new IllegalArgumentException();
        }

        this.major = major;
        this.minor = minor;
        this.build = build;
    }

    public static Version parse(String source) {
        String[] parts = source.split("\\.");
        if (parts.length != 3) {
            throw new RuntimeException("Version string must be contans 3 parts: " + source);
        }

        return new Version(
                Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2])
        );
    }

    public static int compare(Version left, Version right) {
        if (left.major != right.major) {
            return Integer.compare(left.major, right.major);
        }
        if (left.minor != right.minor) {
            return Integer.compare(left.minor, right.minor);
        }
        return Integer.compare(left.build, right.build);
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + build;
    }
}
