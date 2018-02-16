package com.maddob.madroute.api.v1.mapper;

import com.maddob.madroute.api.v1.model.GpsPositionDTO;
import com.maddob.madroute.domain.GpsPosition;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;

public class GpsPositionMapperTest {


    private final GpsPositionMapper mapper = new GpsPositionMapper();

    private final Double latitude = 35.0;
    private final Double longitude = 56.7;
    private final LocalDate date = LocalDate.of(2017, 10, 10);
    private final LocalTime time = LocalTime.of(11, 35, 23);
    private final LocalDateTime localDateTime = LocalDateTime.of(date, time);
    private final Double velocity = 12.3;
    private final Boolean fixed = true;
    private final Integer quality = 9;
    private final Double direction = 13.7;
    private final Double altitude = 56.8;

    @Test
    public void testModelToDTO() {

        // given
        GpsPosition gpsPosition = GpsPosition.builder()
            .latitude(latitude)
            .longitude(longitude)
            .date(date)
            .time(time)
            .velocity(velocity)
            .fixed(fixed)
            .quality(quality)
            .direction(direction)
            .altitude(altitude)
            .build();

        // when
        GpsPositionDTO dto = mapper.modelToDto(gpsPosition);

        // then
        assertEquals(latitude, dto.getLatitude());
        assertEquals(longitude, dto.getLongitude());
        assertEquals(Long.valueOf(localDateTime.toEpochSecond(ZoneOffset.UTC)), dto.getTimestamp());
        assertEquals(velocity, dto.getVelocity());
        assertEquals(fixed, dto.getFixed());
        assertEquals(quality, dto.getQuality());
        assertEquals(direction, dto.getDirection());
        assertEquals(altitude, dto.getAltitude());
    }

    @Test
    public void testDtoToModel() {
        // given
        GpsPositionDTO gpsPositionDTO = new GpsPositionDTO();
        gpsPositionDTO.setLatitude(latitude);
        gpsPositionDTO.setLongitude(longitude);
        gpsPositionDTO.setTimestamp(localDateTime.toEpochSecond(ZoneOffset.UTC));
        gpsPositionDTO.setVelocity(velocity);
        gpsPositionDTO.setFixed(fixed);
        gpsPositionDTO.setQuality(quality);
        gpsPositionDTO.setDirection(direction);
        gpsPositionDTO.setAltitude(altitude);

        // when
        GpsPosition model = mapper.dtoToModel(gpsPositionDTO);

        // then
        assertEquals(latitude, model.getLatitude());
        assertEquals(longitude, model.getLongitude());
        assertEquals(date, model.getDate());
        assertEquals(time, model.getTime());
        assertEquals(velocity, model.getVelocity());
        assertEquals(fixed, model.getFixed());
        assertEquals(quality, model.getQuality());
        assertEquals(direction, model.getDirection());
        assertEquals(altitude, model.getAltitude());
    }

}
