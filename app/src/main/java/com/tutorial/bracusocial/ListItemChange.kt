package com.tutorial.bracusocial

interface ListItemChange {
    fun onItemAdded(item: String)
    fun onItemRemoved(item: String)
}