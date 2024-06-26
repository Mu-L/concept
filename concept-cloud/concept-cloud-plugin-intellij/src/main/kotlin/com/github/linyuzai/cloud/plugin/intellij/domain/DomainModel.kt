package com.github.linyuzai.cloud.plugin.intellij.domain

import com.github.linyuzai.cloud.plugin.intellij.ConceptGraphProperty
import com.github.linyuzai.cloud.plugin.intellij.ConceptGraphPropertyImpl
import com.github.linyuzai.cloud.plugin.intellij.ConceptPropertyGraph
import com.github.linyuzai.cloud.plugin.intellij.builder.lowercaseFirst
import com.github.linyuzai.cloud.plugin.intellij.builder.toSampleName
import com.intellij.openapi.module.Module
import com.intellij.openapi.observable.properties.GraphProperty
import com.intellij.openapi.observable.properties.GraphPropertyImpl
import com.intellij.openapi.observable.properties.PropertyGraph
import java.util.concurrent.CopyOnWriteArrayList
import java.util.function.Consumer

data class DomainModel(
    val initUserClass: String,
    val initDomainModule: Module?,
    val initDomainPackage: String,
    val initDomainObjectClassName: String
) {
    val propertyGraph: ConceptPropertyGraph = ConceptPropertyGraph()
    val userClass: ConceptGraphProperty<String> = property { initUserClass }
    val domainModule: ConceptGraphProperty<Module?> = property { initDomainModule }
    val domainPackage: ConceptGraphProperty<String> = property { initDomainPackage }
    val domainObjectClassName: ConceptGraphProperty<String> = property { initDomainObjectClassName }
    val domainCollectionClassName: ConceptGraphProperty<String> = property { "${initDomainObjectClassName}s" }
    val domainClassComment: ConceptGraphProperty<String> = property { "" }
    val domainPreview: ConceptGraphProperty<String> = property(false) { "" }

    val domainProps: MutableList<DomainProp> = CopyOnWriteArrayList()

    private val onDomainPropAddListeners: MutableCollection<Consumer<DomainProp>> = CopyOnWriteArrayList()
    private val onDomainPropRemoveListeners: MutableCollection<Consumer<DomainProp>> = CopyOnWriteArrayList()

    companion object {

        @JvmStatic
        val RECENTS_KEY_DOMAIN_PACKAGE = "ConceptCloud@DomainPackage"

        @JvmStatic
        val RECENTS_KEY_DOMAIN_PROP_CLASS = "ConceptCloud@DomainPropClass"
    }

    fun addDomainProp(): DomainProp {
        val prop = DomainProp(this, domainProps.size)
        domainProps.add(prop)
        for (onDomainPropAddListener in onDomainPropAddListeners) {
            onDomainPropAddListener.accept(prop)
        }
        return prop
    }

    fun removeDomainProp(index: Int) {
        val remove = domainProps.removeAt(index)
        for (i in index until domainProps.size) {
            domainProps[i].index = i
        }
        for (onDomainPropRemoveListener in onDomainPropRemoveListeners) {
            onDomainPropRemoveListener.accept(remove)
        }
    }

    fun addOnDomainPropAddListener(listener: Consumer<DomainProp>) {
        onDomainPropAddListeners.add(listener)
    }

    fun addOnDomainPropRemoveListener(listener: Consumer<DomainProp>) {
        onDomainPropRemoveListeners.add(listener)
    }

    fun validProps() {
        domainProps.removeIf {
            it.propClass.get().isBlank()
        }
        domainProps.forEach {
            if (it.propName.get().isBlank()) {
                it.propName.set(it.propClass.get().toSampleName().lowercaseFirst())
            }
        }
    }

    fun preview() {
        val imports = mutableSetOf<String>()
        //imports.add(userClass.get())
        val fields = mutableListOf<String>()
        for (prop in domainProps) {
            val className = prop.propClass.get()

            imports.add(className)

            /*val lastIndexOf = className.lastIndexOf(".")
            val classSampleName = if (lastIndexOf > 0 && className.length > 1) {
                className.substring(lastIndexOf + 1)
            } else {
                className
            }*/

            val classSampleName = className.toSampleName()

            val field = buildString {
                prop.propComment.get().apply {
                    if (isNotBlank()) {
                        append("  /**\n")
                        append("   * $this\n")
                        append("   */\n")
                    }
                }
                prop.propNotNull.get().apply {
                    if (this) {
                        append("  @NotNull\n")
                        imports.add("javax.validation.constraints.NotNull")
                    }
                }
                prop.propNotEmpty.get().apply {
                    if (this) {
                        append("  @NotEmpty\n")
                        imports.add("javax.validation.constraints.NotEmpty")
                    }
                }
                append("  private $classSampleName ${prop.propName.get()};\n")
            }

            fields.add(field)
        }

        val content = buildString {
            append("package ${domainPackage.get()};\n\n")
            if (imports.filterNot { it.startsWith("java.lang.") }.sorted().onEach {
                    append("import $it;\n")
                }.isNotEmpty()) {
                append("\n")
            }
            domainClassComment.get().apply {
                if (isNotBlank()) {
                    append("/**\n")
                    append(" * $this\n")
                    append(" */\n")
                }
            }
            append("public class ${domainObjectClassName.get()} {\n\n")
            fields.forEach {
                append("$it\n")
            }
            append("}\n")
        }
        domainPreview.set(content)
    }
}

data class DomainProp(
    val model: DomainModel,
    var index: Int,
    var propClass: ConceptGraphProperty<String> = model.property { "" },
    var propName: ConceptGraphProperty<String> = model.property { "" },
    var propNotNull: ConceptGraphProperty<Boolean> = model.property { false },
    var propNotEmpty: ConceptGraphProperty<Boolean> = model.property { false },
    var propComment: ConceptGraphProperty<String> = model.property { "" },
    var smartFill: Boolean = true,
    var onClassNameUpdateListener: ((String) -> Unit)? = null
)

fun <T> DomainModel.property(preview: Boolean = true, init: () -> T): ConceptGraphProperty<T> {
    return ConceptGraphPropertyImpl(this.propertyGraph, init).apply {
        if (preview) {
            afterChange {
                preview()
            }
        }
    }
}