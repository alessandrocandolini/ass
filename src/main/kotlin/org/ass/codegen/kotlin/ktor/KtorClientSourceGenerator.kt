package org.ass.codegen.kotlin.ktor

import com.squareup.kotlinpoet.*
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.ass.interpreter.AssObject
import org.ass.interpreter.AssVisitorImpl
import org.ass.parser.AssLexer
import org.ass.parser.AssParser

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
            .addType(createClientClass("Client", stat.endpoint.url, firstHeader, firstParameter))
            .build()

        file.writeTo(System.out)
    }

    private fun createClientClass(
        name: String,
        endpoint: String,
        header: AssObject.Variable.StringVar,
        queryParams: AssObject.Variable.NumberVar
    ): TypeSpec {
        val ktorHttpClientClassName = ClassName("", "HttpClient")
        return TypeSpec.classBuilder(name)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("client", ktorHttpClientClassName)
                    .build()
            )
            .addProperty(
                PropertySpec.builder("client", ktorHttpClientClassName)
                    .initializer("client")
                    .addModifiers(KModifier.PRIVATE)
                    .build()
            )
            .addFunction(
                createCallFunction("getPosts", endpoint, header, queryParams)
            )
            .build()
    }

    private fun createCallFunction(
        name: String,
        url: String,
        header: AssObject.Variable.StringVar,
        queryParams: AssObject.Variable.NumberVar
    ): FunSpec {
        return FunSpec.builder(name)
            .addModifiers(KModifier.SUSPEND)
            .addStatement(
                "val response = client.%M<String>(%S){",
                MemberName("", "get"),
                url
            )
            .addStatement("header(%S,%S)", header.name, header.default!!)
            .addStatement("parameter(%S,%S)", queryParams.name, queryParams.default!!)
            .addStatement("}")
            .addStatement("client.close()")
            .addStatement("return response")
            .returns(String::class)
            .build()
    }
}