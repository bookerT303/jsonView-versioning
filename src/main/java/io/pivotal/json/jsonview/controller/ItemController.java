package io.pivotal.json.jsonview.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.pivotal.json.jsonview.model.Item;
import io.pivotal.json.jsonview.model.Versions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/items")
@Api(value = "API Versioning using `accept` header", description = "Item REST API using Json View for versioning", tags = {"ItemController"})
public class ItemController {

    private ObjectMapper mapper;

    public ItemController(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get the Item", response = Item.class)
    public ResponseEntity<String> get(@RequestHeader(value = HttpHeaders.ACCEPT, required = false) String accepts,
                                      @ApiParam(value = "Item Id", required = true) @PathVariable int id) throws JsonProcessingException {

        Class<?> version = determineVersion(accepts);
        if (version == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("");
        }

        Item item = new Item(id, "Item Name", "Owner Name",
                "Owner Credit Card");
        return ResponseEntity.ok(mapper.writerWithView(version).writeValueAsString(item));
    }

    private Class<?> determineVersion(String accepts) {
        if (StringUtils.isEmpty(accepts)) {
            return null;
        }

        switch (accepts) {
            case "application/vnd.jsonview.items.v1+json":
                return Versions.V1.class;

            case "application/vnd.jsonview.items.v2+json":
                return Versions.V2.class;

            case "application/vnd.jsonview.items.v3+json":
                return Versions.V3.class;

            default:
                return null;

        }
    }
}
