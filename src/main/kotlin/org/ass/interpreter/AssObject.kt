package org.ass.interpreter

sealed class AssObject {

    data class Stat(
        val endpoint: Endpoint,
        val get: Method.Get?,
        val head: Method.Head?,
        val post: Method.Post?,
        val put: Method.Put?,
        val delete: Method.Delete?,
        val patch: Method.Patch?,
        val options: Method.Options?
    ) : AssObject()

    data class Endpoint(
        val url: String
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

    sealed class Method : AssObject() {

        data class Get(
            val request: Request,
            val response: Response
        ) : Method()

        data class Head(
            val request: Request,
            val response: Response
        ) : Method()

        data class Post(
            val request: Request,
            val response: Response
        ) : Method()

        data class Put(
            val request: Request,
            val response: Response
        ) : Method()

        data class Delete(
            val request: Request,
            val response: Response
        ) : Method()

        data class Patch(
            val request: Request,
            val response: Response
        ) : Method()

        data class Options(
            val request: Request,
            val response: Response
        ) : Method()

    }

    data class Request(
        val headers: Headers?,
        val queries: Queries?
    ) : AssObject()

    data class Headers(
        val values: List<Variable>
    ) : AssObject()

    data class Queries(
        val values: List<Variable>
    ) : AssObject()

    object Response : AssObject()

}