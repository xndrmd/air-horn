package pe.edu.uesan.airhorn.avatargenerator

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import java.util.*

class AvatarGenerator {
    companion object {
        const val CIRCLE = 1
        const val RECTANGLE = 0

        const val COLOR900 = 900
        const val COLOR700 = 700
        const val COLOR400 = 400

        fun avatarImage(context: Context, size: Int, shape: Int, name: String): Bitmap {
            return generate(context, size, shape, name, COLOR700)
        }

        fun avatarImage(context: Context, size: Int, shape: Int, name: String, colorModel: Int): Bitmap {
            return generate(context, size, shape, name, colorModel)
        }

        private fun generate(context: Context, size: Int, shape: Int, name: String, colorModel: Int): Bitmap {
            val label = name.first().toString().toUpperCase(Locale.ROOT)

            val textSize = (size / 5).toFloat()

            val textPaint = TextPaint()
            textPaint.isAntiAlias = true
            textPaint.textSize = textSize * context.resources.displayMetrics.scaledDensity
            textPaint.color = Color.WHITE

            val paint = Paint()
            paint.isAntiAlias = true

            val rect = Rect(0, 0, size, size)

            if (shape == RECTANGLE) {
                paint.color = RandomColors(colorModel).getColor(name)
            } else {
                paint.color = Color.TRANSPARENT
            }

            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawRect(rect, paint)

            if (shape == RECTANGLE) {
                paint.color = Color.TRANSPARENT
            } else {
                paint.color = RandomColors(colorModel).getColor(name)
            }

            val bounds = RectF(rect)
            bounds.right = textPaint.measureText(label, 0, 1)
            bounds.bottom = textPaint.descent() - textPaint.ascent()

            bounds.left += (rect.width() - bounds.right) / 2.0f
            bounds.top += (rect.height() - bounds.bottom) / 2.0f

            canvas.drawCircle(size.toFloat() / 2, size.toFloat() / 2, size.toFloat() / 2, paint)
            canvas.drawText(label, bounds.left, bounds.top - textPaint.ascent(), textPaint)

            return bitmap
        }
    }
}