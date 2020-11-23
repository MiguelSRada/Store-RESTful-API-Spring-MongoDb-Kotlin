package com.example.demo.items

data class ItemResponse (
        val id: String,
        val name: String,
        val price: Long,
        val quantity: Long
)
fun toItemResponse(item: Item): ItemResponse =
        ItemResponse(item.id.toString(),item.name,item.price,item.quantity)
