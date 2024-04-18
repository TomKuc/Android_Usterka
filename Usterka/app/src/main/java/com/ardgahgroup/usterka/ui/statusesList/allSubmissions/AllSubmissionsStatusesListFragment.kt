package com.ardgahgroup.usterka.ui.statusesList.allSubmissions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ardgahgroup.usterka.databinding.FragmentAllSubmissionsBinding
import com.ardgahgroup.usterka.ui.activities.UserMenuActivity

class AllSubmissionsStatusesListFragment : Fragment() {
    private var _binding: FragmentAllSubmissionsBinding? = null
    private lateinit var statusesListViewModel: AllSubmissionsStatusesListViewModel
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
            ViewModelProvider(this)[AllSubmissionsStatusesListViewModel::class.java]
        _binding = FragmentAllSubmissionsBinding.inflate(inflater, container, false)

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

        return binding.root
    }


    private fun navigate(button: Button) {
        val statusColor = button.text.toString()
        val action =
            AllSubmissionsStatusesListFragmentDirections.actionAllSubmissionsToAllSubmissionsListFragment(
                statusColor
            )
        findNavController().navigate(action)
    }
}