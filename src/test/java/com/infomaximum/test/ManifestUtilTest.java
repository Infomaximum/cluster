package com.infomaximum.test;

import com.infomaximum.cluster.Version;
import com.infomaximum.cluster.utils.ManifestUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ManifestUtilTest {

    @Test
    public void getVersion() {
        Assert.assertTrue(Version.compare(ManifestUtil.getVersion(ManifestUtilTest.class), new Version(0, 0, 0)) > 0);
    }

    @Test
    public void getComponentUUID() {
        Assert.assertEquals("com.infomaximum.cluster", ManifestUtil.getComponentUUID(ManifestUtilTest.class));
    }

    @Test
    public void getManifestValue() throws IOException {
        Assert.assertEquals("1.0", ManifestUtil.getManifestValue("Manifest-Version", ManifestUtilTest.class));
    }
}
