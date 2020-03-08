package org.ass

import AssLexer
import AssParser
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

class AssVisitorImplTest : FunSpec({

    fun parse(input: String): AssObject {
        val charStream = CharStreams.fromString(input)
        val lexer = AssLexer(charStream)
        val tokens = CommonTokenStream(lexer)
        val parser = AssParser(tokens)
        val visitor = AssVisitorImpl()
        return visitor.visit(parser.typeSpec())
    }

    context("Number Test") {

        test("Test parsing Number") {
            val actual = parse("orfeo: Number;")
            val expected = AssObject.Variable.NumberVar(
                "orfeo",
                false,
                null,
                null
            )
            actual shouldBe expected
        }

        test("Test parsing nullable Number") {
            val actual = parse("orfeo: Number?;")
            val expected = AssObject.Variable.NumberVar(
                "orfeo",
                true,
                null,
                null
            )
            actual shouldBe expected
        }

        test("Test parsing Number with default") {
            val actual = parse("orfeo: Number default 12;")
            val expected = AssObject.Variable.NumberVar(
                "orfeo",
                false,
                12,
                null
            )
            actual shouldBe expected
        }

        test("Test parsing Number with options") {
            val actual = parse("orfeo: Number in [3,6,9,12];")
            val expected = AssObject.Variable.NumberVar(
                "orfeo",
                false,
                null,
                listOf(3, 6, 9, 12)
            )
            actual shouldBe expected
        }

        test("Test parsing Number with default and options") {
            val actual = parse("orfeo: Number default 12 in [3,6,9,12];")
            val expected = AssObject.Variable.NumberVar(
                "orfeo",
                false,
                12,
                listOf(3, 6, 9, 12)
            )
            actual shouldBe expected
        }

    }

    context("Decimal Test") {

        test("Test parsing Decimal") {
            val actual = parse("orfeo: Decimal;")
            val expected = AssObject.Variable.DecimalVar(
                "orfeo",
                false,
                null,
                null
            )
            actual shouldBe expected
        }

        test("Test parsing nullable Decimal") {
            val actual = parse("orfeo: Decimal?;")
            val expected = AssObject.Variable.DecimalVar(
                "orfeo",
                true,
                null,
                null
            )
            actual shouldBe expected
        }

        test("Test parsing Decimal with default") {
            val actual = parse("orfeo: Decimal default 12.0;")
            val expected = AssObject.Variable.DecimalVar(
                "orfeo",
                false,
                12.0,
                null
            )
            actual shouldBe expected
        }

        test("Test parsing Decimal with options") {
            val actual = parse("orfeo: Decimal in [3.0,6.0,9.0,12.0];")
            val expected = AssObject.Variable.DecimalVar(
                "orfeo",
                false,
                null,
                listOf(3.0, 6.0, 9.0, 12.0)
            )
            actual shouldBe expected
        }

        test("Test parsing Decimal with default and options") {
            val actual = parse("orfeo: Decimal default 12.0 in [3.0,6.0,9.0,12.0];")
            val expected = AssObject.Variable.DecimalVar(
                "orfeo",
                false,
                12.0,
                listOf(3.0, 6.0, 9.0, 12.0)
            )
            actual shouldBe expected
        }

    }

    context("String Test") {

        test("Test parsing String") {
            val actual = parse("orfeo: String;")
            val expected = AssObject.Variable.StringVar(
                "orfeo",
                false,
                null,
                null
            )
            actual shouldBe expected
        }

        test("Test parsing nullable String") {
            val actual = parse("orfeo: String?;")
            val expected = AssObject.Variable.StringVar(
                "orfeo",
                true,
                null,
                null
            )
            actual shouldBe expected
        }

        test("Test parsing String with default") {
            val actual = parse("orfeo: String default \"string\";")
            val expected = AssObject.Variable.StringVar(
                "orfeo",
                false,
                "string",
                null
            )
            actual shouldBe expected
        }

        test("Test parsing String with options") {
            val actual = parse("orfeo: String in [\"string\",\"string2\"];")
            val expected = AssObject.Variable.StringVar(
                "orfeo",
                false,
                null,
                listOf("string", "string2")
            )
            actual shouldBe expected
        }

        test("Test parsing String with default and options") {
            val actual = parse("orfeo: String default \"string\" in [\"string\",\"string2\"];")
            val expected = AssObject.Variable.StringVar(
                "orfeo",
                false,
                "string",
                listOf("string", "string2")
            )
            actual shouldBe expected
        }

    }

    context("Boolean Test") {

        test("Test parsing Boolean") {
            val actual = parse("orfeo: Boolean;")
            val expected =
                AssObject.Variable.BooleanVar(
                    "orfeo",
                    false,
                    null
                )
            actual shouldBe expected
        }

        test("Test parsing nullable Boolean") {
            val actual = parse("orfeo: Boolean?;")
            val expected = AssObject.Variable.BooleanVar(
                "orfeo",
                true,
                null
            )
            actual shouldBe expected
        }

        test("Test parsing Boolean with default") {
            val actual = parse("orfeo: Boolean default true;")
            val expected = AssObject.Variable.BooleanVar(
                "orfeo",
                false,
                true
            )
            actual shouldBe expected
        }

    }

})