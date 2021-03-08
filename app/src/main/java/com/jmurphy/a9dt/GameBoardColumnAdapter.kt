package com.jmurphy.a9dt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class GameBoardColumnAdapter(val column: Int, val height: Int): RecyclerView.Adapter<GamePieceViewHolder>() {

    private var values = createColumn()

    private fun createColumn(): MutableList<GamePiece>{
        val column = MutableList(height){ index ->
            GamePiece(index+1, column)
        }

        return column
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamePieceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.empty_space, parent, false)
        return GamePieceViewHolder(view)
    }


    override fun onBindViewHolder(holder: GamePieceViewHolder, position: Int) {
        val item = values[position]

        if (item.player?.cpu == true){
            holder.emptyBackground.visibility = View.INVISIBLE
            holder.cpuBackground.visibility = View.VISIBLE
        }
        else if (item.player?.cpu == false){
            holder.emptyBackground.visibility = View.INVISIBLE
            holder.playerBackground.visibility = View.VISIBLE
        }

    }

    override fun getItemCount(): Int {
        return values.size
    }

    fun getFilledRows(): Int {
        return values.filter { it.player!=null }.size
    }

    fun addPiece(piece: GamePiece){
        values[piece.row] = piece
        notifyDataSetChanged()
    }

    fun clearColumn() {
        values = createColumn()
        notifyDataSetChanged()
    }
}


class GamePieceViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val emptyBackground = view.findViewById<ImageView>(R.id.empty_space)
    val playerBackground = view.findViewById<ImageView>(R.id.player_piece)
    val cpuBackground = view.findViewById<ImageView>(R.id.cpu_piece)

}
