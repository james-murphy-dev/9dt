package com.jmurphy.a9dt

import java.util.*
import kotlin.collections.HashMap

data class GamePiece(
    val row: Int,
    val col: Int,
    val player: Player? = null
)

enum class Diagonal{
    //a.k.a northwest -> southeast
    II_IV,
    //a.k.a. northeast -> southwest
    I_III
}

data class Player(
    val cpu: Boolean,
    val color: Int,
    val rows: HashMap<Int, Int> = HashMap(),
    val columns: HashMap<Int, Int> = HashMap(),
    val diagonals: EnumMap<Diagonal, Int> = EnumMap(Diagonal::class.java)
)

data class Rules(
    val winningRow: Int
)
data class Game(
    val startingMove: Player,
    val height: Int,
    val width: Int,
    val rules: Rules,
    val player: Player,
    val cpu: Player,
    val playerMoves: MutableList<GamePiece> = mutableListOf(),
    val cpuMoves: MutableList<GamePiece> = mutableListOf(),
    //all column values in sequence
    val allMoves: MutableList<Int> = mutableListOf(),
    var winner: Player? = null,
    var draw: Boolean? = null
){
    fun addMove(piece: GamePiece){
        if (piece.player?.cpu!!){
            cpuMoves.add(piece)
        }
        else{
            playerMoves.add(piece)
        }

        allMoves.add(piece.col)
    }

    fun getRowForDroppedPiece(column: Int): Int {
        return allMoves.filter { it==column }.size
    }
}