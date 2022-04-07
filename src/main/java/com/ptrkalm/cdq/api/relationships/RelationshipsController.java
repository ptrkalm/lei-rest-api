package com.ptrkalm.cdq.api.relationships;

import com.ptrkalm.cdq.api.responses.RestResponse;
import com.ptrkalm.cdq.api.responses.RestResponseError;
import com.ptrkalm.cdq.api.responses.RestResponsePaged;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class RelationshipsController implements ErrorController {

    @Autowired
    private List<Relationship> allRelationships;

    @Autowired
    private HashMap<String, List<Relationship>> relationshipsByNode;

    @Autowired
    private RelationshipsStatistics relationshipsStatistics;

    @GetMapping("/relationships")
    public RestResponse getRelationships(
            @RequestParam(defaultValue = "0") long page,
            @RequestParam(defaultValue = "100") long page_size) {
        try {
            return new RestResponsePaged(
                    200,
                    getPage(allRelationships, page, page_size),
                    allRelationships.size(),
                    page,
                    page_size
            );
        } catch (Exception e) {
            return error();
        }

    }

    @GetMapping("/relationships/statistics")
    public RestResponse getRelationshipsStatistics() {
        return new RestResponse(
                200,
                relationshipsStatistics);
    }

    @GetMapping("/nodes/{node}/relationships")
    public RestResponse getRelationshipsByNodeID(
            @PathVariable("node") String node,
            @RequestParam(defaultValue = "0") long page,
            @RequestParam(defaultValue = "100") long page_size) {
        List<Relationship> relationships = relationshipsByNode.getOrDefault(node, new ArrayList<>());
        return new RestResponsePaged(
                200,
                getPage(relationships, page, page_size),
                relationships.size(),
                page,
                page_size
        );
    }

    @RequestMapping("/error")
    public RestResponseError error() {
        return new RestResponseError(
                404,
                null,
                "Unsupported endpoint or invalid param."
        );
    }

    private List<Relationship> getPage(List<Relationship> relationships, long pageNumber, long pageSize) {
        return relationships
                .stream()
                .skip(pageNumber  * pageSize)
                .limit(pageSize)
                .toList();
    }
}
