package kz.btsd.matchers

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import okhttp3.Response

const val ERROR_TEMPLATE = "\nField %s:\n\tExpected: \t%s\n\tActual: \t%s"

fun correctResponse(expectedResponseUpdate: Response, contains:Boolean = false, matches:Boolean = false) = object :
    Matcher<Response> {
    override fun test(value: Response): MatcherResult {
        val errors = mutableListOf<String>()

        if (value.code != expectedResponseUpdate.code)
            errors.add(ERROR_TEMPLATE.format("http response code", expectedResponseUpdate.code, value.code))

        var body = value.body!!.string()
        var expectedBody = ""

        try {
            expectedBody = expectedResponseUpdate.body!!.string()
        } catch (e: Exception) {
            expectedBody = expectedResponseUpdate.message
        }

        if (contains) {
            expectedBody.split("|").forEach {
                if (!Regex(pattern = it).containsMatchIn(input = body))
                    errors.add(
                        ERROR_TEMPLATE.format(
                            "body",
                            "the regular expression <<$it>> doesn't contains in the input",
                            body
                        )
                    )
            }
        }

        if (matches)
            if (!Regex(pattern = expectedBody, option = RegexOption.DOT_MATCHES_ALL).matches(input = body))
                errors.add(ERROR_TEMPLATE.format("body", "the regular expression <<$expectedBody>> doesn't match the entire input", body))

        if (!contains && !matches)
            if (body != expectedBody)
                errors.add(ERROR_TEMPLATE.format("body", expectedBody, body))

        return MatcherResult(
            errors.isEmpty(),
            "Response has errors:\n".plus(errors.joinToString("\n")),
            "Response should not be correct"
        )
    }
}
