package org.reactnative.camera.tasks;

//import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.common.InputImage;

import org.reactnative.posedetector.RNPoseDetector;
import org.reactnative.camera.utils.ImageDimensions;

import java.util.List;

public class PoseDetectorAsyncTask extends android.os.AsyncTask<Void, Void, Void> {

  private byte[] mImageData;
  private int mWidth;
  private int mHeight;
  private int mRotation;
  private RNPoseDetector mPoseDetector;
  private PoseDetectorAsyncTaskDelegate mDelegate;
  private double mScaleX;
  private double mScaleY;
  private ImageDimensions mImageDimensions;
  private int mPaddingLeft;
  private int mPaddingTop;
  private String TAG = "RNCamera";

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
    mImageData = imageData;
    mWidth = width;
    mHeight = height;
    mRotation = rotation;
    mDelegate = delegate;
    mPoseDetector = poseDetector;
    mImageDimensions = new ImageDimensions(width, height, rotation, facing);
    mScaleX = (double) (viewWidth) / (mImageDimensions.getWidth() * density);
    mScaleY = 1 / density;
    mPaddingLeft = viewPaddingLeft;
    mPaddingTop = viewPaddingTop;
  }

  @Override
  protected Void doInBackground(Void... ignored) {
    if (isCancelled() || mDelegate == null || mPoseDetector == null) {
      return null;
    }

    InputImage image = InputImage.fromByteArray(mImageData,
            mWidth,
            mHeight,
            mRotation,
            InputImage.IMAGE_FORMAT_YV12
    );

    PoseDetector detector = mPoseDetector.getDetector();
    detector.process(image)
            .addOnSuccessListener(new OnSuccessListener<Pose>() {
              @Override
              public void onSuccess(Pose pose) {
                WritableArray serializedPoses = serializeEventData(pose);
                mDelegate.onPosesDetected(serializedPoses);
                mDelegate.onPoseDetectionTaskCompleted();
              }
            })
            .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(Exception e) {
                Log.e(TAG, "Text recognition task failed" + e);
                mDelegate.onPoseDetectionTaskCompleted();
              }
            });
    return null;
  }

  private WritableMap landmarkToMap(PoseLandmark landmark) {
    WritableMap landmarkData = Arguments.createMap();
    PointF pos = landmark.getPosition();
    landmarkData.putDouble("x", pos.x);
    landmarkData.putDouble("y", pos.y);
    return landmarkData;
  }

  private WritableMap serializeEventData(Pose pose) {
    WritableMap poseData = Arguments.createMap();

    poseData.putMap("leftShoulder", landmarkToMap(pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)));
    poseData.putMap("rightShoulder", landmarkToMap(pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)));
    poseData.putMap("leftElbow", landmarkToMap(pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)));
    poseData.putMap("rightElbow", landmarkToMap(pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)));
    poseData.putMap("leftWrist", landmarkToMap(pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)));
    poseData.putMap("rightWrist", landmarkToMap(pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)));
    poseData.putMap("leftHip", landmarkToMap(pose.getPoseLandmark(PoseLandmark.LEFT_HIP)));
    poseData.putMap("rightHip", landmarkToMap(pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)));
    poseData.putMap("leftKnee", landmarkToMap(pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)));
    poseData.putMap("rightKnee", landmarkToMap(pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)));
    poseData.putMap("leftAnkle", landmarkToMap(pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)));
    poseData.putMap("rightAnkle", landmarkToMap(pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)));
    poseData.putMap("leftPinky", landmarkToMap(pose.getPoseLandmark(PoseLandmark.LEFT_PINKY)));
    poseData.putMap("rightPinky", landmarkToMap(pose.getPoseLandmark(PoseLandmark.RIGHT_PINKY)));
    poseData.putMap("leftIndex", landmarkToMap(pose.getPoseLandmark(PoseLandmark.LEFT_INDEX)));
    poseData.putMap("rightIndex", landmarkToMap(pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX)));
    poseData.putMap("leftThumb", landmarkToMap(pose.getPoseLandmark(PoseLandmark.LEFT_THUMB)));
    poseData.putMap("rightThumb", landmarkToMap(pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB)));
    poseData.putMap("leftHeel", landmarkToMap(pose.getPoseLandmark(PoseLandmark.LEFT_HEEL)));
    poseData.putMap("rightHeel", landmarkToMap(pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL)));
    poseData.putMap("leftFootIndex", landmarkToMap(pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX)));
    poseData.putMap("rightFootIndex", landmarkToMap(pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX)));
    poseData.putMap("nose", landmarkToMap(pose.getPoseLandmark(PoseLandmark.NOSE)));
    poseData.putMap("leftEyeInner", landmarkToMap(pose.getPoseLandmark(PoseLandmark.LEFT_EYE_INNER)));
    poseData.putMap("leftEye", landmarkToMap(pose.getPoseLandmark(PoseLandmark.LEFT_EYE)));
    poseData.putMap("leftEyeOuter", landmarkToMap(pose.getPoseLandmark(PoseLandmark.LEFT_EYE_OUTER)));
    poseData.putMap("rightEyeInner", landmarkToMap(pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_INNER)));
    poseData.putMap("rightEye", landmarkToMap(pose.getPoseLandmark(PoseLandmark.RIGHT_EYE)));
    poseData.putMap("rightEyeOuter", landmarkToMap(pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_OUTER)));
    poseData.putMap("leftEar", landmarkToMap(pose.getPoseLandmark(PoseLandmark.LEFT_EAR)));
    poseData.putMap("rightEar", landmarkToMap(pose.getPoseLandmark(PoseLandmark.RIGHT_EAR)));
    poseData.putMap("leftMouth", landmarkToMap(pose.getPoseLandmark(PoseLandmark.LEFT_MOUTH)));
    poseData.putMap("rightMouth", landmarkToMap(pose.getPoseLandmark(PoseLandmark.RIGHT_MOUTH)));

    return poseData;
  }
}
