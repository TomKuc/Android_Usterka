package com.ardgahgroup.usterka.ui.submissionView.allSubmissionView

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.ardgahgroup.usterka.R
import com.ardgahgroup.usterka.data.LoginRepository
import com.ardgahgroup.usterka.data.model.api.ImageModel
import com.ardgahgroup.usterka.data.model.api.PlaceModel
import com.ardgahgroup.usterka.data.model.api.SubmissionItemModel
import com.ardgahgroup.usterka.data.retrofit.RetrofitClient
import com.ardgahgroup.usterka.databinding.FragmentAllSubmissionViewBinding
import com.ardgahgroup.usterka.ui.activities.UserMenuActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllSubmissionViewFragment : Fragment() {
    private var _binding: FragmentAllSubmissionViewBinding? = null
    private val binding get() = _binding!!
    private val args: AllSubmissionViewFragmentArgs by navArgs()
    private lateinit var mainActivity: UserMenuActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllSubmissionViewBinding.inflate(inflater, container, false)
        val notificationId = args.notificationId
        mainActivity = activity as UserMenuActivity
        APISubmissionView(notificationId)

        return binding.root
    }

    private fun APISubmissionView(Id: Int) {
        val retrofitBuilder = RetrofitClient.getInstance(LoginRepository.userData.Token)
        val retrofitData = retrofitBuilder.getNotifcationById(Id)

        retrofitData.enqueue(object : Callback<SubmissionItemModel> {
            override fun onResponse(
                call: Call<SubmissionItemModel>,
                response: Response<SubmissionItemModel>
            ) {
                val responseBody = response.body()

                if (responseBody != null) {
                    mainActivity.toolbar.title = responseBody.nazwa
                    binding.submissionText.text = responseBody.nazwa
                    binding.descriptionText.text = responseBody.opis_zgloszenia
                    val date = responseBody.happendDate
                    var listDate = listOf<String>()
                    if (date != null) {
                        listDate = date.split("T")
                    }
                    binding.buttonDate.text = listDate[0]
                    val time = listDate[1]
                    listDate = time.split(".")
                    binding.buttonTime.text = listDate[0]
                    if (responseBody.dzial == null) {
                        binding.departmentText.text =
                            getString(R.string.anonymous_submission_department_text)
                    } else {
                        binding.departmentText.text = responseBody.dzial
                    }
                    APISetPlace(responseBody.iD_miejsca)
                    APISetImageDestination(responseBody.id)
                }
            }

            override fun onFailure(call: Call<SubmissionItemModel>, t: Throwable) {
                Log.d("SubmissionViewFragment", "getNotificationById doesn't work: " + t.message)
            }
        })
    }

    private fun APISetPlace(id: Int) {
        val retrofitBuilder = RetrofitClient.getInstance(LoginRepository.userData.Token)
        val retrofitData = retrofitBuilder.getPlaceById(id)

        retrofitData.enqueue(object : Callback<PlaceModel> {
            override fun onResponse(call: Call<PlaceModel>, response: Response<PlaceModel>) {
                val responseBody = response.body()

                if (responseBody != null) {
                    binding.placeText.text = responseBody.miejsce
                }
            }

            override fun onFailure(call: Call<PlaceModel>, t: Throwable) {
                Log.d("SubmissionViewFragment", "getPlaceById doesn't work: " + t.message)
            }
        })
    }

    private fun APISetImageDestination(Id: Int) {
        val retrofitBuilder = RetrofitClient.getInstance(LoginRepository.userData.Token)
        val retrofitData = retrofitBuilder.getImagesForNotification(Id)

        retrofitData.enqueue(object : Callback<List<ImageModel>> {
            override fun onResponse(
                call: Call<List<ImageModel>>,
                response: Response<List<ImageModel>>
            ) {
                val responseBody = response.body()

                if (responseBody != null) {
                    binding.imageCell.visibility = View.GONE
                    APISetImage(responseBody[0].lokalizacja)
                }
            }

            override fun onFailure(call: Call<List<ImageModel>>, t: Throwable) {
                Log.d("SubmissionViewFragment", "getPlaceById doesn't work: " + t.message)
            }
        })
    }

    private fun APISetImage(destination: String) {
        val retrofitBuilder = RetrofitClient.getInstance(LoginRepository.userData.Token)
        val retrofitData = retrofitBuilder.getImageFileFromBase64(destination)

        retrofitData.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val responseBody = response.body()

                if (responseBody != null) {
                    val imageBytes = Base64.decode(responseBody, Base64.DEFAULT)
                    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    binding.imageCell.visibility = View.VISIBLE
                    binding.image.setImageBitmap(decodedImage)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("SubmissionViewFragment", "getImageFileFromBase64 doesn't work: " + t.message)
            }
        })
    }


}