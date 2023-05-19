package com.denobaba.kotlininstagram3

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.denobaba.kotlininstagram3.databinding.ActivityUploadBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.sql.Timestamp
import java.util.UUID

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selecterpicture : Uri? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage : FirebaseStorage


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        registerLauncher()

        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage

    }

    fun upload(view: View){

        val uuid = UUID.randomUUID()  //universal uniq id olarak random bir id alıyoruz
        val imageName="$uuid.jpg"     //resmimizin adını uuid olarak degistirip sonunu jpg yapıyoruz

        val reference= storage.reference //storageye yazdırıcak
        val imageReference = reference.child("images").child(imageName) //storagede images dosyası açıp imagename ismiyle yazıdırcak

        if(selecterpicture != null){
            imageReference.putFile(selecterpicture!!).addOnSuccessListener {
                val uploadPictureReference = storage.reference.child("images").child(imageName)
                uploadPictureReference.downloadUrl.addOnSuccessListener {
                    val downloadUrl = it.toString()
                    val postMap = hashMapOf<String,Any>()
                    postMap.put("downloadUrl",downloadUrl)
                    postMap.put("comment",binding.commentid.text.toString())
                    postMap.put("userEmail",auth.currentUser!!.email!!)
                    postMap.put("date",com.google.firebase.Timestamp.now())

                    firestore.collection("Posts").add(postMap).addOnSuccessListener {
                        finish()

                    }.addOnFailureListener {
                        Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()


                    }




                }.addOnFailureListener{
                    Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()

                }


            }.addOnFailureListener {
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }

        }



    }

    fun selectedimage(view: View){
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){

                Snackbar.make(view,"Permission Needed for gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
                    //request permission
                    permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }.show()
            }else{
                //requestpermission
                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)

            }

        }else{
            val intenttogallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            //start activity for result
            activityResultLauncher.launch(intenttogallery)


        }


    }

    private fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode== RESULT_OK){
                val intentFromResult = result.data
                if(intentFromResult != null){
                    selecterpicture=intentFromResult.data
                    selecterpicture?.let {
                        binding.imageView3.setImageURI(it)
                    }


                }
            }

        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){result ->
            if(result){
                //permissiongranted
                val intenttogallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intenttogallery)


            }else{
                Toast.makeText(this@UploadActivity,"Permission Needed",Toast.LENGTH_LONG).show()

            }

        }

    }
}