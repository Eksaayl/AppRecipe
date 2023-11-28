package com.example.apprecipe

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var imageView: ImageView
    var mTitle: TextView
    var mDescription: TextView
    var mPrice: TextView
    var mCardView: CardView

    init {
        imageView = itemView.findViewById(R.id.ivImage)
        mTitle = itemView.findViewById(R.id.tvTitle)
        mDescription = itemView.findViewById(R.id.tvDescription)
        mPrice = itemView.findViewById(R.id.tvPrice)
        mCardView = itemView.findViewById(R.id.myCardView)
    }
}

class MyAdapter(private val mContext: Context, private val myFoodList: List<FoodData>) :
    RecyclerView.Adapter<FoodViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val mView =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_row_item, parent, false)
        return FoodViewHolder(mView)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, i: Int) {

        Glide.with(mContext)
            .load(myFoodList[i].itemImage)
            .into(holder.imageView)

        //holder.imageView.setImageResource(myFoodList.get(i).getItemImage());
        holder.mTitle.text = myFoodList[i].itemName
        holder.mDescription.text = myFoodList[i].itemDescription
        holder.mPrice.text = myFoodList[i].itemPrice
        holder.mCardView.setOnClickListener {
            val intent = Intent(mContext, DetailActivity::class.java)
            intent.putExtra("Image", myFoodList[holder.adapterPosition].itemImage)
            intent.putExtra("Description", myFoodList[holder.adapterPosition].itemDescription)
            mContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return myFoodList.size
    }
}

