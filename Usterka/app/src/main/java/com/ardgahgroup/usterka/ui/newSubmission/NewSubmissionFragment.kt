package com.ardgahgroup.usterka.ui.newSubmission

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ardgahgroup.usterka.BuildConfig
import com.ardgahgroup.usterka.R
import com.ardgahgroup.usterka.data.LoginRepository
import com.ardgahgroup.usterka.data.model.api.AddImageModel
import com.ardgahgroup.usterka.data.model.api.NewSubmissionModel
import com.ardgahgroup.usterka.data.retrofit.RetrofitClient
import com.ardgahgroup.usterka.databinding.FragmentNewSubmissionBinding
import com.ardgahgroup.usterka.helpers.afterTextChanged
import com.ardgahgroup.usterka.ui.activities.UserMenuActivity
import com.ardgahgroup.usterka.ui.newSubmission.sendResult.SendResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.net.HttpURLConnection
import java.util.*

class NewSubmissionFragment : Fragment() {
    companion object {
        //camera request code
        private val REQUEST_IMAGE_CAPTURE = 1

        //image pick code
        private val IMAGE_PICK_CODE = 1000

        //Permission code
        private val PERMISSION_CODE = 1001
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var _binding: FragmentNewSubmissionBinding? = null
    private lateinit var newSubmissionViewModel: NewSubmissionViewModel

    private val myCalender = Calendar.getInstance()
    private var latestTmpUri: Uri? = null
    private val sendSubmissionResult = MutableLiveData<SendResult>()
    private lateinit var submissionTitle: EditText
    private lateinit var submissionDescription: EditText
    private lateinit var submissionPlace: TextView
    private lateinit var sendSubmissionButton: Button
    private lateinit var sendSubmissionAnonymouslyButton: Button
    private lateinit var imagePreview: ImageView
    private lateinit var mainActivity: UserMenuActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        newSubmissionViewModel = ViewModelProvider(this)[NewSubmissionViewModel::class.java]
        _binding = FragmentNewSubmissionBinding.inflate(inflater, container, false)
        mainActivity = activity as UserMenuActivity

        bindComponents()
        setClickListeners()
        retrieveData()
        observeSubmissionData()
        observeSendingSubmission()

        return binding.root
    }

    private fun setClickListeners() {
        val date = binding.buttonDate
        val time = binding.buttonTime
        date.text = mainActivity.submissionDate
        time.text = mainActivity.submissionTime
        time.setOnClickListener { displayTimePickerDialog(time) }
        date.setOnClickListener { displayDatePickerDialog(date) }

        binding.addImageFromCamera.setOnClickListener { openCamera() }
        binding.addImageFromGallery.setOnClickListener { selectImageFromGallery() }
        binding.deleteImageButton.setOnClickListener {
            binding.imageButtonsContainer.visibility = View.VISIBLE
            binding.imageContainer.visibility = View.GONE
            binding.imageView.setImageResource(0)
            mainActivity.submissionImageUri = null
        }

        submissionPlace.setOnClickListener {
            displayPlacePicker()
        }

        binding.sendSubmissionButton.setOnClickListener {
            mainActivity.loadingProgressBar.showLoadingCircle()
            mainActivity.loadingProgressBar.setTextViewText(getString(R.string.new_submission_progress_bar_text))
            sendSubmissionToServer()
        }
        binding.sendSubmissionAnonymouslyButton.setOnClickListener {
            mainActivity.loadingProgressBar.showLoadingCircle()
            mainActivity.loadingProgressBar.setTextViewText(getString(R.string.new_submission_progress_bar_text))
            sendSubmissionAnonymouslyToServer()
        }
    }

    private fun bindComponents() {
        submissionTitle = binding.titleEditText
        submissionDescription = binding.descriptionEditText
        sendSubmissionButton = binding.sendSubmissionButton
        submissionPlace = binding.placeText
        sendSubmissionAnonymouslyButton = binding.sendSubmissionAnonymouslyButton
        imagePreview = binding.imageView
    }

