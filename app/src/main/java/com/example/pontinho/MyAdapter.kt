package com.example.pontinho

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_recycler_view.view.*

class MyAdapter(
    private val contactList: ArrayList<User>,
    private val context: Context?,
    var clickListener: OnItemClick
): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val photo: CircleImageView = itemView.playerImage
        val name: TextView = itemView.playerName
        val score: TextView = itemView.playerScore

        fun clickEvent(item: User, action: OnItemClick){

            itemView.findViewById<Button>(R.id.playerAddPoints).setOnClickListener{
                action.onItemClick(item, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val listItem: View = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_view, parent, false)
        return MyViewHolder(listItem)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user: User = contactList.get(position)


        if(user.score >= 100){
            user.score = 0
            user.isGameOver = true

        }

        if(user.isGameOver) holder.itemView.setBackgroundColor(context!!.getColor(R.color.colorLostPlayer))

        holder.name.text = user.name
        holder.score.text = user.score.toString()
        holder.photo.setImageResource(user.photo)

        holder.clickEvent(user, clickListener)
    }

    interface OnItemClick{
        fun onItemClick(user: User, position: Int)
    }

}