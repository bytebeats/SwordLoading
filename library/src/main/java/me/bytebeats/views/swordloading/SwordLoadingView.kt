package me.bytebeats.views.swordloading

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.RotateAnimation
import androidx.annotation.ColorInt
import kotlin.random.Random

/**
 * Created by bytebeats on 2021/9/29 : 15:42
 * E-mail: happychinapc@gmail.com
 * Quote: Peasant. Educated. Worker
 */
class SwordLoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val mPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.WHITE
        }
    }

    private val mAnimation by lazy {
        ValueAnimator.ofFloat(0F, -360F).apply {
            interpolator = null
            repeatCount = RotateAnimation.INFINITE
            duration = 1000L
            addUpdateListener { invalidate() }
        }
    }

    private val mCamera by lazy { Camera() }
    private val mMatrix by lazy { Matrix() }
    private val mXfermode by lazy { PorterDuffXfermode(PorterDuff.Mode.DST_OUT) }
    private val coordinates = mutableListOf<Triple<Float, Float, Float>>()

    private var mRadius = 0F

    private var mDuration: Int = 1000
        set(value) {
            field = value
            mAnimation.duration = field.toLong()
        }

    @ColorInt
    private var mSwordColor: Int = Color.WHITE
        set(value) {
            field = value
            mPaint.color = field
        }
    private var mSwordCount: Int = 1
        set(value) {
            if (value < 1) {
                field = 1
            } else {
                field = value
            }
            val deltaZ = 360F / mSwordCount
            coordinates.clear()
            for (i in 0 until mSwordCount) {
                coordinates.add(
                    Triple(
                        Random.Default.nextInt(0, 360).toFloat(),
                        Random.Default.nextInt(-180, 180).toFloat(),
                        i * deltaZ
                    )
                )
            }
        }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SwordLoadingView, defStyleAttr, 0)
        mDuration = a.getInteger(R.styleable.SwordLoadingView_duration, 1000)
        mSwordColor = a.getColor(R.styleable.SwordLoadingView_swordColor, Color.WHITE)
        mSwordCount = a.getInteger(R.styleable.SwordLoadingView_swordCount, 1)
        a.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mRadius = w.coerceAtMost(h) / 3F
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        canvas?.drawColor(Color.BLACK)
        if (mSwordCount == coordinates.size) {
            for (i in 0 until mSwordCount) {
                if (i < mSwordCount) {
                    val c = coordinates[i]
                    canvas?.let {
                        drawSword(
                            it,
                            c.first,
                            c.second,
                            c.third
                        )
                    }
                }
            }
        }
    }

    fun start() {
        mAnimation.start()
    }

    private fun drawSword(canvas: Canvas, rotateX: Float, rotateY: Float, startZ: Float) {
        Log.d(TAG, "rotateX: $rotateX, rotateY: $rotateY, startZ: $startZ")
        val layer =
            canvas.saveLayer(0f, 0F, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        mMatrix.reset()
        mCamera.save()
        mCamera.rotateX(rotateX)
        mCamera.rotateY(rotateY)
        mCamera.rotateZ((mAnimation.animatedValue as Float) + startZ)
        mCamera.getMatrix(mMatrix)
        mCamera.restore()

        val halfW = width / 2F
        val halfH = height / 2F

        mMatrix.preTranslate(-halfW, -halfH)
        mMatrix.postTranslate(halfW, halfH)
        canvas.concat(mMatrix)
        canvas.drawCircle(halfW, halfH, mRadius, mPaint)
        mPaint.xfermode = mXfermode
        canvas.drawCircle(halfW, halfH - 0.05F * mRadius, mRadius * 1.01F, mPaint)
        canvas.restoreToCount(layer)
        mPaint.xfermode = null
    }

    companion object {
        private const val TAG = "SwordLoadingView"
    }
}