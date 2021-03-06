package com.ptrkalm.cdq.api.relationships;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public class RelationshipsStatistics {
    private long lowestNumberOfRelationsOfOneNode;
    private long highestNumberOfRelationsOfOneNode;
    private double averageNumberOfRelationsOfOneNode;
}