    private fun retrieveData() {

        binding.titleEditText.setText(mainActivity.submissionTitle)
        binding.descriptionEditText.setText(mainActivity.submissionDescription)
        binding.buttonTime.text = mainActivity.submissionTime
        binding.buttonDate.text = mainActivity.submissionDate
        binding.departmentText.text = LoginRepository.userData.Department
        binding.placeText.text = mainActivity.submissionPlace.name

        if (mainActivity.submissionImageUri != null) {
            imagePreview.setImageURI(mainActivity.submissionImageUri)
            binding.imageButtonsContainer.visibility = View.GONE
            binding.imageContainer.visibility = View.VISIBLE
            val image = mainActivity.submissionImageUri
            imagePreview.setImageURI(image)
        }
    }

    private fun observeSubmissionData() {
        newSubmissionViewModel.submissionFormState.observe(this@NewSubmissionFragment, Observer {
            val submissionState = it ?: return@Observer

            sendSubmissionButton.isEnabled = submissionState.isDataValid
            sendSubmissionAnonymouslyButton.isEnabled = submissionState.isDataValid

            if (submissionState.titleError != null) {
                submissionTitle.error = getString(submissionState.titleError)
            }
            if (submissionState.descriptionError != null) {
                submissionDescription.error = getString(submissionState.descriptionError)
            }
            if (submissionState.placeError != null) {
                submissionPlace.error = getString(submissionState.placeError)
            }
        })

        submissionTitle.afterTextChanged {
            val title = binding.titleEditText.text.toString()
            mainActivity.submissionTitle = title.trim()
            getSubmissionData()
        }

        submissionDescription.afterTextChanged {
            val description = binding.descriptionEditText.text.toString()
            mainActivity.submissionDescription = description.trim()
            getSubmissionData()
        }

        if (submissionPlace.text != getString(R.string.place_picker_title)) {
            getSubmissionData()
        }
    }

    private fun observeSendingSubmission() {
        sendSubmissionResult.observe(this@NewSubmissionFragment, Observer {
            val sendResult = it ?: return@Observer

            if (sendResult.submissionSent) {
                if (sendResult.sendingImage) {
                    mainActivity.loadingProgressBar.setTextViewText(getString(R.string.new_submission_image_progress_bar_text))
                } else {
                    mainActivity.loadingProgressBar.hideLoadingCircle()
                    displaySubmissionSentFragment(true)
                }
            }
            if (sendResult.imageSent) {
                mainActivity.loadingProgressBar.hideLoadingCircle()
                displaySubmissionSentFragment(true)
            }
            if (sendResult.error) {
                mainActivity.loadingProgressBar.hideLoadingCircle()
                displaySubmissionSentFragment(false)
            }
        })
    }

    private fun getSubmissionData() {
        newSubmissionViewModel.submissionDataChanged(
            submissionTitle.text.toString(),
            submissionDescription.text.toString(),
            submissionPlace.text.toString()
        )
    }

    private fun displayTimePickerDialog(button: Button) {
        val hour = myCalender.get(Calendar.HOUR_OF_DAY)
        val minute = myCalender.get(Calendar.MINUTE)
        val simpleTimeFormat = SimpleDateFormat("HH:mm", Locale.forLanguageTag("PL"))

        val timePickerDialog = TimePickerDialog(
            this.requireActivity(),
            R.style.DateAndTimePickerTheme,
            { _, _hour, _minute ->
                myCalender.set(Calendar.HOUR_OF_DAY, _hour)
                myCalender.set(Calendar.MINUTE, _minute)
                button.setText(simpleTimeFormat.format(myCalender.time))
            }, hour, minute, true
        )
        timePickerDialog.show()
        timePickerDialog.setOnDismissListener {
            mainActivity.submissionTime = binding.buttonTime.text.toString()
        }
    }

    private fun displayDatePickerDialog(button: Button) {
        val year = myCalender.get(Calendar.YEAR)
        val month = myCalender.get(Calendar.MONTH)
        val day = myCalender.get(Calendar.DAY_OF_MONTH)
        val simpleDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.forLanguageTag("PL"))

