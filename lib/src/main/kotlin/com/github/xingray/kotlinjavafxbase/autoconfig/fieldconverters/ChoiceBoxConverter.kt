package com.github.xingray.kotlinjavafxbase.autoconfig.fieldconverters

import com.github.xingray.kotlinbase.config.FieldConverter
import javafx.collections.ObservableList
import javafx.scene.control.ChoiceBox
import javafx.util.StringConverter

class ChoiceBoxConverter : FieldConverter<ChoiceBox<*>, String> {
    override fun getConfig(targetField: ChoiceBox<*>): String {
        val converter: StringConverter<Any> = targetField.getConverter() as StringConverter<Any>
        val value: Any = targetField.getValue()
        return converter.toString(value)
//        return value.toString()
    }

    override fun restoreConfig(targetField: ChoiceBox<*>, state: String) {
        val converter: StringConverter<*> = targetField.getConverter()
        val items: ObservableList<*> = targetField.getItems()
        if (items == null || items.isEmpty()) {
            targetField.getSelectionModel().select(0)
            return
        }
        if (converter != null) {
            val o = converter.fromString(state)
            if (o == null) {
                targetField.getSelectionModel().select(0)
                return
            }
            var index = items.indexOf(o)
            if (index < 0) {
                index = 0
            }
            targetField.getSelectionModel().select(index)
            return
        }

        for (o in items) {
            val itemString = o?.toString() ?: ""
            if (itemString == state) {
                val index = items.indexOf(o)
                if (index < 0) {
                    continue
                }
                targetField.getSelectionModel().select(index)
                return
            }
        }
        targetField.getSelectionModel().select(0)
    }
}
