package org.reactnative.camera.tasks;

import org.reactnative.imagelabeler.RNImageLabeler;

public class ImageLabelerAsyncTask extends android.os.AsyncTask<Void, Void, Void> {

  private RNImageLabeler mImageLabeler;
  private ImageLabelerAsyncTaskDelegate mDelegate;
  // private String TAG = "RNCamera";

  public ImageLabelerAsyncTask(
      ImageLabelerAsyncTaskDelegate delegate,
      RNImageLabeler imageLabeler,
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
    mImageLabeler = imageLabeler;
  }

  @Override
  protected Void doInBackground(Void... ignored) {
    mDelegate.onImageLabelingError(mImageLabeler);
    return null;
  }
}
