package com.cookandroid.google_login_1116_2nd.navigation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cookandroid.google_login_1116_2nd.LoginActivity
import com.cookandroid.google_login_1116_2nd.MainActivity
import com.cookandroid.google_login_1116_2nd.R
import com.cookandroid.google_login_1116_2nd.navigation.model.ContentDTO
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class UserFragment : Fragment(){

    var fragmentView : View?=null
    var firestore : FirebaseFirestore?=null
    var uid : String?=null
    var auth : FirebaseAuth?=null
    var currentUserUid : String?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var fragmentView=LayoutInflater.from(activity).inflate(R.layout.fragment_user,container,false)
        uid=arguments?.getString("destinationUid")
        firestore= FirebaseFirestore.getInstance()
        auth= FirebaseAuth.getInstance()
        currentUserUid=auth?.currentUser?.uid
        var account_btn_follow_signout=fragmentView.findViewById<Button>(R.id.account_btn_follow_signout)
        if(uid==currentUserUid){
            //mypage
            account_btn_follow_signout?.text=getString(R.string.signout)
            account_btn_follow_signout?.setOnClickListener {
                activity?.finish()
                startActivity(Intent(activity,LoginActivity::class.java))
                auth?.signOut()
            }
        }else{
            //other user
            account_btn_follow_signout?.text=getString(R.string.follow)
            var mainactivity=LayoutInflater.from(activity).inflate(R.layout.activity_main,container,false)
            var toolbar_username=mainactivity?.findViewById<TextView>(R.id.toolbar_username)
            toolbar_username?.text=arguments?.getString("userId")
            var toolbar_btn_back=mainactivity?.findViewById<TextView>(R.id.toolbar_btn_back)
            var bottom_naviagtion=mainactivity?.findViewById<BottomNavigationView>(R.id.bottom_naviagtion)
            var toolbar_title_image=mainactivity?.findViewById<BottomNavigationView>(R.id.toolbar_title_image)
            toolbar_btn_back?.setOnClickListener{
                bottom_naviagtion?.selectedItemId=R.id.action_home
            }
            toolbar_title_image?.visibility=View.GONE
            toolbar_username?.visibility=View.VISIBLE
            toolbar_btn_back?.visibility=View.VISIBLE
        }
        var account_recyclerview=fragmentView.findViewById<RecyclerView>(R.id.account_recyclerview)
        account_recyclerview?.adapter=UserFragmentRecyclerViewAdapter()
        account_recyclerview?.layoutManager = GridLayoutManager(activity , 3)

        return fragmentView
    }
    inner class UserFragmentRecyclerViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        var contentDTOs : ArrayList<ContentDTO> = arrayListOf()

        init{
            firestore?.collection("images")?.whereEqualTo("uid",uid)?.addSnapshotListener{
                querySnapshot,firebaseFirestore->
                if(querySnapshot==null) return@addSnapshotListener
                for(snapshot in querySnapshot.documents){
                    contentDTOs.add(snapshot.toObject(ContentDTO::class.java)!!)
                }
                var account_tv_post_count=fragmentView?.findViewById<TextView>(R.id.account_tv_post_count)
                account_tv_post_count?.text=contentDTOs.size.toString()
                notifyDataSetChanged()
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var width=resources.displayMetrics.widthPixels/3
            var imageview=ImageView(parent.context)
            imageview.layoutParams=LinearLayoutCompat.LayoutParams(width,width)
            return CustomViewHolder(imageview)
        }

        inner class CustomViewHolder(var imageview: ImageView) : RecyclerView.ViewHolder(imageview) {

        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var imageview=(holder as CustomViewHolder).imageview
            Glide.with(holder.itemView.context).load(contentDTOs[position].imageUri).apply(RequestOptions().centerCrop()).into(imageview)

        }

        override fun getItemCount(): Int {
            return contentDTOs.size
        }

    }
}