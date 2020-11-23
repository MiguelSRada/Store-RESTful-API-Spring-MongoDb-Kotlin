package com.example.demo.items


import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/items")
class ItemController(private val itemService: ItemService) {

    @GetMapping
    fun getAllItems(): List<ItemResponse> = itemService.getItems()

    @ResponseBody
    @GetMapping("/{itemId}")
    fun getItemById(@PathVariable("itemId") id: String): ResponseEntity<ItemResponse> {
        val item = itemService.getItemById(id)
        return if (item == null) ResponseEntity.notFound().build() else ResponseEntity.ok(item)
    }

    @PostMapping
    fun createItem(@RequestBody request: ItemRequest): ItemResponse =
            itemService.saveItem(request.name!!, request.price!!, request.quantity!!)

    @PutMapping("/{itemId}")
    fun updateItem(
            @RequestBody request: ItemRequest,
            @PathVariable("itemId") id: String): ResponseEntity<ItemResponse> {

        val item = itemService.putItem(id, request.name, request.price, request.quantity)

        return if (item == null) ResponseEntity.notFound().build() else ResponseEntity.ok(item)
    }

    @DeleteMapping("/{itemId}")
    fun deleteItem(@PathVariable("itemId") id: String): ResponseEntity<Unit> =
            if (itemService.deleteById(id)) ResponseEntity(HttpStatus.OK) else ResponseEntity.notFound().build()
}