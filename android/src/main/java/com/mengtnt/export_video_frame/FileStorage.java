/** 
MIT License

Copyright (c) 2019 mengtnt

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.mengtnt.export_video_frame;

import android.graphics.Bitmap;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileStorage {

    private static FileStorage instance;
    private final File directory;
    private String directoryName;
    private boolean external;
    private Context context;

    private FileStorage() {
        // Assuming the storage directory is a public directory for pictures
        this.external = true;
        this.directoryName = "ExportImage";
        Log.d("ExportVideoPlugin", "File Storage Directory: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
        this.directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), this.directoryName);
        if (!this.directory.exists()) {
            this.directory.mkdirs();
        }
    }

    void setContext(Context context) {
        this.context = context;
    }

    public static synchronized FileStorage getInstance() {
        if (instance == null) {
            instance = new FileStorage();
        }
        return instance;
    }

    static FileStorage share() {
        return instance;
    }

    public String getFilePath(String fileName) {
        File file = new File(directory, fileName);
        return file.getAbsolutePath();
    }

    public boolean createFile(String fileName, Bitmap bitmap) {
        Bitmap resizedBitmap = resizeBitmap(bitmap, 640, 640);

        File file = new File(directory, fileName);
        Log.d("ExportVideoPlugin", "FileStorage directory: "+directory);
        Log.d("ExportVideoPlugin", "FileStorage fileName: "+fileName);
        try (FileOutputStream out = new FileOutputStream(file)) {
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Calculate the scale factor
        float scaleFactor = Math.min((float) maxWidth / width, (float) maxHeight / height);

        int newWidth = Math.round(width * scaleFactor);
        int newHeight = Math.round(height * scaleFactor);

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }

    Boolean cleanCache() {
        File directory;
        if(external){
            directory = getAlbumStorageDir(directoryName);
        }
        else {
            directory = context.getDir(directoryName, Context.MODE_PRIVATE);
        }
        File[] files = directory.listFiles();
        Boolean success = true;
        for (File file : files){
            Boolean result = file.delete();
            if (!result) {
                success = false;
            }
        }
        return success;
    }

    private File getAlbumStorageDir(String albumName) {
        return new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
    }
}

