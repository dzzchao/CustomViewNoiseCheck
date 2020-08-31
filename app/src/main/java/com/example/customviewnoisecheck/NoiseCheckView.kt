package com.example.customviewnoisecheck

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.Log
import android.view.View

fun Int.sp2px(): Int {
    val fontScale = Resources.getSystem().displayMetrics.scaledDensity
    return (this * fontScale + 0.5f).toInt()
}

fun Int.dp2px(): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

fun Int.px2dp(): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (this / scale + 0.5f).toInt()
}

/**
 * 设置时间 - 默认3s
 *
 * 设置开始动画
 * 设置开始数字
 *
 */
class NoiseCheckView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mainPaint = Paint()
    private val shortPaint = Paint()
    private val textPaint = Paint()

    var circleOutColor = Color.GREEN
    var circleInColor = Color.GREEN
    var mDecibel = ""


    fun setDecibel(decibel: String) {
        mDecibel = decibel
    }

    /**
     * @param decibelResult 结果分贝值
     * @param decibelQualified 合格分贝值
     * @param inColor 错误的情况下内环颜色
     * @param outColor 错误的情况下外环颜色
     */
    fun setDecibelResult(decibelResult: Int, decibelQualified: Int, inColor: Int, outColor: Int) {
        //判断如果大于多少就变颜色
        if (decibelResult > decibelQualified) {
            //不合格
            circleOutColor = Color.RED
            circleInColor = Color.RED
        } else {
            //合格
            circleOutColor = Color.GREEN
            circleInColor = Color.GREEN
        }
        mDecibel = decibelResult.toString()
        circleInColor = inColor
        circleOutColor = outColor
        invalidate()
    }


    init {
        circleOutColor = Color.GREEN
        circleInColor = Color.GREEN

        with(mainPaint, {
            isAntiAlias = true
            style = Paint.Style.FILL
            strokeWidth = 3F
            color = circleOutColor
        })

        with(shortPaint, {
            isAntiAlias = true
            style = Paint.Style.FILL
            strokeWidth = 1.dp2px().toFloat()
            color = circleInColor
        })


        with(textPaint, {
            isAntiAlias = true
            style = Paint.Style.FILL
            strokeWidth = 1.dp2px().toFloat()
            color = circleOutColor
            textAlign = Paint.Align.CENTER
        })
    }


    fun start() {
        var tempLongX = -1L
        object : CountDownTimer(3000, 10) {
            override fun onTick(millisUntilFinished: Long) {
                val long = millisUntilFinished / 100
                if (tempLongX == long) {
                    return
                }
                tempLongX = long
                val intX = 30 - tempLongX
                outCircleNum = (intX / 30.0 * 72).toInt()
                inCircleNum = (intX / 30.0 * 60).toInt()
                log("动画进行中 $outCircleNum $inCircleNum")
                mDecibel = (60..80).random().toString()
                invalidate()
            }

            override fun onFinish() {
                log("动画结束")
            }
        }.start()
    }

    private var outCircleNum = 72
    private var inCircleNum = 60

    override fun onDraw(canvas: Canvas) {
        val centerX = measuredWidth / 2.toFloat()
        val centerY = measuredHeight / 2.toFloat()

        log("centerX = $centerX centerY = $centerY")

        //画圈圈
        mainPaint.color = circleOutColor
        shortPaint.color = circleInColor
        canvas.save()
        for (i in 1..outCircleNum) {
            canvas.drawLine(
                centerX,
                (centerY * 0.2).toFloat(),
                centerX,
                (centerY * 0.05).toFloat(),
                mainPaint
            )
            canvas.rotate(5f, centerX, centerY)
        }
        canvas.restore()
        canvas.save()
        for (i in 1..inCircleNum) {
            canvas.drawLine(
                centerX,
                (centerY * 0.3).toFloat(),
                centerX,
                (centerY * 0.25).toFloat(),
                shortPaint
            )
            canvas.rotate(6f, centerX, centerY)
        }
        canvas.restore()

        //画文字
        with(textPaint, {
            isFakeBoldText = true
            textSkewX = -0.1F
            textSize = 36.sp2px().toFloat()
            color = circleOutColor
        })
        canvas.drawText(mDecibel, centerX, centerY + 10.dp2px(), textPaint)
        with(textPaint, {
            textSize = 14.sp2px().toFloat()
        })
        canvas.drawText("db", centerX, centerY + 25.dp2px(), textPaint)

    }

    private fun log(msg: String) {
        Log.d("ASDF", msg)
    }
}