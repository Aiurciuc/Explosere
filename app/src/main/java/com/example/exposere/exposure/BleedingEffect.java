package com.example.exposere.exposure;

import android.graphics.Bitmap;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Range;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

import static org.opencv.core.Core.absdiff;
import static org.opencv.core.Core.addWeighted;

public class BleedingEffect {

    private static double alpha = 0.95;
    private static double beta = 1 - alpha;
    private static double gamma = 0;

    public static Bitmap convert (Bitmap headImage, Bitmap backgound){
        Mat back = new Mat(backgound.getWidth(), backgound.getHeight(), CvType.CV_8UC1);
        Mat head = new Mat(headImage.getWidth(), headImage.getHeight(), CvType.CV_8UC1);
        Bitmap bmpHead = headImage.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap bmpBack = backgound.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmpHead, head);
        Utils.bitmapToMat(bmpBack, back);
        back = BleedingEffect.resize(head, back);
        Mat output = new Mat();

        Mat newHead = new Mat(backgound.getWidth(), backgound.getHeight(), CvType.CV_8UC1);
        Mat onews = new Mat(head.height(), head.width(), CvType.CV_8UC1);
        Scalar s = new Scalar(255);
        onews.setTo(s);
        Mat out = new Mat(backgound.getWidth(), backgound.getHeight(), CvType.CV_8UC1);
        Imgproc.cvtColor(head,newHead,Imgproc.COLOR_BGR2GRAY);


        absdiff(newHead, onews, out);

        double x[] = out.get(0,1);
        double y[] = onews.get(0,1);
        Mat corpedOutput = new Mat();

        addWeighted(back, alpha, head, beta, gamma, output);
        output.copyTo(corpedOutput, out);
        Bitmap image = Bitmap.createBitmap(corpedOutput.cols(), corpedOutput.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(corpedOutput, image);
        return image;

    }

    private static Mat resize(Mat ref, Mat toResize){
        if (ref.width() < toResize.width()){
            Range heightRange = new Range(0, ref.height());
            Range widthRange = new Range(0, ref.width());
            Mat output = new Mat(toResize, heightRange, widthRange);
            return output;
        }
        else{
            Mat addition = Mat.zeros(ref.height(), ref.width() - toResize.width(), CvType.CV_8UC4 );
            ArrayList<Mat> sources = new ArrayList<Mat>();
            sources.add(toResize);
            sources.add(addition);
            Mat output =  new Mat(ref.width(), ref.height(), CvType.CV_8UC4);
            Core.hconcat(sources, output);
            return output;
        }

    }
}
