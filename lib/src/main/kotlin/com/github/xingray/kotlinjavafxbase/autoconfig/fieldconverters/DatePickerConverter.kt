package com.github.xingray.kotlinjavafxbase.autoconfig.fieldconverters

import com.github.xingray.kotlinbase.config.FieldConverter
import javafx.scene.control.DatePicker


class DatePickerConverter : FieldConverter<DatePicker, String> {
    override fun getConfig(targetField: DatePicker): String {
        return targetField.getEditor().getText()
    }

    override fun restoreConfig(targetField: DatePicker, state: String) {
        targetField.getEditor().setText(state)
    }
}
