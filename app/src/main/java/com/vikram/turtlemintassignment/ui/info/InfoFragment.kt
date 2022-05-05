package com.vikram.turtlemintassignment.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.vikram.turtlemintassignment.R
import com.vikram.turtlemintassignment.TurtlemintApplication
import com.vikram.turtlemintassignment.databinding.FragmentInfoBinding
import com.vikram.turtlemintassignment.other.formattedDate
import com.vikram.turtlemintassignment.other.setupSnackbar
import com.vikram.turtlemintassignment.other.smartTruncate
import com.vikram.turtlemintassignment.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_info.*

class InfoFragment : Fragment() {


    private val viewModel by viewModels<InfoViewModel> {
        InfoViewModelFactory((requireContext().applicationContext as TurtlemintApplication).issuesRepository)
    }

    val args: InfoFragmentArgs by navArgs()

    lateinit var glide: RequestManager

    lateinit var commentsAdapter: CommentsAdapter

    lateinit var binding: FragmentInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoBinding.inflate(inflater, container, false)

        (requireActivity() as MainActivity).supportActionBar?.title = "Issue Detail"

        glide = Glide.with(requireContext()).setDefaultRequestOptions(
            RequestOptions().placeholder(R.drawable.ic_baseline_portrait)
                .error(R.drawable.ic_baseline_portrait)
        )
        commentsAdapter = CommentsAdapter(glide)

        setUpRecyclerView(binding)

        subscribeToObserver(binding)



        return binding.root
    }

    private fun setUpRecyclerView(binding: FragmentInfoBinding) {
        binding.rvInfoComments.apply {
            visibility = View.INVISIBLE
            adapter = commentsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun subscribeToObserver(binding: FragmentInfoBinding) {
        binding.root.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)

        viewModel.start(args.issueNum)
        viewModel.commentItems.observe(viewLifecycleOwner) {
            commentsAdapter.commentsItems = it.reversed()
        }
        viewModel.dataLoading.observe(viewLifecycleOwner) { dataLoading ->
            if (!dataLoading) {

                binding.rvInfoComments.visibility = View.VISIBLE
                binding.pbComments.visibility=View.GONE
            }else{
                binding.pbComments.visibility=View.VISIBLE
            }

        }

        viewModel.issuesItem.observe(viewLifecycleOwner) {
            it?.let { issuesItem ->
                glide.load(issuesItem.avatar).into(ivAvatarInfo)
                tvNameInfo.text = issuesItem.name
                tvTitleInfo.text = issuesItem.title
                tvDescriptionInfo.text = issuesItem.description.smartTruncate(200)
                tvDateInfo.text = formattedDate(issuesItem.date)
            }
        }

    }


    override fun onDestroyView() {
        commentsAdapter.commentsItems = emptyList()
        binding.rvInfoComments.recycledViewPool.clear()
        super.onDestroyView()

    }

}