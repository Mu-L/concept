package com.github.linyuzai.inherit.processor.utils;

import com.github.linyuzai.inherit.processor.handler.GenerateMethodsWithFieldsHandler;
import com.github.linyuzai.inherit.processor.handler.InheritHandler;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.Collection;

public class InheritUtils {

    /**
     * 通过 flag 获得对应的 {@link InheritHandler}
     */
    public static Collection<InheritHandler> getInheritHandlers(Collection<String> flags) {
        Collection<InheritHandler> handlers = new ArrayList<>();
        if (flags.contains(InheritFlag.BUILDER.name()) ||
                flags.contains(InheritFlag.GETTER.name()) ||
                flags.contains(InheritFlag.SETTER.name())) {
            handlers.add(new GenerateMethodsWithFieldsHandler(flags));
        }
        return handlers;
    }

    /**
     * 注解是指定名称的注解
     *
     * @param annotation 注解
     * @param className  注解全限定名
     * @return 是否是指定注解
     */
    public static boolean isAnnotation(AnnotationMirror annotation, String className) {
        DeclaredType declaredType = annotation.getAnnotationType();
        if (isClass(declaredType)) {
            Symbol.TypeSymbol typeSymbol = ((Type.ClassType) declaredType).tsym;
            if (isClass(typeSymbol)) {
                return className.equals(((Symbol.ClassSymbol) typeSymbol).fullname.toString());
            }
        }
        return false;
    }

    /**
     * 是否是 Class
     */
    public static boolean isClass(Element element) {
        //return element.getKind().isClass();
        return element instanceof Symbol.ClassSymbol;
    }

    /**
     * 是否是 Class
     */
    public static boolean isClass(TypeMirror mirror) {
        return mirror instanceof Type.ClassType;
    }

    public static boolean isImportDefined(JCTree.JCCompilationUnit compilationUnit, JCTree.JCImport jcImport) {
        for (JCTree.JCImport anImport : compilationUnit.getImports()) {
            String string = anImport.qualid.toString();
            if (string.equals(jcImport.qualid.toString()) || string.startsWith("java.lang")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 字段是否在类中定义
     */
    public static boolean isFieldDefined(JCTree.JCClassDecl classDef, JCTree.JCVariableDecl varDef) {
        for (JCTree def : classDef.defs) {
            if (isNonStaticVariable(def)) {
                JCTree.JCVariableDecl has = (JCTree.JCVariableDecl) def;
                /*if (isVariableEqual(has, varDef)) {
                    return true;
                }*/
                if (has.name.contentEquals(varDef.name)) {
                    //名称相同就算一样
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 方法是否在类中定义
     */
    public static boolean isMethodDefined(JCTree.JCClassDecl classDef, JCTree.JCMethodDecl methodDef) {
        for (JCTree def : classDef.defs) {
            if (isNonStaticMethod(def)) {
                JCTree.JCMethodDecl has = (JCTree.JCMethodDecl) def;
                if (isMethodEqual(has, methodDef)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否是非静态的字段
     */
    public static boolean isNonStaticVariable(JCTree tree) {
        return tree instanceof JCTree.JCVariableDecl &&
                !((JCTree.JCVariableDecl) tree).mods.getFlags().contains(Modifier.STATIC);
    }

    /**
     * 是否是非静态的方法并且不是构造方法
     */
    public static boolean isNonStaticMethod(JCTree tree) {
        return tree instanceof JCTree.JCMethodDecl &&
                !"<init>".equals(((JCTree.JCMethodDecl) tree).getName().toString()) &&
                !((JCTree.JCMethodDecl) tree).mods.getFlags().contains(Modifier.STATIC);
    }

    /**
     * 方法是否相等
     */
    public static boolean isMethodEqual(JCTree.JCMethodDecl methodDef1, JCTree.JCMethodDecl methodDef2) {
        return methodDef1.name.contentEquals(methodDef2.name) && //名称
                //methodDef1.mods.getFlags().equals(methodDef2.mods.getFlags()) && //访问限制
                //isRestypeEqual(methodDef1.restype, methodDef2.restype) && //返回值
                isVariablesEqual(methodDef1.params, methodDef2.params); //入参
    }

    /**
     * 返回值是否相等
     */
    public static boolean isRestypeEqual(JCTree.JCExpression restype1, JCTree.JCExpression restype2) {
        //基本类型
        if (restype1 instanceof JCTree.JCPrimitiveTypeTree &&
                restype2 instanceof JCTree.JCPrimitiveTypeTree) {
            return ((JCTree.JCPrimitiveTypeTree) restype1).typetag.equals(((JCTree.JCPrimitiveTypeTree) restype2).typetag);
        }
        //有可能都为 null
        if (restype1.type == null || restype2.type == null) {
            return false;
        }
        return restype1.type.equals(restype2.type);
    }

    /**
     * 字段列表是否相等
     */
    public static boolean isVariablesEqual(List<JCTree.JCVariableDecl> varDef1s, List<JCTree.JCVariableDecl> varDef2s) {
        //数量相等
        if (varDef1s.size() != varDef2s.size()) {
            return false;
        }
        //每个字段都相等
        for (int i = 0; i < varDef1s.size(); i++) {
            if (!isVariableEqual(varDef1s.get(i), varDef2s.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 字段是否相等
     */
    public static boolean isVariableEqual(JCTree.JCVariableDecl varDef1, JCTree.JCVariableDecl varDef2) {
        return varDef1.name.contentEquals(varDef2.name) && //名称
                //varDef1.mods.getFlags().equals(varDef2.mods.getFlags()) && //访问限制
                varDef1.vartype.type.equals(varDef2.vartype.type); //类型
    }
}
