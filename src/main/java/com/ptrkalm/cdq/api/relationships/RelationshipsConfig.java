package com.ptrkalm.cdq.api.relationships;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedInputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.IntStream;
import java.util.zip.ZipInputStream;

@Configuration
public class RelationshipsConfig {

    @Bean
    public List<Relationship> allRelationships() {
        Document document = downloadAndUnpackXML();
        return getRelationships(document);
    }

    @Bean
    public HashMap<String, List<Relationship>> relationshipsByNode() {
        HashMap<String, List<Relationship>> map = new HashMap<>();
        allRelationships()
                .forEach(relationship -> putNodes(map, relationship));
        return map;
    }

    @Bean
    public RelationshipsStatistics relationshipsStatistics() {
        SortedSet<Map.Entry<String, List<Relationship>>> sortedNodes = new TreeSet<>(
                Comparator.comparingInt(entry -> entry.getValue().size())
        );

        sortedNodes.addAll(relationshipsByNode().entrySet());

        return new RelationshipsStatistics(
                sortedNodes.first().getValue().size(),
                sortedNodes.last().getValue().size(),
                (double) allRelationships().size() / relationshipsByNode().size()
        );
    }

    private void putNodes(HashMap<String, List<Relationship>> map, Relationship relationship) {
        String startNode = relationship.getStartNode();
        String endNode = relationship.getEndNode();

        putNode(map, relationship, startNode);
        putNode(map, relationship, endNode);
    }

    private void putNode(HashMap<String, List<Relationship>> map, Relationship relationship, String node) {
        if (map.containsKey(node)) {
            map.get(node).add(relationship);
        } else {
            map.put(node, Lists.newArrayList(relationship));
        }
    }

    private List<Relationship> getRelationships(Document xmlDocument) {
        NodeList relationshipNodes = xmlDocument.getElementsByTagName("rr:Relationship");
        return IntStream.range(0, relationshipNodes.getLength())
                .mapToObj(i -> (Element) relationshipNodes.item(i))
                .map(relationship -> new Relationship(
                        getNodeIDByTagName(relationship, "rr:StartNode"),
                        getNodeIDByTagName(relationship, "rr:EndNode"),
                        getRelationshipType(relationship)
                )).toList();
    }

    private String getNodeIDByTagName(Element relationship, String tagName) {
        return ((Element) relationship
                .getElementsByTagName(tagName).item(0))
                .getElementsByTagName("rr:NodeID").item(0)
                .getTextContent();
    }

    private RelationshipType getRelationshipType(Element relationship) {
        return RelationshipType.valueOf(
                relationship
                        .getElementsByTagName("rr:RelationshipType").item(0)
                        .getTextContent()
        );
    }

    private Document downloadAndUnpackXML() {
        try {
            URL url = new URL("https://leidata.gleif.org/api/v1/concatenated-files/rr/20210930/zip");
            BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());
            ZipInputStream zipInputStream = new ZipInputStream(bufferedInputStream);
            zipInputStream.getNextEntry();
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            return documentBuilder.parse(zipInputStream);
        } catch (Exception e) {
            throw new MissingResourceException(
                    "Cannot download or unpack xml file",
                    getClass().getName(),
                    e.getMessage());
        }
    }
}
