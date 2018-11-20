package com.exlskills.android.com.exlskills.android.remote

import com.google.gson.JsonElement

data class GraphResponse (
    val data: JsonElement?,
    val errors: List<GraphError>?
)

data class GraphError (
    val message: String,
    val locations: List<GraphErrorLocation>
)

data class GraphErrorLocation (
    val line: Int,
    val column: Int
)

data class CoursePagingConnection (
    val coursePaging: CourseEdges
)

data class CourseByIdLiteResponse (
    val courseById: CourseLiteMeta
)

data class CourseEdges (
    val edges: List<CourseNode>
)

data class CourseNode (
    val node: CourseLiteMeta
)

data class CourseLiteMeta (
    val id: String,
    val title: String,
    val headline: String,
    val enrolled_count: Int,
    val view_count: Int,
    val logo_url: String,
    val skill_level: Int,
    val est_minutes: Int,
    val primary_topic: String,
    val verified_cert_cost: Double,
    val delivery_methods: List<String>
)

data class Card (
    val id: String
)

data class InstructorTimekit (
    val intervals: List<TimekitInterval>
)

data class TimekitInterval (
    val credits: Double,
    val project_id: String,
    val duration_seconds: Int
)

data class InstructorProfileResponse (
    val userProfile: InstructorProfile
)

data class InstructorProfile (
    val id: String,
    val username: String,
    val full_name: String,
    val headline: String,
    val biography: String,
    val avatar_url: String,
    val instructor_topics_locale: String,
    val instructor_timekit: InstructorTimekit,
    val location_name: String,
    val linkedin_username: String,
    val twitter_username: String
)

data class ListInstructorsResponse (
    val listInstructors: InstructorEdges
)

data class InstructorEdges (
    val edges: List<InstructorNode>
)

data class InstructorNode (
    val node: InstructorLiteProfile
)

data class InstructorLiteProfile (
    val id: String,
    val username: String,
    val full_name: String,
    val headline: String,
    val biography: String,
    val avatar_url: String,
    val instructor_topics_locale: String
)

data class ListDigitalDiplomasResponse (
    val listDigitalDiplomas: DigitalDiplomaEdges
)

data class DigitalDiplomaEdges (
    val edges: List<DigitalDiplomaNode>
)

data class DigitalDiplomaNode (
    val node: DigitalDiploma
)
data class DigitalDiplomaResponse (
    val getDigitalDiplomaById: DigitalDiploma
)

data class DigitalDiploma (
    val id: String,
    val title: String,
    val headline: String,
    val description: String,
    val logo_url: String,
    val skill_level: Int,
    val est_minutes: Int,
    val primary_topic: String,
    val info_md: String,
    val is_project: Boolean,
    val topics: List<String>,
    val plans: List<DigitalDiplomaPlan>,
    val instructor_timekit: InstructorTimekit
)

data class DigitalDiplomaPlan (
    val _id: String,
    val title: String,
    val headline: String,
    val cost: String,
    val is_hidden: String,
    val opens_at: String,
    val closes_at: String,
    val is_shipping_required: String
)

data class CourseMetaAndUnits (
    val meta: CourseFullMeta,
    val units: List<CourseUnit>
)

data class CourseByIdAndUnitsPagingResponse (
    val courseById: CourseFullMeta,
    val unitPaging: CourseUnitEdges
)

data class CourseUnitEdges (
    val edges: List<CourseUnitNode>
)

data class CourseUnitNode (
    val node: CourseUnit
)

data class CourseFullMeta (
    val id: String,
    val title: String,
    val description: String,
    val headline: String,
    val enrolled_count: Int,
    val view_count: Int,
    val logo_url: String,
    val info_md: String,
    val repo_url: String,
    val skill_level: Int,
    val est_minutes: Int,
    val primary_topic: String,
    val verified_cert_cost: Double,
    val delivery_methods: List<String>,
    val instructor_timekit: InstructorTimekit
)

data class CourseUnit (
    val id: String,
    val unit_progress_state: Int,
    val title: String,
    val headline: String,
    val attempts_left: Int,
    val attempts: Int,
    val final_exam_weight_pct: Double,
    val last_attempted_at: String,
    val passed: Boolean,
    val index: Int,
    val ema: Double,
    val grade: Double,
    val is_continue_exam: Boolean,
    val exam_attempt_id: String,
    val sections_list: List<UnitSection>
)

data class UnitSection (
    val id: String,
    val ema: Double,
    val title: String,
    val headline: String,
    val cards_list: List<SectionCardLiteMeta>
)

data class SectionCardLiteMeta (
    val id: String,
    val ema: Double,
    val title: String
)

data class UserProfileCourseRolesResponse (
    val userProfile: UserProfileCourseRoles
)

data class UserProfileCourseRoles (
    val course_roles: UserCourseRoleEdges
)

data class UserCourseRoleEdges (
    val edges: List<UserCourseRoleNode>
)

data class UserCourseRoleNode (
    val node: UserCourseRole
)

data class UserCourseRole (
    val id: String,
    val course_id: String,
    val role: String,
    val last_accessed_at: String
)
