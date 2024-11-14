package com.example.sutoriapuri.ui.map

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.sutoriapuri.R
import com.example.sutoriapuri.data.Result
import com.example.sutoriapuri.data.ViewModelFactory
import com.example.sutoriapuri.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private  var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private val mapViewModel: MapViewModel by viewModels<MapViewModel>{
        ViewModelFactory.getInstance(requireContext())
    }


    private val callback = OnMapReadyCallback { googleMap ->
        mapViewModel.getLocation().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    val boundsBuilder = LatLngBounds.Builder()
                    result.data.forEach { data ->
                        val latLng = LatLng(data.lat!!, data.lon!!)
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(data.name)
                                .snippet(data.description)
                        )
                        boundsBuilder.include(latLng)
                    }
                    val bounds = boundsBuilder.build()
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
                    showLoading(false)
                }
                is Result.Error -> showLoading(false)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        showLoading(false)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun showLoading(isLoading: Boolean) {
        binding.pbMap.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}