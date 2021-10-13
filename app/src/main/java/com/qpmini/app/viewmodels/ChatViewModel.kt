package com.qpmini.app.viewmodels

import android.content.Context
import androidx.lifecycle.*
import com.qpmini.app.R
import com.qpmini.app.data.models.ChatResponse
import com.qpmini.app.data.models.Chats
import com.qpmini.app.data.models.Messages
import com.qpmini.app.data.models.User
import com.qpmini.app.dispatchers.TaskDispatchers
import com.qpmini.app.repositories.Repository
import com.qpmini.app.util.MessageType
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

    private val _sendMessageStatus = MutableLiveData<Resource<String>>()
    val sendMessageStatus: LiveData<Resource<String>>
        get() = _sendMessageStatus

    private lateinit var creatorId : UUID
    private lateinit var participantId : UUID
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
                participantId = chat.participantId

                val user = chatRepository.getUser(chat.participantId).data

                if(user != null) {
                    _users.postValue(
                        Resource.Success(user)
                    )
                    chatId?.let {
                        fetchMessages(it)
                    }
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
                            handleUserDbSave(user)
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

            val chat = Chats(null, participantId = user.id, creatorId = UUID.randomUUID())
            chatRepository.saveChat(chat)
        }
    }


    /**
     * send message to server
     * based on response and success save it in local db
     * and show in message screen
    * */
    fun sendMessage(message : String) {
        _sendMessageStatus.postValue(Resource.Loading())
        if (networkHelper.isNetworkConnected()) {
            viewModelScope.launch(taskDispatchers.io()) {
                //temporarily save message in list
                val currentTime = Calendar.getInstance().time
                val sMessage = Messages(
                    id = null,
                    chatId = chatId!!,
                    senderId = creatorId,
                    messageType = MessageType.TEXT,
                    messageBody = message,
                    timestamp = currentTime)

                val tempList = _messages.value?.toMutableList()
                tempList?.add(sMessage)
                _messages.postValue(tempList)

                val response = chatRepository.sendMessage(participantId.toString(), message)
                if(response.isSuccessful) {
                    _sendMessageStatus.postValue(Resource.Success(""))
                    saveMessagesInDB(sMessage,response.body())
                } else {
                    _sendMessageStatus.postValue(Resource.Error(response.message(), null))
                }
            }
        } else {
            _sendMessageStatus.postValue(Resource.Error(context.getString(R.string.no_internet_connection), null))
        }

        //refresh message list from db
        chatId?.let {
            fetchMessages(it)
        }
    }

    private fun saveMessagesInDB(sentMessage : Messages, receivedMessage : ChatResponse?) {
        viewModelScope.launch(taskDispatchers.io()) {
            chatId?.let {
                //save sent message
                chatRepository.saveMessage(sentMessage)

                //save received message
                receivedMessage?.let {
                    if(it.body.isNotEmpty() && it.type.isNotEmpty()) {
                        val rTime = Calendar.getInstance().time
                        val rMessage = Messages(
                            id = null,
                            chatId = chatId!!,
                            senderId = participantId,
                            messageType = MessageType.valueOf(it.type),
                            messageBody = it.body,
                            timestamp = rTime)
                        chatRepository.saveMessage(rMessage)
                    }
                }
            }
        }
    }

}