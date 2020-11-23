package com.example.demo

import com.example.demo.items.Item
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
@Document(collection = "Buy")
class Buy (
        @Id
        val id: ObjectId = ObjectId.get(),
        val item: Item,
        val quantity: Long,
        val items: MutableList<Item> = mutableListOf()
)
