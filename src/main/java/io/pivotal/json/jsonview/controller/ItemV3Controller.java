package io.pivotal.json.jsonview.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.pivotal.json.jsonview.model.Item;
import io.pivotal.json.jsonview.model.Versions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "v3/items")
public class ItemV3Controller {

    private ObjectMapper mapper;

    public ItemV3Controller(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @GetMapping(value = "/{id}", produces = "application/vnd.jsonview.items.v3+json")
    public ResponseEntity<String> get(@PathVariable int id) throws JsonProcessingException {

        Item item = new Item(id, "Item Name", "Owner Name",
                "Owner Credit Card");
        return ResponseEntity.ok(mapper.writerWithView(Versions.V3.class).writeValueAsString(item));
    }
}
