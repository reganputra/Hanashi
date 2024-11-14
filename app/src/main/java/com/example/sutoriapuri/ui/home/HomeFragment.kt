package com.example.sutoriapuri.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sutoriapuri.data.ViewModelFactory
import com.example.sutoriapuri.databinding.FragmentHomeBinding
import com.example.sutoriapuri.ui.adapter.StoriesAdapter

class HomeFragment : Fragment() {

   private lateinit var binding: FragmentHomeBinding
   private val homeViewModel: HomeViewModel by viewModels {
       ViewModelFactory.getInstance(requireContext())
   }
    private val storyAdapter = StoriesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading(false)
        displayStories()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    private fun displayStories(){
        homeViewModel.getStoriesPaging().observe(viewLifecycleOwner){result ->
            storyAdapter.submitData(lifecycle, result)
        }
        storyAdapter.addLoadStateListener { loadState ->
            when(loadState.source.refresh){
                is LoadState.Loading -> showLoading(true)
                is LoadState.NotLoading -> showLoading(false)
                is LoadState.Error -> showLoading(false)
            }

        }
        binding.rvListStories.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = storyAdapter
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbHome.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}