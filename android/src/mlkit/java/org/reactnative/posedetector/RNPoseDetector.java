package org.reactnative.posedetector;

import android.content.Context;
import android.util.Log;

import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions;


public class RNPoseDetector {

  private PoseDetector mPoseDetector = null;
  private PoseDetectorOptions.Builder mBuilder;

  public RNPoseDetector(Context context) {
    mBuilder = new PoseDetectorOptions.Builder();
  }

  public boolean isOperational() {
    // Legacy api from GMV
    return true;
  }

  public PoseDetector getDetector() {

    if (mPoseDetector == null) {
      createPoseDetector();
    }
    return mPoseDetector;
  }

  public void release() {
    if (mPoseDetector != null) {
      try {
        mPoseDetector.close();
      } catch (Exception e) {
        Log.e("RNCamera", "Attempt to close PoseDetector failed");
      }
      mPoseDetector = null;
    }
  }

  private void createPoseDetector() {
    PoseDetectorOptions options = mBuilder.build();
    mPoseDetector = PoseDetection.getClient(options);
  }
}
