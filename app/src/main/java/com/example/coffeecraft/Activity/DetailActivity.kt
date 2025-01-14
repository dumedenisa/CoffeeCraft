package com.example.coffeecraft.Activity

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.coffeecraft.Adapter.SizeAdapter
import com.example.coffeecraft.Model.ItemsModel
import com.example.coffeecraft.R
import com.example.coffeecraft.databinding.ActivityDetailBinding
import com.example.project1762.Helper.ManagmentCart
import com.google.firebase.database.FirebaseDatabase

class DetailActivity : BaseActivity() {
    lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemsModel
    private lateinit var managmentCart: ManagmentCart
    private var sizePrice: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managmentCart = ManagmentCart(this)

        bundle()
        initSizeList()
    }

    private fun initSizeList() {
        val sizeList = arrayListOf("1", "2", "3", "4")

        val sizeAdapter = SizeAdapter(sizeList)
        binding.sizeList.adapter = sizeAdapter
        binding.sizeList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        sizeAdapter.setOnSizeClickListener { size ->
            sizePrice = size.toInt()
            updatePrice()
        }
    }

    private fun bundle() {
        binding.apply {
            item = intent.getParcelableExtra("object")!!
            titleTxt.text = item.title
            descriptionTxt.text = item.description
            priceTxt.text = "$${item.price}"
            ratingBar.rating = item.rating.toFloat()

            if (item.picUrl.isNotEmpty()) {
                Glide.with(this@DetailActivity)
                    .load(item.picUrl[0])
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(100)))
                    .into(binding.picMain)
            }

            checkIfFavorite(item) { isFavorite ->
                if (isFavorite) {
                    binding.favBtn.apply {
                        layoutParams.width = 48.dpToPx()
                        layoutParams.height = 48.dpToPx()
                        setImageResource(R.drawable.favorite_filled_fixed)
                    }
                } else {
                    binding.favBtn.setImageResource(R.drawable.favorite_white)
                }
            }

            addToCartBtn.setOnClickListener {
                item.numberInCart = Integer.valueOf(numberItemTxt.text.toString())
                managmentCart.insertItems(item)
            }

            backBtn.setOnClickListener {
                startActivity(Intent(this@DetailActivity, MainActivity::class.java))
            }

            plusCart.setOnClickListener {
                numberItemTxt.text = (item.numberInCart + 1).toString()
                item.numberInCart++
            }

            minusCart.setOnClickListener {
                if (item.numberInCart > 0) {
                    numberItemTxt.text = (item.numberInCart - 1).toString()
                    item.numberInCart--
                }
            }

            favBtn.setOnClickListener {
                checkIfFavorite(item) { isFavorite ->
                    if (isFavorite) {
                        removeFromFavorites(item)
                    } else {
                        addToFavorites(item)
                    }
                }
            }
        }
    }

    private fun updatePrice() {
        val totalPrice = item.price + sizePrice
        binding.priceTxt.text = "$$totalPrice"
    }

    private fun addToFavorites(item: ItemsModel) {
        val database = FirebaseDatabase.getInstance()
        val favoritesRef = database.getReference("favorites")

        favoritesRef.child(item.title).setValue(item)
            .addOnSuccessListener {
                binding.favBtn.apply {
                    layoutParams.width = 48.dpToPx()
                    layoutParams.height = 48.dpToPx()
                    setImageResource(R.drawable.favorite_filled_fixed)
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to add to favorites: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeFromFavorites(item: ItemsModel) {
        val database = FirebaseDatabase.getInstance()
        val favoritesRef = database.getReference("favorites")

        favoritesRef.child(item.title).removeValue()
            .addOnSuccessListener {
                binding.favBtn.setImageResource(R.drawable.favorite_white)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to remove from favorites: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkIfFavorite(item: ItemsModel, callback: (Boolean) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val favoritesRef = database.getReference("favorites")

        favoritesRef.child(item.title).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                callback(true)
            } else {
                callback(false)
            }
        }.addOnFailureListener {
            callback(false)
        }
    }

    private fun Int.dpToPx(): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            resources.displayMetrics
        ).toInt()
    }




}
