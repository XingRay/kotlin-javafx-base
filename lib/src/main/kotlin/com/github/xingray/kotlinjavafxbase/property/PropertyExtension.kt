package com.github.xingray.kotlinjavafxbase.property

import javafx.beans.Observable
import javafx.beans.binding.Bindings
import javafx.beans.binding.ObjectBinding
import javafx.beans.property.ObjectProperty
import javafx.beans.property.StringProperty

fun <T> ObjectProperty<T>.bindUntilGetValue(provider: () -> T?, vararg observables: Observable) {
    val property = this
    if (property.value != null) {
        return
    }
    // 创建一个绑定逻辑，查询设备类型
    val binding = Bindings.createObjectBinding({
        provider() // 查询设备类型
    }, *observables)

    // 将属性绑定到查询结果上
    property.bind(binding)

    // 当查询结果不为空时，解绑属性
    binding.addListener { _, _, newValue ->
        if (newValue != null) {
            property.unbind() // 解除绑定
            property.set(newValue) // 手动设置值
        }
    }
}

fun StringProperty.bindUntilGetValue(provider: () -> String?, vararg observables: Observable) {
    val property = this
    if (property.value != null) {
        return
    }
    // 创建一个绑定逻辑，查询设备类型
    val binding = Bindings.createObjectBinding({
        provider() // 查询设备类型
    }, *observables)

    // 将属性绑定到查询结果上
    property.bind(binding)

    // 当查询结果不为空时，解绑属性
    binding.addListener { _, _, newValue ->
        if (newValue != null) {
            property.unbind() // 解除绑定
            property.set(newValue) // 手动设置值
        }
    }
}


//iconUsb.imageProperty().bind(
//Bindings.createObjectBinding(
//{
//    val directConnection = device.directConnection
//    if (directConnection != null && directConnection.status == AdbDeviceStatus.ONLINE) {
//        Image("image/img_link.png")
//    } else {
//        Image("image/img_unlink.png")
//    }
//
//},
//device.directConnectionProperty
//)
//)

fun <T, R> ObjectProperty<T>.createBoolValueBinding(provider: (T) -> Boolean, trueValue: R, falseValue: R): ObjectBinding<R> {
    val property = this
    return Bindings.createObjectBinding({
        if (provider(property.value)) {
            trueValue
        } else {
            falseValue
        }
    }, property)
}