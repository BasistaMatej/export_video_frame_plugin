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

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.embedding.engine.plugins.FlutterPlugin;

/** ExportVideoFramePlugin */
public class ExportVideoFramePlugin implements FlutterPlugin, MethodCallHandler {
  private MethodChannel channel;
  private Context applicationContext;

  /** Plugin registration. */
  @Override
  public void onAttachedToEngine(FlutterPluginBinding binding) {
    channel = new MethodChannel(binding.getBinaryMessenger(), "export_video_frame");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onDetachedFromEngine(FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
    channel = null;
  }

  @Override
  public void onMethodCall(MethodCall call, final Result result) {
    switch (call.method) {
      case "cleanImageCache": {
        Boolean success = FileStorage.share().cleanCache();
        if (success) {
          result.success("success");
        } else {
          result.error("Clean exception", "Fail", null);
        }
        break;
      }
      case "exportGifImagePathList": {
        String filePath = call.argument("filePath").toString();
        Number quality = call.argument("quality");
        ExportImageTask task = new ExportImageTask();
        task.execute(filePath,quality);
        task.setCallBack(new Callback() {
          @Override
          public void exportPath(ArrayList<String> list) {
            if (list != null) {
              result.success(list);
            } else {
              result.error("Media exception","Get frame fail", null);
            }
          }
        });
        break;
      }
      case "exportImage": {
        String filePath = call.argument("filePath").toString();
        Number number = call.argument("number");
        Number quality = call.argument("quality");
        ExportImageTask task = new ExportImageTask();
        task.execute(filePath,number.intValue(),quality);
        task.setCallBack(new Callback() {
          @Override
          public void exportPath(ArrayList<String> list) {
            if (list != null) {
              result.success(list);
            } else {
              result.error("Media exception","Get frame fail", null);
            }
          }
        });
        break;
      }
      case "exportImageBySeconds": {
        @SuppressWarnings("unchecked")
        Map<String, Object> arguments = (Map<String, Object>) call.arguments;
        String filePath = (String) arguments.get("filePath");
        Integer duration = (Integer) arguments.get("duration");
        Double radian = (Double) arguments.get("radian");

        if (filePath != null && duration != null && radian != null) {
          Log.d("ExportVideoPlugin", "ExportVideoPlugina asdp");
          new Thread(() -> {
            String originImg = ExportManager.exportImagePathBySecond(filePath, duration, (float) radian.floatValue());
            if (originImg != null) {
              result.success(originImg);
            } else {
              result.success(""); // Returning an empty string if result is null
            }
          }).start();
        } else {
          result.success(""); // Returning an empty string if arguments are invalid
        }
        break;
      }
      default:
        result.notImplemented();
        break;
    }

  }

}
