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
        ZonedDateTime wptTime = wayPoint.getTime().get();
        final GpsPosition gpsPosition = GpsPosition.builder()
            .latitude(wayPoint.getLatitude().doubleValue())
                .longitude(wayPoint.getLongitude().doubleValue())
                .time(LocalTime.of(wptTime.getHour(), wptTime.getMinute(), wptTime.getSecond()))
                .date(LocalDate.of(wptTime.getYear(), wptTime.getMonth(), wptTime.getDayOfMonth()))
                .build();
        return gpsPosition;
    }
}
