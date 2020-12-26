package org.reactnative.camera.tasks;

import com.facebook.react.bridge.WritableMap;
import org.reactnative.posedetector.RNPoseDetector;

public interface PoseDetectorAsyncTaskDelegate {

    void onPoseDetected(WritableMap pose);

    void onPoseDetectionError(RNPoseDetector poseDetector);

    void onPoseDetectionTaskCompleted();
}
