package me.lucanius.prac.tools.registration;

import com.google.common.collect.ImmutableSet;
import lombok.SneakyThrows;
import me.lucanius.prac.Twilight;

import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * I don't know who the author is.
 * @edited Lucanius
 */
public final class ClassRegistration {

    private final Twilight plugin = Twilight.getInstance();

    public ClassRegistration init(String packageName) {
        for (Class<?> clazz : getClassesInPackage(packageName)) {
            try {
                clazz.newInstance();
            } catch (Exception ignored) {}
        }

        return this;
    }

    @SneakyThrows
    public Collection<Class<?>> getClassesInPackage(String packageName) {
        final Collection<Class<?>> classes = new ArrayList<>();
        final CodeSource codeSource = plugin.getClass().getProtectionDomain().getCodeSource();
        final URL resource = codeSource.getLocation();

        final String relPath = packageName.replace(".", "/");
        final String resPath = resource.getPath().replace("%20", " ");
        final String jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");

        final JarFile jarFile = new JarFile(jarPath);
        final Enumeration<JarEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {
            final JarEntry entry = entries.nextElement();
            final String entryName = entry.getName();
            String className = null;
            if (entryName.endsWith(".class") && entryName.startsWith(relPath) && entryName.length() > relPath.length() + "/".length()) {
                className = entryName.replace("/", ".").replace("\\", ".").replace(".class", "");
            }
            if (className != null) {
                Class<?> clazz = null;
                try {
                    clazz = Class.forName(className);
                } catch (final ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (clazz != null) {
                    classes.add(clazz);
                }
            }
        }

        jarFile.close();

        return ImmutableSet.copyOf(classes);
    }
}
