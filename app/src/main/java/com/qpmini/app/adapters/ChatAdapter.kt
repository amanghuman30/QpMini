package com.qpmini.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.qpmini.app.R
import com.qpmini.app.data.models.Messages
import com.qpmini.app.data.models.User
import com.qpmini.app.util.MessageType
import com.qpmini.app.util.timeStampConversionToTime
import de.hdodenhof.circleimageview.CircleImageView

class ChatAdapter(private val participant : User) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private val MSG_TYPE_LEFT_RECEIVED = 0
    private val MSG_TYPE_RIGHT_RECEIVED = 1

    private val differItemCallback = object : DiffUtil.ItemCallback<Messages>() {
        override fun areItemsTheSame(oldItem: Messages, newItem: Messages): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Messages, newItem: Messages): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differItemCallback)

    inner class ChatViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tvChat: TextView = itemView.findViewById(R.id.tv_chat)
        val tvTime: TextView = itemView.findViewById(R.id.tv_chat_time_received)
        val imageCard: CardView? = itemView.findViewById(R.id.card_left_image)
        val leftImage: ImageView? = itemView.findViewById(R.id.iv_image_received)
        val userImage: CircleImageView? = itemView.findViewById(R.id.left_user_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return if (viewType == MSG_TYPE_RIGHT_RECEIVED) {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_item_right_sent, parent, false)
            ChatViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_item_left_received, parent, false)
            ChatViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = differ.currentList[position]
        holder.apply {
            tvTime.text = timeStampConversionToTime(message.timestamp)

            if(differ.currentList[position].senderId == participant.id) {
                tvChat.setTextColor(ContextCompat.getColor(tvChat.context, R.color.black))
                userImage?.let {
                    Glide.with(holder.itemView).load(participant.imageURL).apply(
                        RequestOptions()
                            .placeholder(R.drawable.ic_user_default_white)
                    ).into(it)
                }
                //Received message
                if(message.messageType == MessageType.TEXT) {
                    tvChat.visibility = View.VISIBLE
                    imageCard?.visibility = View.GONE
                    tvChat.text = message.messageBody
                } else {
                    tvChat.visibility = View.GONE
                    imageCard?.visibility = View.VISIBLE
                    tvChat.text = ""
                    leftImage?.let {
                        Glide.with(holder.itemView).load(message.messageBody).apply(
                            RequestOptions()
                                .placeholder(R.drawable.ic_placeholder_img)
                        ).into(it)
                    }
                }
            } else {
                // Sent message
                tvChat.text = message.messageBody
                tvChat.setTextColor(ContextCompat.getColor(tvChat.context, R.color.white))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(differ.currentList[position].senderId == participant.id)
                    MSG_TYPE_LEFT_RECEIVED
                else
                    MSG_TYPE_RIGHT_RECEIVED
    }
}