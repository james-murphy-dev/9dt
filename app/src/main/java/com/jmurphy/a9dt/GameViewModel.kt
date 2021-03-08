package com.jmurphy.a9dt

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

class GameViewModel(context: Application): AndroidViewModel(context) {

    private val repo = GameRepository(context)
    private val gameLiveData = MutableLiveData<Game>()

    private var winningRow = Integer.valueOf(context.getString(R.string.default_winning_row))
    private var rules = getDefaultRules()

    private fun getDefaultRules(): Rules {
        return Rules(winningRow)
    }

    val playerColor = context.getColor(R.color.player_color)
    val cpuColor = context.getColor(R.color.cpu_color)

    private lateinit var game : Game
    private lateinit var player: Player
    private lateinit var cpu: Player

    fun getGameData(): LiveData<Game> {
        return gameLiveData
    }

    fun createGame(){
        player = Player(false, playerColor)
        cpu = Player(true, cpuColor)

    }

    fun restartGame(startingPlayer: Player){
        restartGame(startingPlayer, getDefaultRules())
    }

    fun restartGame(startingPlayer: Player, rules: Rules) {

        val height = getApplication<Application>().getString(R.string.default_height)
        val width = getApplication<Application>().getString(R.string.default_width)

        player.rows.clear()
        player.columns.clear()
        player.diagonals.clear()

        cpu.rows.clear()
        cpu.columns.clear()
        cpu.diagonals.clear()

        game = Game(startingPlayer, height.toInt(), width.toInt(), rules, player, cpu)

        if (startingPlayer.cpu){
            repo.sendPlayerMove(game.allMoves)
        }
    }

    fun addPiece(newPiece: GamePiece){

        val player = newPiece.player!!
        //horizontal
        var piecesInRow = player.rows.getOrDefault(newPiece.row, 0)
        piecesInRow++
        player.rows.put(newPiece.row, piecesInRow)

        //veritcal
        var piecesInColumn = player.columns.getOrDefault(newPiece.col, 0)
        piecesInColumn++
        player.columns.put(newPiece.col, piecesInColumn)

        //diagonal
        var diagonalPiecesItoIII = player.diagonals.getOrDefault(Diagonal.I_III, 0)
        if (newPiece.col==newPiece.row){
            //I-III quadrant line
            diagonalPiecesItoIII++
            player.diagonals.put(Diagonal.I_III, diagonalPiecesItoIII)
        }

        var diagonalPiecesIItoIV = player.diagonals.getOrDefault(Diagonal.II_IV, 0)
        if (newPiece.col - 1 == game.height - newPiece.row){
            //II-IV quadrant line
            diagonalPiecesIItoIV++
            player.diagonals.put(Diagonal.II_IV, diagonalPiecesIItoIV)
        }

       game.addMove(newPiece)

        //check for draw
        if (game.allMoves.size == game.height * game.width){
            game.draw = true
            gameLiveData.value = game
        }
        //check for winning move
        else if (piecesInRow == game.rules.winningRow ||
            piecesInColumn == game.rules.winningRow ||
            diagonalPiecesIItoIV == game.rules.winningRow ||
            diagonalPiecesItoIII == game.rules.winningRow){

            setWinner(player)
        }
        else if (!player.cpu){
            repo.sendPlayerMove(game.allMoves)
        }

    }

    fun setWinner(player: Player?) {
        game.winner = player
        gameLiveData.value = game
    }

    fun getCpuMove(): LiveData<GamePiece> {
        return Transformations.map(repo.movesListLiveData) { movesList ->
            val cpuLatestMove = movesList.last()

            val row = game.getRowForDroppedPiece(cpuLatestMove)
            val col = cpuLatestMove

            val piece = GamePiece(row, col, game.cpu)

            //addPiece(piece)

            piece
        }
    }

    fun getPlayer(): Player {
        return game.player
    }


}