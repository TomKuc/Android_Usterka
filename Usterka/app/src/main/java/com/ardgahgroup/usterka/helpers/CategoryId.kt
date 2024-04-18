package com.ardgahgroup.usterka.helpers

class SubmissionStatusCategory() {
    var categoryId: Int = 0
    var categoryName: String = ""

    constructor(category: String) : this() {
        categoryName = category
        when (category) {
            "Nowe" -> categoryId = 1
            "Zgłoszone" -> categoryId = 2
            "Przyjęte" -> categoryId = 3
            "Realizowane" -> categoryId = 4
            "Zrealiowane" -> categoryId = 5
            "Zakończone" -> categoryId = 6
            "Zawieszone" -> categoryId = 7
            "Usunięte" -> categoryId = 8
        }
    }
}