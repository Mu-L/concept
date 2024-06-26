package com.github.linyuzai.plugin.zip.concept;

import com.github.linyuzai.plugin.core.concept.AbstractPlugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Getter
@RequiredArgsConstructor
public class ZipFilePlugin extends AbstractPlugin implements ZipPlugin {

    protected final File file;

    protected final URL url;

    @Override
    public Object getId() {
        return url;
    }

    @SneakyThrows
    @Override
    public void collectEntries(PluginContext context, Consumer<Entry> consumer) {
        try (ZipFile zf = createZipFile()) {
            Enumeration<? extends ZipEntry> entries = zf.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                URL id = new URL(url, name);
                consumer.accept(createZipPluginEntry(id, name));
            }
        }
    }

    protected ZipFile createZipFile() throws IOException {
        return new ZipFile(file);
    }

    protected ZipPluginEntry createZipPluginEntry(Object id, String name) {
        return new ZipFilePluginEntry(id, name, this, file);
    }
}
