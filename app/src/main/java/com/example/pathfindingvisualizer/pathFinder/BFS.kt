package com.example.path.pathFinder

import com.example.pathfindingvisualizer.pathFinder.PathFinder
import kotlinx.coroutines.delay
import java.util.*

class BFS(rows: Int, cols: Int) {

    private val dist = Array(rows) { IntArray(cols) }
    private val path = Array(rows) { CharArray(cols) }

    suspend fun solve(
        board: Array<IntArray>,
        startX: Int,
        startY: Int,
        endX: Int,
        endY: Int,
        speed: Long
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

        val queue: Queue<Pair<Int, Int>> = LinkedList()

        queue.offer(Pair(startX, startY))
        dist[startX][startY] = 0

        while (!queue.isEmpty()) {
            val currX = queue.element().first
            val currY = queue.element().second
            queue.poll()

            var flag = true

            for (i in 0..3) {
                if (isValid(currX + dx[i], currY + dy[i], path, board)) {
                    if (1 + dist[currX][currY] < dist[currX + dx[i]][currY + dy[i]]) {

                        board[currY + dy[i]][currX + dx[i]] = PathFinder.EXPLORE_HEAD_CELL_CODE

                        delay(speed)

                        when (i) {
                            0 -> {
                                path[currY + dy[i]][currX + dx[i]] = 'U'
                            }
                            1 -> {
                                path[currY + dy[i]][currX + dx[i]] = 'L'
                            }
                            2 -> {
                                path[currY + dy[i]][currX + dx[i]] = 'D'
                            }
                            else -> {
                                path[currY + dy[i]][currX + dx[i]] = 'R'
                            }
                        }
                        if ((currX + dx[i] == endX) && (currY + dy[i] == endY)) {
                            board[currY + dy[i]][currX + dx[i]] = PathFinder.EXPLORE_CELL_CODE
                            flag = false
                            break
                        }
                        dist[currX + dx[i]][currY + dy[i]] = dist[currX][currY] + 1
                        queue.offer(Pair(currX + dx[i], currY + dy[i]))
                        board[currY + dy[i]][currX + dx[i]] = PathFinder.EXPLORE_CELL_CODE
                    }
                }
            }
            if (!flag) {
                break
            }
        }
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