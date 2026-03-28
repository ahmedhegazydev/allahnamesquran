package com.example.allahnamesquran.features.details.share

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.core.content.FileProvider
import com.example.allahnamesquran.features.details.AyahUiModel
import com.example.allahnamesquran.features.details.DetailsUiState
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max

object DetailsShareHelper {

    fun shareAsImage(context: Context, state: DetailsUiState) {
        val imageFile = createShareImage(context, state)
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            imageFile
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_TEXT, buildShareText(state))
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            clipData = android.content.ClipData.newUri(context.contentResolver, imageFile.name, uri)
        }

        context.startActivity(Intent.createChooser(shareIntent, null))
    }

    private fun createShareImage(context: Context, state: DetailsUiState): File {
        val density = context.resources.displayMetrics.density
        val width = (1080 * density / 3f).toInt()
        val outerPadding = 24f * density
        val cardPadding = 28f * density
        val cardWidth = width - (outerPadding * 2)
        val cardInnerWidth = cardWidth - (cardPadding * 2)

        val titlePaint = textPaint(18f * density, true, Color.parseColor("#F5E6B5"), Typeface.DEFAULT_BOLD)
        val namePaint = textPaint(30f * density, true, Color.WHITE, Typeface.DEFAULT_BOLD)
        val latinPaint = textPaint(14f * density, false, Color.parseColor("#E7E2F8"), Typeface.DEFAULT_BOLD)
        val bodyPaint = textPaint(16f * density, true, Color.parseColor("#1E1B2E"), Typeface.DEFAULT)
        val sectionPaint = textPaint(17f * density, true, Color.parseColor("#2C2450"), Typeface.DEFAULT_BOLD)
        val statLabelPaint = textPaint(13f * density, true, Color.parseColor("#6D6588"), Typeface.DEFAULT)
        val statValuePaint = textPaint(16f * density, true, Color.parseColor("#2C2450"), Typeface.DEFAULT_BOLD)
        val ayahPaint = textPaint(18f * density, true, Color.parseColor("#1E1B2E"), Typeface.DEFAULT_BOLD)
        val refPaint = textPaint(13f * density, true, Color.parseColor("#6D6588"), Typeface.DEFAULT)
        val footerPaint = textPaint(12f * density, true, Color.parseColor("#7A7390"), Typeface.DEFAULT_BOLD)

        val summaryTitle = "اسم الله الحسنى"
        val occurrencesLabel = "عدد الآيات"
        val pagesLabel = "عدد الصفحات"
        val ajzaaLabel = "عدد الأجزاء"
        val samplesTitle = "الآيات التي ذُكر فيها الاسم"
        val locationsTitle = "تفاصيل المواضع"
        val footerText = "تم الإنشاء من تطبيق آيات الأسماء الحسنى"

        val uniquePages = state.ayahs.map { it.page }.distinct().sorted()
        val uniqueJuz = state.ayahs.map { it.juz }.distinct().sorted()
        val ayahPreviewItems = state.ayahs.take(3)
        val locationItems = buildLocationItems(state.ayahs)

        val descriptionLayout = createLayout(state.description.ifBlank { "لا يوجد وصف متاح لهذا الاسم." }, bodyPaint, cardInnerWidth.toInt())
        val samplesTitleLayout = createLayout(samplesTitle, sectionPaint, cardInnerWidth.toInt())
        val locationsTitleLayout = createLayout(locationsTitle, sectionPaint, cardInnerWidth.toInt())
        val footerLayout = createLayout(footerText, footerPaint, cardInnerWidth.toInt(), Layout.Alignment.ALIGN_CENTER)

        val ayahLayouts = ayahPreviewItems.map { ayah ->
            ShareAyahLayout(
                textLayout = createLayout(ayah.text, ayahPaint, cardInnerWidth.toInt()),
                referenceLayout = createLayout(
                    "${ayah.surahName} • آية ${ayah.ayahNumber} • صفحة ${ayah.page} • جزء ${ayah.juz}",
                    refPaint,
                    cardInnerWidth.toInt()
                )
            )
        }

        val locationLayouts = locationItems.map { createLayout(it, refPaint, cardInnerWidth.toInt()) }

        val statsHeight = 116f * density
        val nameBlockHeight = 120f * density
        val descriptionHeight = descriptionLayout.height + (30f * density)
        val samplesHeight = if (ayahLayouts.isEmpty()) {
            0f
        } else {
            samplesTitleLayout.height + (22f * density) + ayahLayouts.sumOf {
                (it.textLayout.height + it.referenceLayout.height + (36f * density)).toInt()
            }
        }
        val locationsHeight = if (locationLayouts.isEmpty()) {
            0f
        } else {
            locationsTitleLayout.height + (22f * density) + locationLayouts.sumOf { it.height + (10f * density).toInt() }
        }
        val footerHeight = footerLayout.height + (26f * density)

        val imageHeight = (outerPadding * 2 + cardPadding * 2 + nameBlockHeight + descriptionHeight + statsHeight + samplesHeight + locationsHeight + footerHeight + 80f * density).toInt()

        val bitmap = Bitmap.createBitmap(width, max(imageHeight, (900 * density / 3f).toInt()), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        drawBackground(canvas, width.toFloat(), bitmap.height.toFloat())

        val cardRect = RectF(outerPadding, outerPadding, width - outerPadding, bitmap.height - outerPadding)
        val cardPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor("#FFFDF9") }
        val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.argb(22, 44, 36, 80) }
        canvas.drawRoundRect(
            RectF(cardRect.left, cardRect.top + (8f * density), cardRect.right, cardRect.bottom + (8f * density)),
            42f * density,
            42f * density,
            shadowPaint
        )
        canvas.drawRoundRect(cardRect, 42f * density, 42f * density, cardPaint)

        var y = outerPadding + cardPadding
        val centerX = width / 2f

        canvas.drawText(summaryTitle, centerX, y + titlePaint.textSize, titlePaint.apply { textAlign = Paint.Align.CENTER })
        y += 34f * density
        canvas.drawText(state.name, centerX, y + namePaint.textSize, namePaint.apply { textAlign = Paint.Align.CENTER })
        y += 44f * density
        if (state.englishName.isNotBlank()) {
            canvas.drawText(state.englishName, centerX, y + latinPaint.textSize, latinPaint.apply { textAlign = Paint.Align.CENTER })
            y += 34f * density
        }

        y += 10f * density
        drawTextBlock(canvas, descriptionLayout, cardRect.left + cardPadding, y)
        y += descriptionLayout.height + (28f * density)

        y = drawStatsRow(
            canvas = canvas,
            startX = cardRect.left + cardPadding,
            topY = y,
            totalWidth = cardInnerWidth,
            density = density,
            stats = listOf(
                ShareStat(occurrencesLabel, state.ayahsCount.toString()),
                ShareStat(pagesLabel, uniquePages.size.toString()),
                ShareStat(ajzaaLabel, uniqueJuz.size.toString())
            ),
            labelPaint = statLabelPaint,
            valuePaint = statValuePaint
        )

        y += 26f * density

        if (ayahLayouts.isNotEmpty()) {
            drawTextBlock(canvas, samplesTitleLayout, cardRect.left + cardPadding, y)
            y += samplesTitleLayout.height + (18f * density)
            ayahLayouts.forEachIndexed { index, item ->
                val blockHeight = item.textLayout.height + item.referenceLayout.height + (22f * density)
                val blockRect = RectF(
                    cardRect.left + cardPadding,
                    y,
                    cardRect.right - cardPadding,
                    y + blockHeight
                )
                drawSoftCard(canvas, blockRect, density, if (index == 0) "#F6F1FF" else "#F8F5EE")
                drawTextBlock(canvas, item.textLayout, blockRect.left + (16f * density), y + (16f * density))
                drawTextBlock(canvas, item.referenceLayout, blockRect.left + (16f * density), y + (16f * density) + item.textLayout.height + (8f * density))
                y += blockHeight + (14f * density)
            }
        }

        if (locationLayouts.isNotEmpty()) {
            drawTextBlock(canvas, locationsTitleLayout, cardRect.left + cardPadding, y)
            y += locationsTitleLayout.height + (14f * density)
            locationLayouts.forEach { layout ->
                drawTextBlock(canvas, layout, cardRect.left + cardPadding, y)
                y += layout.height + (8f * density)
            }
        }

        y += 14f * density
        drawTextBlock(canvas, footerLayout, cardRect.left + cardPadding, y)

        val shareDir = File(context.cacheDir, "shared").apply { mkdirs() }
        val outputFile = File(shareDir, "allah-name-${state.nameId ?: 0}.png")
        FileOutputStream(outputFile).use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        return outputFile
    }

    private fun buildLocationItems(ayahs: List<AyahUiModel>): List<String> {
        if (ayahs.isEmpty()) return emptyList()
        val maxItems = 18
        val visibleItems = ayahs.take(maxItems).mapIndexed { index, ayah ->
            "${index + 1}. ${ayah.surahName} • آية ${ayah.ayahNumber} • صفحة ${ayah.page} • جزء ${ayah.juz}"
        }
        return if (ayahs.size > maxItems) {
            visibleItems + "... وباقي ${ayahs.size - maxItems} موضع داخل التطبيق"
        } else {
            visibleItems
        }
    }

    private fun buildShareText(state: DetailsUiState): String {
        val header = buildString {
            append(state.name)
            if (state.description.isNotBlank()) {
                append('\n')
                append(state.description)
            }
            append("\nعدد الآيات: ${state.ayahsCount}")
        }

        val details = state.ayahs.take(20).joinToString(separator = "\n") {
            "- ${it.surahName} | آية ${it.ayahNumber} | صفحة ${it.page} | جزء ${it.juz}"
        }

        return if (details.isBlank()) header else "$header\n\n$details"
    }

    private fun drawBackground(canvas: Canvas, width: Float, height: Float) {
        val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f,
                0f,
                width,
                height,
                intArrayOf(
                    Color.parseColor("#1E163B"),
                    Color.parseColor("#3D2F6E"),
                    Color.parseColor("#6E5AA7")
                ),
                null,
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, width, height, backgroundPaint)
    }

    private fun drawStatsRow(
        canvas: Canvas,
        startX: Float,
        topY: Float,
        totalWidth: Float,
        density: Float,
        stats: List<ShareStat>,
        labelPaint: TextPaint,
        valuePaint: TextPaint
    ): Float {
        val spacing = 12f * density
        val boxWidth = (totalWidth - (spacing * (stats.size - 1))) / stats.size
        val boxHeight = 92f * density
        stats.forEachIndexed { index, stat ->
            val left = startX + index * (boxWidth + spacing)
            val rect = RectF(left, topY, left + boxWidth, topY + boxHeight)
            drawSoftCard(canvas, rect, density, "#F4EEFF")
            canvas.drawText(stat.value, rect.centerX(), rect.top + (34f * density), valuePaint.apply { textAlign = Paint.Align.CENTER })
            canvas.drawText(stat.label, rect.centerX(), rect.top + (64f * density), labelPaint.apply { textAlign = Paint.Align.CENTER })
        }
        return topY + boxHeight
    }

    private fun drawSoftCard(canvas: Canvas, rect: RectF, density: Float, colorHex: String) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.parseColor(colorHex) }
        canvas.drawRoundRect(rect, 24f * density, 24f * density, paint)
    }

    private fun drawTextBlock(canvas: Canvas, layout: StaticLayout, left: Float, top: Float) {
        canvas.save()
        canvas.translate(left, top)
        layout.draw(canvas)
        canvas.restore()
    }

    private fun createLayout(
        text: String,
        paint: TextPaint,
        width: Int,
        alignment: Layout.Alignment = Layout.Alignment.ALIGN_OPPOSITE
    ): StaticLayout {
        return StaticLayout.Builder
            .obtain(text, 0, text.length, paint, width)
            .setAlignment(alignment)
            .setLineSpacing(0f, 1.2f)
            .setIncludePad(false)
            .build()
    }

    private fun textPaint(size: Float, rtl: Boolean, color: Int, typeface: Typeface): TextPaint {
        return TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = size
            this.color = color
            this.typeface = typeface
            isFakeBoldText = typeface == Typeface.DEFAULT_BOLD
            textAlign = if (rtl) Paint.Align.RIGHT else Paint.Align.CENTER
        }
    }

    private data class ShareAyahLayout(
        val textLayout: StaticLayout,
        val referenceLayout: StaticLayout
    )

    private data class ShareStat(
        val label: String,
        val value: String
    )
}