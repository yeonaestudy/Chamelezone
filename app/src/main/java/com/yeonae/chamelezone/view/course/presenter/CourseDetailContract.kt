package com.yeonae.chamelezone.view.course.presenter

import com.yeonae.chamelezone.network.model.CourseResponse

interface CourseDetailContract {

    interface View {
        var presenter: Presenter
        fun showCourseDetail(courseList: List<CourseResponse>)
    }

    interface Presenter {
        fun getCourseDetail(courseNumber: Int)
    }
}