        val datePickerDialog = DatePickerDialog(
            this.requireActivity(),
            R.style.DateAndTimePickerTheme,
            { _, _year, _month, _day ->
                myCalender.set(java.util.Calendar.YEAR, _year)
                myCalender.set(java.util.Calendar.MONTH, _month)
                myCalender.set(java.util.Calendar.DAY_OF_MONTH, _day)
                button.text = simpleDateFormat.format(myCalender.time)
                mainActivity.submissionDateSend = SimpleDateFormat(
                    "yyyy/dd/MM",
                    Locale.forLanguageTag("PL")
                ).format(myCalender.time)
            }, year, month, day
        )
        datePickerDialog.show()
        datePickerDialog.setOnDismissListener {
            mainActivity.submissionDate = binding.buttonDate.text.toString()
        }
    }

    private fun displayPlacePicker() {
        val action = NewSubmissionFragmentDirections.actionNewSubmissionToSelectPlaceFragment()
        findNavController().navigate(action)
    }

    private fun readBytes(context: Context, uri: Uri): ByteArray? =
        context.contentResolver.openInputStream(uri)?.buffered()?.use { it.readBytes() }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".jpg", context?.cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(
            requireContext().applicationContext,
            "${BuildConfig.APPLICATION_ID}.fileprovider",
            tmpFile
        )
    }

    private fun openCamera() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    addResultImageToSubmission(uri)
                }
            }
        }

    private fun addResultImageToSubmission(uri: Uri) {
        imagePreview.setImageURI(uri)
        mainActivity.submissionImageUri = uri
        binding.imageButtonsContainer.visibility = View.GONE
        binding.imageContainer.visibility = View.VISIBLE
    }

    private fun selectImageFromGallery() {
        selectImageFromGalleryResult.launch("image/*")
    }

    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                addResultImageToSubmission(uri)
            }
        }

    private fun sendSubmissionToServer() {
        val retrofitBuilder = RetrofitClient.getInstance(LoginRepository.userData.Token)

        val submissionModel = NewSubmissionModel(
            LoginRepository.userData.Id,
            mainActivity.submissionDescription,
            getString(R.string.submission_source_name),
            mainActivity.submissionPlace.id,
            mainActivity.submissionTitle,
            mainActivity.submissionDateSend,
            mainActivity.submissionTime
        )
        val retrofitData = retrofitBuilder.addNotification(submissionModel)

        retrofitData.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                val responseBody = response.body()
                when (response.code()) {
                    HttpURLConnection.HTTP_CREATED -> {
                        if (mainActivity.submissionImageUri != null) {
                            sendSubmissionResult.value =
                                SendResult(submissionSent = true, sendingImage = true)
                            val submissionId = responseBody ?: return
                            sendImageToServer(submissionId)
                        } else {
                            sendSubmissionResult.value =
                                SendResult(submissionSent = true, sendingImage = false)
                        }
                    }
                    else -> {
                        sendSubmissionResult.value = SendResult(error = true)
                        Log.d(
                            "NewSubmissionFragment",
                            "Error occurred while sending submission: HTTP CODE " + response.code()
                        )
                    }
                }
            }
            override fun onFailure(call: Call<Int>, t: Throwable) {
                sendSubmissionResult.value = SendResult(error = true)
                Log.d(
                    "NewSubmissionFragment",
                    "Error occurred while sending submission: " + t.message
                )
            }
        })
    }

    private fun sendSubmissionAnonymouslyToServer() {
        val retrofitBuilder = RetrofitClient.getInstance(LoginRepository.userData.Token)

        val notificationModel = NewSubmissionModel(
            LoginRepository.userData.Id,
            mainActivity.submissionDescription,
            "Aplikacja mobilna",
            mainActivity.submissionPlace.id,
            mainActivity.submissionTitle,
            mainActivity.submissionDateSend,
            mainActivity.submissionTime
        )
        val retrofitData = retrofitBuilder.addSubmissionAnonymously(notificationModel)

        retrofitData.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                val responseBody = response.body()
                when (response.code()) {
                    HttpURLConnection.HTTP_CREATED -> {
                        if (mainActivity.submissionImageUri != null) {
                            sendSubmissionResult.value =
                                SendResult(submissionSent = true, sendingImage = true)
                            sendImageToServer(responseBody!!)
                        } else {
                            sendSubmissionResult.value =
                                SendResult(sendingImage = false, submissionSent = true)
                        }
                    }
                    else -> sendSubmissionResult.value = SendResult(error = true)
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                sendSubmissionResult.value = SendResult(error = true)
                Log.d(
                    "NewSubmissionFragment",
                    "Error occurred while sending anonymous submission: " + t.message
                )
            }
        })
    }

    private fun sendImageToServer(submissionId: Int) {
        val retrofitBuilder = RetrofitClient.getInstance(LoginRepository.userData.Token)
        val fileName = "ImageForSubmission$submissionId"
        val image = readBytes(requireContext(), mainActivity.submissionImageUri!!)
        val encodedImage = Base64.encodeToString(image, Base64.DEFAULT)

        val imageAddModel =
            AddImageModel(fileName, encodedImage, submissionId, mainActivity.submissionPlace.id)
        val retrofitData = retrofitBuilder.addImageFromModel(imageAddModel)

        retrofitData.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() == HttpURLConnection.HTTP_CREATED) {
                    sendSubmissionResult.value = SendResult(imageSent = true)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                sendSubmissionResult.value = SendResult(error = true)
                Log.d(
                    "NewSubmissionFragment",
                    "Error occurred while adding image to submission: " + t.message
                )
            }
        })
    }

    private fun displaySubmissionSentFragment(isSent: Boolean) {
        val action =
            NewSubmissionFragmentDirections.actionNewSubmissionToWrongSendNotificationFragment(
                isSent
            )
        findNavController().navigate(action)
    }

}


