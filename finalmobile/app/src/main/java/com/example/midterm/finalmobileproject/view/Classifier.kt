package com.example.midterm.finalmobileproject.view

import android.annotation.SuppressLint
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.*
import org.tensorflow.lite.Interpreter

class Classifier {
    //constructor(assets: AssetManager?, mModelPath: String, mLabelPath: String, mInputSize: Int)

    private val BATCH_SIZE = 1
    private var interpreter: Interpreter? = null
    private var labelList: List<String>? = null
    private var INPUT_SIZE = 0
    private val PIXEL_SIZE = 3
    private val IMAGE_MEAN = 0.0f
    private val IMAGE_STD = 255.0f
    private val MAX_RESULTS = 3f
    private val THRESHOLD = 0.4f
    private val QUANT = false
    private lateinit var options: Interpreter.Options

    //    @Throws(IOException::class)
    constructor(
        assetManager: AssetManager,
        modelPath: String,
        labelPath: String,
        inputSize: Int
    ) {
        INPUT_SIZE = inputSize
        options = Interpreter.Options()
        options.setNumThreads(5)
        options.setUseNNAPI(true)
        interpreter = loadModelFile(assetManager, modelPath)?.let { Interpreter(it, options) }
        labelList = loadLabelList(assetManager, labelPath)
    }

    class Recognition(i: String, s: String, confidence: Float) {
        var id = ""
        var title = ""
        var confidence = 0f
        override fun toString(): String {
            return title
        }

        init {
            id = i
            title = s
            this.confidence = confidence
        }
    }

    @Throws(IOException::class)
    private fun loadModelFile(assetManager: AssetManager, MODEL_FILE: String): MappedByteBuffer? {
        val fileDescriptor = assetManager.openFd(MODEL_FILE)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    @Throws(IOException::class)
    private fun loadLabelList(assetManager: AssetManager, labelPath: String): List<String>? {
        val labelList: MutableList<String> = ArrayList()
        val reader = BufferedReader(InputStreamReader(assetManager.open(labelPath)))
        Log.e("895",labelPath)
//        var line: String
//        while (reader.readLine().also { line = it } != null) {
//            labelList.add(line)
//        }
        while (true) {
            val line = reader.readLine() ?: break
            labelList.add(line)
        }
        reader.close()
        return labelList
    }

    fun recognizeImage(bitmap: Bitmap?): List<Recognition?>? {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap!!, INPUT_SIZE, INPUT_SIZE, false)
        val byteBuffer = convertBitmapToByteBuffer(scaledBitmap)
        val result = Array(1) {
            FloatArray(
                labelList!!.size
            )
        }
        interpreter?.run(byteBuffer, result)
        return getSortedResultFloat(result)
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer: ByteBuffer
        byteBuffer = ByteBuffer.allocateDirect(4 * INPUT_SIZE * INPUT_SIZE * PIXEL_SIZE)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(INPUT_SIZE * INPUT_SIZE)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel = 0
        for (i in 0 until INPUT_SIZE) {
            for (j in 0 until INPUT_SIZE) {
                val `val` = intValues[pixel++]
                byteBuffer.putFloat(((`val` shr 16 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                byteBuffer.putFloat(((`val` shr 8 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                byteBuffer.putFloat(((`val` and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
            }
        }
        return byteBuffer
    }

    @SuppressLint("DefaultLocale")
    private fun getSortedResultFloat(labelProbArray: Array<FloatArray>): List<Recognition?>? {
        val pq = PriorityQueue<Recognition>(
            MAX_RESULTS.toInt()
        ) { lhs, rhs -> java.lang.Float.compare(rhs.confidence, lhs.confidence) }
        for (i in labelList!!.indices) {
            val confidence = labelProbArray[0][i]
            if (confidence > THRESHOLD) {
                pq.add(
                    Recognition(
                        "" + i,
                        if (labelList!!.size > i) labelList!![i] else "unknown",
                        confidence
                    )
                )
            }
        }
        val recognitions = ArrayList<Recognition?>()
        val recognitionsSize = Math.min(pq.size.toFloat(), MAX_RESULTS).toInt()
        for (i in 0 until recognitionsSize) {
            recognitions.add(pq.poll())
        }
        return recognitions
    }
}