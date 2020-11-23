package com.example.demo.items

import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.json.JSONObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ItemControllerIntTest(
        @Autowired
        private val itemRepository: ItemRepository,
        @Autowired
        private val itemService: ItemService,
        @Autowired
        private val restTemplate: TestRestTemplate
) {
    private val defaultItemId = ObjectId.get()

    @LocalServerPort
    protected var port: Int = 8081

    @BeforeEach
    fun setUp() {
        itemRepository.deleteAll()
    }

    private fun getRootUrl(): String? = "http://localhost:$port/items"
    private fun saveOneDefaultItem() = itemRepository.save(Item(defaultItemId,"itemName", 250, 100))


    @Test
    fun getAllItems() {
        saveOneDefaultItem()

        val response = restTemplate.getForEntity(
                getRootUrl() ,
                Array<ItemResponse>::class.java)

        assertThat(response.body!!.toList()).isEqualTo(listOf(ItemResponse(defaultItemId.toString(),"itemName", 250, 100)))
        assertThat(response.statusCodeValue).isEqualTo(200)

    }

    @Test
    fun getItemById() {
        saveOneDefaultItem()

        val response = restTemplate.getForObject(
                getRootUrl() + "/$defaultItemId",
                ItemResponse::class.java
        )

        assertThat(response).isEqualTo(ItemResponse(defaultItemId.toString(),"itemName", 250, 100))
    }

    @Test
    fun createItem() {
        itemService.saveItem("defaultItem", 250, 10)

        val response = restTemplate.postForEntity(
                getRootUrl() + "/$defaultItemId",
                ItemRequest("defaultItem", 250, 10),
                ItemResponse::class.java
        )

        assertThat(ItemResponse(defaultItemId.toString(),"defaultItem", 250, 10)).isEqualTo(response.body)
        assertThat(response.statusCodeValue).isEqualTo(200)

    }

    @Test
    fun updateItem() {
        saveOneDefaultItem()

        val response = restTemplate.exchange(
                getRootUrl() + "/$defaultItemId",
                HttpMethod.PUT,
                ResponseEntity.ok(Item(defaultItemId,"defaultItem", 250, 10)),
                ItemResponse::class.java
        )

        assertThat(ItemResponse(defaultItemId.toString(),"defaultItem", 250, 10)).isEqualTo(response.body)
        assertThat(response.statusCodeValue).isEqualTo(200)
    }

    @Test
    fun deleteItem() {

        saveOneDefaultItem()

        val response = restTemplate.exchange(
                getRootUrl() + "/$defaultItemId",
                HttpMethod.PUT,
                ResponseEntity.ok(Item(defaultItemId,"newDefaultName", 250, 10)),
                ItemResponse::class.java,
                JSONObject("{name : newDefaultName}")
        )

        assertThat(ItemResponse(defaultItemId.toString(),"newDefaultName", 250, 10)).isEqualTo(response.body)
        assertThat(response.statusCodeValue).isEqualTo(200)
    }
}