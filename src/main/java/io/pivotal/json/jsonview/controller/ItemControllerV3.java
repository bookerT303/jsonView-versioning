package io.pivotal.json.jsonview.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.pivotal.json.jsonview.model.Item;
import io.pivotal.json.jsonview.model.Versions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "v3/items")
@Api(value = "API Versioning using `accept` header", description = "Item REST API using Json View for versioning", tags = {"ItemControllerV3"})
public class ItemControllerV3 {

    private ObjectMapper mapper;

    public ItemControllerV3(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @GetMapping(value = "/{id}", produces = "application/vnd.jsonview.items.v3+json")
    @ApiOperation(value = "Get the V3 Item", response = Item.class)
    public ResponseEntity<String> get(
            @ApiParam(value = "Item Id", required = true) @PathVariable int id) throws JsonProcessingException {

        Item item = new Item(id, "Item Name", "Owner Name",
                "Owner Credit Card");
        return ResponseEntity.ok(mapper.writerWithView(Versions.V3.class).writeValueAsString(item));
    }
}
