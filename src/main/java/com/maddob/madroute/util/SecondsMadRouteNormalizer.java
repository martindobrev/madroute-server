package com.maddob.madroute.util;

import com.maddob.madroute.domain.GpsPosition;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SecondsMadRouteNormalizer implements MadRouteNormalizer {
    @Override
    public List<GpsPosition> normalize(final List<GpsPosition> gpsPositionList) {
        if (gpsPositionList == null ||gpsPositionList.isEmpty()) {
            return new ArrayList<>();
        }
        final List<GpsPosition> normalizedPositions = new ArrayList<>();
        for (int i = 0; i < gpsPositionList.size(); i++) {
            final GpsPosition current = gpsPositionList.get(i);
            GpsPosition next = null;
            if (gpsPositionList.size() - 1 > i) {
                next = gpsPositionList.get(i + 1);
            }
            normalizedPositions.addAll(positionsToBeAddedToList(current, next));
        }
        return normalizedPositions;
    }

    private List<GpsPosition> positionsToBeAddedToList(final GpsPosition current, final GpsPosition next) throws IllegalArgumentException {
        if (current == null) {
            throw new IllegalArgumentException("current position cannot be null");
        }

        final List<GpsPosition> gpsPositions = new ArrayList<>();
        gpsPositions.add(current);
        if (next != null && next.getTime().isAfter(current.getTime())) {
            final int initialDifference = next.getTime().toSecondOfDay() - current.getTime().toSecondOfDay();
            if (initialDifference > 1) {
                final double minLat = Math.min(next.getLatitude(), current.getLatitude());
                final double maxLat = Math.max(next.getLatitude(), current.getLatitude());
                final double minLng = Math.min(next.getLongitude(), current.getLongitude());
                final double maxLng = Math.max(next.getLongitude(), current.getLongitude());
                // final double minAlt = Math.min(next.getAltitude(), current.getAltitude());
                // final double maxAlt = Math.max(next.getAltitude(), current.getAltitude());
                final double latStepDiff = (maxLat - minLat) / (double) initialDifference;
                final double lngStepDiff = (maxLng - minLng) / (double) initialDifference;
                // final double altStepDiff = (maxAlt - minAlt) / (double) initialDifference;
                GpsPosition calculatedNext;
                int i = 1;
                while (i < initialDifference) {
                    calculatedNext = GpsPosition.builder()
                            .latitude(minLat + latStepDiff * (double) i)
                            .longitude(minLng + lngStepDiff * (double) i)
                            // .altitude(minAlt + altStepDiff * (double) i)
                            .time(current.getTime().plusSeconds(i))
                            .date(current.getDate())
                            .generated(true)
                            .build();
                    gpsPositions.add(calculatedNext);
                    i++;
                }
            }
        }
        return gpsPositions;
    }
}
