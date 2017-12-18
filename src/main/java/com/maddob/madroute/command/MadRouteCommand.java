package com.maddob.madroute.command;

import com.maddob.madroute.domain.GpsPosition;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(exclude = "gpsData")
public class MadRouteCommand {
    private Long id;
    private String name;
    private String location;
    private String description;
    private String videoId;
    private Double distance;
    private Duration duration;
    private List<GpsPosition> gpsData;
}
