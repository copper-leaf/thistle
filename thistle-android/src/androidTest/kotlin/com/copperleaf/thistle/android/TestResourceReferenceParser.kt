package com.copperleaf.thistle.android

import com.copperleaf.kudzu.parser.ParserContext
import com.copperleaf.thistle.android.parser.ColorResourceReferenceNode
import com.copperleaf.thistle.android.parser.DrawableResourceReferenceNode
import com.copperleaf.thistle.android.parser.ResourceReferenceParser
import com.copperleaf.thistle.android.parser.StringResourceReferenceNode
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class TestResourceReferenceParser : StringSpec({

    "testResourceReferenceParser" {
        val parser = ResourceReferenceParser()

        val (parseStringRefResult, _) = parser.parse(ParserContext.fromString("@string/string_value"))
        parseStringRefResult.shouldBeInstanceOf<StringResourceReferenceNode>()
        parseStringRefResult.resourceName shouldBe "string_value"

        val (parseColorRefResult, _) = parser.parse(ParserContext.fromString("@color/color_value"))
        parseColorRefResult.shouldBeInstanceOf<ColorResourceReferenceNode>()
        parseColorRefResult.resourceName shouldBe "color_value"

        val (parseDrawableRefResult, _) = parser.parse(ParserContext.fromString("@drawable/drawable_value"))
        parseDrawableRefResult.shouldBeInstanceOf<DrawableResourceReferenceNode>()
        parseDrawableRefResult.resourceName shouldBe "drawable_value"
    }
})
