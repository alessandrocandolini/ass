package org.ass

import AssBaseVisitor
import AssLexer
import AssParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.lang.IllegalArgumentException

fun main() {

    val textToParse = """
        orfeo: Number default 12 in [0,10,20];
        orfeo: Boolean default true;
        orfeo: Decimal default 2.0 in [0.0,1.0,2.0];
        orfeo: String default "Orfeo" in ["Orfeo","Alessandro"];
        """.trim()
    val charStream = CharStreams.fromString(textToParse)
    val lexer = AssLexer(charStream)
    val tokens = CommonTokenStream(lexer)
    val parser = AssParser(tokens)
    val visitor = AssVisitorImpl()
    println("Parsing \"$textToParse\"...")
    val value = visitor.visit(parser.stat())
    println("Parsed $value")
}

sealed class AssObject {

    data class Stat(
        val variables: List<Variable>
    ) : AssObject()

    sealed class Variable : AssObject() {

        data class StringVar(
            val name: String,
            val isNullable: Boolean,
            val default: String?,
            val options: List<String>?
        ) : Variable()

        data class NumberVar(
            val name: String,
            val isNullable: Boolean,
            val default: Int?,
            val options: List<Int>?
        ) : Variable()

        data class DecimalVar(
            val name: String,
            val isNullable: Boolean,
            val defaults: Double?,
            val options: List<Double>?
        ) : Variable()

        data class BooleanVar(
            val name: String,
            val isNullable: Boolean,
            val default: Boolean?
        ) : Variable()

    }

    data class StringValueList(
        val values: List<String>
    ) : AssObject()

    data class NumberValueList(
        val values: List<Int>
    ) : AssObject()

    data class DecimalValueList(
        val values: List<Double>
    ) : AssObject()

}

class AssVisitorImpl : AssBaseVisitor<AssObject>() {

    override fun visitStat(ctx: AssParser.StatContext): AssObject.Stat {
        val variables = ctx.typeSpec().map { visitTypeSpec(it) }
        return AssObject.Stat(variables)
    }

    override fun visitTypeSpec(ctx: AssParser.TypeSpecContext): AssObject.Variable {
        return when {
            ctx.typeSpecBoolean() != null -> visitTypeSpecBoolean(ctx.typeSpecBoolean())
            ctx.typeSpecString() != null -> visitTypeSpecString(ctx.typeSpecString())
            ctx.typeSpecDecimal() != null -> visitTypeSpecDecimal(ctx.typeSpecDecimal())
            ctx.typeSpecNumber() != null -> visitTypeSpecNumber(ctx.typeSpecNumber())
            else -> throw IllegalArgumentException("Type not recognised")
        }
    }

    override fun visitTypeSpecString(ctx: AssParser.TypeSpecStringContext): AssObject.Variable.StringVar {
        return AssObject.Variable.StringVar(
            ctx.IDENTIFIER().text,
            ctx.TYPE_STRING().text.contains("?"),
            //TODO this is pure shit
            ctx.STRING_VALUE()?.text?.replace("\"", ""),
            ctx.stringValueList()?.let { visitStringValueList(it).values }
        )
    }

    override fun visitTypeSpecNumber(ctx: AssParser.TypeSpecNumberContext): AssObject.Variable.NumberVar {
        return AssObject.Variable.NumberVar(
            ctx.IDENTIFIER().text,
            ctx.TYPE_NUMBER().text.contains("?"),
            ctx.NUMBER_VALUE()?.text?.toInt(),
            ctx.numberValueList()?.let { visitNumberValueList(it).values }
        )
    }

    override fun visitTypeSpecDecimal(ctx: AssParser.TypeSpecDecimalContext): AssObject.Variable.DecimalVar {
        return AssObject.Variable.DecimalVar(
            ctx.IDENTIFIER().text,
            ctx.TYPE_DECIMAL().text.contains("?"),
            ctx.DECIMAL_VALUE()?.text?.toDouble(),
            ctx.decimalValueList()?.let { visitDecimalValueList(it).values }
        )
    }

    override fun visitTypeSpecBoolean(ctx: AssParser.TypeSpecBooleanContext): AssObject.Variable.BooleanVar {
        return AssObject.Variable.BooleanVar(
            ctx.IDENTIFIER().text,
            ctx.TYPE_BOOLEAN().text.contains("?"),
            ctx.BOOL_VALUE()?.text?.let { it == "true" }
        )
    }

    override fun visitNumberValueList(ctx: AssParser.NumberValueListContext): AssObject.NumberValueList {
        val values = ctx.NUMBER_VALUE().map { it.text.toInt() }
        return AssObject.NumberValueList(values)
    }

    override fun visitDecimalValueList(ctx: AssParser.DecimalValueListContext): AssObject.DecimalValueList {
        val values = ctx.DECIMAL_VALUE().map { it.text.toDouble() }
        return AssObject.DecimalValueList(values)
    }

    override fun visitStringValueList(ctx: AssParser.StringValueListContext): AssObject.StringValueList {
        //TODO this is pure shit
        val values = ctx.STRING_VALUE().map { it.text.replace("\"", "") }
        return AssObject.StringValueList(values)
    }
}