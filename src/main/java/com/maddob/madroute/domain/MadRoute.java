package com.maddob.madroute.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode
public class MadRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;
    private Double distance;
    private Long duration;

    @Lob
    private String videoId;

    @Lob
    private String description;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private Byte[] gpsData;
}
