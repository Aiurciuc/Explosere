package com.example.exposere.exposure;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Range;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.Core.addWeighted;

public class BleedingEffect {

    private static double alpha = 0.5;
    private static double beta = 1 - alpha;
    private static double gamma = 0;

    public static Bitmap convert (Bitmap imageBack, Bitmap imageFront){
        Mat back = new Mat(imageBack.getWidth(), imageBack.getHeight(), CvType.CV_8UC1);
        Mat front = new Mat(imageBack.getWidth(), imageBack.getHeight(), CvType.CV_8UC1);
        Bitmap bmpFront = imageFront.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap bmpBack = imageBack.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmpFront, front);
        Utils.bitmapToMat(bmpBack, back);
        Range heightRange = new Range(0, front.height());
        Range widthRange = new Range(0, front.width());
        Mat newbackMat = new Mat(back, heightRange, widthRange);
        Mat output = new Mat();
        addWeighted(newbackMat, alpha, front, beta, gamma, output);
        Imgproc.cvtColor(output, output, Imgproc.COLOR_GRAY2RGBA, 4);
        Bitmap image = Bitmap.createBitmap(output.cols(), output.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(output, image);
        return image;

    }
}
