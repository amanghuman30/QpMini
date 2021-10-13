package com.qpmini.app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.qpmini.app.databinding.ActivityChatBinding
import com.qpmini.app.util.Resource
import com.qpmini.app.viewmodels.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChatBinding

    val chatViewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        chatViewModel.user.observe(this, {
            when(it) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE

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