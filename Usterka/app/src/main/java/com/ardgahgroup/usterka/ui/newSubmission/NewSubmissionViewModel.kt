package com.ardgahgroup.usterka.ui.newSubmission

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ardgahgroup.usterka.R

class NewSubmissionViewModel : ViewModel() {

    private val _submissionForm = MutableLiveData<SubmissionFormState>()
    val submissionFormState: LiveData<SubmissionFormState> = _submissionForm

    private fun isSubmissionTitleValid(submissionTitle: String): Boolean {
        return submissionTitle.length > 5 && submissionTitle.isNotBlank()
    }

    private fun isSubmissionDescriptionValid(submissionDescription: String): Boolean {
        return submissionDescription.length > 10 && submissionDescription.isNotBlank()
    }
    private fun isPlaceChosen(place: String): Boolean {
        return place != "Wybierz miejsce"
    }

    fun submissionDataChanged(
        submissionTitle: String,
        submissionDescription: String,
        place: String
    ) {
        if (!isSubmissionTitleValid(submissionTitle)) {
            _submissionForm.value =
                SubmissionFormState(titleError = R.string.new_submission_title_error_text)
        }
        if (!isSubmissionDescriptionValid(submissionDescription)) {
            _submissionForm.value =
                SubmissionFormState(descriptionError = R.string.new_submission_description_error_text)
        }
        if (!isPlaceChosen(place)) {
            _submissionForm.value =
                SubmissionFormState(placeError = R.string.new_submission_place_error_text)
        }
        // All data valid
        if (isSubmissionTitleValid(submissionTitle) &&
            isSubmissionDescriptionValid(submissionDescription) &&
            isPlaceChosen(place)
        ) {
            _submissionForm.value = SubmissionFormState(isDataValid = true)
        }
    }
}