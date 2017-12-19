package com.maddob.madroute.api.v1.mapper;

import com.maddob.madroute.api.v1.model.GpsPositionDTO;
import com.maddob.madroute.domain.GpsPosition;
import org.springframework.stereotype.Component;

@Component
public class GpsPositionMapper {

    public GpsPositionDTO modelToDto(final GpsPosition gpsPosition) {
        final GpsPositionDTO dto = new GpsPositionDTO();
        dto.setLatitude(gpsPosition.getLatitude());
        dto.setLongitude(gpsPosition.getLongitude());
        dto.setAltitude(gpsPosition.getAltitude());
        dto.setDate(gpsPosition.getDate());
        dto.setTime(gpsPosition.getTime());
        dto.setDirection(gpsPosition.getDirection());
        dto.setFixed(gpsPosition.getFixed());
        dto.setQuality(gpsPosition.getQuality());
        dto.setVelocity(gpsPosition.getVelocity());
        return dto;
    }

    public GpsPosition dtoToModel(final GpsPositionDTO gpsPositionDTO) {
        final GpsPosition gpsPosition = new GpsPosition();
        gpsPosition.setLatitude(gpsPositionDTO.getLatitude());
        gpsPosition.setLongitude(gpsPositionDTO.getLongitude());
        gpsPosition.setAltitude(gpsPositionDTO.getAltitude());
        gpsPosition.setDate(gpsPositionDTO.getDate());
        gpsPosition.setTime(gpsPositionDTO.getTime());
        gpsPosition.setDirection(gpsPositionDTO.getDirection());
        gpsPosition.setFixed(gpsPositionDTO.getFixed());
        gpsPosition.setQuality(gpsPositionDTO.getQuality());
        gpsPosition.setVelocity(gpsPositionDTO.getVelocity());
        return gpsPosition;
    }
}
