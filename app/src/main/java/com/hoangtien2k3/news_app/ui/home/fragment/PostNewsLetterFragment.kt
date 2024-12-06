package com.hoangtien2k3.news_app.ui.home.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hoangtien2k3.news_app.R
import com.hoangtien2k3.news_app.databinding.FragmentPostNewsletterBinding
import com.hoangtien2k3.news_app.ui.home.viewmodel.PostNewsLetterViewModel
import com.hoangtien2k3.news_app.utils.Resource

class PostNewsLetterFragment : Fragment() {
    private lateinit var binding: FragmentPostNewsletterBinding
    private lateinit var viewModel: PostNewsLetterViewModel
    private lateinit var title: String
    private lateinit var link: String
    private lateinit var img: String
    private lateinit var pubDate: String
    private lateinit var category: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostNewsletterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[PostNewsLetterViewModel::class.java]
        initUI()

        binding.back.setOnClickListener { requireActivity().onBackPressed() }
        binding.btnPostNews.setOnClickListener {
            title = binding.txtTitle.text.toString()
            link = binding.txtLink.text.toString()
            img = binding.txtImage.text.toString()
            pubDate = binding.txtDate.text.toString()

            if (title == "" || link == "" || img == "" || pubDate == "" || category == "") {
                Toast.makeText(
                    requireContext(),
                    R.string.full_information.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
            viewModel.postNewsLetter(title, link, img, pubDate, category)
        }

        observeViewModel()
    }

    private fun initUI() {


        // create category bản tin
        val spinner: Spinner = binding.txtCategory
        val categories = listOf(
            "tin_noi_bat",
            "tin_moi_nhat",
            "tin_the_gioi",
            "tin_phap_luat",
            "tin_giao_duc",
            "tin_suc_khoe",
            "tin_doi_song",
            "tin_khoa_hoc",
            "tin_kinh_doanh",
            "tin_tam_su",
            "tin_so_hoa",
            "tin_du_lich"
        )
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = categories[position]
                category = selectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun observeViewModel() {
        viewModel.postNewsLetterResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Error -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.post_information_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> {
                    Log.d("PostNewsLetterFragment", "Đang tải...")
                }
                is Resource.Success -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.post_information_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
//                    requireActivity().onBackPressedDispatcher.onBackPressed()
                    // Reset các trường input về giá trị mặc định
                    binding.txtTitle.text?.clear()
                    binding.txtLink.text?.clear()
                    binding.txtImage.text?.clear()
                    binding.txtDate.text?.clear()
                    binding.txtCategory.setSelection(0) // Đặt Spinner về item đầu tiên
                }
            }
        }
    }

}