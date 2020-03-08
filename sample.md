# Rationale
This file will represent our reference for the first POC.

# Type System
Available types:
- **String**: a string;
- **Char**: a single character;
- **Boolean**: boolean value. Can be either `true` or `false`;
- **Number**: integer number;
- **Decimal**: decimal number;
- **Price**: price;
- **Date**: date in ISO-8601 format;
- **Time**: date with time;
- **UUID**: universal unique id;

## Mandatory vs Optional fields
Every field is mandatory, unless marked with the optional `?` notation.

Example:
```
limit: Number?
```

## Value constraints
Sometimes you might need to constrain your inputs, so you might want to use the `in` keyword to specify all the possible alternatives.

Example 1
```
limit: Number? in 10|50|100
```

Example 2
```
type: String in "blocked"|"active"|"inactive"
```

## Defaults
Sometimes you want to have defaults. In this scenario use the keyword `default`

Example 1
```
limit: Number? default 100
```

Example 2
```
limit: Number? in 10|50|100 default 100
```

# Endpoint declaration
The http endpoint to locate the request (without base URL).
Supported verbs are:
- **GET**
- **HEAD**
- **POST**
- **PUT**
- **DELETE**
- **PATCH**
- **OPTIONS**

Example:
```
GET {    
    ...
}
```

## Verb definition
Every verb definition has multiple fields to specify what's the expected format of the request and the response.

Example:
```
GET {
    request {
        ...
    }
    response {
        ...
    }
}
```

### Request
#### Headers
List of the headers to be sent

Example:
```
GET {
    request {
        headers {
            authorization: String
            content-type: String in "application/json"|"application/xml" default "application/json"
        }
        ...
    }
    ...
} 
```
#### Query
List of query parameters to be sent along with its type.
Example:
```
GET {
    request {
        query {
            "id" : UUID,
            "limit": Number,
            "offset": String
        }
        ...
    }
    ...
} 
```

# Example file
This is an example file
```
endpoint: /api/v1/products/

GET {
    request {
        headers {
            "authorization" : String
            "content-type": String in ["application/json","application.xml"]
        }
        query {
            "id" : UUID,
            "limit": Number,
            "offset": String
        }
    }
    response {
        200 {
            content {
                [
                    {
                        "id": UUID,
                        "description": String,
                        "tag": String[]?,
                        "price": Price,
                        "published": DateTime
                        "available": Boolean?
                    }
                ]
            }
        }
        404 {
            content {

            }
        }
    }
}
```