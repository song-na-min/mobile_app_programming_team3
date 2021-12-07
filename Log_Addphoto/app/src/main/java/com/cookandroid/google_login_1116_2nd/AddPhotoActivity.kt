package com.cookandroid.google_login_1116_2nd

//import android.support.v7.app.AppCompatActivity
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
//import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.cookandroid.google_login_1116_2nd.navigation.model.ContentDTO
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.text.SimpleDateFormat
import java.util.*
////import com.company.howl.howlstagram.R
////import com.company.howl.howlstagram.model.ContentDTO
//import com.cookandroid.google_login_1116_2nd.R
//import com.google.firebase.firestore.FieldValue
//import com.google.firebase.firestore.FirebaseFirestore


class AddPhotoActivity : AppCompatActivity() {

    val PICK_IMAGE_FROM_ALBUM = 0
    var storage: FirebaseStorage? = null
    var photoUri: Uri? = null
    var auth : FirebaseAuth?=null
    var firestore:FirebaseFirestore?=null

    lateinit var addphoto_image:ImageView
    lateinit var addphoto_btn_upload : Button
    lateinit var addphoto_edit_explain : EditText



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)

        var addphoto_btn_upload=findViewById<Button>(R.id.addphoto_btn_upload)
        storage = FirebaseStorage.getInstance()
        auth= FirebaseAuth.getInstance()
       firestore = FirebaseFirestore.getInstance()

        val photoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
        photoPickerIntent.type = "image/*"
       startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)

//        addphoto_image.setOnClickListener {
//            val photoPickerIntent = Intent(Intent.ACTION_PICK)
//            photoPickerIntent.type = "image/*"
//            startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)
//        }

        addphoto_btn_upload.setOnClickListener {
            //Toast.makeText(this,"button click",Toast.LENGTH_LONG).show()
            contentUpload() }
    }
//
//     fun onCreateView(inflater : LayoutInflater,container:ViewGroup?,
//        savedInstanceState: Bundle?):View?{
//        viewProfile=inflater.inflate(R.layout.activity_add_photo,container,false)
//        storage= FirebaseStorage.getInstance()
//        addphoto_btn_upload.setOnClickListener{
//        val photoPickerIntent = Intent(Intent.ACTION_PICK)
//        photoPickerIntent.type = "image/*"
//        startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)
//        }
//        return viewProfile
//    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    var addphoto_image=findViewById<ImageView>(R.id.addphoto_image)
    super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==PICK_IMAGE_FROM_ALBUM) {
            if (resultCode == Activity.RESULT_OK) {
                photoUri = data?.data
                addphoto_image.setImageURI(photoUri)
                //Toast.makeText(this,photoUri.toString(),Toast.LENGTH_LONG).show()
            } else {
                //Toast.makeText(this,"onactivityresult_else_finish",Toast.LENGTH_LONG).show()
               finish()
            }
        }
    }

    fun contentUpload(){
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "IMAGE_"+timestamp+"_.jpg"
        var storageReference=storage?.reference
        var imageRef=storageReference?.child(imageFileName)
        var storageRef=storage?.reference?.child("images")?.child(imageFileName)
        var addphoto_edit_explain=findViewById<EditText>(R.id.addphoto_edit_explain)

        storageRef?.putFile(photoUri!!)?.continueWithTask(){
            task: Task<UploadTask.TaskSnapshot> ->
            return@continueWithTask storageRef.downloadUrl}?.addOnSuccessListener { uri->
                var contentDTO=ContentDTO()

                contentDTO.imageUri=uri.toString()
                contentDTO.uid=auth?.currentUser?.uid
                contentDTO.userId=auth?.currentUser?.email
                contentDTO.explain=addphoto_edit_explain.text.toString()
                contentDTO.timestamp=System.currentTimeMillis()

                firestore?.collection("images")?.document()?.set(contentDTO)
                setResult(Activity.RESULT_OK)
                finish()
            }
        }



}




