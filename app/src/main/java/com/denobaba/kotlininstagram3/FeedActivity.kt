package com.denobaba.kotlininstagram3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.denobaba.kotlininstagram3.adapter.FeedAdapter
import com.denobaba.kotlininstagram3.databinding.ActivityFeedBinding
import com.denobaba.kotlininstagram3.databinding.ActivityUploadBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FeedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var postArrayList: ArrayList<Post>
    private lateinit var feedAdapter: FeedAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth= Firebase.auth
        db= Firebase.firestore
        postArrayList = ArrayList<Post>()
        getData()
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        feedAdapter = FeedAdapter(postArrayList)
        binding.recycleView.adapter = feedAdapter




    }

    private fun getData(){
        db.collection("Posts").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if (error !=null){
                Toast.makeText(this, error.localizedMessage,Toast.LENGTH_LONG).show()

            }else{
                if(value != null){
                    if(!value.isEmpty){
                        val documents = value.documents
                        postArrayList.clear()
                        for (i in documents){
                            val comment= i.get("comment") as String
                            val userEmail= i.get("userEmail") as String
                            val downloadUrl= i.get("downloadUrl") as String
                            val post = Post(userEmail,comment,downloadUrl)

                            println(comment)
                            println(userEmail)

                            println(downloadUrl)

                            postArrayList.add(post)





                        }
                        feedAdapter.notifyDataSetChanged()

                    }

                }
            }
        }


    }








    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.post_logout_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        if (item.itemId == R.id.postit) {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)

        }
        return super.onOptionsItemSelected(item)


    }

}