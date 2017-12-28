package com.maddob.madroute.api.v1.mapper;

import com.maddob.madroute.api.v1.model.MadRouteDTO;
import com.maddob.madroute.domain.GpsPosition;
import com.maddob.madroute.domain.MadRoute;
import com.maddob.madroute.parsers.NMEAParser;
import com.maddob.madroute.util.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MadRouteMapper {

    @Autowired
    private final GpsPositionMapper gpsPositionMapper;

    @Autowired
    private final NMEAParser nmeaParser;

    @Autowired
    private final DataUtils dataUtils;


    public MadRouteMapper(GpsPositionMapper gpsPositionMapper, NMEAParser nmeaParser, DataUtils dataUtils) {
        this.gpsPositionMapper = gpsPositionMapper;
        this.nmeaParser = nmeaParser;
        this.dataUtils = dataUtils;
    }

    public MadRouteDTO modelToDto(final MadRoute madRouteModel, final boolean includeGpsData) {
        final MadRouteDTO dto = new MadRouteDTO();
        dto.setDescription(madRouteModel.getDescription());
        dto.setDistance(madRouteModel.getDistance());
        dto.setId(madRouteModel.getId());
        dto.setVideoId(madRouteModel.getVideoId());
        dto.setDuration(madRouteModel.getDuration());
        dto.setLocation(madRouteModel.getLocation());
        dto.setName(madRouteModel.getName());
        if (includeGpsData && madRouteModel.getGpsData() != null) {
            List<GpsPosition> gpsPositionList = nmeaParser.parse(madRouteModel.getGpsData());
            dto.setGpsData(gpsPositionList.stream().map(gpsPositionMapper::modelToDto).collect(Collectors.toList()));
        }
        return dto;
    }

    public MadRoute dtoToModel(final MadRouteDTO madRouteDTO) {
        final MadRoute madRoute = new MadRoute();
        madRoute.setId(madRouteDTO.getId());
        madRoute.setName(madRouteDTO.getName());
        madRoute.setLocation(madRouteDTO.getLocation());
        madRoute.setDistance(madRouteDTO.getDistance());
        madRoute.setDuration(madRouteDTO.getDuration());
        madRoute.setVideoId(madRouteDTO.getVideoId());
        madRoute.setDescription(madRouteDTO.getDescription());

        if (madRouteDTO.getBase64GpsData() != null) {
            madRoute.setGpsData(dataUtils.base64StringToByteArray(madRouteDTO.getBase64GpsData()));
        }

        return madRoute;
    }

}
