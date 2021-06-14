package com.example.pathfindingvisualizer

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.pathfindingvisualizer.pathBoard.PathGrid
import com.example.pathfindingvisualizer.pathFinder.PathFinder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var pathGrid: PathGrid
    private var finder = PathFinder()
    private var speed: Long = 0
    private var algorithmToApply: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setSpeedSpinner()
        setAlgoSpinner()

        pathGrid = findViewById(R.id.pathGrid)
        finder = pathGrid.getFinder()

        val btnSolve = findViewById<Button>(R.id.btn_solve)
        val btnReset = findViewById<Button>(R.id.btn_reset)

        btnSolve.setOnClickListener {

            // setting solving status to true , so that
            // user can't manipulate the gird while opeartions
            pathGrid.setSolving(true)


            //Since UI needs to be updated and work is less,
            //performing the operation in Main Thread
            GlobalScope.launch(Dispatchers.Main) {

                btnSolve.isEnabled = false
                btnSolve.setBackgroundColor(Color.parseColor("#DFBF9C"))
                btnSolve.setTextColor(Color.parseColor("#000000"))

                btnReset.isEnabled = false
                btnReset.setBackgroundColor(Color.parseColor("#9D9FA2"))

                when (algorithmToApply) {
                    BFS_ALGO -> {
                        val found = finder.solveBFS(speed)
                        makeToast(found)
                    }

                    else -> {
                        val found = finder.solveDFS(speed)
                        makeToast(found)
                    }
                }

                btnSolve.isEnabled = true
                btnSolve.setBackgroundColor(Color.parseColor("#333333"))
                btnSolve.setTextColor(Color.parseColor("#FFFFFF"))
                btnReset.isEnabled = true
                btnReset.setBackgroundColor(Color.parseColor("#333333"))
            }
        }

        btnReset.setOnClickListener {
            pathGrid.setSolving(false)
            finder.resetGrid()
            pathGrid.invalidate()
        }

    }

    private fun makeToast(found: Boolean) {
        if (found) {
            val toast = Toast.makeText(this@MainActivity, "Path Found.", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.BOTTOM, 0, -200)
            toast.show()
        } else {
            val toast = Toast.makeText(this@MainActivity, "No Path Found.", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.BOTTOM, 0, -200)
            toast.show()
        }
    }

    private fun setAlgoSpinner() {
        val algoSpinner: Spinner = findViewById(R.id.algo_spinner)
        algoSpinner.onItemSelectedListener = this
        ArrayAdapter.createFromResource(
            this,
            R.array.algo_array,
            R.layout.spinner_list_text
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_list_style)
            algoSpinner.adapter = adapter
        }
    }

    private fun setSpeedSpinner() {
        val speedSpinner: Spinner = findViewById(R.id.speed_spinner)
        speedSpinner.onItemSelectedListener = this
        ArrayAdapter.createFromResource(
            this,
            R.array.speed_array,
            R.layout.spinner_list_text
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_list_style)
            speedSpinner.adapter = adapter
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent!!.id) {
            R.id.speed_spinner -> {
                speed = when (parent.selectedItem.toString()) {
                    "Fast" -> {
                        FAST
                    }
                    "Average" -> {
                        AVG
                    }
                    else -> {
                        SLOW
                    }
                }
            }

            R.id.algo_spinner -> {
                algorithmToApply = when (parent.selectedItem.toString()) {
                    "BFS" -> {
                        BFS_ALGO
                    }
                    else -> {
                        DFS_ALGO
                    }

                }
            }
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    companion object {
        const val FAST: Long = 30
        const val AVG: Long = 60
        const val SLOW: Long = 120

        const val BFS_ALGO = 1
        const val DFS_ALGO = 2
    }
}