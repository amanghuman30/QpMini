package com.qpmini.app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.qpmini.app.R
import com.qpmini.app.adapters.ChatAdapter
import com.qpmini.app.databinding.ActivityChatBinding
import com.qpmini.app.util.Resource
import com.qpmini.app.util.hideSoftKeyBoard
import com.qpmini.app.viewmodels.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChatBinding

    private val chatViewModel: ChatViewModel by viewModels()
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        addObservers()

        binding.apply {
            sendButton.setOnClickListener {
                val message = etChat.text.toString()
                if(message.isNotEmpty()) {
                    chatViewModel.sendMessage(message)
                }
                etChat.setText("")
                hideSoftKeyBoard(it)
            }
        }
    }

    /**
     * Add observers to user fetching, Message list updates and send message status
    * */
    private fun addObservers() {
        chatViewModel.user.observe(this, {
            when(it) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.apply {
                        it.data?.let { user ->
                            headText.text = user.name
                            Glide.with(root).load(user.imageURL).apply(RequestOptions()
                                .placeholder(R.drawable.ic_user_default_white)
                            ).into(headImage)

                            chatAdapter = ChatAdapter(user)
                            chatRV.apply {
                                adapter = chatAdapter
                                layoutManager = LinearLayoutManager(this@ChatActivity)
                            }
                        }
                    }
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root,"${it.message}",Snackbar.LENGTH_SHORT).show()
                }
            }
        })

        chatViewModel.messages.observe(this, {
            it?.let {
                chatAdapter.differ.submitList(it)
                if(it.isNotEmpty())
                    binding.chatRV.smoothScrollToPosition(it.size-1)
            }
        })

        chatViewModel.sendMessageStatus.observe(this, {
            when(it) {
                is Resource.Success -> {
                }
                is Resource.Loading -> {
                }
                is Resource.Error -> {
                    Snackbar.make(binding.root,"${it.message}",Snackbar.LENGTH_SHORT).show()
                }
            }
        })
    }
}