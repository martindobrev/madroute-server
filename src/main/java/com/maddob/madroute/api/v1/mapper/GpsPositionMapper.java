package com.maddob.madroute.api.v1.mapper;

import com.maddob.madroute.api.v1.model.GpsPositionDTO;
import com.maddob.madroute.domain.GpsPosition;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class GpsPositionMapper {

    public GpsPositionDTO modelToDto(final GpsPosition gpsPosition) {
        final GpsPositionDTO dto = new GpsPositionDTO();
        dto.setLatitude(gpsPosition.getLatitude());
        dto.setLongitude(gpsPosition.getLongitude());
        dto.setAltitude(gpsPosition.getAltitude());
        dto.setTimestamp(LocalDateTime.of(gpsPosition.getDate(), gpsPosition.getTime()).toEpochSecond(ZoneOffset.UTC));
        dto.setDirection(gpsPosition.getDirection());
        dto.setFixed(gpsPosition.getFixed());
        dto.setQuality(gpsPosition.getQuality());
        dto.setVelocity(gpsPosition.getVelocity());
        return dto;
    }

    public GpsPosition dtoToModel(final GpsPositionDTO gpsPositionDTO) {

        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(gpsPositionDTO.getTimestamp(), 0, ZoneOffset.UTC);

        final GpsPosition gpsPosition = GpsPosition.builder()
                .latitude(gpsPositionDTO.getLatitude())
                .longitude(gpsPositionDTO.getLongitude())
                .altitude(gpsPositionDTO.getAltitude())
                .date(localDateTime.toLocalDate())
                .time(localDateTime.toLocalTime())
                .direction(gpsPositionDTO.getDirection())
                .fixed(gpsPositionDTO.getFixed())
                .quality(gpsPositionDTO.getQuality())
                .velocity(gpsPositionDTO.getVelocity())
                .build();
        return gpsPosition;
    }
}
