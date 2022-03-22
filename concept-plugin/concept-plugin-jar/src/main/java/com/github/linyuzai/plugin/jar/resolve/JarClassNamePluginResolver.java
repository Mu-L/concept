package com.github.linyuzai.plugin.jar.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolve.AbstractPluginResolver;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.jar.JarPlugin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@DependOnResolvers(JarPathNamePluginResolver.class)
public class JarClassNamePluginResolver extends AbstractPluginResolver {

    @Override
    public void resolve(PluginContext context) {
        List<String> filenames = context.get(JarPlugin.PATH_NAME);
        Map<String, String> classNameMap = new LinkedHashMap<>();
        for (String filename : filenames) {
            if (filename.endsWith(".class")) {
                String className = filename.substring(0, filename.lastIndexOf("."))
                        .replaceAll("/", ".");
                classNameMap.put(filename, className);
            }
        }
        context.set(JarPlugin.CLASS_NAME, classNameMap);
    }

    @Override
    public boolean support(PluginContext context) {
        return context.contains(JarPlugin.PATH_NAME);
    }
}