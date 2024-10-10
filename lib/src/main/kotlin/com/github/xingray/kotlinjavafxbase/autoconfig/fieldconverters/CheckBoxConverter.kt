package com.github.xingray.kotlinjavafxbase.autoconfig.fieldconverters

import com.github.xingray.kotlinbase.config.FieldConverter
import javafx.scene.control.CheckBox


class CheckBoxConverter : FieldConverter<CheckBox, String> {

    override fun getConfig(targetField: CheckBox): String {
        return if (targetField.isSelected) "1" else "0"
    }

    override fun restoreConfig(targetField: CheckBox, state: String) {
        targetField.isSelected = ("1" == state)
    }
}
