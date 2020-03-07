package org.ass

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