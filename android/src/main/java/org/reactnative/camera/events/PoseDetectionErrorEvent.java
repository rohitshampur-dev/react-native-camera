package org.reactnative.camera.events;

import androidx.core.util.Pools;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import org.reactnative.camera.CameraViewManager;
import org.reactnative.posedetector.RNPoseDetector;

public class PoseDetectionErrorEvent extends Event<PoseDetectionErrorEvent> {
  private static final Pools.SynchronizedPool<PoseDetectionErrorEvent> EVENTS_POOL = new Pools.SynchronizedPool<>(3);
  private RNPoseDetector mPoseDetector;

  private PoseDetectionErrorEvent() {
  }

  public static PoseDetectionErrorEvent obtain(int viewTag, RNPoseDetector poseDetector) {
    PoseDetectionErrorEvent event = EVENTS_POOL.acquire();
    if (event == null) {
      event = new PoseDetectionErrorEvent();
    }
    event.init(viewTag, poseDetector);
    return event;
  }

  private void init(int viewTag, RNPoseDetector poseDetector) {
    super.init(viewTag);
    mPoseDetector = poseDetector;
  }

  @Override
  public short getCoalescingKey() {
    return 0;
  }

  @Override
  public String getEventName() {
    return CameraViewManager.Events.EVENT_ON_POSE_DETECTION_ERROR.toString();
  }

  @Override
  public void dispatch(RCTEventEmitter rctEventEmitter) {
    rctEventEmitter.receiveEvent(getViewTag(), getEventName(), serializeEventData());
  }

  private WritableMap serializeEventData() {
    WritableMap map = Arguments.createMap();
    map.putBoolean("isOperational", mPoseDetector != null && mPoseDetector.isOperational());
    return map;
  }
}
