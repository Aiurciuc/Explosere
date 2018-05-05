package com.example.exposere.exposure;

import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class BlackAndWhite {

    public static Bitmap convert(Bitmap image) {

        Mat tmp = new Mat(image.getWidth(), image.getHeight(), CvType.CV_8UC1);
        Utils.bitmapToMat(image, tmp);
        Imgproc.cvtColor(tmp, tmp, Imgproc.COLOR_RGB2GRAY);
        //there could be some processing
        Imgproc.cvtColor(tmp, tmp, Imgproc.COLOR_GRAY2RGB, 4);
        Utils.matToBitmap(tmp, image);
        return image;
    }
}
