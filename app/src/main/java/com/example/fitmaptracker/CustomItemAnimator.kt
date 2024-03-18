package com.example.fitmaptracker

import android.animation.AnimatorInflater
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView


    class CustomItemAnimator(private val animationRes: Int) : DefaultItemAnimator() {

        override fun animateMove(holder: RecyclerView.ViewHolder, fromX: Int, fromY: Int, toX: Int, toY: Int): Boolean {
            val view = holder.itemView
            fromX.let { fromX ->
                fromY.let { fromY ->
                    toX.let { toX ->
                        toY.let { toY ->
                            val animation = AnimatorInflater.loadAnimator(holder.itemView.context, animationRes)
                            animation.setTarget(view)
                            animation.start()
                        }
                    }
                }
            }
            return true
        }
}