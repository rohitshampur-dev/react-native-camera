package org.reactnative.camera.events;

import androidx.core.util.Pools;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import org.reactnative.camera.CameraViewManager;

public class ObjectsDetectedEvent extends Event<ObjectsDetectedEvent> {
  private static final Pools.SynchronizedPool<ObjectsDetectedEvent> EVENTS_POOL =
      new Pools.SynchronizedPool<>(3);

  private WritableArray mData;

  private ObjectsDetectedEvent() {}

  public static ObjectsDetectedEvent obtain(int viewTag, WritableArray data) {
    ObjectsDetectedEvent event = EVENTS_POOL.acquire();
    if (event == null) {
      event = new ObjectsDetectedEvent();
    }
    event.init(viewTag, data);
    return event;
  }

  private void init(int viewTag, WritableArray data) {
    super.init(viewTag);
    mData = data;
  }

  @Override
  public short getCoalescingKey() {
    if (mData.size() > Short.MAX_VALUE) {
      return Short.MAX_VALUE;
    }

    return (short) mData.size();
  }

  @Override
  public String getEventName() {
    return CameraViewManager.Events.EVENT_ON_OBJECTS_DETECTED.toString();
  }

  @Override
  public void dispatch(RCTEventEmitter rctEventEmitter) {
    rctEventEmitter.receiveEvent(getViewTag(), getEventName(), serializeEventData());
  }

  private WritableMap serializeEventData() {
    WritableMap event = Arguments.createMap();
    event.putString("type", "object");
    event.putArray("objects", mData);
    event.putInt("target", getViewTag());
    return event;
  }
}
