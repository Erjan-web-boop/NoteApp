package com.example.noteapp.ui.fragments.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.App
import com.example.noteapp.R
import com.example.noteapp.data.models.NoteModel
import com.example.noteapp.databinding.FragmentNoteAppBinding
import com.example.noteapp.interfaces.OnClickItem
import com.example.noteapp.ui.adapter.NoteAdapter
import com.example.noteapp.utils.PreferenceHelper
import com.google.android.material.navigation.NavigationView


class NoteAppFragment : Fragment(),OnClickItem{

    private lateinit var binding: FragmentNoteAppBinding
    private val noteAdapter = NoteAdapter(this,this)
    private var isLinearLayout = true
    private lateinit var sharedPreferences: PreferenceHelper
    private var isIcon1 = true
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoteAppBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = PreferenceHelper().apply {
            unit(requireContext())}
        initialize()
        setupListener()
        getData()

        drawerLayout = binding.drawerLayout
        navView = binding.navigationView
        binding.ivMenu.setOnClickListener {
            drawerLayout.openDrawer(navView)
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.chat -> {
                    findNavController().navigate(R.id.action_noteAppFragment_to_chatFragment)
                    drawerLayout.closeDrawer(navView)
                    true
                }
                else -> false
            }
        }

        binding.ivShape.setOnClickListener{
            toggleLayout()
            toggleIcon()
        }

        if (sharedPreferences.layout) {
            binding.rvNote.layoutManager = LinearLayoutManager(requireContext())
        } else {
            binding.rvNote.layoutManager = GridLayoutManager(requireContext(), 2)
        }

    }


    private fun toggleIcon(){
        if(isIcon1){
            binding.ivShape.setImageResource(R.drawable.shape2)
        }else{
            binding.ivShape.setImageResource(R.drawable.shape)
        }
        isIcon1 = !isIcon1
    }

    private fun toggleLayout() {
        if (sharedPreferences.layout) {
            binding.rvNote.layoutManager = GridLayoutManager(requireContext(), 2)
            sharedPreferences.layout = false
        } else {
            binding.rvNote.layoutManager = LinearLayoutManager(requireContext())
            sharedPreferences.layout = true
        }
        isLinearLayout = !isLinearLayout
    }

    private fun initialize() {
        binding.rvNote.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = noteAdapter
        }
    }

    private fun setupListener() {
        binding.btnPlus.setOnClickListener{
            findNavController().navigate(R.id.action_noteAppFragment_to_noteDetailFragment)
        }
    }
    private fun getData() {
        App.appDataBase?.noteDao()?.getAll()?.observe(viewLifecycleOwner){
            noteAdapter.submitList(it)
        }
    }

    override fun onLongClick(noteModel: NoteModel) {
        val builder = AlertDialog.Builder(requireContext())
        with(builder){
            setTitle("Вы действительно хотите удалить?")
            setPositiveButton("Да"){dialog, _ ->
                App.appDataBase?.noteDao()?.deleteNote(noteModel)
            }
            setNegativeButton("Нет"){ dialog, _ ->
                dialog.cancel()
            }
            show()
        }
        builder.create()
    }

    override fun onClick(noteModel: NoteModel) {
            val action = NoteAppFragmentDirections.actionNoteAppFragmentToNoteDetailFragment(noteModel.id)
            findNavController().navigate(action)
    }



}