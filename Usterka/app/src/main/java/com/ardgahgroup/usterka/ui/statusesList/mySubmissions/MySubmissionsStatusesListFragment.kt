package com.ardgahgroup.usterka.ui.statusesList.mySubmissions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ardgahgroup.usterka.R
import com.ardgahgroup.usterka.databinding.FragmentMySubmissionsBinding
import com.ardgahgroup.usterka.ui.activities.UserMenuActivity
import es.dmoral.toasty.Toasty


class MySubmissionsStatusesListFragment : Fragment() {

    private var _binding: FragmentMySubmissionsBinding? = null
    private lateinit var statusesListViewModel: MySubmissionsStatusesListViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity = activity as UserMenuActivity
        mainActivity.loadingProgressBar.hideLoadingCircle()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        statusesListViewModel =
            ViewModelProvider(this)[MySubmissionsStatusesListViewModel::class.java]

        _binding = FragmentMySubmissionsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val buttonReported = binding.buttonReported
        val buttonInProgress = binding.buttonInProgress
        val buttonAccepted = binding.buttonAccepted
        val buttonFinished = binding.buttonFinished
        val buttonDeleted = binding.buttonDeleted
        val buttonSuspended = binding.buttonSuspended

        buttonAccepted.setOnClickListener { navigate(buttonAccepted) }
        buttonReported.setOnClickListener { navigate(buttonReported) }
        buttonInProgress.setOnClickListener { navigate(buttonInProgress) }
        buttonFinished.setOnClickListener { navigate(buttonFinished) }
        buttonDeleted.setOnClickListener { navigate(buttonDeleted) }
        buttonSuspended.setOnClickListener { navigate(buttonSuspended) }

        return root
    }

    private fun navigate(button: Button) {
        val statusColor = button.text.toString()
        val action =
            MySubmissionsStatusesListFragmentDirections.actionMySubmissionsStatusesToMySubmissionsWithStatusList(
                statusColor
            )
        findNavController().navigate(action)
    }

    private fun noSubmissionWithStatus() {
        Toasty.custom(
            requireContext(),
            "Nie ma zgłoszeń o danym statusie",
            R.drawable.ic_status_list_no_submissions_info,
            R.color.blue,
            Toast.LENGTH_LONG,
            true,
            true
        ).show();
    }


//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(MySubmissionsViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}