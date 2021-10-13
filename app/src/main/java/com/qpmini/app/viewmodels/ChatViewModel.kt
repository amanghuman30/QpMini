package com.qpmini.app.viewmodels

import android.content.Context
import androidx.lifecycle.*
import com.qpmini.app.R
import com.qpmini.app.data.models.Chats
import com.qpmini.app.data.models.Messages
import com.qpmini.app.data.models.User
import com.qpmini.app.dispatchers.TaskDispatchers
import com.qpmini.app.repositories.Repository
import com.qpmini.app.util.NetworkHelper
import com.qpmini.app.util.PARTICIPANT_ID
import com.qpmini.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val chatRepository : Repository,
    private val networkHelper: NetworkHelper,
    private val taskDispatchers: TaskDispatchers
) : ViewModel() {

    private val _users = MutableLiveData<Resource<User>>()
    val user: LiveData<Resource<User>>
        get() = _users

    private val _messages = MutableLiveData<List<Messages>>()
    val messages: LiveData<List<Messages>>
        get() = _messages

    private lateinit var creatorId : UUID
    private var chatId: Int? = null

    init {
        fetchUsers()
    }

    /**
     * Check if chat is available in local db
     * get the participant and get their profile data
     * fetch stored messages in local db for display
     *
     * if not available use repository to fetch details from remote server
    * */
    private fun fetchUsers() {
        viewModelScope.launch(taskDispatchers.io()) {
            _users.postValue(Resource.Loading())
            val chats = chatRepository.getChats().data?.let {
                it
            } ?: emptyList()

            if(chats.isNotEmpty()) {
                val chat = chats[0]
                chatId = chat.id
                creatorId = chat.creatorId

                val user = chatRepository.getUser(chat.participantId).data

                if(user != null) {
                    _users.postValue(
                        Resource.Success(user)
                    )
                    fetchMessages(chat.id)
                } else  {
                    _users.postValue(
                        Resource.Error(context.getString(R.string.error_fetching_user))
                    )
                }
            } else {
                if (networkHelper.isNetworkConnected()) {
                    val userId = UUID.fromString(PARTICIPANT_ID)
                    chatRepository.getUserFromRemote(userId)?.let {
                        val user = it.data

                        if(user != null) {
                            _users.postValue(Resource.Success(user))

                        } else {
                            _users.postValue(Resource.Error("${it.message}"))
                        }
                    }
                } else _users.postValue(Resource.Error(context.getString(R.string.no_internet_connection), null))
            }
        }
    }

    /**
    *load chat message from local database
     */
    private fun fetchMessages(chatId : Int) {
        viewModelScope.launch(taskDispatchers.io()) {
            _messages.postValue(chatRepository.getMessages(chatId).value)
        }
    }

    /**
     * handle saving logic of user in db
     * create new user in db
     * create new chat in db with participant id and creator id
    * */
    private fun handleUserDbSave(user: User) {
        viewModelScope.launch {
            chatRepository.saveUser(user)

            val chat = Chats(id = 1, participantId = user.id, creatorId = UUID.randomUUID())
            chatRepository.saveChat(chat)
        }
    }

}