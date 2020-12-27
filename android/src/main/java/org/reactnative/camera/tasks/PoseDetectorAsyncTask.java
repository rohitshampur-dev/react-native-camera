package org.reactnative.camera.tasks;

import org.reactnative.posedetector.RNPoseDetector;

public class PoseDetectorAsyncTask extends android.os.AsyncTask<Void, Void, Void> {

  private RNPoseDetector mPoseDetector;
  private PoseDetectorAsyncTaskDelegate mDelegate;
  // private String TAG = "RNCamera";

  public PoseDetectorAsyncTask(
      PoseDetectorAsyncTaskDelegate delegate,
      RNPoseDetector poseDetector,
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
    mPoseDetector = poseDetector;
  }

  @Override
  protected Void doInBackground(Void... ignored) {
    mDelegate.onPoseDetectionError(mPoseDetector);
    return null;
  }
}
