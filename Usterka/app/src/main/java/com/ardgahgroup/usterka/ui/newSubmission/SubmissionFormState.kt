package com.ardgahgroup.usterka.ui.newSubmission


/** Submission form data state: isDataValid (form ready to send) or error message. */
data class SubmissionFormState(
    val titleError: Int? = null,
    val descriptionError: Int? = null,
    val placeError: Int? = null,
    val isDataValid: Boolean = false
)
