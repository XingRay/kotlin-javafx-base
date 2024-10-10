package com.github.xingray.kotlinjavafxbase.autoconfig.fieldconverters

import com.github.xingray.kotlinbase.config.FieldConverter
import javafx.scene.control.TextInputControl


class TextInputControlConverter : FieldConverter<TextInputControl, String> {
    override fun getConfig(targetField: TextInputControl): String {
        return targetField.getText()
    }

    override fun restoreConfig(targetField: TextInputControl, state: String) {
        targetField.setText(state)
    }
}
