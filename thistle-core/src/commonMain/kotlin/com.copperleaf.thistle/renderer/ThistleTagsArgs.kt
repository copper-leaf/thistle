package com.copperleaf.thistle.renderer

import com.copperleaf.thistle.parser.ThistleTag
import kotlin.properties.ReadOnlyProperty

class ThistleTagsArgs(
    val tag: ThistleTag,
    val args: Map<String, Any>
) {
    val argsVisited: MutableList<String> = mutableListOf()

    inline fun int(
        value: Int? = null,
        name: String? = null,
    ): ReadOnlyProperty<Nothing?, Int> = parameter(value, name)

    inline fun double(
        value: Double? = null,
        name: String? = null,
    ): ReadOnlyProperty<Nothing?, Double> = parameter(value, name)

    inline fun string(
        value: String? = null,
        name: String? = null,
    ): ReadOnlyProperty<Nothing?, String> = parameter(value, name)

    inline fun boolean(
        value: Boolean? = null,
        name: String? = null,
    ): ReadOnlyProperty<Nothing?, Boolean> = parameter(value, name)

    inline fun <reified ParameterType> enum(
        value: ParameterType? = null,
        name: String? = null,
        crossinline options: () -> Map<String, ParameterType>,
    ): ReadOnlyProperty<Nothing?, ParameterType> = parameter<String, ParameterType>(
        value,
        name
    ) { parameterKey, stringValue ->
        val optionsMap = options()
        check(optionsMap.containsKey(stringValue)) {
            "Property '$parameterKey' must be one of ${optionsMap.keys} for $tag"
        }

        optionsMap[stringValue]!!
    }

    fun checkNoMoreArgs() {
        val extraParams = args.keys - argsVisited
        check(extraParams.isEmpty()) {
            "Unknown parameters '$extraParams' for $tag"
        }
    }

    inline fun <reified ParameterType> parameter(
        hardcodedValue: ParameterType? = null,
        name: String? = null,
    ): ReadOnlyProperty<Nothing?, ParameterType> = ReadOnlyProperty { _, prop ->
        val paramName = name ?: prop.name

        check(paramName !in argsVisited) {
            "$tag has multiple properties named '$paramName'!"
        }
        argsVisited.add(paramName)

        // if hardcodedValue is provided, do not allow the value to be read from the param map
        if (hardcodedValue != null) {
            check(!args.containsKey(paramName)) {
                "Unknown parameter '$paramName' for $tag!"
            }

            return@ReadOnlyProperty hardcodedValue
        } else {
            check(args.containsKey(paramName)) {
                "$tag is missing required value for '$paramName'!"
            }

            val value: Any = args[paramName]!!

            check(value is ParameterType) {
                "$tag expects '$paramName' to be ${ParameterType::class.simpleName}, got $value"
            }

            value
        }
    }

    inline fun <reified InputParameterType, reified OutputParameterType> parameter(
        hardcodedValue: OutputParameterType? = null,
        name: String? = null,
        crossinline mapper: (String, InputParameterType) -> OutputParameterType
    ): ReadOnlyProperty<Nothing?, OutputParameterType> = ReadOnlyProperty { _, prop ->
        val paramName = name ?: prop.name

        check(paramName !in argsVisited) {
            "$tag has multiple properties named '$paramName'!"
        }
        argsVisited.add(paramName)

        // if hardcodedValue is provided, do not allow the value to be read from the param map
        if (hardcodedValue != null) {
            check(!args.containsKey(paramName)) {
                "Unknown parameter '$paramName' for $tag!"
            }

            return@ReadOnlyProperty hardcodedValue
        } else {
            check(args.containsKey(paramName)) {
                "$tag is missing required value for '$paramName'!"
            }

            val value: Any = args[paramName]!!

            check(value is InputParameterType) {
                "$tag expects '$paramName' to be ${InputParameterType::class.simpleName}, got $value"
            }

            val result = mapper(paramName, value)

            result
        }
    }
}
