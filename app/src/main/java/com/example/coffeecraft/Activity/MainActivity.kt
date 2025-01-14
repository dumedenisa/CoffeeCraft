package com.example.coffeecraft.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeecraft.Adapter.CategoryAdapter
import com.example.coffeecraft.Adapter.PopularAdapter
import com.example.coffeecraft.Model.CategoryModel
import com.example.coffeecraft.ViewModel.MainViewModel
import com.example.coffeecraft.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initCategory()
        initPopular()
        bottomMenu()
    }

    private fun bottomMenu() {
        binding.cartBtn.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    private fun initPopular() {
        binding.progressBarPopular.visibility = View.VISIBLE
        viewModel.popular.observe(this, Observer {
            binding.recyclerViewPopular.layoutManager = GridLayoutManager(this@MainActivity, 2)
            binding.recyclerViewPopular.adapter = PopularAdapter(it)
            binding.progressBarPopular.visibility = View.GONE
        })
        viewModel.loadPopular()
    }

    private fun initCategory() {
        binding.progressBarCategory.visibility = View.VISIBLE
        viewModel.category.observe(this, Observer {
            binding.recyclerViewCategory.layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            val adapter = CategoryAdapter(it) { category ->
                onCategoryClick(category)
            }
            binding.recyclerViewCategory.adapter = adapter
            binding.progressBarCategory.visibility = View.GONE
        })
        viewModel.loadCategory()
    }

    private fun onCategoryClick(category: CategoryModel) {
        when (category.title) {
            "Coffee" -> navigateToCoffeeActivity()
            //"Hot Chocolate" -> navigateToHotChocolateActivity()
            //"Lemonade" -> navigateToLemonadeActivity()
        }
    }

    private fun navigateToCoffeeActivity() {
        val intent = Intent(this, CoffeeActivity::class.java)
        startActivity(intent)
    }
}