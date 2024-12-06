package com.example.ia08_doodle
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private lateinit var drawView: DrawView
    private lateinit var btnClear: Button
    private lateinit var btnColor: Button
    private lateinit var btnSize: Button
    private lateinit var btnOpacity: Button
    private lateinit var scaleBar: SeekBar
    private lateinit var colorLayout: LinearLayout
    private lateinit var btnRed: Button
    private lateinit var btnOrange: Button
    private lateinit var btnGreen: Button
    private lateinit var btnBlue: Button
    private lateinit var btnPurple: Button
    private lateinit var btnBlack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout)

        //Initialize Variables
        drawView = findViewById(R.id.draw_view)
        btnClear = findViewById(R.id.btn_clear)
        btnColor = findViewById(R.id.btn_color)
        btnSize = findViewById(R.id.btn_size)
        btnOpacity = findViewById(R.id.btn_opacity)
        scaleBar = findViewById(R.id.scale_bar)
        colorLayout = findViewById(R.id.color_layout)
        btnRed = findViewById(R.id.btn_red)
        btnOrange = findViewById(R.id.btn_orange)
        btnGreen = findViewById(R.id.btn_green)
        btnBlue = findViewById(R.id.btn_blue)
        btnPurple = findViewById(R.id.btn_purple)
        btnBlack = findViewById(R.id.btn_black)

        // Clear Function
        btnClear.setOnClickListener {
            drawView.clearCanvas()
            resetTools()
        }

        //Button Control for Color
        btnColor.setOnClickListener {
            toggleColorLayout()
        }

        //Set Up Color's Color Layout Buttons
        btnRed.setOnClickListener { changeBrushColorAndHide(Color.RED) }
        btnOrange.setOnClickListener { changeBrushColorAndHide(Color.rgb(255, 165, 0)) }
        btnGreen.setOnClickListener { changeBrushColorAndHide(Color.GREEN) }
        btnBlue.setOnClickListener { changeBrushColorAndHide(Color.BLUE) }
        btnPurple.setOnClickListener { changeBrushColorAndHide(Color.rgb(128, 0, 128)) }
        btnBlack.setOnClickListener { changeBrushColorAndHide(Color.BLACK) }

        //Changing the Brush Size
        btnSize.setOnClickListener {
            toggleSeekBar()
            scaleBar.progress = drawView.getBrushSize().toInt()
            scaleBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    drawView.changeBrushSize(progress.toFloat())
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    toggleSeekBar()
                }
            })
        }

        //Changing the Opacity
        btnOpacity.setOnClickListener {
            toggleSeekBar()
            scaleBar.progress = drawView.getBrushOpacity() / 25
            scaleBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    val opacity = progress * 25
                    drawView.changeBrushOpacity(opacity)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    toggleSeekBar()
                }
            })
        }

    }

    // Functions to show/hide visibility
    private fun toggleSeekBar() {
        scaleBar.visibility = if (scaleBar.visibility == View.GONE) View.VISIBLE else View.GONE
    }

    private fun toggleColorLayout() {
        colorLayout.visibility = if (colorLayout.visibility == View.GONE) View.VISIBLE else View.GONE
    }

    //Color Button - Change the Color Functionality
    private fun changeBrushColorAndHide(color: Int) {
        drawView.changeBrushColor(color)
        colorLayout.visibility = View.GONE
    }

    //Function to Reset tools to Default Values
    private fun resetTools() {
        drawView.changeBrushColor(Color.BLACK)
        drawView.changeBrushSize(20f)
        drawView.changeBrushOpacity(255)
    }
}