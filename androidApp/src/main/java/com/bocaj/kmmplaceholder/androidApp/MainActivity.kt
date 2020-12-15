package com.bocaj.kmmplaceholder.androidApp

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.bocaj.kmmplaceholder.androidApp.databinding.SingleTextItemLayoutBinding
import com.bocaj.kmmplaceholder.shared.SharedApi
import com.bocaj.kmmplaceholder.shared.model.User
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    // Example savedStateHandle usage -- allows reading values from the activity/fragment
//    val selectedUserId: String = savedStateHandle["uid"] ?: throw IllegalArgumentException("Missing User Id")

//    val currentName: MutableLiveData<String> by lazy {
//        MutableLiveData<String>()
//    }
    private val mUserList = MutableLiveData<List<User>>(emptyList())
    val userList: LiveData<List<User>> = mUserList
//    fun getUsers(): LiveData<List<User>> {
//        return userList
//    }

    fun loadUsers() {
        viewModelScope.launch {
            val users = SharedApi.getUsers()
            println(users)
            mUserList.value = users
        }
    }
}

class MainActivity : AppCompatActivity() {

//    val viewModel: MainViewModel by viewModels()
    private val viewModel: MainViewModel by viewModels()

    private val itemLayout = { parent: ViewGroup, _: Int ->
        SingleTextItemLayoutBinding.inflate(this.layoutInflater, parent, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.loadUsers()
        val tv: TextView = findViewById(R.id.text_view)
        val rv: RecyclerView = findViewById(R.id.rv_user_list)

        rv.bind2(viewModel.userList.value ?: listOf())
                .map (itemLayout, { _, _ -> true }) { item ->
                    this as SingleTextItemLayoutBinding
                    this.tvItemTitle.text = item.name ?: "UNKNOWN, Unknown"
                }

        viewModel.userList.observe(this) { users ->
            rv.update2(users)
//            tv.text = users.joinToString("\n") { it.name }
        }

    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }
}
