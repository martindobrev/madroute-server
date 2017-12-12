package com.maddob.madroute.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@EqualsAndHashCode
public class GpsPosition {

    private Double latitude;
    private Double longitude;
    private LocalDate date;
    private LocalTime time;
    private Double velocity;
    private Boolean fixed;
    private Integer quality;
    private Double direction;
    private Double altitude;

}
