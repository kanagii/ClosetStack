package com.example.closetstack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BoardAdapter(
    private val boards: List<Board>,
    private val onBoardClick: (Board) -> Unit
) : RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() {

    inner class BoardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivBoardCover: ImageView = view.findViewById(R.id.ivBoardCover)
        val tvBoardName: TextView = view.findViewById(R.id.tvBoardName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_board, parent, false)
        return BoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        val board = boards[position]
        holder.ivBoardCover.setImageResource(board.coverImageRes)
        holder.tvBoardName.text = board.name
        holder.itemView.setOnClickListener { onBoardClick(board) }
    }

    override fun getItemCount() = boards.size
}