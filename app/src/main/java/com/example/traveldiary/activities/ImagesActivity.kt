package com.example.traveldiary.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.traveldiary.databinding.ActivityImagesBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.example.traveldiary.adapters.ImagesAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ImagesActivity : AppCompatActivity() {

    private lateinit var binding:ActivityImagesBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var mList = mutableListOf<String>()
    private lateinit var adapter: ImagesAdapter
    private lateinit var user: FirebaseUser
    private lateinit var delImg: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVars()
        getImages()
    }

    private fun initVars() {
        firebaseFirestore = FirebaseFirestore.getInstance()
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ImagesAdapter(mList)
        binding.recyclerView.adapter = adapter

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getImages(){
        binding.progressBar.visibility = View.VISIBLE
        user = FirebaseAuth.getInstance().currentUser!!
        val diaryId = intent.getStringExtra("diary_id").toString()
        firebaseFirestore.collection("images")
            .get().addOnSuccessListener {
                for(i in it){
                    if (i.data["user_id"] != user.uid.toString()) continue
                    if (i.data["diary_id"] != diaryId) continue
                    mList.add(i.data["img"].toString())
                }
                adapter.notifyDataSetChanged()
                binding.progressBar.visibility = View.GONE
            }
    }

}