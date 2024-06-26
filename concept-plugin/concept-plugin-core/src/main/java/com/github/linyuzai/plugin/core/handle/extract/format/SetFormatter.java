package com.github.linyuzai.plugin.core.handle.extract.format;

import com.github.linyuzai.plugin.core.tree.PluginTree;
import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@link Map} 转 {@link Set} 的格式器
 */
@Getter
@RequiredArgsConstructor
public class SetFormatter extends TreeNodePluginFormatter<Set<Object>> {

    /**
     * {@link Set} 的类型
     */
    private final Class<?> setClass;

    public SetFormatter() {
        this(Set.class);
    }

    /**
     * 格式化，根据 {@link Set} 类型实例化并添加 {@link Map} 的 value
     *
     * @param nodes 被格式化的对象
     * @return {@link Set} 格式的插件
     */
    @Override
    public Set<Object> formatNodes(List<PluginTree.Node> nodes) {
        Set<Object> set = ReflectionUtils.newSet(setClass);
        set.addAll(nodes.stream()
                .map(PluginTree.Node::getValue)
                .collect(Collectors.toList()));
        return set;
    }
}
