package org.reactnative.objectdetector;

import android.content.Context;
import android.util.Log;

import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;


public class RNObjectDetector {

  private ObjectDetector mObjectDetector = null;
  private ObjectDetectorOptions.Builder mBuilder;

  public RNObjectDetector(Context context) {
    mBuilder = new ObjectDetectorOptions.Builder();
  }

  public boolean isOperational() {
    // Legacy api from GMV
    return true;
  }

  public ObjectDetector getDetector() {

    if (mObjectDetector == null) {
      createObjectDetector();
    }
    return mObjectDetector;
  }

  public void release() {
    if (mObjectDetector != null) {
      try {
        mObjectDetector.close();
      } catch (Exception e) {
        Log.e("RNCamera", "Attempt to close ObjectDetector failed");
      }
      mObjectDetector = null;
    }
  }

  private void createObjectDetector() {
    ObjectDetectorOptions options = mBuilder.build();
    mObjectDetector = ObjectDetection.getClient(options);
  }
}
