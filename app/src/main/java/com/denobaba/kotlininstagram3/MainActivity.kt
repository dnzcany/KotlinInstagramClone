package com.denobaba.kotlininstagram3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.denobaba.kotlininstagram3.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
        val currentuser = auth.currentUser
        if (currentuser !=null) {
            val intent = Intent(this,FeedActivity::class.java)
            startActivity(intent)
            finish()

        }

    }



    fun signInClicked(view: View){
        val email  = binding.mail1.text.toString()
        val password = binding.password1.text.toString()

        if(email.equals("")||password.equals("")){
            Toast.makeText(this,"Please Write Email And Password",Toast.LENGTH_LONG).show()
        }else{
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                val intent =Intent(this@MainActivity,FeedActivity::class.java)
                startActivity(intent)
                finish()

            }.addOnFailureListener {
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }

        }








    }


    fun signup(view: View){
        val email= binding.mail1.text.toString()
        val password= binding.password1.text.toString()
        if (email.equals("") || password.equals("")){
            Toast.makeText(this,"Please Write Email And Password",Toast.LENGTH_LONG).show()

        }else{
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                val intent =Intent(this@MainActivity,FeedActivity::class.java)
                startActivity(intent)
                finish()

            }.addOnFailureListener {
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }

        }


    }
}