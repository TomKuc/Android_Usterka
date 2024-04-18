package com.ardgahgroup.usterka.ui.newSubmission.selectPlace

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ardgahgroup.usterka.R
import com.ardgahgroup.usterka.data.LoginRepository
import com.ardgahgroup.usterka.data.model.api.PlaceModel
import com.ardgahgroup.usterka.data.model.selectPlace.SelectPlaceAdapter
import com.ardgahgroup.usterka.data.model.selectPlace.SelectPlaceRowData
import com.ardgahgroup.usterka.data.retrofit.RetrofitClient
import com.ardgahgroup.usterka.databinding.FragmentSelectPlaceBinding
import com.ardgahgroup.usterka.ui.activities.UserMenuActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SelectPlaceFragment : Fragment() {

    private var _binding: FragmentSelectPlaceBinding? = null
    private val binding get() = _binding!!
    private val list = mutableListOf<SelectPlaceRowData>()
    private var currentPlace: SelectPlaceRowData = SelectPlaceRowData("Miejsce", -1, -1)
    var stack = ArrayDeque<SelectPlaceRowData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectPlaceBinding.inflate(inflater, container, false)
        val root: View = binding.root
        list.clear()

        val adapterBinding =
            SelectPlaceAdapter(requireContext(), R.layout.list_place_view_element, list)
        val listOfPlaces = binding.listView
        listOfPlaces.adapter = adapterBinding
        APIPlaceList(adapterBinding, 0)

        listOfPlaces.setOnItemClickListener { _: AdapterView<*>, _: View, position: Int, _: Long ->
            currentPlace = list[position]
            stack.push(currentPlace)
            APIPlaceList(adapterBinding, list[position].id)
        }

        binding.textViewRollback.setOnClickListener {
            stack.pop()
            if (!stack.isEmpty()) {
                currentPlace = stack.peek()
                APIPlaceList(adapterBinding, currentPlace.id)
            } else {
                currentPlace = SelectPlaceRowData("Miejsce", -1, -1)
                APIPlaceList(adapterBinding, 0)
                binding.textViewRollback.visibility = View.GONE
            }
        }
        return root
    }

    private fun APIPlaceList(adapterBinding: SelectPlaceAdapter, idParent: Int) {
        val mainActivity: UserMenuActivity = activity as UserMenuActivity
        val retrofitBuilder = RetrofitClient.getInstance(LoginRepository.userData.Token)
        val retrofitData = retrofitBuilder.getPlacesByParentID(idParent)
        list.clear()

        retrofitData.enqueue(object : Callback<List<PlaceModel>?> {
            override fun onResponse(
                call: Call<List<PlaceModel>?>,
                response: Response<List<PlaceModel>?>
            ) {
                val responseBody = response.body()

                adapterBinding.notifyDataSetChanged()
                if (responseBody != null) {
                    if (binding.textViewRollback.visibility == View.GONE && idParent != 0) {
                        binding.textViewRollback.visibility = View.VISIBLE
                    }
                    binding.textViewRollback.text = currentPlace.name
                    for (myData in responseBody) {
                        val name = myData.miejsce
                        val id = myData.id
                        val idParent = myData.iD_parent
                        list.add(SelectPlaceRowData(name, id, idParent))
                    }
                } else if (idParent != 0) {
                    mainActivity.submissionPlace = currentPlace
                    val action =
                        SelectPlaceFragmentDirections.actionSelectPlaceFragmentToNewSubmission()
                    findNavController().navigate(action)
                }
            }

            override fun onFailure(call: Call<List<PlaceModel>?>, t: Throwable) {
                Log.d("SelectPlaceFragment", "getPlacesByParentID doesn't work: " + t.message)
            }
        })
    }

}

