package com.copperleaf.thistle.android

import com.copperleaf.kudzu.parser.ParserContext
import com.copperleaf.thistle.android.parser.ColorResourceReferenceNode
import com.copperleaf.thistle.android.parser.DrawableResourceReferenceNode
import com.copperleaf.thistle.android.parser.ResourceReferenceParser
import com.copperleaf.thistle.android.parser.StringResourceReferenceNode
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class TestResourceReferenceParser {

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun testResourceReferenceParser() {
        val parser = ResourceReferenceParser()

        val (parseStringRefResult, _) = parser.parse(ParserContext.fromString("@string/string_value"))
        assertIs<StringResourceReferenceNode>(parseStringRefResult)
        assertEquals("string_value", parseStringRefResult.resourceName)

        val (parseColorRefResult, _) = parser.parse(ParserContext.fromString("@color/color_value"))
        assertIs<ColorResourceReferenceNode>(parseColorRefResult)
        assertEquals("color_value", parseColorRefResult.resourceName)

        val (parseDrawableRefResult, _) = parser.parse(ParserContext.fromString("@drawable/drawable_value"))
        assertIs<DrawableResourceReferenceNode>(parseDrawableRefResult)
        assertEquals("drawable_value", parseDrawableRefResult.resourceName)
    }

}
