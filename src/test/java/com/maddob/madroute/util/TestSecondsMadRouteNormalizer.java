package com.maddob.madroute.util;

import com.maddob.madroute.domain.GpsPosition;
import org.junit.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestSecondsMadRouteNormalizer {


    private SecondsMadRouteNormalizer secondsMadRouteNormalizer = new SecondsMadRouteNormalizer();

    private List<GpsPosition> createEachSecondGpsPositionList() {
        final List<GpsPosition> positions = new ArrayList<>();
        LocalTime testTime = LocalTime.of(10, 0, 0);
        positions.add(GpsPosition.builder().latitude(10d).longitude(10d).altitude(10d).time(testTime).build());
        positions.add(GpsPosition.builder().latitude(11d).longitude(10d).altitude(10d).time(testTime.plusSeconds(1)).build());
        positions.add(GpsPosition.builder().latitude(11d).longitude(11d).altitude(10d).time(testTime.plusSeconds(2)).build());
        positions.add(GpsPosition.builder().latitude(11d).longitude(11d).altitude(11d).time(testTime.plusSeconds(3)).build());
        return positions;
    }

    private List<GpsPosition> createGpsPositionListWithThreeSecondsInterval() {
        final List<GpsPosition> positions = new ArrayList<>();
        LocalTime testTime = LocalTime.of(10, 0, 0);
        positions.add(GpsPosition.builder().latitude(10d).longitude(10d).altitude(10d).time(testTime).build());
        positions.add(GpsPosition.builder().latitude(13d).longitude(13d).altitude(13d).time(testTime.plusSeconds(3)).build());
        return positions;
    }

    @Test
    public void testEmptyArrayIsReturnedWhenPositionsToNormalizeAreNull() {
        // When trying to normalize null
        final List<GpsPosition> normalizedPositions = secondsMadRouteNormalizer.normalize(null);

        // Then
        assertNotNull(normalizedPositions);
    }

    @Test
    public void testEmptyArrayIsReturnedWhenPositionsToNormalizeAreEmpty() {
        // When trying to normalize empty position list
        final List<GpsPosition> normalizedPositions = secondsMadRouteNormalizer.normalize(null);

        // Then
        assertNotNull(normalizedPositions);
    }

    @Test
    public void testGpsPositionsEachSecondAreNotProcessed() {
        // Given an evenly spread position data (each second)
        final List<GpsPosition> evenlySpreadPositionData = createEachSecondGpsPositionList();

        // When
        final List<GpsPosition> normalizedPositions = secondsMadRouteNormalizer.normalize(evenlySpreadPositionData);

        // Then
        assertEquals(evenlySpreadPositionData.size(), normalizedPositions.size());
        for (int i = 0; i < normalizedPositions.size(); i++) {
            final GpsPosition testPosition = evenlySpreadPositionData.get(i);
            final GpsPosition normalizedTestPosition = normalizedPositions.get(i);
            assertEquals(testPosition.getLatitude(), normalizedTestPosition.getLatitude());
            assertEquals(testPosition.getLatitude(), normalizedTestPosition.getLatitude());
            assertEquals(testPosition.getAltitude(), normalizedTestPosition.getAltitude());
            assertEquals(testPosition.getTime(), normalizedTestPosition.getTime());
        }
    }

    @Test
    public void testAdditionalGpsPositionsAddedWhenDataIntervalMoreThanOneSecond() {
        // Given a not evenly spread position data (3 second interval)
        final List<GpsPosition> unevenlySpreadPositionData = createGpsPositionListWithThreeSecondsInterval();

        // When
        final List<GpsPosition> normalizedPositions = secondsMadRouteNormalizer.normalize(unevenlySpreadPositionData);

        // Then
        assertEquals(4, normalizedPositions.size());
        for (int i = 0; i < normalizedPositions.size(); i++) {
            final GpsPosition normalizedTestPosition = normalizedPositions.get(i);
            assertEquals(Double.valueOf(10d + i) , normalizedTestPosition.getLatitude());
            assertEquals(Double.valueOf(10d + i), normalizedTestPosition.getLatitude());
            assertEquals(LocalTime.of(10, 0, 0).plusSeconds(i), normalizedTestPosition.getTime());
            if (i == 1 || i == 2) {
                assertEquals("Generated position " + i + " shall have property 'generated' set to true",
                        true, normalizedTestPosition.getGenerated());
            }
        }
    }
}
