package org.reactnative.camera.events;

import androidx.core.util.Pools;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import org.reactnative.camera.CameraViewManager;

public class PoseDetectedEvent extends Event<PoseDetectedEvent> {
  private static final Pools.SynchronizedPool<PoseDetectedEvent> EVENTS_POOL =
      new Pools.SynchronizedPool<>(3);

  private WritableMap mData;

  private PoseDetectedEvent() {}

  public static PoseDetectedEvent obtain(int viewTag, WritableMap data) {
    PoseDetectedEvent event = EVENTS_POOL.acquire();
    if (event == null) {
      event = new PoseDetectedEvent();
    }
    event.init(viewTag, data);
    return event;
  }

  private void init(int viewTag, WritableMap data) {
    super.init(viewTag);
    mData = data;
  }

  // @Override
  // public short getCoalescingKey() {
  //   if (mData.size() > Short.MAX_VALUE) {
  //     return Short.MAX_VALUE;
  //   }

  //   return (short) mData.size();
  // }

  @Override
  public String getEventName() {
    return CameraViewManager.Events.EVENT_ON_POSE_DETECTED.toString();
  }

  @Override
  public void dispatch(RCTEventEmitter rctEventEmitter) {
    rctEventEmitter.receiveEvent(getViewTag(), getEventName(), serializeEventData());
  }

  private WritableMap serializeEventData() {
    WritableMap event = Arguments.createMap();
    event.putString("type", "pose");
    event.putMap("pose", mData);
    event.putInt("target", getViewTag());
    return event;
  }
}
