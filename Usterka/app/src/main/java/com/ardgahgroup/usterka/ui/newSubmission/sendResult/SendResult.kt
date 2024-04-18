package com.ardgahgroup.usterka.ui.newSubmission.sendResult

/** Submission send result : submissionSent, imageSent (success), sendingImage (inProgress) or error. */
data class SendResult(
    val submissionSent: Boolean = false,
    val sendingImage: Boolean = false,
    val imageSent: Boolean = false,
    val error: Boolean = false
)