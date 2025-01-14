package com.example.coffeecraft.Model

import android.os.Parcel
import android.os.Parcelable

data class ItemsModel(
    var title: String = "",
    var description: String = "",
    var picUrl: ArrayList<String> = ArrayList(),
    var price: Double = 0.0,
    var rating: Double = 0.0,
    var numberInCart: Int = 0,
    var extra: String = "",
    var sizePrice: Int = 0,
    var category: String = "",
    var caffeineLevel: String = "",
    var sweetness: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.createStringArrayList() as ArrayList<String>,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeStringList(picUrl)
        parcel.writeDouble(price)
        parcel.writeDouble(rating)
        parcel.writeInt(numberInCart)
        parcel.writeString(extra)
        parcel.writeInt(sizePrice)
        parcel.writeString(category)
        parcel.writeString(caffeineLevel)
        parcel.writeString(sweetness)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ItemsModel> {
        override fun createFromParcel(parcel: Parcel): ItemsModel {
            return ItemsModel(parcel)
        }

        override fun newArray(size: Int): Array<ItemsModel?> {
            return arrayOfNulls(size)
        }
    }


    fun toCoffeeModel(): CoffeeModel {
        return CoffeeModel(
            title = this.title,
            description = this.description,
            price = this.price,
            rating = this.rating,
            picUrl = this.picUrl.firstOrNull() ?: "",
            numberInCart = this.numberInCart
        )
    }
}

data class CoffeeModel(
    var title: String = "",
    var description: String = "",
    var price: Double = 0.0,
    var rating: Double = 0.0,
    var picUrl: String = "",
    var numberInCart: Int = 0
)
