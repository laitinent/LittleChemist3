package com.example.littlechemist3

import android.graphics.RectF
import androidx.compose.ui.geometry.Offset

class VisualElements {
    companion object {
        val lista = mutableListOf<VisualNode>()//mutableListOf
        val lines = mutableListOf<RectF>()

        fun Add(shape: VisualNode) {
            lista.add(shape)
            //   invalidate()
        }

        fun SetCurrent(x: Int) {
            lista[x].Current = true
        }

        fun AddLine(start: Offset, end: Offset) {
            lines.add(RectF(start.x, start.y, end.x, end.y))
        }

        /**
         * Reset graph
         */
        fun Clear() {
            lines.clear()
            lista.clear()
            //invalidate()
        }

        fun ResetStrokes() {
            lista.forEach {
                it.Current = false
            }
        }
    }
}