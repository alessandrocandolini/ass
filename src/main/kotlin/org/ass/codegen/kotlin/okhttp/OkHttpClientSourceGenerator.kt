package org.ass.codegen.kotlin.okhttp

val TEMPLATE = """
    endpoint: "https://jsonplaceholder.typicode.com/posts";
    GET {
        request {
            headers {
                authorization: String default "authToken";
            }
            query {
                limit: Number default 10;
            }
        }
        response {
            200 {
            }
        }
    }
""".trimIndent()

//TODO