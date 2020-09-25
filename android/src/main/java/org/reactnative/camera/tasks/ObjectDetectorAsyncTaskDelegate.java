package org.reactnative.camera.tasks;

import com.facebook.react.bridge.WritableArray;
import org.reactnative.objectdetector.RNObjectDetector;

public interface ObjectDetectorAsyncTaskDelegate {

    void onObjectsDetected(WritableArray objects);

    void onObjectDetectionError(RNObjectDetector objectDetector);

    void onObjectDetectionTaskCompleted();
}
