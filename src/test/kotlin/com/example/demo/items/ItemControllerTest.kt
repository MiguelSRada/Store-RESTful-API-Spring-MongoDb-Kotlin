package com.example.demo.items

import org.bson.types.ObjectId
import org.json.JSONObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders


@ExtendWith(SpringExtension::class, MockitoExtension::class)
internal class ItemControllerTest {
    @Mock
    private lateinit var itemService: ItemService

    private lateinit var mockMvc: MockMvc

    private val defaultItemId = ObjectId.get()


    @BeforeEach
    fun beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(ItemController(itemService)).build()
    }

    @Test
    fun getAllItems() {

        given(itemService.getItems()).willReturn(listOf(ItemResponse(defaultItemId.toString(),"itemName",250,100)))

        mockMvc.perform(get("/items"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("[0].id").value(defaultItemId.toString()))
                .andExpect(jsonPath("[0].name").value("itemName"))
                .andExpect(jsonPath("[0].price").value(250))
                .andExpect(jsonPath("[0].quantity").value(100))
    }

    @Test
    fun getItemById() {
        given(itemService.getItemById(defaultItemId.toString())).willReturn(ItemResponse(defaultItemId.toString(),"itemName",250,100))

        mockMvc.perform(get("/items/${defaultItemId}"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("id").value(defaultItemId.toString()))
                .andExpect(jsonPath("name").value("itemName"))
                .andExpect(jsonPath("price").value(250))
                .andExpect(jsonPath("quantity").value(100))
    }

    @Test
    fun createItem() {
        given(itemService.saveItem("itemName",250,100)).willReturn(ItemResponse(defaultItemId.toString(),"itemName",250,100))

        mockMvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONObject("{name : itemName, price : 250, quantity : 100}").toString()))

                //.content("{\"name\": \"itemName\",\"price\": \"250\", \"quantity\": \"100\"} "))
                .andExpect(status().isOk)
                .andExpect(jsonPath("id").value(defaultItemId.toString()))
                .andExpect(jsonPath("name").value("itemName"))
                .andExpect(jsonPath("price").value(250))
                .andExpect(jsonPath("quantity").value(100))
    }

    @Test
    fun updateItem() {
        given(itemService.putItem(defaultItemId.toString(), "newItemName")).willReturn(ItemResponse(defaultItemId.toString(),"newItemName",250,100))

        mockMvc.perform(put("/items/${defaultItemId}").contentType(MediaType.APPLICATION_JSON).content("{\"name\": \"newItemName\"} "))
                .andExpect(status().isOk)
                .andExpect(jsonPath("id").value(defaultItemId.toString()))
                .andExpect(jsonPath("name").value("newItemName"))
                .andExpect(jsonPath("price").value(250))
                .andExpect(jsonPath("quantity").value(100))
    }

    @Test
    fun deleteItem() {
        given(itemService.deleteById(defaultItemId.toString())).willReturn(true)

        mockMvc.perform(delete("/items/${defaultItemId}"))
                .andExpect(status().isOk)
    }
}