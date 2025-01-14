package com.example.coffeecraft.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coffeecraft.Activity.DetailActivity
import com.example.coffeecraft.Model.ItemsModel
import com.example.coffeecraft.databinding.ItemCoffeeBinding

class CoffeeAdapter(private var items: MutableList<ItemsModel>) :
    RecyclerView.Adapter<CoffeeAdapter.Viewholder>() {

    private var context: Context? = null

    class Viewholder(val binding: ItemCoffeeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeAdapter.Viewholder {
        context = parent.context
        val binding = ItemCoffeeBinding.inflate(LayoutInflater.from(context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: CoffeeAdapter.Viewholder, position: Int) {
        val item = items[position]

        holder.binding.titleTxt.text = item.title
        holder.binding.priceTxt.text = "$${item.price}"
        holder.binding.ratingBar.rating = item.rating.toFloat()
        holder.binding.extraTxt.text = item.extra

        Glide.with(holder.itemView.context)
            .load(item.picUrl[0])
            .into(holder.binding.pic)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("object", item)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<ItemsModel>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}

