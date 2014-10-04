package com.yemyatthu.bumc;

import android.app.Application;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import java.util.HashMap;

/**
 * Created by SH on 10/4/14.
 */
public class BUMC extends Application {

  HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

  public synchronized Tracker getTracker(TrackerName trackerId) {
    if (!mTrackers.containsKey(trackerId)) {

      GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
      Tracker t = analytics.newTracker(R.xml.global_tracker);
      mTrackers.put(trackerId, t);
    }
    return mTrackers.get(trackerId);
  }

  /**
   * Enum used to identify the tracker that needs to be used for tracking.
   *
   * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
   * storing them all in Application object helps ensure that they are created only once per
   * application instance.
   */
  public enum TrackerName {
    GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
  }
}
