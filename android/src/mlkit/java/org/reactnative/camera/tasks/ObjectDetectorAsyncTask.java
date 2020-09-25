package org.reactnative.camera.tasks;

//import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.common.InputImage;

import org.reactnative.objectdetector.RNObjectDetector;
import org.reactnative.camera.utils.ImageDimensions;

import java.util.List;

public class ObjectDetectorAsyncTask extends android.os.AsyncTask<Void, Void, Void> {

  private byte[] mImageData;
  private int mWidth;
  private int mHeight;
  private int mRotation;
  private RNObjectDetector mObjectDetector;
  private ObjectDetectorAsyncTaskDelegate mDelegate;
  private double mScaleX;
  private double mScaleY;
  private ImageDimensions mImageDimensions;
  private int mPaddingLeft;
  private int mPaddingTop;
  private String TAG = "RNCamera";

  public ObjectDetectorAsyncTask(
      ObjectDetectorAsyncTaskDelegate delegate,
      RNObjectDetector ObjectDetector,
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
    mObjectDetector = ObjectDetector;
    mImageDimensions = new ImageDimensions(width, height, rotation, facing);
    mScaleX = (double) (viewWidth) / (mImageDimensions.getWidth() * density);
    mScaleY = 1 / density;
    mPaddingLeft = viewPaddingLeft;
    mPaddingTop = viewPaddingTop;
  }

  @Override
  protected Void doInBackground(Void... ignored) {
    if (isCancelled() || mDelegate == null || mObjectDetector == null) {
      return null;
    }

    InputImage image = InputImage.fromByteArray(mImageData,
            mWidth,
            mHeight,
            mRotation,
            InputImage.IMAGE_FORMAT_YV12
    );

    ObjectDetector labeler = mObjectDetector.getDetector();
    labeler.process(image)
            .addOnSuccessListener(new OnSuccessListener<List<DetectedObject>>() {
              @Override
              public void onSuccess(List<DetectedObject> objects) {
                WritableArray serializedObjects = serializeEventData(objects);
                mDelegate.onObjectsDetected(serializedObjects);
                mDelegate.onObjectDetectionTaskCompleted();
              }
            })
            .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(Exception e) {
                Log.e(TAG, "Text recognition task failed" + e);
                mDelegate.onObjectDetectionTaskCompleted();
              }
            });
    return null;
  }

  private WritableArray serializeEventData(List<DetectedObject> objects) {
    WritableArray objectsList = Arguments.createArray();

    for (DetectedObject object: objects) {
      WritableMap serializedDetectedObject = Arguments.createMap();

      Integer trackingID = object.getTrackingId();
      serializedDetectedObject.putInt("trackingId", trackingID);

      WritableMap bounds = processBounds(object.getBoundingBox());
      serializedDetectedObject.putMap("bounds", bounds);
  
      objectsList.pushMap(serializedDetectedObject);
    }

    return objectsList;
  }

  private WritableMap processBounds(Rect frame) {
    WritableMap origin = Arguments.createMap();
    int x = frame.left;
    int y = frame.top;

    if (frame.left < mWidth / 2) {
      x = x + mPaddingLeft / 2;
    } else if (frame.left > mWidth /2) {
      x = x - mPaddingLeft / 2;
    }

    if (frame.top < mHeight / 2) {
      y = y + mPaddingTop / 2;
    } else if (frame.top > mHeight / 2) {
      y = y - mPaddingTop / 2;
    }

    origin.putDouble("x", x * mScaleX);
    origin.putDouble("y", y * mScaleY);

    WritableMap size = Arguments.createMap();
    size.putDouble("width", frame.width() * mScaleX);
    size.putDouble("height", frame.height() * mScaleY);

    WritableMap bounds = Arguments.createMap();
    bounds.putMap("origin", origin);
    bounds.putMap("size", size);
    return bounds;
  }

}
