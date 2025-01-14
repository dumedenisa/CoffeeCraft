package com.example.coffeecraft.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeecraft.Adapter.CartAdapter
import com.example.coffeecraft.Helper.ChangeNumberItemsListener
import com.example.coffeecraft.Model.ItemsModel
import com.example.coffeecraft.databinding.ActivityCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.project1762.Helper.ManagmentCart

class CartActivity : BaseActivity() {
    lateinit var binding: ActivityCartBinding
    private lateinit var managment: ManagmentCart
    private var tax: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managment = ManagmentCart(this)

        calculateCart()
        setVariable()
        initCartList()
    }

    private fun initCartList() {
        with(binding) {
            cartView.layoutManager = LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
            cartView.adapter = CartAdapter(
                managment.getListCart(),
                this@CartActivity,
                object : ChangeNumberItemsListener {
                    override fun onChanged() {
                        calculateCart()
                    }
                })
        }
    }

    private fun setVariable() {
        binding.backBtn.setOnClickListener { finish() }
        binding.checkOutBtn.setOnClickListener {
            proceedCheckout()
        }
    }

    private fun calculateCart() {
        val percentTax = 0.02
        val delivery = 2.0
        val sizeExtraCost = managment.getListCart().sumOf { it.sizePrice * it.numberInCart }

        tax = Math.round((managment.getTotalFee() * percentTax) * 100) / 100.0
        val total = Math.round((managment.getTotalFee() + tax + delivery + sizeExtraCost) * 100) / 100
        val itemTotal = Math.round(managment.getTotalFee() * 100) / 100

        with(binding) {
            totalFeeTxt.text = "$$itemTotal"
            taxTxt.text = "$$tax"
            deliveryTxt.text = "$$delivery"
            totalTxt.text = "$$total"
        }
    }

    private fun proceedCheckout() {
        val orderList = managment.getListCart()

        if (orderList.isEmpty()) {
            return
        }

        val database = FirebaseDatabase.getInstance()
        val ordersRef = database.getReference("orders")

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "guest"
        val orderId = ordersRef.push().key ?: System.currentTimeMillis().toString()
        val orderData = mapOf(
            "userId" to userId,
            "orderId" to orderId,
            "items" to orderList,
            "totalPrice" to binding.totalTxt.text.toString(),
            "tax" to binding.taxTxt.text.toString(),
            "deliveryFee" to 2.0,
            "timestamp" to System.currentTimeMillis()
        )

        ordersRef.child(orderId).setValue(orderData)
            .addOnSuccessListener {
                managment.clearCart()
                initCartList()
                calculateCart()

                val intent = Intent(this, ThankYouActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to place order: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
