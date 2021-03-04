package com.jmurphy.a9dt

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class GameViewModel(context: Application): AndroidViewModel(context) {

    private val repo = GameRepository()
    private val gameLiveData = MutableLiveData<Game>()

    val playerColor = context.getColor(R.color.player_color)
    val cpuColor = context.getColor(R.color.cpu_color)

    private lateinit var game : Game
    private lateinit var player: Player
    private lateinit var cpu: Player

    fun getGameData(): LiveData<Game> {
        return gameLiveData
    }

    fun createGame(){
        val playerColorHex = Integer.toHexString(playerColor)
        val cpuColorHex = Integer.toHexString(cpuColor)
        player = Player(false, playerColorHex)
        cpu = Player(true, cpuColorHex)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun restartGame(startingPlayer: Player, rules: Rules) {

        val height = getApplication<Application>().getString(R.string.default_height)
        val width = getApplication<Application>().getString(R.string.default_width)

        player.rows.clear()
        player.columns.clear()

        cpu.rows.clear()
        cpu.columns.clear()

        game = Game(startingPlayer, height.toInt(), width.toInt(), rules)
    }

    fun addPiece(player: Player, row: Int, col: Int){


        val newPiece = GamePiece(row, col, player)

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
            //I_III quadrant line
            diagonalPiecesItoIII++
            player.diagonals.put(Diagonal.I_III, diagonalPiecesItoIII)
        }

        var diagonalPiecesIItoIV = player.diagonals.getOrDefault(Diagonal.II_IV, 0)
        if (newPiece.col - 1 == game.height - newPiece.row){
            //II_IV quadrant line
            diagonalPiecesIItoIV++
            player.diagonals.put(Diagonal.II_IV, diagonalPiecesIItoIV)
        }

        val moves: List<GamePiece> = game.addMove(player, newPiece)

        //check for winning move
        if (piecesInRow == game.rules.winningRow ||
            piecesInColumn == game.rules.winningRow ||
            diagonalPiecesIItoIV == game.rules.winningRow ||
            diagonalPiecesItoIII == game.rules.winningRow){

            setWinner(player)
        }
        else{
            repo.sendPlayerMove(game.allMoves)
        }

    }

    private fun setWinner(player: Player) {
        game.winner = player
        gameLiveData.value = game
    }

    private fun winnerFound(newPiece: GamePiece, previousMoves: MutableList<GamePiece>): Boolean {
        val rows = HashMap<Int, Int>()
        val columns = HashMap<Int, Int>()


        previousMoves.asReversed().forEachIndexed(){ index, gamePiece ->
            if (gamePiece.col==newPiece.col){
                var piecesInColumn = columns.
                columns.put(g)
            }

            if (gamePiece.row == newPiece.row){

                var piecesInRow = rows.getOrDefault(gamePiece.row, 0)
                piecesInRow++

                rows.put(gamePiece.row, piecesInRow)
            }
        }
        //check diagonally
        if (lastPiece.row== 0 || lastPiece.row==){

        }

        //check horizontally

        //check vertically
    }


}