package com.ardgahgroup.usterka.ui.newSubmission.sendResult

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ardgahgroup.usterka.R
import com.ardgahgroup.usterka.databinding.FragmentSendNotificationBinding
import com.ardgahgroup.usterka.ui.activities.UserMenuActivity

class SendSubmissionResultFragment : Fragment() {
    private val binding get() = _binding!!
    private var _binding: FragmentSendNotificationBinding? = null
    private val args: SendSubmissionResultFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSendNotificationBinding.inflate(inflater, container, false)
        val isSent = args.check

        if (isSent) {
            binding.imageView.setImageResource(R.drawable.succces)
            binding.textView.setTextColor(
                ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.blue
                )
            )
            binding.textView.text = getString(R.string.new_submission_button_send_message_succes)
            binding.button.setBackgroundResource(R.drawable.login_login_button_background)
            binding.buttonBorder.setBackgroundResource(R.drawable.login_button_border)

            val mainActivity: UserMenuActivity = activity as UserMenuActivity
            mainActivity.clearSubmissionData()
        }

        binding.button.setOnClickListener {
            val action =
                SendSubmissionResultFragmentDirections.actionWrongSendNotificationFragmentToNewSubmission()
            findNavController().navigate(action)
        }

        return binding.root
    }
}