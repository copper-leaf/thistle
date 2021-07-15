package com.copperleaf.thistle.core.renderer

import com.copperleaf.thistle.core.parser.ThistleTagFactory
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

@Suppress("NOTHING_TO_INLINE")
class ThistleTagsArgs(
    val tagFactory: ThistleTagFactory<*, *>,
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

    inline fun <reified ParameterType : Any> enum(
        value: ParameterType? = null,
        name: String? = null,
        crossinline options: () -> Map<String, ParameterType>,
    ): ReadOnlyProperty<Nothing?, ParameterType> = parameter<String, ParameterType>(
        value,
        name
    ) { parameterKey, stringValue ->
        val optionsMap = options()
        check(optionsMap.containsKey(stringValue)) {
            "Property '$parameterKey' must be one of ${optionsMap.keys} for $tagFactory"
        }

        optionsMap[stringValue]!!
    }

    fun checkNoMoreArgs() {
        val extraParams = args.keys - argsVisited
        check(extraParams.isEmpty()) {
            "Unknown parameters '$extraParams' for $tagFactory"
        }
    }

    inline fun <reified ParameterType : Any> parameter(
        hardcodedValue: ParameterType? = null,
        name: String? = null,
    ): ReadOnlyProperty<Nothing?, ParameterType> = LazyThistleParameterProvider<ParameterType, ParameterType>(
        thistleTagsArgs = this,
        hardcodedValue = hardcodedValue,
        name = name,
        checkInputType = { it is ParameterType },
        inputParameterTypeClass = ParameterType::class,
        checkOutputType = { true },
        outputParameterTypeClass = ParameterType::class,
        mapper = { _, it -> it }
    )

    inline fun <reified InputParameterType : Any, reified OutputParameterType : Any> parameter(
        hardcodedValue: OutputParameterType? = null,
        name: String? = null,
        noinline mapper: (String, InputParameterType) -> OutputParameterType
    ): ReadOnlyProperty<Nothing?, OutputParameterType> = LazyThistleParameterProvider(
        thistleTagsArgs = this,
        hardcodedValue = hardcodedValue,
        name = name,
        checkInputType = { it is InputParameterType },
        inputParameterTypeClass = InputParameterType::class,
        checkOutputType = { it is OutputParameterType },
        outputParameterTypeClass = OutputParameterType::class,
        mapper = mapper
    )
}

@Suppress("UNCHECKED_CAST")
class LazyThistleParameterProvider<InputParameterType : Any, OutputParameterType : Any>(
    private val thistleTagsArgs: ThistleTagsArgs,
    private val hardcodedValue: OutputParameterType? = null,
    private val name: String? = null,

    private val inputParameterTypeClass: KClass<InputParameterType>,
    private val checkInputType: (Any) -> Boolean,

    private val outputParameterTypeClass: KClass<OutputParameterType>,
    private val checkOutputType: (InputParameterType) -> Boolean,

    private val mapper: (String, InputParameterType) -> OutputParameterType
) : ReadOnlyProperty<Nothing?, OutputParameterType>, Lazy<OutputParameterType> {

    private var _value: OutputParameterType? = null
    override val value: OutputParameterType get() = _value!!

    override fun isInitialized(): Boolean {
        return _value != null
    }

    override fun getValue(thisRef: Nothing?, property: KProperty<*>): OutputParameterType {
        if (!isInitialized()) {
            _value = with(thistleTagsArgs) {
                val paramName = name ?: property.name

                check(paramName !in argsVisited) {
                    "$tagFactory has multiple properties named '$paramName'!"
                }
                argsVisited.add(paramName)

                // if hardcodedValue is provided, do not allow the value to be read from the param map
                if (hardcodedValue != null) {
                    check(!args.containsKey(paramName)) {
                        "Unknown parameter '$paramName' for $tagFactory!"
                    }

                    hardcodedValue
                } else {
                    check(args.containsKey(paramName)) {
                        "$tagFactory is missing required value for '$paramName'!"
                    }

                    val value: Any = args[paramName]!!

                    check(checkInputType(value)) {
                        "$tagFactory expects '$paramName' to be ${inputParameterTypeClass.simpleName}, got $value"
                    }

                    val result = mapper(paramName, value as InputParameterType)

                    check(checkOutputType(result as InputParameterType)) {
                        "$tagFactory expects '$paramName' to be ${outputParameterTypeClass.simpleName}, got $value"
                    }

                    result
                }
            }
        }

        return value
    }
}
