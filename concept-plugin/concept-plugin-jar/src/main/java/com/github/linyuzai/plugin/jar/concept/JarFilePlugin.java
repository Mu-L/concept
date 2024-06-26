package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import com.github.linyuzai.plugin.jar.read.JarClassReader;
import com.github.linyuzai.plugin.zip.concept.ZipFilePlugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;

public class JarFilePlugin extends ZipFilePlugin implements JarPlugin {

    public JarFilePlugin(File file, URL url) {
        super(file, url);
    }

    @Override
    protected JarFile createZipFile() throws IOException {
        return new JarFile(file);
    }

    @Override
    protected JarPluginEntry createZipPluginEntry(Object id, String name) {
        return new JarFilePluginEntry(id, name, this, file);
    }

    @Override
    public void onPrepare(PluginContext context) {
        PluginTree tree = context.get(PluginTree.class);
        if (tree == null) {
            return;
        }
        Map<String, Content> classes = new HashMap<>();
        Map<String, Content> packages = new HashMap<>();
        collectClassContents(tree.getRoot(), classes, packages);
        JarPluginClassLoader classLoader =
                new JarPluginClassLoader(packages, classes, getClass().getClassLoader());
        addReader(new JarClassReader(this, classLoader));
    }

    protected void collectClassContents(PluginTree.Node node,
                                        Map<String, Content> classes,
                                        Map<String, Content> packages) {
        node.forEach(it -> {
            Object value = it.getValue();
            if (value instanceof Entry) {
                Entry entry = (Entry) value;
                if (entry.getName().endsWith("/")) {
                    packages.computeIfAbsent(entry.getName(), name -> findManifestContent(it));
                } else if (entry.getName().endsWith(".class")) {
                    classes.putIfAbsent(entry.getName(), entry.getContent());
                }
            }
        });
    }

    protected Content findManifestContent(PluginTree.Node node) {
        PluginTree.Node parent = node.getParent();
        for (PluginTree.Node child : parent.getChildren()) {
            Object childValue = child.getValue();
            if (childValue instanceof Entry) {
                Entry childEntry = (Entry) childValue;
                if (JarFile.MANIFEST_NAME.equals(child.getName())) {
                    return childEntry.getContent();
                }
            }
        }
        return null;
    }
}
