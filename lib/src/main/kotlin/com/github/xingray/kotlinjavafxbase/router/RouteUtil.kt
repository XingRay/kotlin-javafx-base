package com.github.xingray.kotlinjavafxbase.router

object RouteUtil {
    fun getRoutePath(cls: Class<*>): String? {
        val routePathAnnotation = cls.getAnnotation(RoutePath::class.java) ?: return null
        return routePathAnnotation.value
    }
}
