package com.github.xingray.kotlinjavafxbase.autoconfig

import com.github.xingray.kotlinbase.config.FieldConverter
import com.github.xingray.kotlinbase.config.MapConfig
import com.github.xingray.kotlinbase.util.ConfigUtil
import com.github.xingray.kotlinbase.util.StringUtil
import com.github.xingray.kotlinbase.util.SystemUtil
import java.io.File
import java.util.concurrent.Executor

class AutoConfig(controller: Any, private var ioExecutor: Executor?, private var uiExecutor: Executor?) {

    private var mapConfig: MapConfig = MapConfig(controller)
    private var path: String? = null

    init {
        val name = controller.javaClass.canonicalName
        val names = name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        setPath((SystemUtil.getUserDirectory() + File.separator + "config" + File.separator + StringUtil.toString(names, File.separator)).toString() + ".properties")
    }

    fun setPath(path: String?) {
        this.path = path
    }

    fun <T> addFieldConverter(cls: Class<T>, fieldConverter: FieldConverter<T, String>) {
        mapConfig.addFieldConverter(cls, fieldConverter as FieldConverter<Any, String>)
    }

    fun save() {
        val configMap: Map<String, String> = mapConfig.config
        ioExecutor!!.execute { ConfigUtil.saveConfig(path, configMap as Map<String?, String?>) }
    }

    fun restore() {
        ioExecutor!!.execute {
            val configMap: Map<String, String>? = ConfigUtil.loadConfig(path)
            if (configMap != null) {
                uiExecutor!!.execute { mapConfig.updateTarget(configMap) }
            }
        }
    }
}