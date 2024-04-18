package com.ardgahgroup.usterka.ui.submissionsList.mySubmissions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ardgahgroup.usterka.R
import com.ardgahgroup.usterka.data.LoginRepository
import com.ardgahgroup.usterka.data.model.api.SubmissionsWithStatusModel
import com.ardgahgroup.usterka.data.model.submissionsList.SubmissionsListAdapter
import com.ardgahgroup.usterka.data.model.submissionsList.SubmissionsListRowData
import com.ardgahgroup.usterka.data.retrofit.RetrofitClient
import com.ardgahgroup.usterka.databinding.FragmentSubmissionsListBinding
import com.ardgahgroup.usterka.helpers.SubmissionStatusCategory
import com.ardgahgroup.usterka.ui.activities.UserMenuActivity
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubmissionsListFragment : Fragment() {

    private var _binding: FragmentSubmissionsListBinding? = null
    private lateinit var viewModel: SubmissionsListViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var submissionStatus: String
    private lateinit var mainActivity: UserMenuActivity
    private val list = mutableListOf<SubmissionsListRowData>()
    private val args: SubmissionsListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[SubmissionsListViewModel::class.java]
        _binding = FragmentSubmissionsListBinding.inflate(inflater, container, false)
        submissionStatus = args.statusName

        mainActivity = activity as UserMenuActivity
        mainActivity.toolbar.title = submissionStatus

        val adapterBinding = SubmissionsListAdapter(
            requireContext(),
            R.layout.list_view_element,
            list,
            submissionStatus
        )
        val listOfSubmissions = binding.listView
        listOfSubmissions.adapter = adapterBinding

        val status = SubmissionStatusCategory(submissionStatus)
        if (list.isEmpty()) {
            APISubmissionList(adapterBinding, status)
        }

        listOfSubmissions.setOnItemClickListener { _: AdapterView<*>, _: View, position: Int, _: Long ->
            val action =
                SubmissionsListFragmentDirections.actionMySubmissionsWithStatusListToMySubmissionView(
                    list[position].id
                )
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun APISubmissionList(
        adapterBinding: SubmissionsListAdapter,
        status: SubmissionStatusCategory
    ) {
        mainActivity.loadingProgressBar.showLoadingCircle()
        mainActivity.loadingProgressBar.setTextViewText(getString(R.string.submission_list_progress_bar_text))
        val retrofitBuilder = RetrofitClient.getInstance(LoginRepository.userData.Token)
        val retrofitData = retrofitBuilder.getNotifcationByUserAndStatus(
            LoginRepository.userData.Id,
            status.categoryId
        )

        retrofitData.enqueue(object : Callback<List<SubmissionsWithStatusModel>?> {
            override fun onResponse(
                call: Call<List<SubmissionsWithStatusModel>?>,
                response: Response<List<SubmissionsWithStatusModel>?>
            ) {
                val responseBody = response.body()

                APIAnonymousSubmissionList(adapterBinding, status)
                if (responseBody != null) {
                    for (myData in responseBody) {
                        val name = myData.name ?: continue
                        val id = myData.id
                        list.add(SubmissionsListRowData(name, id))
                    }
                }
            }

            override fun onFailure(call: Call<List<SubmissionsWithStatusModel>?>, t: Throwable) {
                Log.d("ListOfReportActivity", "getNotification doesn't work: " + t.message)
            }
        })
    }

    private fun APIAnonymousSubmissionList(
        adapterBinding: SubmissionsListAdapter,
        status: SubmissionStatusCategory
    ) {
        val RetrofitBuilder = RetrofitClient.getInstance(LoginRepository.userData.Token)
        val retrofitData = RetrofitBuilder.getAnonymousSubmissions(status.categoryId)

        retrofitData.enqueue(object : Callback<List<SubmissionsWithStatusModel>?> {
            override fun onResponse(
                call: Call<List<SubmissionsWithStatusModel>?>,
                response: Response<List<SubmissionsWithStatusModel>?>
            ) {
                val responseBody = response.body()

                adapterBinding.notifyDataSetChanged()
                if (responseBody != null) {
                    for (myData in responseBody) {
                        var name = myData.name ?: return
                        var id = myData.id
                        list.add(SubmissionsListRowData(name, id))
                    }
                    list.sortBy { it.id }
                    list.reverse()

                }
                if (list.isEmpty()) {
                    emptyListToast()
                }
                mainActivity.loadingProgressBar.hideLoadingCircle()
            }

            override fun onFailure(call: Call<List<SubmissionsWithStatusModel>?>, t: Throwable) {
                Log.d("ListOfReportActivity", "getAnonymousSubmissions doesn't work: " + t.message)
            }
        })
    }

    private fun emptyListToast() {
        Toasty.custom(requireContext(), R.string.submissions_list_no_submissions_with_status_found,
            R.drawable.ic_status_list_no_submissions_info, R.color.blue, Toast.LENGTH_LONG, true, true).show()
    }
}
