package com.vikram.turtlemintassignment.ui.issues

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.vikram.turtlemintassignment.R
import com.vikram.turtlemintassignment.TurtlemintApplication
import com.vikram.turtlemintassignment.databinding.FragmentIssuesBinding
import com.vikram.turtlemintassignment.other.setupSnackbar
import com.vikram.turtlemintassignment.ui.MainActivity


class IssuesFragment : Fragment() {

    private val viewModel by viewModels<IssuesViewModel> {
        IssuesViewModelFactory((requireContext().applicationContext as TurtlemintApplication).issuesRepository)
    }

    lateinit var issuesAdapter: IssuesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentIssuesBinding.inflate(inflater, container, false)

//        try {
//            (requireActivity() as MainActivity).supportActionBar?.title = "Issues"
//        }catch (e:Exception){
//
//        }

        val glide = Glide.with(requireContext()).setDefaultRequestOptions(
            RequestOptions().placeholder(R.drawable.ic_baseline_portrait)
                .error(R.drawable.ic_baseline_portrait)
        )
        issuesAdapter = IssuesAdapter(glide)

        issuesAdapter.setOnItemClickListener {
            findNavController().navigate(
                IssuesFragmentDirections.actionIssuesFragmentToInfoFragment(
                    it
                )
            )
        }

        setUpRecyclerView(binding)
        subscribeToObserver(binding)


        return binding.root
    }


    private fun subscribeToObserver(binding: FragmentIssuesBinding) {

        binding.root.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)

        viewModel.dataLoading.observe(viewLifecycleOwner) { dataLoading ->
            if (!dataLoading) {

                binding.rvIssuesItems.visibility = View.VISIBLE
                binding.pbIssuesItems.visibility=View.GONE
            }else{
                binding.pbIssuesItems.visibility=View.VISIBLE
            }

        }


        viewModel.items.observe(viewLifecycleOwner) {
            issuesAdapter.issuesItems = it.reversed()
        }



    }

    private fun setUpRecyclerView(binding: FragmentIssuesBinding) {
        binding.rvIssuesItems.apply {
           visibility=View.INVISIBLE
            adapter = issuesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }


}