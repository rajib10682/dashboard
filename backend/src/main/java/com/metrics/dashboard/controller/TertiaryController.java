package com.metrics.dashboard.controller;

import com.metrics.dashboard.entity.Item;
import com.metrics.dashboard.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tertiary")
@CrossOrigin(origins = "*")
public class TertiaryController {
    
    @Autowired
    private ItemRepository itemRepository;
    
    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items = itemRepository.findAll();
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/{itemId}")
    public ResponseEntity<Item> getItem(@PathVariable String itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        return item.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable String itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isPresent()) {
            itemRepository.deleteById(itemId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
