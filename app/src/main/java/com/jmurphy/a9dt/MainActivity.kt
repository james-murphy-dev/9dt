package com.jmurphy.a9dt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
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
    private lateinit var background: ConstraintLayout

    private lateinit var gameOverScreen: Group
    private lateinit var newGameScreen: Group


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gameBoard = findViewById(R.id.game_board)
        background = findViewById(R.id.text_background)

        gameOverScreen = findViewById(R.id.game_over)
        newGameScreen = findViewById(R.id.new_game)

        val playerColor = applicationContext.getColor(R.color.player_color)

        val cpuColor = applicationContext.getColor(R.color.cpu_color)

        val resetBtn = background.findViewById<TextView>(R.id.reset_btn)
        resetBtn.setOnClickListener {
            //start new game
            newGame()
        }

        val playerStartBtn = background.findViewById<TextView>(R.id.player_start_btn)
        val cpuStartBtn = background.findViewById<TextView>(R.id.cpu_start_btn)

        playerStartBtn.setOnClickListener {
            val startingMove = Player(false, playerColor)
            resetGameBoard(startingMove)
        }

        cpuStartBtn.setOnClickListener {
            val startingMove = Player(true, cpuColor)
            resetGameBoard(startingMove)
        }

        viewModel.getGameData().observe(this, { gameBoard ->
            if (gameBoard.winner!=null){
                //winner declared
                background.visibility = View.VISIBLE

                gameOverScreen.visibility = View.VISIBLE

                val winLabel = findViewById<TextView>(R.id.win_label)
                val color = gameBoard.winner?.color!!
                winLabel.setTextColor(color)

                findViewById<TextView>(R.id.draw_label).visibility = View.GONE
                winLabel.visibility = View.VISIBLE

            }
            else if (gameBoard.draw!=null && gameBoard.draw!!){
                gameOverScreen.visibility = View.VISIBLE
                background.visibility = View.VISIBLE
                findViewById<TextView>(R.id.win_label).visibility = View.GONE
                findViewById<TextView>(R.id.draw_label).visibility = View.VISIBLE

            }
        })

        viewModel.getCpuMove().observe(this, {
            dropGamePiece(it, adapters[it.col])
        })

        setupGameBoard()

    }

    private fun setupGameBoard() {
        viewModel.createGame()

        val col1 = gameBoard.findViewById<RecyclerView>(R.id.col_1_rv)
        val col2 = gameBoard.findViewById<RecyclerView>(R.id.col_2_rv)
        val col3 = gameBoard.findViewById<RecyclerView>(R.id.col_3_rv)
        val col4 = gameBoard.findViewById<RecyclerView>(R.id.column_4)
        val columns: List<RecyclerView> = listOf(col1, col2, col3, col4)

        val height = applicationContext.getString(R.string.default_height)

        columns.forEachIndexed{ index, column ->
            val adapter = GameBoardColumnAdapter(index, Integer.valueOf(height))
            val layoutManager = LinearLayoutManager(applicationContext)
            layoutManager.reverseLayout = true
            column.adapter = adapter
            column.layoutManager = layoutManager

            val viewGroup: ViewGroup = column.parent as ViewGroup

            val columnBtn = viewGroup.getChildAt(1)

            columnBtn.setOnClickListener{

                if (adapter.getFilledRows()!=adapter.height){
                    val row = adapter.getFilledRows()
                    val player = viewModel.getPlayer()
                    val gamePiece = GamePiece(row, index, player)

                    dropGamePiece(gamePiece, adapter)

                    gameBoard.isEnabled = false
                }
            }
            adapters.add(adapter)
        }

        newGame()
    }

    private fun resetGameBoard(startingPlayer: Player){
        adapters.forEach{
            it.clearColumn()
        }

        background.visibility = View.GONE
        newGameScreen.visibility = View.GONE

        viewModel.restartGame(startingPlayer)
    }

    private fun dropGamePiece(piece: GamePiece, adapter: GameBoardColumnAdapter) {
        adapter.addPiece(piece)
        viewModel.addPiece(piece)
        gameBoard.isEnabled = true
    }

    private fun gameBoardFilled(): Boolean {

        return adapters.filter {  it.getFilledRows()==it.itemCount }.size==adapters.size
    }


    private fun newGame() {
        gameOverScreen.visibility = View.GONE
        newGameScreen.visibility = View.VISIBLE
    }


}