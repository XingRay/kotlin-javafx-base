package com.github.xingray.kotlinjavafxbase.router

import com.github.xingray.kotlinbase.util.StringUtil


object PageUtil {
    fun getLayoutPath(cls: Class<*>): String {
        val layoutPathAnnotation = cls.getAnnotation(LayoutPath::class.java)
            ?: throw IllegalArgumentException("class:" + cls.name + " is not annotated by " + LayoutPath::class.java.name)
        val path = layoutPathAnnotation.value
        require(!StringUtil.isEmpty(path)) { "class:" + cls.name + " @" + LayoutPath::class.java.name + " param is empty" }
        require(path.startsWith("/")) { "class:" + cls.name + " @" + LayoutPath::class.java.simpleName + " param error, path must start with / , like /fxml/layout.fxml" }
        return path
    }
}
