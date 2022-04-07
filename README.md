
# REST API for relationship records from LEI register
Application at startup downloads zip file containing relationship records from LEI database using url below:<br>
https://leidata.gleif.org/api/v1/concatenated-files/rr/20210930/zip

API serves information such as:
- statistics from dataset:
  - what is the lowest number of relationships of one node
  - what is the highest number of relationships of one node
  - what is the avarage number of relationships for the nodes
- relations by the node ID,
- lists of all relations

## Setup
Steps to clone, build and run application using docker:
```bash
git clone https://github.com/ptrkalm/lei-rest-api.git
```

```bash
cd lei-rest-api
```

```bash
docker-compose up
```

API will be served on localhost with port 80.

## API Documentation
Relationship attributes
| name-            | type   | description              |
|------------------|--------|--------------------------|
| startNode        | string | nodeID of the start node |
| endNode          | string | nodeID of the end node   |
| relationshipType | string | type of the relationship |

### Get all relationships
```bash
GET /relationships
```

Optional params
| name      | type | description        | default |
|-----------|------|--------------------|---------|
| page      | long | number of the page | 0       |
| page_size | long | size of the page   | 100     |

Attributes
| name         | type  | description                      |
|--------------|-------|----------------------------------|
| status       | int   | status code                      |
| result       | array | list of relationships            |
| totalResults | long  | number of all results in dataset |
| pageNumber   | long  | number of page                   |
| pageSize     | long  | size of the page                 |

Example request

```bash
curl --location --request GET 'http://localhost/relationships?page=10&page_size=3'
```

Example response

```json
{
    "status": 200,
    "result": [
        {
            "startNode": "097900BHLO0000089043",
            "endNode": "097900BFEJ0000024657",
            "relationshipType": "IS_DIRECTLY_CONSOLIDATED_BY"
        },
        {
            "startNode": "097900BFBW0000007583",
            "endNode": "097900BFBW0000007680",
            "relationshipType": "IS_ULTIMATELY_CONSOLIDATED_BY"
        },
        {
            "startNode": "097900BHCD0000066364",
            "endNode": "315700KHGU6QQQQA2A84",
            "relationshipType": "IS_ULTIMATELY_CONSOLIDATED_BY"
        }
    ],
    "totalResults": 253604,
    "pageNumber": 10,
    "pageSize": 3
}
```

### Get all relationships of the node
```bash
GET /nodes/{node}/relationships
```
Path params
| name  | type   | description        |
|-------|--------|--------------------|
| node  | string | nodeID of the node |

Optional params
| name      | type | description        | default |
|-----------|------|--------------------|---------|
| page      | long | number of the page | 0       |
| page_size | long | size of the page   | 100     |

Attributes
| name         | type  | description                      |
|--------------|-------|----------------------------------|
| status       | int   | status code                      |
| result       | array | list of relationships            |
| totalResults | long  | number of all results in dataset |
| pageNumber   | long  | number of page                   |
| pageSize     | long  | size of the page                 |

Example request

```bash
curl --location --request GET 'http://localhost/nodes/097900CAKA0000018017/relationships'
```

Example response

```json
{
    "status": 200,
    "result": [
        {
            "startNode": "097900CAKA0000018017",
            "endNode": "097900BHLY0000092724",
            "relationshipType": "IS_ULTIMATELY_CONSOLIDATED_BY"
        },
        {
            "startNode": "097900CAKA0000018017",
            "endNode": "3157000TNINEGYK0F710",
            "relationshipType": "IS_DIRECTLY_CONSOLIDATED_BY"
        },
        {
            "startNode": "097900CAKA0000018017",
            "endNode": "259400O79EWIK4XZPL40",
            "relationshipType": "IS_ULTIMATELY_CONSOLIDATED_BY"
        }
    ],
    "totalResults": 3,
    "pageNumber": 0,
    "pageSize": 100
}
```

### Get statistics of the dataset
```bash
GET /relationships/statistics
```

Attributes
| name                              | type   | description                               |
|-----------------------------------|--------|-------------------------------------------|
| lowestNumberOfRelationsOfOneNode  | long   | lowest number of relations of one node    |
| highestNumberOfRelationsOfOneNode | long   | highest number of relations of one node   |
| avarageNumberOfRelationsOfOneNode | double | avarage number of relations for the nodes |

Example request

```bash
curl --location --request GET 'http://localhost/relationships/statistics'
```

Example response

```json
{
    "status": 200,
    "result": {
        "lowestNumberOfRelationsOfOneNode": 1,
        "highestNumberOfRelationsOfOneNode": 2530,
        "averageNumberOfRelationsOfOneNode": 1.4244454804338424
    }
}
```

### Error response
Attributes 
| name         | type   | description              |
|--------------|--------|--------------------------|
| status       | long   | status code              |
| result       |        | null                     |
| errorMassage | string | description of the error |
```json
{
    "status": 404,
    "result": null,
    "errorMassage": "Unsupported endpoint or invalid param."
}
```

TODO: Distinguish various types of errors (internal error, not found, unsupported, invalid request, etc.)
