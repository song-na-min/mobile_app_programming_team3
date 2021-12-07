package com.cookandroid.google_login_1116_2nd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager

import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
//import kotlin.android.synthetic.main.activity_main.*

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cookandroid.google_login_1116_2nd.navigation.*


import com.google.android.material.bottomnavigation.BottomNavigationView.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.jar.Manifest

class MainActivity : AppCompatActivity(), OnNavigationItemSelectedListener{
//    private var firebaseAuth: FirebaseAuth? = null
//    private var googleSignInClient: GoogleSignInClient? = null
//   // private var binding: ActivityMainBinding? = null

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       val bottom_naviagtion=findViewById<BottomNavigationView>(R.id.bottom_naviagtion)

       bottom_naviagtion.setOnNavigationItemSelectedListener(this)
       // 앨범 접근 권한 요청
       ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
       //푸시토큰 서버 등록
      // registerPushToken()

       //fragment가 main화면에 뜨도록-그러니까 그 사용자의 image view를 주르륵 보여주는 화면을 home버튼을 누르면 뜨도록 했다
       //다른 아이콘으로 바꾸려면 밑에 있는 item id중 원하는 거로 변경하기
       bottom_naviagtion.selectedItemId=R.id.action_home

    }
//    fun registerPushToken(){
//        var pushToken = FirebaseInstanceId.getInstance().token
//        var uid = FirebaseAuth.getInstance().currentUser?.uid
//        var map = mutableMapOf<String,Any>()
//        map["pushtoken"] = pushToken!!
//        FirebaseFirestore.getInstance().collection("pushtokens").document(uid!!).set(map)
//    }
override fun onNavigationItemSelected(item: MenuItem): Boolean {
    setToolbarDefault()
    when (item.itemId) {
        R.id.action_home -> {

            val detailViewFragment = DetailViewFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_content, detailViewFragment)
                .commit()
            return true
        }
        R.id.action_search -> {
            val gridFragment = GridFragment()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_content, gridFragment)
                .commit()
            return true
        }
        R.id.action_add_photo -> {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //startActivity(Intent(this,AddPhotoActivity::class.java))
            startActivity(Intent(this, AddPhotoActivity::class.java))
               // Toast.makeText(this, "add_photo.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "스토리지 읽기 권한이 없습니다.", Toast.LENGTH_LONG).show()
           }

            return true
        }
        R.id.action_favorite_alarm -> {
            val alarmFragment = AlarmFragment()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_content, alarmFragment)
                .commit()
            return true
        }
        R.id.action_account -> {
            val userFragment = UserFragment()
            var bundle=Bundle()
            var uid=FirebaseAuth.getInstance().currentUser?.uid
            bundle.putString("destinationUid",uid)
            userFragment.arguments=bundle
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_content, userFragment)
                .commit()
            return true
        }
    }
    return false
}
    fun setToolbarDefault(){
        var toolbar_username=findViewById<TextView>(R.id.toolbar_username)
        var toolbar_btn_back=findViewById<ImageView>(R.id.toolbar_btn_back)
        var toolbar_title_image=findViewById<ImageView>(R.id.toolbar_title_image)
        toolbar_username.visibility= View.GONE
        toolbar_btn_back.visibility=View.GONE
        toolbar_title_image.visibility=View.VISIBLE

    }

}

