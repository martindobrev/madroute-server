package com.maddob.madroute.api.v1.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class GpsPositionDTO {
    private Double latitude;
    private Double longitude;
    private Long timestamp;
    private Double velocity;
    private Boolean fixed;
    private Integer quality;
    private Double direction;
    private Double altitude;
}
