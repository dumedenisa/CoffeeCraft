package com.example.coffeecraft.Activity

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.coffeecraft.Adapter.CoffeeAdapter
import com.example.coffeecraft.Model.ItemsModel
import com.example.coffeecraft.databinding.ActivityCoffeeBinding
import com.google.firebase.database.*

class CoffeeActivity : BaseActivity() {

    private lateinit var binding: ActivityCoffeeBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var favoritesRef: DatabaseReference
    private lateinit var itemsRef: DatabaseReference
    private lateinit var adapter: CoffeeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoffeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        favoritesRef = database.getReference("favorites")
        itemsRef = database.getReference("Items")

        setupRecyclerView()
        observeFavorites()

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        adapter = CoffeeAdapter(mutableListOf())
        binding.coffeeView.layoutManager = GridLayoutManager(this, 2)
        binding.coffeeView.adapter = adapter
    }

    private fun observeFavorites() {
        favoritesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(favoritesSnapshot: DataSnapshot) {
                val favoriteCaffeineLevels = mutableSetOf<String>()

                for (snapshot in favoritesSnapshot.children) {
                    val favorite = snapshot.getValue(ItemsModel::class.java)
                    favorite?.let {
                        favoriteCaffeineLevels.add(it.caffeineLevel)
                    }
                }

                if (favoriteCaffeineLevels.isNotEmpty()) {
                    findSimilarItems(favoriteCaffeineLevels)
                } else {
                    adapter.updateData(emptyList())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CoffeeActivity, "Failed to fetch favorites: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun findSimilarItems(favoriteCaffeineLevels: Set<String>) {
        itemsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(itemsSnapshot: DataSnapshot) {
                val similarItems = mutableListOf<ItemsModel>()

                for (snapshot in itemsSnapshot.children) {
                    val item = snapshot.getValue(ItemsModel::class.java)
                    if (item != null) {
                        if (item.caffeineLevel in favoriteCaffeineLevels) {
                            similarItems.add(item)
                        }
                    }
                }

                if (similarItems.isNotEmpty()) {
                    adapter.updateData(similarItems)
                } else {
                    adapter.updateData(emptyList())
                    Toast.makeText(
                        this@CoffeeActivity,
                        "No similar items found.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@CoffeeActivity,
                    "Failed to fetch items: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
