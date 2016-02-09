package com.util;

import com.conf.ClassLoadConfig;
import com.server.classload.packetloader.PacketLoader;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseUtil {
    private static final Logger LOG = Logger.getLogger(BaseUtil.class.getName());

    /**
     * Get all packet loader instances from refereed package.
     * @param scannedPackage package with classes.
     * @return list with loaders.
     */
    public static List<PacketLoader> getLoaderInstancesFromPackage(String scannedPackage) {
        String scannedPath      = scannedPackage.replace(ClassLoadConfig.DOT, ClassLoadConfig.SLASH);
        URL scannedUrl          = Thread.currentThread().getContextClassLoader().getResource(scannedPath);

        if (scannedUrl == null) {
            LOG.log(Level.WARNING, String.format(
                    ClassLoadConfig.BAD_PACKAGE_ERROR,
                    scannedPath,
                    scannedPackage));

            return null;
        } else {
            File[] scannedDir           = new File(scannedUrl.getFile()).listFiles();
            List<PacketLoader> classes  = new ArrayList<>();

            if (scannedDir != null) {
                for (File file : scannedDir) {
                    String resource         = scannedPackage + ClassLoadConfig.DOT + file.getName();
                    int endIndex            = resource.length() - ClassLoadConfig.CLASS_SUFFIX.length();
                    String className        = resource.substring(0, endIndex);

                    try {
                        Object loader = Class.forName(className).newInstance();
                        if (loader instanceof PacketLoader) {
                            classes.add((PacketLoader) loader);
                        }
                    } catch (Exception e) {
                        LOG.log(Level.SEVERE, e.toString(), e.getMessage());
                    }
                }
            }

            return classes;
        }
    }

    /**
     * Algorithm for generating integer from string.
     * @param className string for generating.
     * @return int for string.
     */
    public static int getUniqueIdForClassName(String className) {
        int rez = 0;
        for (char c : className.toCharArray()) {
            rez += ((c << 16) | (c >> 16)) ^ className.length();
        }

        return rez;
    }
}