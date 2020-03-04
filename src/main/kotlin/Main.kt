import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

fun main() {
    val textToParse = """name: String? default "orfeo" in ["orfeo","alessandro"]"""
    val charStream = CharStreams.fromString(textToParse)
    val lexer = AssLexer(charStream)
    val tokens = CommonTokenStream(lexer)
    val parser = AssParser(tokens)
    val visitor = AssVisitorImpl()
    println("Parsing \"$textToParse\"...")
    val value = visitor.visit(parser.program())
    println("The parsed node is: $value")
}

sealed class Variable {
    data class StringVar(
        val identifier: String,
        val isNullable: Boolean,
        val defaultValue: String?,
        val allowedValues: List<String>
    ) : Variable()
}

class AssVisitorImpl : AssBaseVisitor<List<Variable>>() {

    private lateinit var variables: MutableList<Variable>

    override fun visitProgram(ctx: AssParser.ProgramContext?): List<Variable> {
        println("start visitProgram")
        variables = mutableListOf()
        super.visitProgram(ctx)
        println("end visitProgram")
        return variables.toList()
    }

    override fun visitStrvar(ctx: AssParser.StrvarContext): List<Variable> {
        println("start visitStrvar")
        //TODO replace uppercase with lowercase to obtain the right rule
        val allowedValues = ctx.STRING_VALUE_LIST().text
            .replace("[", "")
            .replace("]", "")
            .replace("\"", "")
            .split(",")

        val stringvar = Variable.StringVar(
            ctx.IDENTIFIER().text,
            ctx.TYPE_STRING().text.endsWith("?"),
            ctx.STRING_VALUE().text,
            allowedValues
        )
        variables.add(stringvar)
        println("end visitStrvar")
        return variables
    }
}