package com.jmurphy.a9dt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val viewModel: GameViewModel by viewModels()

    private val adapters = mutableListOf<GameBoardColumnAdapter>()

    private lateinit var gameBoard: ConstraintLayout
    private lateinit var background: RelativeLayout

    private lateinit var gameOverScreen: Group
    private lateinit var newGameScreen: Group

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gameBoard = findViewById(R.id.game_board)
        background = findViewById(R.id.text_background)

        gameOverScreen = findViewById(R.id.game_over)
        newGameScreen = findViewById(R.id.new_game)

        val resetBtn = background.findViewById<TextView>(R.id.reset_btn)
        resetBtn.setOnClickListener {
            //start new game
            newGame()
        }

        val playerStartBtn = background.findViewById<TextView>(R.id.player_start_btn)
        val cpuStartBtn = background.findViewById<TextView>(R.id.cpu_start_btn)

        playerStartBtn.setOnClickListener {
            val color = Integer.toHexString(applicationContext.getColor(R.color.player_color))
            val startingMove = Player(false, color)

            viewModel.restartGame(startingMove)
            background.visibility = View.GONE

            resetGameBoard(startingMove)
        }

        cpuStartBtn.setOnClickListener {
            val color = Integer.toHexString(applicationContext.getColor(R.color.cpu_color))
            val player = Player(true, color)

            viewModel.restartGame(player)
            background.visibility = View.GONE

            resetGameBoard(player)
        }

        setupGameBoard()


        viewModel.getGameData().observe(this, { gameBoard ->
            if (gameBoard.winner!=null){
                //winner declared
                background.visibility = View.VISIBLE
                gameOverScreen.visibility = View.VISIBLE
            }
        })

        viewModel.getCpuMove().observe(this, {
            dropGamePiece(it, adapters[it.col])
        })
    }

    private fun setupGameBoard() {

        val col1 = gameBoard.findViewById<RecyclerView>(R.id.column_1)
        val col2 = gameBoard.findViewById<RecyclerView>(R.id.column_2)
        val col3 = gameBoard.findViewById<RecyclerView>(R.id.column_3)
        val col4 = gameBoard.findViewById<RecyclerView>(R.id.column_4)

        val columns: List<RecyclerView> = listOf(col1, col2, col3, col4)

        val height = applicationContext.getString(R.string.default_height)
        columns.forEachIndexed{ index, column ->
            val adapter = GameBoardColumnAdapter(index, Integer.valueOf(height))
            val layoutManager = LinearLayoutManager(applicationContext)
            layoutManager.reverseLayout = true
            column.adapter = adapter
            column.layoutManager = layoutManager

            column.setOnClickListener{
                val row = adapter.itemCount - 1
                val player = viewModel.getPlayer()
                val gamePiece = GamePiece(row, index, player)

                dropGamePiece(gamePiece, adapter)
            }

            adapters.add(adapter)
        }
    }

    private fun resetGameBoard(startingPlayer: Player){
        adapters.forEach{
            it.clearColumn()
        }

        viewModel.restartGame(startingPlayer)
    }

    private fun dropGamePiece(piece: GamePiece, adapter: GameBoardColumnAdapter) {
        adapter.addPiece(piece)
        viewModel.addPiece(piece)
    }


    private fun newGame() {
        gameOverScreen.visibility = View.GONE
        newGameScreen.visibility = View.VISIBLE
    }


}