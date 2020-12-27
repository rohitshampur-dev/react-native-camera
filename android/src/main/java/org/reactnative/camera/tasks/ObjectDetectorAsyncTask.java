package org.reactnative.camera.tasks;

import org.reactnative.objectdetector.RNObjectDetector;

public class ObjectDetectorAsyncTask extends android.os.AsyncTask<Void, Void, Void> {

  private RNObjectDetector mObjectDetector;
  private ObjectDetectorAsyncTaskDelegate mDelegate;
  // private String TAG = "RNCamera";

  public ObjectDetectorAsyncTask(
      ObjectDetectorAsyncTaskDelegate delegate,
      RNObjectDetector objectDetector,
      byte[] imageData,
      int width,
      int height,
      int rotation,
      float density,
      int facing,
      int viewWidth,
      int viewHeight,
      int viewPaddingLeft,
      int viewPaddingTop
  ) {
    mDelegate = delegate;
    mObjectDetector = objectDetector;
  }

  @Override
  protected Void doInBackground(Void... ignored) {
    mDelegate.onObjectDetectionError(mObjectDetector);
    return null;
  }
}