// FIXME WSZYSTKO PONIŻEJ POTRZEBNE DO INŻYNIERKI WIĘC NIECH ZOSTANIE NARAZIE !!!
//    private fun dispatchGalleryIntent() {
////        check runtime permission
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                if (context?.let { it1 -> checkSelfPermission(it1, Manifest.permission.READ_EXTERNAL_STORAGE) } ==
//                    PackageManager.PERMISSION_DENIED){
//                    //permission denied
//                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
//                    //show popup to request runtime permission
//                    requestPermissions(permissions, PERMISSION_CODE);
//                }
//                else{
//                    //permission already granted
//                    pickImageFromGallery()
//                }
//            }
//            else{
//                //system OS is < Marshmallow
//                pickImageFromGallery()
//            }
//        }

//    private fun dispatchTakePictureIntent() {
//        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmm", Locale.forLanguageTag("PL")).format(Date())
//        val storageDirectory: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//        try {
//            val imageFile: File = File.createTempFile("IMG_${timeStamp}_", ".jpg", storageDirectory)
//            currentPhotoPath = imageFile.absolutePath
//
//            val imageUri = context?.let { FileProvider.getUriForFile(it, "com.ardgahgroup.usterka.fileprovider", imageFile) }
//            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
//            // new way
//            getCameraImage.launch(takePictureIntent)
//            // old way
//            // startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//        } catch (e: ActivityNotFoundException) {
//            e.printStackTrace()
//            // display error state to the user
//        }
//    }

//    //Receiver
//    private val getCameraImage = registerForActivityResult( ActivityResultContracts.StartActivityForResult() ) {
//        if (it.resultCode == RESULT_OK) {
//            val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
//            binding.imageView.setImageBitmap(bitmap)
//            binding.imageContainer.visibility = View.VISIBLE
//            binding.imageAddButtonsContainer.visibility = View.GONE
//        }
//    }

//    private fun pickImageFromGallery() {
//        //Intent to pick image
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = "image/*"
//        startActivityForResult(intent, IMAGE_PICK_CODE)
//    }

//    //handle requested permission result
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when(requestCode){
//            PERMISSION_CODE -> {
//                if (grantResults.size >0 && grantResults[0] ==
//                    PackageManager.PERMISSION_GRANTED){
//                    //permission from popup granted
//                    pickImageFromGallery()
//                }
//                else{
//                    //permission from popup denied
//                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }

//    //handle result of picked image
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
//            binding.imageContainer.visibility = View.VISIBLE
//            binding.imageAddButtonsContainer.visibility = View.GONE
//            binding.imageView.setImageURI(data?.data)
//            binding.imageContainer.setOnClickListener { pickImageFromGallery() }
//        }
//
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
//            binding.imageView.setImageBitmap(bitmap)
//            binding.imageContainer.visibility = View.VISIBLE
//            binding.imageAddButtonsContainer.visibility = View.GONE
//        }
//    }
