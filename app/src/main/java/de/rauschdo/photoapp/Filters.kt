package de.rauschdo.photoapp

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Shader
import android.widget.ImageView
import java.util.Random

object Filters {

    // INVERT EFFECT
    @JvmStatic
    fun invertEffect(iv: ImageView, src: Bitmap) {
        // create new bitmap with the same settings as source bitmap
        val bmOut = Bitmap.createBitmap(src.width, src.height, src.config)
        // color info
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixelColor: Int
        // image size
        val height = src.height
        val width = src.width

        // scan through every pixel
        for (y in 0 until height) {
            for (x in 0 until width) {
                // get one pixel
                pixelColor = src.getPixel(x, y)
                // saving alpha channel
                A = Color.alpha(pixelColor)
                // inverting byte for each R/G/B channel
                R = 255 - Color.red(pixelColor)
                G = 255 - Color.green(pixelColor)
                B = 255 - Color.blue(pixelColor)
                // set newly-inverted pixel to output image
                bmOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }

        // return final image
        iv.setImageBitmap(bmOut)
    }

    // COLOR FILTER EFFECT
    fun colorFilterEffect(iv: ImageView, src: Bitmap, red: Double, green: Double, blue: Double) {
        // image size
        val width = src.width
        val height = src.height
        // create output bitmap
        val bmOut = Bitmap.createBitmap(width, height, src.config)
        // color information
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int

        // scan through all pixels
        for (x in 0 until width) {
            for (y in 0 until height) {
                // get pixel color
                pixel = src.getPixel(x, y)
                // apply filtering on each channel R, G, B
                A = Color.alpha(pixel)
                R = (Color.red(pixel) * red).toInt()
                G = (Color.green(pixel) * green).toInt()
                B = (Color.blue(pixel) * blue).toInt()
                // set new color pixel to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }

        // return final image
        iv.setImageBitmap(bmOut)
    }

    // CONTRAST EFFECT
    @JvmStatic
    fun contrastEffect(iv: ImageView, src: Bitmap, value: Double) {
        // image size
        val width = src.width
        val height = src.height
        // create output bitmap
        val bmOut = Bitmap.createBitmap(width, height, src.config)
        // color information
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int
        // get contrast value
        val contrast = Math.pow((100 + value) / 100, 2.0)

        // scan through all pixels
        for (x in 0 until width) {
            for (y in 0 until height) {
                // get pixel color
                pixel = src.getPixel(x, y)
                A = Color.alpha(pixel)
                // apply filter contrast for every channel R, G, B
                R = Color.red(pixel)
                R = (((R / 255.0 - 0.5) * contrast + 0.5) * 255.0).toInt()
                if (R < 0) {
                    R = 0
                } else if (R > 255) {
                    R = 255
                }
                G = Color.red(pixel)
                G = (((G / 255.0 - 0.5) * contrast + 0.5) * 255.0).toInt()
                if (G < 0) {
                    G = 0
                } else if (G > 255) {
                    G = 255
                }
                B = Color.red(pixel)
                B = (((B / 255.0 - 0.5) * contrast + 0.5) * 255.0).toInt()
                if (B < 0) {
                    B = 0
                } else if (B > 255) {
                    B = 255
                }

                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }

        // return final image
        iv.setImageBitmap(bmOut)
    }

    // GRAYSCALE EFFECT
    @JvmStatic
    fun grayscaleEffect(iv: ImageView, bm: Bitmap, red: Double, green: Double, blue: Double) {
        // create output bitmap
        val bmOut = Bitmap.createBitmap(bm.width, bm.height, bm.config)
        // pixel information
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int

        // get image size
        val width = bm.width
        val height = bm.height

        // scan through every single pixel
        for (x in 0 until width) {
            for (y in 0 until height) {
                // get one pixel color
                pixel = bm.getPixel(x, y)
                // retrieve color of all channels
                A = Color.alpha(pixel)
                R = Color.red(pixel)
                G = Color.green(pixel)
                B = Color.blue(pixel)
                // take conversion up to one single value
                B = (red * R + green * G + blue * B).toInt()
                G = B
                R = G
                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }
        iv.setImageBitmap(bmOut)
    }

    // SEPIA EFFECT
    @JvmStatic
    fun sepiaEffect(
        iv: ImageView,
        bm: Bitmap,
        depth: Int,
        red: Double,
        green: Double,
        blue: Double
    ) {
        // create output bitmap
        val bmOut = Bitmap.createBitmap(bm.width, bm.height, bm.config)
        // constant grayscale
        val GS_RED = 0.3
        val GS_GREEN = 0.59
        val GS_BLUE = 0.11
        // color information
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int

        // scan through all pixels
        for (x in 0 until bm.width) {
            for (y in 0 until bm.height) {
                // get pixel color
                pixel = bm.getPixel(x, y)
                // get color on each channel
                A = Color.alpha(pixel)
                R = Color.red(pixel)
                G = Color.green(pixel)
                B = Color.blue(pixel)

                // apply grayscale sample
                R = (GS_RED * R + GS_GREEN * G + GS_BLUE * B).toInt()
                G = R
                B = G

                // apply intensity level for sepia-toning on each channel
                R = (R + depth * red).toInt()
                if (R > 255) {
                    R = 255
                }
                G = (G + depth * green).toInt()
                if (G > 255) {
                    G = 255
                }
                B = (B + depth * blue).toInt()
                if (B > 255) {
                    B = 255
                }

                // set new pixel color to output image
                bmOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }

        // return final image
        iv.setImageBitmap(bmOut)
    }

    // GAMMA EFFECT
    fun gammaEffect(iv: ImageView, bm: Bitmap, red: Double, green: Double, blue: Double) {
        // create output image
        val bmOut = Bitmap.createBitmap(bm.width, bm.height, bm.config)
        // color information
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int

        // constant value curve
        val MAX_SIZE = 256
        val MAX_VALUE_DBL = 255.0
        val MAX_VALUE_INT = 255
        val REVERSE = 1.0

        // gamma arrays
        val gammaR = IntArray(MAX_SIZE)
        val gammaG = IntArray(MAX_SIZE)
        val gammaB = IntArray(MAX_SIZE)

        // setting values for every gamma channels
        for (i in 0 until MAX_SIZE) {
            gammaR[i] = Math.min(
                MAX_VALUE_INT,
                (MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / red) + 0.5).toInt()
            )
            gammaG[i] = Math.min(
                MAX_VALUE_INT,
                (MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / green) + 0.5).toInt()
            )
            gammaB[i] = Math.min(
                MAX_VALUE_INT,
                (MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / blue) + 0.5).toInt()
            )
        }

        // apply gamma table
        for (x in 0 until bm.width) {
            for (y in 0 until bm.height) {
                // get pixel color
                pixel = bm.getPixel(x, y)
                A = Color.alpha(pixel)
                // look up gamma
                R = gammaR[Color.red(pixel)]
                G = gammaG[Color.green(pixel)]
                B = gammaB[Color.blue(pixel)]
                // set new color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }

        // return final image
        iv.setImageBitmap(bmOut)
    }

    // POSTERIZE EFFECT
    fun posterizeEffect(iv: ImageView, src: Bitmap, bitOffset: Int) {
        // get image size
        val width = src.width
        val height = src.height
        // create output bitmap
        val bmOut = Bitmap.createBitmap(width, height, src.config)
        // color information
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int

        // scan through all pixels
        for (x in 0 until width) {
            for (y in 0 until height) {
                // get pixel color
                pixel = src.getPixel(x, y)
                A = Color.alpha(pixel)
                R = Color.red(pixel)
                G = Color.green(pixel)
                B = Color.blue(pixel)

                // round-off color offset
                R = R + bitOffset / 2 - (R + bitOffset / 2) % bitOffset - 1
                if (R < 0) {
                    R = 0
                }
                G = G + bitOffset / 2 - (G + bitOffset / 2) % bitOffset - 1
                if (G < 0) {
                    G = 0
                }
                B = B + bitOffset / 2 - (B + bitOffset / 2) % bitOffset - 1
                if (B < 0) {
                    B = 0
                }

                // set pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }

        // return final image
        iv.setImageBitmap(bmOut)
    }

    // BRIGHTNESS EFFECT
    @JvmStatic
    fun brightnessEffect(iv: ImageView, src: Bitmap, value: Int) {
        // image size
        val width = src.width
        val height = src.height
        // create output bitmap
        val bmOut = Bitmap.createBitmap(width, height, src.config)
        // color information
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int

        // scan through all pixels
        for (x in 0 until width) {
            for (y in 0 until height) {
                // get pixel color
                pixel = src.getPixel(x, y)
                A = Color.alpha(pixel)
                R = Color.red(pixel)
                G = Color.green(pixel)
                B = Color.blue(pixel)

                // increase/decrease each channel
                R += value
                if (R > 255) {
                    R = 255
                } else if (R < 0) {
                    R = 0
                }
                G += value
                if (G > 255) {
                    G = 255
                } else if (G < 0) {
                    G = 0
                }
                B += value
                if (B > 255) {
                    B = 255
                } else if (B < 0) {
                    B = 0
                }

                // apply new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }

        // return final image
        iv.setImageBitmap(bmOut)
    }

    // BOOST EFFECT
    fun boostColorEffect(iv: ImageView, src: Bitmap, type: Int, percent: Float) {
        val width = src.width
        val height = src.height
        val bmOut = Bitmap.createBitmap(width, height, src.config)
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int
        for (x in 0 until width) {
            for (y in 0 until height) {
                pixel = src.getPixel(x, y)
                A = Color.alpha(pixel)
                R = Color.red(pixel)
                G = Color.green(pixel)
                B = Color.blue(pixel)
                if (type == 1) {
                    R = (R * (1 + percent)).toInt()
                    if (R > 255) R = 255
                } else if (type == 2) {
                    G = (G * (1 + percent)).toInt()
                    if (G > 255) G = 255
                } else if (type == 3) {
                    B = (B * (1 + percent)).toInt()
                    if (B > 255) B = 255
                }
                bmOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }

        // return final image
        iv.setImageBitmap(bmOut)
    }

    // FLIP EFFECT
    const val FLIP_VERTICAL = 1
    const val FLIP_HORIZONTAL = 2
    fun flipEffect(iv: ImageView, src: Bitmap, type: Int) {
        // create new matrix for transformation
        val matrix = Matrix()
        // if vertical
        if (type == FLIP_VERTICAL) {
            // y = y * -1
            matrix.preScale(1.0f, -1.0f)
        } else if (type == FLIP_HORIZONTAL) {
            // x = x * -1
            matrix.preScale(-1.0f, 1.0f)
            // unknown type
        }
        val bmOut = Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)

        // return final image
        iv.setImageBitmap(bmOut)
    }

    // GRAIN EFFECT
    const val COLOR_MIN = 0x00
    const val COLOR_MAX = 0xFF
    fun grainEffect(iv: ImageView, source: Bitmap) {
        // get image size
        val width = source.width
        val height = source.height
        val pixels = IntArray(width * height)
        // get pixel array from source
        source.getPixels(pixels, 0, width, 0, 0, width, height)
        // a random object
        val random = Random()
        var index = 0
        // iteration through pixels
        for (y in 0 until height) {
            for (x in 0 until width) {
                // get current index in 2D-matrix
                index = y * width + x
                // get random color
                val randColor = Color.rgb(
                    random.nextInt(COLOR_MAX),
                    random.nextInt(COLOR_MAX), random.nextInt(COLOR_MAX)
                )
                // OR
                pixels[index] = pixels[index] or randColor
            }
        }
        // output bitmap
        val bmOut = Bitmap.createBitmap(width, height, source.config)
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height)

        // return final image
        iv.setImageBitmap(bmOut)
    }

    // DIRTY EFFECT
    fun dirtyEffect(iv: ImageView, source: Bitmap) {
        // get image size
        val width = source.width
        val height = source.height
        val pixels = IntArray(width * height)
        // get pixel array from source
        source.getPixels(pixels, 0, width, 0, 0, width, height)
        // random object
        val random = Random()
        var R: Int
        var G: Int
        var B: Int
        var index = 0
        var thresHold = 0
        // iteration through pixels
        for (y in 0 until height) {
            for (x in 0 until width) {
                // get current index in 2D-matrix
                index = y * width + x
                // get color
                R = Color.red(pixels[index])
                G = Color.green(pixels[index])
                B = Color.blue(pixels[index])
                // generate threshold
                thresHold = random.nextInt(COLOR_MAX)
                if (R < thresHold && G < thresHold && B < thresHold) {
                    pixels[index] = Color.rgb(COLOR_MIN, COLOR_MIN, COLOR_MIN)
                }
            }
        }
        // output bitmap
        val bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height)

        // return final image
        iv.setImageBitmap(bmOut)
    }

    // SNOW EFFECT
    fun showEffect(iv: ImageView, source: Bitmap) {
        // get image size
        val width = source.width
        val height = source.height
        val pixels = IntArray(width * height)
        // get pixel array from source
        source.getPixels(pixels, 0, width, 0, 0, width, height)
        // random object
        val random = Random()
        var R: Int
        var G: Int
        var B: Int
        var index = 0
        var thresHold = 50
        // iteration through pixels
        for (y in 0 until height) {
            for (x in 0 until width) {
                // get current index in 2D-matrix
                index = y * width + x
                // get color
                R = Color.red(pixels[index])
                G = Color.green(pixels[index])
                B = Color.blue(pixels[index])
                // generate threshold
                thresHold = random.nextInt(COLOR_MAX)
                if (R > thresHold && G > thresHold && B > thresHold) {
                    pixels[index] = Color.rgb(COLOR_MAX, COLOR_MAX, COLOR_MAX)
                }
            }
        }
        // output bitmap
        val bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height)

        // return final image
        iv.setImageBitmap(bmOut)
    }

    // REFLECTION EFFECT
    fun reflectionEffect(iv: ImageView, originalImage: Bitmap) {
        // gap space between original and reflected
        val reflectionGap = 4
        // get image size
        val width = originalImage.width
        val height = originalImage.height

        // this will not scale but will flip on the Y axis
        val matrix = Matrix()
        matrix.preScale(1f, -1f)

        // create a Bitmap with the flip matrix applied to it.
        // we only want the bottom half of the image
        val reflectionImage =
            Bitmap.createBitmap(originalImage, 0, height / 2, width, height / 2, matrix, false)

        // create a new bitmap with same width but taller to fit reflection
        val bitmapWithReflection =
            Bitmap.createBitmap(width, height + height / 2, Bitmap.Config.ARGB_8888)

        // create a new Canvas with the bitmap that's big enough for
        // the image plus gap plus reflection
        val canvas = Canvas(bitmapWithReflection)
        // draw in the original image
        canvas.drawBitmap(originalImage, 0f, 0f, null)
        // draw in the gap
        val defaultPaint = Paint()
        canvas.drawRect(
            0f,
            height.toFloat(),
            width.toFloat(),
            (height + reflectionGap).toFloat(),
            defaultPaint
        )
        // draw in the reflection
        canvas.drawBitmap(reflectionImage, 0f, (height + reflectionGap).toFloat(), null)

        // create a shader that is a linear gradient that covers the reflection
        val paint = Paint()
        val shader = LinearGradient(
            0f, originalImage.height.toFloat(), 0f,
            (
                    bitmapWithReflection.height + reflectionGap).toFloat(), 0x70ffffff, 0x00ffffff,
            Shader.TileMode.CLAMP
        )
        // set the paint to use this shader (linear gradient)
        paint.setShader(shader)
        // set the Transfer mode to be porter duff and destination in
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.DST_IN))
        // draw a rectangle using the paint with our linear gradient
        canvas.drawRect(
            0f,
            height.toFloat(),
            width.toFloat(),
            (bitmapWithReflection.height + reflectionGap).toFloat(),
            paint
        )


        // return final image
        iv.setImageBitmap(bitmapWithReflection)
    }
} //@end
