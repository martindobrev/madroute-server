package com.maddob.madroute.api.v1.mapper;

import com.maddob.madroute.api.v1.model.GpsPositionDTO;
import com.maddob.madroute.domain.GpsPosition;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class GpsPositionMapper {

    GpsPositionDTO modelToDto(final GpsPosition gpsPosition) {
        return GpsPositionDTO.builder()
            .latitude(gpsPosition.getLatitude())
            .longitude(gpsPosition.getLongitude())
            .altitude(gpsPosition.getAltitude())
            .timestamp(LocalDateTime.of(gpsPosition.getDate(), gpsPosition.getTime()).toEpochSecond(ZoneOffset.UTC))
            .direction(gpsPosition.getDirection())
            .fixed(gpsPosition.getFixed())
            .quality(gpsPosition.getQuality())
            .velocity(gpsPosition.getVelocity())
            .generated(gpsPosition.getGenerated())
            .build();
    }

    GpsPosition dtoToModel(final GpsPositionDTO gpsPositionDTO) {

        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(gpsPositionDTO.getTimestamp(), 0, ZoneOffset.UTC);

        return GpsPosition.builder()
            .latitude(gpsPositionDTO.getLatitude())
            .longitude(gpsPositionDTO.getLongitude())
            .altitude(gpsPositionDTO.getAltitude())
            .date(localDateTime.toLocalDate())
            .time(localDateTime.toLocalTime())
            .direction(gpsPositionDTO.getDirection())
            .fixed(gpsPositionDTO.getFixed())
            .quality(gpsPositionDTO.getQuality())
            .velocity(gpsPositionDTO.getVelocity())
            .generated(gpsPositionDTO.getGenerated())
            .build();
    }
}
