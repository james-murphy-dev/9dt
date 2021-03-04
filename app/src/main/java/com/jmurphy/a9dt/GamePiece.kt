package com.jmurphy.a9dt

import java.util.*
import kotlin.collections.HashMap

data class GamePiece(
    val row: Int,
    val col: Int,
    val player: Player,
    val order: Int
)

enum class Diagonal{
    //a.k.a northwest -> southeast
    II_IV,
    //a.k.a. northeast -> southwest
    I_III
}

data class Player(
    val cpu: Boolean,
    val color: String,
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
    val playerMoves: MutableList<GamePiece> = mutableListOf(),
    val cpuMoves: MutableList<GamePiece> = mutableListOf(),
    //all column values in sequence
    val allMoves: MutableList<Int> = mutableListOf(),
    var winner: Player? = null

){
    fun addMove(player: Player, piece: GamePiece): List<GamePiece>{
        if (player.cpu){
            cpuMoves.add(piece)
        }
        else{
            playerMoves.add(piece)
        }

        allMoves.add(piece.col)
    }
}