package com.example.demo.items

import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class, MockitoExtension::class)
class ItemServiceTest {

    private  var itemRepository: ItemRepository = mock(ItemRepository::class.java)

    private  var itemService: ItemService = ItemService(itemRepository)

    private val defaultItemId = ObjectId.get()

    @Test
    fun getItems(){

        given(itemRepository.findAll()).willReturn(listOf(Item(defaultItemId,"itemName",250,100)))

        val result = itemService.getItems()

        assertThat(result).isEqualTo(listOf(toItemResponse(Item(defaultItemId,"itemName",250,100))))
    }

    @Test
    fun getItemById() {
        given(itemRepository.findOneById(defaultItemId)).willReturn(Item(defaultItemId,"itemName",250,100))

        val result = itemService.getItemById(defaultItemId.toString())

        assertThat(result).isEqualTo(ItemResponse(defaultItemId.toString(),"itemName",250,100))
    }
    @Test
    fun saveItem() {
        given(itemRepository.save(Item(defaultItemId,"itemName",250,100))).willReturn(Item(defaultItemId,"itemName",250,100))

        val result = itemService.saveItem("itemName",250,100)

        assertThat(result).isEqualTo(ItemResponse(defaultItemId.toString(),"itemName",250,100))

    }
    @Test
    fun putItem() {
        given(itemRepository.findOneById(defaultItemId)).willReturn(Item(defaultItemId,"itemName",250,100))
        given(itemRepository.save(Item(defaultItemId,"newItemName",250,100))).willReturn(Item(defaultItemId,"newItemName",250,100))

        val result = itemService.putItem(defaultItemId.toString(),"newItemName")

        assertThat(result).isEqualTo(ItemResponse(defaultItemId.toString(),"newItemName",250,100))

    }
    @Test
    fun deleteById() {
        given(itemRepository.findOneById(defaultItemId)).willReturn(Item(defaultItemId,"itemName",250,100))

        val result = itemService.deleteById(defaultItemId.toString())

        assertThat(result).isEqualTo(true)

    }
}