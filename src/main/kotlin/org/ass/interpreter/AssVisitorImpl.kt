package org.ass.interpreter

import org.ass.parser.AssBaseVisitor
import org.ass.parser.AssParser
import java.lang.IllegalArgumentException

class AssVisitorImpl : AssBaseVisitor<AssObject>() {

    override fun visitStat(ctx: AssParser.StatContext): AssObject.Stat {
        return AssObject.Stat(
            visitEndpoint(ctx.endpoint()),
            ctx.get()?.let { visitGet(it) },
            ctx.head()?.let { visitHead(it) },
            ctx.post()?.let { visitPost(it) },
            ctx.put()?.let { visitPut(it) },
            ctx.delete()?.let { visitDelete(it) },
            ctx.patch()?.let { visitPatch(it) },
            ctx.option()?.let { visitOption(it) }
        )
    }

    override fun visitEndpoint(ctx: AssParser.EndpointContext): AssObject.Endpoint {
        //TODO this is pure shit
        return AssObject.Endpoint(ctx.STRING_VALUE().text.replace("\"", ""))
    }

    override fun visitGet(ctx: AssParser.GetContext): AssObject.Method.Get {
        val request = visitRequest(ctx.request())
        val response = visitResponse(ctx.response())
        return AssObject.Method.Get(request, response)
    }

    override fun visitHead(ctx: AssParser.HeadContext): AssObject.Method.Head {
        val request = visitRequest(ctx.request())
        val response = visitResponse(ctx.response())
        return AssObject.Method.Head(request, response)
    }

    override fun visitPost(ctx: AssParser.PostContext): AssObject.Method.Post {
        val request = visitRequest(ctx.request())
        val response = visitResponse(ctx.response())
        return AssObject.Method.Post(request, response)
    }

    override fun visitPut(ctx: AssParser.PutContext): AssObject.Method.Put {
        val request = visitRequest(ctx.request())
        val response = visitResponse(ctx.response())
        return AssObject.Method.Put(request, response)
    }

    override fun visitDelete(ctx: AssParser.DeleteContext): AssObject.Method.Delete {
        val request = visitRequest(ctx.request())
        val response = visitResponse(ctx.response())
        return AssObject.Method.Delete(request, response)
    }

    override fun visitPatch(ctx: AssParser.PatchContext): AssObject.Method.Patch {
        val request = visitRequest(ctx.request())
        val response = visitResponse(ctx.response())
        return AssObject.Method.Patch(request, response)
    }

    override fun visitOption(ctx: AssParser.OptionContext): AssObject.Method.Options {
        val request = visitRequest(ctx.request())
        val response = visitResponse(ctx.response())
        return AssObject.Method.Options(request, response)
    }

    override fun visitRequest(ctx: AssParser.RequestContext): AssObject.Request {
        val headers = ctx.headers()?.let { visitHeaders(it) }
        val query = ctx.queries()?.let { visitQueries(it) }
        return AssObject.Request(headers, query)
    }

    override fun visitResponse(ctx: AssParser.ResponseContext): AssObject.Response {
        return AssObject.Response
    }

    override fun visitResponseCode(ctx: AssParser.ResponseCodeContext): AssObject {
        return super.visitResponseCode(ctx)
    }

    override fun visitHeaders(ctx: AssParser.HeadersContext): AssObject.Headers {
        val variables = ctx.typeSpec().map { visitTypeSpec(it) }
        return AssObject.Headers(variables)
    }

    override fun visitQueries(ctx: AssParser.QueriesContext): AssObject.Queries {
        val variables = ctx.typeSpec().map { visitTypeSpec(it) }
        return AssObject.Queries(variables)
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