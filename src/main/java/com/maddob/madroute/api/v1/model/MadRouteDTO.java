package com.maddob.madroute.api.v1.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"gpsData", "base64GpsData"})
public class MadRouteDTO {
    private Long id;
    private String name;
    private String location;
    private String description;
    private String videoId;
    private List<GpsPositionDTO> gpsData;
    private Double distance;
    private Long duration;
    private Long timestamp;
    private String base64GpsData;
}