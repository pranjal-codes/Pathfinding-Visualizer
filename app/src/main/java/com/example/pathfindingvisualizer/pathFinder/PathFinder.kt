package com.example.pathfindingvisualizer.pathFinder

import com.example.path.pathFinder.BFS
import com.example.path.pathFinder.DFS
import kotlinx.coroutines.delay

class PathFinder {
    var startX = 1
    var startY = 1
    var endX = 15
    var endY = 15
    var obstacleX = 8
    var obstacleY = 8


    private val rows = 16
    private val cols = 16

    val board = Array(rows) { IntArray(cols) }

    init {
        board[1][1] = START_CELL_CODE //for Start
        board[8][8] = OBSTACLE_CELL_CODE //for obstacle
        board[15][15] = END_CELL_CODE //for End
    }

    companion object {
        const val EMPTY_CELL_CODE = 0
        const val START_CELL_CODE = 1
        const val OBSTACLE_CELL_CODE = 2
        const val END_CELL_CODE = 3
        const val EXPLORE_CELL_CODE = 4
        const val EXPLORE_HEAD_CELL_CODE = 5
        const val FINAL_PATH_CELL_CODE = 6
    }

    fun resetGrid() {

        for (i in 1..15) {
            for (j in 1..15) {
                board[i][j] = EMPTY_CELL_CODE
            }
        }
        startX = 1
        startY = 1
        endX = 15
        endY = 15

        obstacleX = 8
        obstacleY = 8

        board[1][1] = START_CELL_CODE //for Start
        board[8][8] = OBSTACLE_CELL_CODE //for obstacle
        board[15][15] = END_CELL_CODE //for End
        clearCells()
    }

    suspend fun solveBFS(speed: Long): Boolean {
        clearCells()

        val bfs = BFS(rows, cols)
        bfs.solve(board, startX, startY, endX, endY, speed)
        return drawSolution(bfs.getPath())
    }

    suspend fun solveDFS(speed: Long): Boolean {
        clearCells()

        val dfs = DFS(rows, cols)
        dfs.solve(speed, startX, startY, endX, endY, board)

        return drawSolution(dfs.getPath())

    }

    private suspend fun drawSolution(path: Array<CharArray>): Boolean {
        var i = endX
        var j = endY

        if(path[j][i]=='O') return false
        while (path[j][i] != 'O') {
            delay(20)
            when {
                path[j][i] == 'U' -> {
                    j--
                }
                path[j][i] == 'D' -> {
                    j++
                }
                path[j][i] == 'L' -> {
                    i--
                }
                else -> {
                    i++
                }
            }
            board[j][i] = FINAL_PATH_CELL_CODE
        }
        return true
    }

    private fun clearCells() {
        for (i in 1..15) {
            for (j in 1..15) {
                if (board[i][j] == EXPLORE_CELL_CODE ||
                    board[i][j] == EXPLORE_HEAD_CELL_CODE ||
                    board[i][j] == FINAL_PATH_CELL_CODE
                ) {
                    board[i][j] = EMPTY_CELL_CODE
                }
            }
        }
    }


}