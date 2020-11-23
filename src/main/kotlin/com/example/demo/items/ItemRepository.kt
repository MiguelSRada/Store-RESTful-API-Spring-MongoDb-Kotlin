package com.example.demo.items

import com.example.demo.items.Item
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface ItemRepository : MongoRepository<Item, Long> {

    fun findOneById(id: ObjectId): Item?

    fun deleteById(id: ObjectId)

}


