package com.example.closetstack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SaveToBoardBottomSheet : BottomSheetDialogFragment() {

    private val boards = listOf(
        Board("Streetwear", R.drawable.img_post1),
        Board("Fits", R.drawable.img_post2),
        Board("Accessories", R.drawable.img_post3),
        Board("Old Money", R.drawable.img_post4),
        Board("Inspo", R.drawable.img_post5),
        Board("My Closet", R.drawable.img_post6)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.bottom_sheet_save_to_board, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.ivCloseSaveBoard).setOnClickListener { dismiss() }

        val rvBoards = view.findViewById<RecyclerView>(R.id.rvBoards)
        rvBoards.layoutManager = LinearLayoutManager(requireContext())
        rvBoards.adapter = BoardAdapter(boards) { board ->
            Toast.makeText(requireContext(), "Saved to \"${board.name}\"", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        view.findViewById<LinearLayout>(R.id.llCreateBoard).setOnClickListener {
            Toast.makeText(requireContext(), "Create board coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getTheme() = R.style.BottomSheetDarkTheme
}