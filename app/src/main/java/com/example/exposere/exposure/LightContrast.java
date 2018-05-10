package com.example.exposere.exposure;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * mareste luminozitatea imaginii, creste contrastul
 * Created by Alexandra on 05-May-18.
 */

public class LightContrast {

    public static Bitmap doBrightness(Bitmap image, int value)
    {
        // image size
        int width = image.getWidth();
        int height = image.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, image.getConfig());
        // color information
        int A, R, G, B;
        int pixel;

        // scan through all pixels
        for (int x = 0; x < width; ++x)
        {
            for (int y = 0; y < height; ++y)
            {
                // get pixel color
                pixel = image.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                // increase/decrease each channel
                R += value;
                if (R > 255)
                {
                    R = 255;
                } else if (R < 0)
                {
                    R = 0;
                }

                G += value;
                if (G > 255)
                {
                    G = 255;
                } else if (G < 0)
                {
                    G = 0;
                }

                B += value;
                if (B > 255)
                {
                    B = 255;
                } else if (B < 0)
                {
                    B = 0;
                }

                // apply new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return bmOut;
    }

}
