package com.example.demo.items

import com.example.demo.Buy
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "Item")
data class Item (
        @Id
        val id: ObjectId = ObjectId.get(),
        var name: String,
        var price: Long,
        var quantity: Long,
)