package com.github.linyuzai.plugin.core.logger;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.event.PluginErrorEvent;
import com.github.linyuzai.plugin.core.event.PluginEventListener;
import com.github.linyuzai.plugin.core.exception.PluginLoadException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PluginErrorLogger implements PluginEventListener {

    private final PluginLogger logger;

    @Override
    public void onEvent(Object event) {
        if (event instanceof PluginErrorEvent) {
            Throwable error = ((PluginErrorEvent) event).getError();
            if (error instanceof PluginLoadException) {
                Plugin plugin = ((PluginLoadException) error).getContext().getPlugin();
                String message = plugin == null ? "Load error" : "Load error: " + plugin;
                logger.error(message, error);
            } else {
                logger.error("Plugin error", error);
            }
        }
    }
}
