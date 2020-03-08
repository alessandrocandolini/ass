package org.ass

import AssLexer
import AssParser
import com.squareup.kotlinpoet.*
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

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

fun main() {
    val charStream = CharStreams.fromString(TEMPLATE)
    val lexer = AssLexer(charStream)
    val tokens = CommonTokenStream(lexer)
    val parser = AssParser(tokens)
    val visitor = AssVisitorImpl()
    val stat = visitor.visit(parser.stat()) as AssObject.Stat
    AssClientSourceGenerator().generateClient(stat)
}

class AssClientSourceGenerator {

    fun generateClient(stat: AssObject.Stat) {
        val firstHeader = stat.get!!.request.headers!!.values.first() as AssObject.Variable.StringVar
        val firstParameter = stat.get!!.request.queries!!.values.first() as AssObject.Variable.NumberVar
        val file = FileSpec.builder("", "Client")
            .addImport("io.ktor.client", "HttpClient")
            .addImport("io.ktor.client.request", "get", "header", "parameter")
            .addType(
                TypeSpec.classBuilder("Client")
                    .primaryConstructor(
                        FunSpec.constructorBuilder()
                            .addParameter("client", ClassName("", "HttpClient"))
                            .build()
                    )
                    .addProperty(
                        PropertySpec.builder("client", ClassName("", "HttpClient"))
                            .initializer("client")
                            .addModifiers(KModifier.PRIVATE)
                            .build()
                    )
                    .addFunction(
                        FunSpec.builder("get")
                            .addModifiers(KModifier.SUSPEND)
                            .addStatement(
                                "val response = client.%M<String>(%S){",
                                MemberName("", "get"),
                                stat.endpoint.url
                            )
                            .addStatement("header(%S,%S)", firstHeader.name, firstHeader.default!!)
                            .addStatement("parameter(%S,%S)", firstParameter.name, firstParameter.default!!)
                            .addStatement("}")
                            .addStatement("client.close()")
                            .addStatement("return response")
                            .returns(String::class)
                            .build()
                    )
                    .build()
            )
            .build()

        file.writeTo(System.out)
    }
}