package com.rf.laundrybooking

import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.InitBinder
import java.beans.PropertyEditorSupport
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@ControllerAdvice
class LocalDateControllerAdvice {
    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(LocalDate::class.java, object : PropertyEditorSupport() {
            @Throws(IllegalArgumentException::class)
            override fun setAsText(text: String) {
                LocalDate.parse(text, DateTimeFormatter.ISO_DATE)
            }
        })
    }
}