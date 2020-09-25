package org.reactnative.camera.events;

import androidx.core.util.Pools;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import org.reactnative.camera.CameraViewManager;
import org.reactnative.objectdetector.RNObjectDetector;

public class ObjectDetectionErrorEvent extends Event<ObjectDetectionErrorEvent> {
  private static final Pools.SynchronizedPool<ObjectDetectionErrorEvent> EVENTS_POOL = new Pools.SynchronizedPool<>(3);
  private RNObjectDetector mobjectDetector;

  private ObjectDetectionErrorEvent() {
  }

  public static ObjectDetectionErrorEvent obtain(int viewTag, RNObjectDetector objectDetector) {
    ObjectDetectionErrorEvent event = EVENTS_POOL.acquire();
    if (event == null) {
      event = new ObjectDetectionErrorEvent();
    }
    event.init(viewTag, objectDetector);
    return event;
  }

  private void init(int viewTag, RNObjectDetector objectDetector) {
    super.init(viewTag);
    mobjectDetector = objectDetector;
  }

  @Override
  public short getCoalescingKey() {
    return 0;
  }

  @Override
  public String getEventName() {
    return CameraViewManager.Events.EVENT_ON_OBJECT_DETECTION_ERROR.toString();
  }

  @Override
  public void dispatch(RCTEventEmitter rctEventEmitter) {
    rctEventEmitter.receiveEvent(getViewTag(), getEventName(), serializeEventData());
  }

  private WritableMap serializeEventData() {
    WritableMap map = Arguments.createMap();
    map.putBoolean("isOperational", mobjectDetector != null && mobjectDetector.isOperational());
    return map;
  }
}
