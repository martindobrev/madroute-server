package com.maddob.madroute.parsers;

import com.maddob.madroute.domain.GpsPosition;
import com.maddob.madroute.util.DataUtils;
import io.jenetics.jpx.GPX;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class GPXParser {

    private final DataUtils dataUtils;

    public GPXParser(DataUtils dataUtils) {
        this.dataUtils = dataUtils;
    }


    public List<GpsPosition> parse(Byte[] fileData) throws IOException {
        GPX gpx = GPX.read(new ByteArrayInputStream(dataUtils.unwrapByteArray(fileData)));

        List<TrackSegment> trackSegments = gpx.getTracks().stream().map(track -> track.getSegments()).reduce(new ArrayList<>(), (a, b) -> {
            a.addAll(b);
            return a;
        });

        return trackSegments.stream().map(trackSegment -> trackSegment.getPoints()).reduce(new ArrayList<>(), (a, b) -> {
            a.addAll(b);
            return a;
        }).stream().map(this::trackPointToGpsPosition).collect(Collectors.toList());
    }

    private GpsPosition trackPointToGpsPosition(final WayPoint wayPoint) {
        if (wayPoint == null) {
            return null;
        }
        final GpsPosition gpsPosition = new GpsPosition();
        gpsPosition.setLatitude(wayPoint.getLatitude().doubleValue());
        gpsPosition.setLongitude(wayPoint.getLongitude().doubleValue());
        ZonedDateTime wptTime = wayPoint.getTime().get();
        gpsPosition.setTime(LocalTime.of(wptTime.getHour(), wptTime.getMinute(), wptTime.getSecond()));
        gpsPosition.setDate(LocalDate.of(wptTime.getYear(), wptTime.getMonth(), wptTime.getDayOfMonth()));
        return gpsPosition;
    }
}
