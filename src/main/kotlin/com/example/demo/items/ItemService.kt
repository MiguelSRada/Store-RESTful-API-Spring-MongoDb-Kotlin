package com.example.demo.items

import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class ItemService(private val itemRepository: ItemRepository) {

    fun getItems(): List<ItemResponse> = itemRepository.findAll().map { toItemResponse(it) }

    fun getItemById(id: String): ItemResponse? {
        itemRepository.findOneById(ObjectId(id))?.let { return toItemResponse(it) }
        return null
    }

    fun saveItem(name: String, price: Long, quantity: Long): ItemResponse {
        val item = itemRepository.save(
                Item(
                        name = name,
                        price = price,
                        quantity = quantity
                )
        )
        return toItemResponse(item)
    }

    fun putItem(id: String, newName: String? = null, nexPrice: Long? = null, newQuantity: Long? = null): ItemResponse? {
        val item = itemRepository.findOneById(ObjectId(id))
        return if (item != null) {
            val updatedItem = item.apply {
                newName?.let { name = newName }
                nexPrice?.let { price = nexPrice }
                newQuantity?.let { quantity = newQuantity }
            }
            toItemResponse(itemRepository.save(updatedItem))
        } else null
    }

    fun deleteById(id: String): Boolean {
        val item = itemRepository.findOneById(ObjectId(id))
        return if (item != null) {
            itemRepository.deleteById(ObjectId(id))
            true
        } else false
    }

}
