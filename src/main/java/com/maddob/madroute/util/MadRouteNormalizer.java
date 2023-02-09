package com.maddob.madroute.util;

import com.maddob.madroute.domain.GpsPosition;

import java.util.List;

/**
 * Basic interface for route normalization
 *
 * Different formats store gps position in different time intervals.
 * Some of them really optimize the provided data really good. Unfortunately this makes
 * video synchronization really difficult. To enable simple synchronization,
 * position shall be provided for each second, so that no calculations are necessary
 * during video playback on client.
 *
 */
public interface MadRouteNormalizer {
    List<GpsPosition> normalize(List<GpsPosition> gpsPositionList);
}
