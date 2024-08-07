package com.github.linyuzai.plugin.jar.extension;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.jar.concept.PluginClassLoader;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.jar.JarFile;

/**
 * 扩展jar插件类加载器
 */
public class ExJarPluginClassLoader extends PluginClassLoader {

    static {
        registerAsParallelCapable();
    }

    /**
     * Create a new {@link ExJarPluginClassLoader} instance.
     *
     * @param urls   the URLs from which to load classes and resources
     * @param parent the parent class loader for delegation
     * @since 2.3.1
     */
    public ExJarPluginClassLoader(Plugin plugin, URL[] urls, ClassLoader parent) {
        super(plugin, urls, parent);
    }

    @Override
    protected void definePackage(String className, String packageName) {
        String packageEntryName = packageName.replace('.', '/') + "/";
        String classEntryName = className.replace('.', '/') + ".class";
        for (URL url : getURLs()) {
            try {
                URLConnection connection = url.openConnection();
                if (connection instanceof JarURLConnection) {
                    JarFile jarFile = ((JarURLConnection) connection).getJarFile();
                    if (jarFile.getEntry(classEntryName) != null && jarFile.getEntry(packageEntryName) != null
                            && jarFile.getManifest() != null) {
                        definePackage(packageName, jarFile.getManifest(), url);
                    }
                }
            } catch (IOException ex) {
                // Ignore
            }
        }
    }
}
