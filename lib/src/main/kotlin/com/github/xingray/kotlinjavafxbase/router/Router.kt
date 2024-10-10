package com.github.xingray.kotlinjavafxbase.router

import com.github.xingray.kotlinjavafxbase.page.Controller


class Router {
    private val classMap: MutableMap<String, Class<*>> = HashMap()

    fun init(vararg classes: Class<out Controller?>?) {
        for (cls in classes) {
            register(cls as Class<out Controller?>)
        }
    }

    private fun register(cls: Class<out Controller?>) {
        val routePathAnnotation = cls.getAnnotation(RoutePath::class.java) ?: return
        val routePath = routePathAnnotation.value
        classMap[routePath] = cls
    }
}
