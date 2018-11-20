package com.exlskills.android.com.exlskills.android.remote

import com.google.gson.Gson
import com.google.gson.JsonElement
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class Graph {
    private val gqlEndpoint = "https://gql-api.exlskills.com/graph"
    private val client = OkHttpClient()
    private val gson = Gson()
    private val jwt: String = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiMVNaS2gxNlZXaGVFIiwibG9jYWxlIjoiZW4iLCJhdmF0YXJfdXJsIjoiaHR0cHM6Ly93d3cuZ3JhdmF0YXIuY29tL2F2YXRhci8wMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMD9kPXJvYm9oYXNoIiwiZnVsbF9uYW1lIjoiQW5vbnltb3VzIFRhaHIiLCJpc19kZW1vIjp0cnVlLCJoYXNfY29tcGxldGVkX2ZpcnN0X3R1dG9yaWFsIjpmYWxzZSwiaXNfdmVyaWZpZWQiOmZhbHNlLCJzdWJzY3JpcHRpb24iOlt7ImxldmVsIjoxLCJjcmVhdGVkX2F0IjoiMjAxOC0xMS0xOVQxNzoyMzoyMC45NDhaIiwidXBkYXRlZF9hdCI6IjIwMTgtMTEtMTlUMTc6MjM6MjAuOTQ4WiJ9XSwiaWF0IjoxNTQyNjQ4MjAwfQ.Uz5IViDChvexccHqXpudkQV9FOo4kfp-jsDkUgStW0kC5UjPJbpgW6FCiliCLhFtd9ydSwiZ1IOa8hoPYM4BiQHRa5_UrjATGi30OcMeTj8wP3kgVrz5LQlkdlsJAWt9X2zjX4ZihbHcoVyYWEw2lrpTyTnPupPlc7TocZVyzIumXdhz8CrbcPEdX4-WYyBmgO6lzTffClagpua3Bz3PuIwYv9inHgnaCVjRoYiw8i3MwI8QEc8rbEA7AWVgojzP6Ghu19jnV4FX-f0zNa_vXmQylQYGCxaJ98b3vCFXtS5N_ucSQmpG7Tk-HB7ueezhdUBvhBamuxLHluHysIBmVEuAagmAa6A3Uh9FpWOSnaRJUwHc3wrzvNgapU8zCo13UFvzsqxZeJD_PsRHD22pl2hT7bRwHwNOW1yTnWB4gBUABQu6Qwc_F5_FrxlpqSgZZ7NbwsvPr3GN3Dkw4euCp2w5OG4ioP7G5cP3P2AG7dAGRppJ3tN0rGNZU8Mws2hzryr0jbFOZ4tJIUNoUyvN2sDr0e4ueFnjldgDShZoRvahAqqAB32gfcxOnJUj8xTopo0cx2e9IqSG6qVHwRFhD5kXzBxOZ0nmbb4DSKUWqk7oW93pD21VEfiysz3IY8kc9iW6F4-wvaN0ani16ki0UrhA7AgJByAsCW9fJMyeFHk"
    // TODO user data is required here?

    fun getSectionCard(courseId: String, unitId: String, sectionId: String, cardId: String, cb: GraphCallback<Card>) {
        val q = """
            {
                cardEntry(course_id: "${courseId}", unit_id: "${unitId}", section_id: "${sectionId}", card_id: "${cardId}") {
                id
                index
                title
                headline
                content_id
                tags
                content {
                    id
                    version
                    content
                }
                question {
                    id
                    question_text
                    question_type
                    hint_exists
                    data {
                        id
                        tmpl_files
                        environment_key
                        use_advanced_features
                        explanation
                        src_files
                        options {
                            id
                            seq
                            text
                        }
                    }
                }
            }
        """.trimIndent()
        this.run(q, object: GraphCallback<JsonElement> {
            override fun onFailure(msg: String, code: Int) = cb.onFailure(msg, code)
            override fun onSuccess(respData: JsonElement) {
                cb.onSuccess(gson.fromJson<Card>(respData, Card::class.java))
            }
        })
    }

    fun getCourseById(courseId: String, cb: GraphCallback<Course>) {
        val q = """
            {
                courseById(course_id: "${courseId}") {
                    id
                    title
                    description
                    headline
                    enrolled_count
                    view_count
                    logo_url
                    skill_level
                    est_minutes
                    primary_topic
                    info_md
                    verified_cert_cost
                    repo_url
                    delivery_methods
                    instructor_timekit {
                        intervals {
                            credits
                            project_id
                            duration_seconds
                        }
                    }
                }
            }
        """.trimIndent()
        this.run(q, object: GraphCallback<JsonElement> {
            override fun onFailure(msg: String, code: Int) = cb.onFailure(msg, code)
            override fun onSuccess(respData: JsonElement) {
                cb.onSuccess(gson.fromJson<Course>(respData, Course::class.java))
            }
        })
    }

    fun getAllCourses(listType: String, cb: GraphCallback<List<Course>>) {
        val q = """
            {
                coursePaging(first: 9999, resolverArgs: [{ param: "list", value: "${listType}" }], filterValues: null) {
                    edges {
                        node {
                            id
                            title
                            headline
                            enrolled_count
                            view_count
                            logo_url
                            skill_level
                            est_minutes
                            primary_topic
                            verified_cert_cost
                            delivery_methods
                        }
                    }
                }
                topicFilter {
                    value
                }
            }
        """.trimIndent()
        this.run(q, object: GraphCallback<JsonElement> {
            override fun onFailure(msg: String, code: Int) = cb.onFailure(msg, code)
            override fun onSuccess(respData: JsonElement) {
                println(respData.toString())
                val pConn = gson.fromJson<CoursePagingConnection>(respData, CoursePagingConnection::class.java)
                val courses = pConn.coursePaging.edges.map { el ->
                    el.node
                }
                cb.onSuccess(courses)
            }
        })
    }

    private fun run(query: String, cb: GraphCallback<JsonElement>) {
        val qObj = JSONObject()
        qObj.put("query", query)
        qObj.put("operationName", null)
        qObj.put("variables", null)
        val request = Request.Builder()
            .url(gqlEndpoint)
            .header("Accept", "application/json")
            .header("Cookie", "token=$jwt")
            .method("POST", RequestBody.create(MediaType.parse("application/json"), qObj.toString(0)))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Encountered failure")
            }
            override fun onResponse(call: Call, response: Response) {
                println("Woah great success!")
                val rVal = response.body()?.string()
                println(rVal)

                val gqlResp = gson.fromJson<GraphResponse>(rVal, GraphResponse::class.java)
                // TODO need to analyze the response for GQL errors, since onFailure will only be invoked by a NETWORK ERROR
                cb.onSuccess(gqlResp.data!!)
            }
        })
    }
}
