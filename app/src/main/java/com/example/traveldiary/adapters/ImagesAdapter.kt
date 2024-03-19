package com.example.traveldiary.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.traveldiary.databinding.EachImageItemBinding
import com.squareup.picasso.Picasso

class ImagesAdapter(private var mList: MutableList<String>, private val onDeleteClickListener: OnDeleteClickListener) :
    RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder>() {

    interface OnDeleteClickListener {
        fun onDeleteClick(position: Int)
    }

    inner class ImagesViewHolder(var binding: EachImageItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val binding = EachImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        with(holder.binding) {
            Picasso.get().load(mList[position]).into(imageView)
            imageDelete.setOnClickListener {
                onDeleteClickListener.onDeleteClick(holder.adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun removeItem(position: Int) {
        mList.removeAt(position)
        notifyItemRemoved(position)
    }
}
