package com.cookandroid.google_login_1116_2nd.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cookandroid.google_login_1116_2nd.R
import com.cookandroid.google_login_1116_2nd.databinding.FragmentDetailBinding
import com.cookandroid.google_login_1116_2nd.navigation.model.ContentDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DetailViewFragment : Fragment() {
    var firestore : FirebaseFirestore?=null
    var uid=FirebaseAuth.getInstance().currentUser?.uid
    private var _binding: FragmentDetailBinding?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentDetailBinding.inflate(inflater,container,false)
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_detail, container, false)
        firestore=FirebaseFirestore.getInstance()
        var detailviewfragment_recyclerview=view.findViewById<RecyclerView>(R.id.detailviewfragment_recyclerview)
        detailviewfragment_recyclerview.adapter=DetailViewREcyclerViewAdapter()
        detailviewfragment_recyclerview.layoutManager=LinearLayoutManager(activity)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
    inner class DetailViewREcyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        var contentDTOs : ArrayList<ContentDTO> =arrayListOf()
        var contentUidList : ArrayList<String> =arrayListOf()

        init{

            firestore?.collection("images")?.orderBy("timestamp")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                contentDTOs.clear()
                contentUidList.clear()
                if(querySnapshot==null)return@addSnapshotListener
                for(snapshot in querySnapshot!!.documents){
                    var item=snapshot.toObject(ContentDTO::class.java)
                    contentDTOs.add(item!!)
                    contentUidList.add(snapshot.id)

                }
                notifyDataSetChanged()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view=LayoutInflater.from(parent.context).inflate(R.layout.item_detail,parent,false)
            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewholder = (holder as CustomViewHolder).itemView
            var detailviewitem_profile_textView = viewholder.findViewById<TextView>(R.id.detailviewitem_profile_textview)
            var detailviewitem_profile_image=viewholder.findViewById<ImageView>(R.id.detailviewitem_profile_image)
            var detailviewitemimageview_content=viewholder.findViewById<ImageView>(R.id.detailviewitem_imageview_content)
            var detailviewitem_explain_textview=viewholder.findViewById<TextView>(R.id.detailviewitem_explain_textview)
            var detailviewitem_favoritecounter_textview=viewholder.findViewById<TextView>(R.id.detailviewitem_favoritecounter_textview)
            var detailviewitem_favorite_imageview=viewholder.findViewById<ImageView>(R.id.detailviewitem_favorite_imageview)
            //userid
            detailviewitem_profile_textView.text=contentDTOs!![position].userId
            //profile image
            Glide.with(holder.itemView.context).load(contentDTOs!![position].imageUri).into(detailviewitem_profile_image)
            //explain of content
            detailviewitem_explain_textview.text=contentDTOs!![position].explain
            //likes
            detailviewitem_favoritecounter_textview.text="Likes"+contentDTOs!![position].favoriteCount
            //image
            Glide.with(holder.itemView.context).load(contentDTOs!![position].imageUri).into(detailviewitemimageview_content)

            detailviewitem_favorite_imageview.setOnClickListener{
                favoriteEvent(position)
            }
            if(contentDTOs!![position].favorites.containsKey(uid)){
                detailviewitem_favorite_imageview.setImageResource(R.drawable.ic_favorite)
            }else {
                detailviewitem_favorite_imageview.setImageResource(R.drawable.ic_favorite_border)
            }

            //상대방으로 이동?
            detailviewitem_profile_image.setOnClickListener{
                var fragment=UserFragment()
                var bundle=Bundle()
                bundle.putString("destinationUid",contentDTOs[position].uid)
                bundle.putString("userId",contentDTOs[position].userId)
                fragment.arguments=bundle
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.main_content,fragment)?.commit()
            }
        }

        fun favoriteEvent(position: Int){
            var tsDoc=firestore?.collection("images")?.document(contentUidList[position])
            firestore?.runTransaction { transaction->

                var contentDTO=transaction.get(tsDoc!!).toObject(ContentDTO::class.java)
                if(contentDTO!!.favorites.containsKey(uid)){
                    contentDTO?.favoriteCount=contentDTO?.favoriteCount-1
                    contentDTO?.favorites.remove(uid)
                }else{
                    contentDTO?.favoriteCount=contentDTO?.favoriteCount+1
                    contentDTO?.favorites[uid!!]=true
                }
                transaction.set(tsDoc,contentDTO)
            }

        }

        override fun getItemCount(): Int {
            return contentDTOs.size
        }

    }
}