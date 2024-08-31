package com.example.noteapp.ui.fragments.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentChatBinding
import com.example.noteapp.ui.adapter.ChatAdapter
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore


class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private var chatAdapter = ChatAdapter()
    private val db = Firebase.firestore
    private lateinit var query: Query
    private lateinit var listener: ListenerRegistration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        setupListener()
        observeMessager()

        drawerLayout = binding.drawerLayout
        navView = binding.navigationView

        binding.ivMenu.setOnClickListener {
            drawerLayout.openDrawer(navView)
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Home -> {
                    findNavController().navigate(R.id.action_chatFragment_to_noteAppFragment)
                    drawerLayout.closeDrawer(navView)
                    true
                }
                else -> false
            }
        }

    }

    fun initialize(){
        binding.rvChat.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }
    }

    private fun setupListener() {
        binding.sendBtn.setOnClickListener {
            val user = hashMapOf(
                "name" to binding.etMessage.text.toString()
            )
            db.collection("user").add(user).addOnCompleteListener{}
            binding.etMessage.text.clear()
        }
    }

    private fun observeMessager() {
        query = db.collection("user")
        listener = query.addSnapshotListener{value, error ->
            if(error != null){
                return@addSnapshotListener
            }
            value?.let{snapshot ->
                val messages = mutableListOf<String>()
                for(doc in snapshot.documents){
                    val message = doc.getString("name")
                    message?.let {
                        messages.add(message)
                    }
                }
                chatAdapter.submitList(messages)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        listener.remove()
    }

}