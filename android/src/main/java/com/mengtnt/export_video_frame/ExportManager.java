package com.mengtnt.export_video_frame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExportManager {

    public static String exportImagePathBySecond(String filePath, int milli, float radian) {
        File file = new File(filePath);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(filePath);

        // Calculate the time in microseconds
        long timeUs = milli * 1000;

        Bitmap bitmap = retriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);

        if (bitmap != null) {
            // Rotate the image by radian
            Bitmap rotatedBitmap = rotateBitmap(bitmap, -radian);

            // Save the bitmap to file
            String name = timeUs + String.format("%.4f", radian);
            FileStorage fileStorage = FileStorage.getInstance();
            String outputPath = fileStorage.getFilePath(name);

            if (fileStorage.createFile(name, rotatedBitmap)) {
                return outputPath;
            }
        }

        return null;
    }

    public static List<String> exportGifImagePathList(String filePath, double quality) {
        List<String> imagePaths = new ArrayList<>();
        File file = new File(filePath);

        // Use a third-party library or custom implementation to handle GIF extraction
        // Example using a library like Glide for GIF processing
        // You would need to adapt this part according to the library you're using

        return imagePaths;
    }

    public static void exportImagePathList(String filePath, int number, double quality, ImageExportCallback callback) {
        File file = new File(filePath);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(filePath);

        // Get video duration in microseconds
        long durationUs = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) * 1000;

        List<String> imageList = new ArrayList<>();
        List<Long> times = new ArrayList<>();

        long step = durationUs / number;

        for (int i = 0; i < number; i++) {
            long timeUs = i * step;
            times.add(timeUs);
        }

        for (long timeUs : times) {
            Bitmap bitmap = retriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);

            if (bitmap != null) {
                // Save the bitmap to file
                String name = filePath + "+" + timeUs;
                FileStorage fileStorage = FileStorage.getInstance();
                String outputPath = fileStorage.getFilePath(name);

                if (fileStorage.createFile(name, bitmap)) {
                    imageList.add(outputPath);
                }
            }
        }

        callback.onComplete(imageList);
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, float degrees) {
        // Implement rotation logic
        return bitmap; // Placeholder
    }

    public interface ImageExportCallback {
        void onComplete(List<String> imagePaths);
    }
}

