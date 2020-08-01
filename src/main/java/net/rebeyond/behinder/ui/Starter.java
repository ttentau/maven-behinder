//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.rebeyond.behinder.ui;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class Starter {
    public Starter() {
    }

    public static void main(String[] args) {
        System.setProperty("org.eclipse.swt.browser.DefaultType", "ie,webkit");
        addJarToClasspath(getArchFilename("swt"));
        Main main = new Main();
        main.start();
    }

    public static String getArchFilename(String prefix) {
        return prefix + "_" + getOSName() + "_" + getArchName() + ".jar";
    }

    private static String getOSName() {
        String osNameProperty = System.getProperty("os.name");
        if (osNameProperty == null) {
            throw new RuntimeException("os.name property is not set");
        } else {
            osNameProperty = osNameProperty.toLowerCase();
            if (osNameProperty.contains("win")) {
                return "win";
            } else if (osNameProperty.contains("mac")) {
                return "osx";
            } else if (!osNameProperty.contains("linux") && !osNameProperty.contains("nix")) {
                throw new RuntimeException("Unknown OS name: " + osNameProperty);
            } else {
                return "linux";
            }
        }
    }

    private static String getArchName() {
        String osArch = System.getProperty("os.arch");
        return osArch != null && osArch.contains("64") ? "64" : "32";
    }

    public static void addJarToClasspath(String jarFile) {
        try {
            URL url = Starter.class.getClassLoader().getResource("lib/" + jarFile);
            URLClassLoader urlClassLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
            Class<?> urlClass = URLClassLoader.class;
            Method method = urlClass.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(urlClassLoader, url);
        } catch (Throwable var5) {
            var5.printStackTrace();
        }

    }
}
