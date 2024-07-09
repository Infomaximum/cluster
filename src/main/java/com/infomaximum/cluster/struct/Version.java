package com.infomaximum.cluster.struct;

public class Version {

    public final int product;
    public final int major;
    public final int minor;
    public final int patch;

    private final String toString;

    public Version(int product, int major, int minor, int patch) {
        if (product < 0 || major < 0 || minor < 0 || patch < 0) {
            throw new IllegalArgumentException();
        }

        this.product = product;
        this.major = major;
        this.minor = minor;
        this.patch = patch;

        this.toString = product + "." + major + "." + minor + "." + patch;
    }

    public static Version parse(String source) throws IllegalArgumentException {
        String[] parts = source.split("\\.");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Version string must be contains 4 parts: " + source);
        }

        return new Version(
                Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2]),
                Integer.parseInt(parts[3])
        );
    }

    public static Version parseTaskUpdate(String source) throws IllegalArgumentException {
        String[] parts = source.split("\\.");
        if (parts.length == 4) {
            if (!"x".equals(parts[3])) {
                throw new IllegalArgumentException("In version string field patch not equal 'x': " + source);
            }
            return new Version(
                    Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[2]),
                    0
            );
        } else {
            throw new IllegalArgumentException("Version string must be contains 4 parts: " + source);
        }
    }

    public static int compare(Version left, Version right) {
        if (left.product != right.product) {
            return Integer.compare(left.product, right.product);
        }
        if (left.major != right.major) {
            return Integer.compare(left.major, right.major);
        }
        if (left.minor != right.minor) {
            return Integer.compare(left.minor, right.minor);
        }
        return Integer.compare(left.patch, right.patch);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Version)) return false;

        Version version = (Version) o;

        if (product != version.product) return false;
        if (major != version.major) return false;
        if (minor != version.minor) return false;
        return patch == version.patch;
    }

    @Override
    public int hashCode() {
        int result = product;
        result = 31 * result + major;
        result = 31 * result + minor;
        result = 31 * result + patch;
        return result;
    }

    @Override
    public String toString() {
        return toString;
    }
}
