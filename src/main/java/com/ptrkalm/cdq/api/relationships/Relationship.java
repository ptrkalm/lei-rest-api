package com.ptrkalm.cdq.api.relationships;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public class Relationship {
    private final String startNode;
    private final String endNode;
    private final RelationshipType relationshipType;
}
