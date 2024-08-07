package com.example.littlechemist3

import androidx.compose.ui.geometry.Offset

//import android.graphics.drawable.shapes.Shape

/**
 * all data needed for drawing
 * @param point The coordinates
 * @param tb ToolboxItem - What to draw
 * @param Current Is this newest member in chain (defaults false)
 */
class VisualNode (val point:Offset /*val x:Float, val y:Float, val s: Size*/, val tb:ToolBoxItem2, var Current:Boolean=false) // (Size was shape),was val tb:ToolBoxItem

/**
 * Item visible in top toolbox
 * @param text Text
 * @param color Color
 * @param drawableResId Image id
 */
data class ToolBoxItem2(val text:String, val color:Int, val drawableResId: Int) // use image
