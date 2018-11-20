package com.exlskills.android.com.exlskills.android.remote

import com.google.gson.JsonElement

data class GraphResponse (
    val data: JsonElement?,
    val errors: Array<GraphError>?
)

data class GraphError (
    val message: String,
    val locations: Array<GraphErrorLocation>
)

data class GraphErrorLocation (
    val line: Int,
    val column: Int
)

data class CoursePagingConnection (
    val coursePaging: CourseEdges
)

data class CourseEdges (
    val edges: Array<CourseNode>
)

data class CourseNode (
    val node: Course
)

data class Course (
    val id: String
)

data class Card (
    val id: String
)

data class User (
    val id: String
)
