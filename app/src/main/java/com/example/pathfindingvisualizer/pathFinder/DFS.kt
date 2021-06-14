package com.example.path.pathFinder

import com.example.pathfindingvisualizer.pathFinder.PathFinder
import kotlinx.coroutines.delay

class DFS(rows: Int, cols: Int) {

    private val dist = Array(rows) { IntArray(cols) }
    private val path = Array(rows) { CharArray(cols) }


    suspend fun solve(
        speed: Long,
        startX: Int,
        startY: Int,
        endX: Int,
        endY: Int,
        board: Array<IntArray>,
    ) {

        for (i in 1..15) {
            for (j in 1..15) {
                dist[i][j] = 999
            }
        }
        for (i in 1..15) {
            for (j in 1..15) {
                path[i][j] = 'O'
            }
        }
        val dx = arrayListOf(0, 1, 0, -1)
        val dy = arrayListOf(1, 0, -1, 0)

        dist[startX][startY] = 0

        dfs(startX, startY, endX, endY, dx, dy, speed, board)

    }


    private suspend fun dfs(
        startX: Int,
        startY: Int,
        endX: Int,
        endY: Int,
        dx: ArrayList<Int>,
        dy: ArrayList<Int>,
        speed: Long,
        board: Array<IntArray>
    ): Boolean {

        for (i in 0..3) {

            if (isValid(startX + dx[i], startY + dy[i], path, board)) {
                if (1 + dist[startX][startY] < dist[startX + dx[i]][startY + dy[i]]) {
                    board[startY + dy[i]][startX + dx[i]] = PathFinder.EXPLORE_HEAD_CELL_CODE
                    delay(speed)
                    when (i) {
                        0 -> {
                            path[startY + dy[i]][startX + dx[i]] = 'U'
                        }
                        1 -> {
                            path[startY + dy[i]][startX + dx[i]] = 'L'
                        }
                        2 -> {
                            path[startY + dy[i]][startX + dx[i]] = 'D'
                        }
                        else -> {
                            path[startY + dy[i]][startX + dx[i]] = 'R'
                        }
                    }
                    if ((startX + dx[i] == endX) && (startY + dy[i] == endY)) {
                        board[startY + dy[i]][startX + dx[i]] = PathFinder.EXPLORE_CELL_CODE
                        return true
                    }
                    dist[startX + dx[i]][startY + dy[i]] = dist[startX][startY] + 1
                    board[startY + dy[i]][startX + dx[i]] = PathFinder.EXPLORE_CELL_CODE
                    if (dfs(startX + dx[i], startY + dy[i], endX, endY, dx, dy, speed, board))
                        return true
                }
            }
        }

        board[startY][startX] = PathFinder.END_CELL_CODE
        delay(15)
        board[startY][startX] = PathFinder.EXPLORE_CELL_CODE
        return false
    }

    private fun isValid(
        i: Int,
        j: Int,
        path: Array<CharArray>,
        board: Array<IntArray>
    ): Boolean {
        if (i >= 1 && j >= 1 && i <= 15 && j <= 15 && path[j][i] == 'O' && board[j][i] != PathFinder.OBSTACLE_CELL_CODE) {
            return true
        }
        return false
    }

    fun getPath(): Array<CharArray> {
        return path
    }

}