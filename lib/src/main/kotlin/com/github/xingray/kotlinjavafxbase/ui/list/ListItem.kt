package com.github.xingray.coinfarmer.ui.list

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty

class ListItem<T>(
    item: T,
    selected: Boolean = false
) {
    val itemProperty: SimpleObjectProperty<T> = SimpleObjectProperty(item)
    val selectedProperty: SimpleBooleanProperty = SimpleBooleanProperty(selected)

    val item: T
        get() = itemProperty.get()
}