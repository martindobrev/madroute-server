package com.maddob.madroute.services;

import com.maddob.madroute.api.v1.mapper.GpsPositionMapper;
import com.maddob.madroute.api.v1.mapper.MadRouteMapper;
import com.maddob.madroute.api.v1.model.MadRouteDTO;
import com.maddob.madroute.domain.GpsPosition;
import com.maddob.madroute.domain.MadRoute;
import com.maddob.madroute.parsers.NMEAParser;
import com.maddob.madroute.repositories.MadRouteRepository;
import com.maddob.madroute.util.GeoUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MadRouteServiceImpl implements MadRouteService {

    private final MadRouteRepository madRouteRepository;
    private final NMEAParser nmeaParser;
    private final MadRouteMapper madRouteMapper;
    private final GpsPositionMapper gpsPositionMapper;

    public MadRouteServiceImpl(MadRouteRepository madRouteRepository, NMEAParser nmeaParser, MadRouteMapper madRouteMapper, GpsPositionMapper gpsPositionMapper) {
        this.madRouteRepository = madRouteRepository;
        this.nmeaParser = nmeaParser;
        this.madRouteMapper = madRouteMapper;
        this.gpsPositionMapper = gpsPositionMapper;
    }

    @Override
    public MadRoute save(final MadRoute routeToBeSaved) {
        if (routeToBeSaved.getGpsData() != null) {
            final List<GpsPosition> gpsPositionList = nmeaParser.parse(routeToBeSaved.getGpsData());

            double distance = 0;
            if (gpsPositionList.size() > 1) {
                for (int i = 1; i < gpsPositionList.size(); i++) {
                    distance += GeoUtils.distance(gpsPositionList.get(i - 1), gpsPositionList.get(i));
                }
            }

            final GpsPosition startPosition = gpsPositionList.get(0);
            final GpsPosition finalPosition = gpsPositionList.get(gpsPositionList.size() - 1);
            if (startPosition != null && finalPosition != null) {
                final LocalDateTime start = LocalDateTime.of(startPosition.getDate(), startPosition.getTime());
                final LocalDateTime end = LocalDateTime.of(finalPosition.getDate(), finalPosition.getTime());
                routeToBeSaved.setDuration(Duration.between(start, end).getSeconds());
            }
            routeToBeSaved.setDistance(distance);

        }

        return madRouteRepository.save(routeToBeSaved);
    }

    @Override
    public List<MadRouteDTO> getMadRoutes() {
        final List<MadRouteDTO> madRouteDTOs = new ArrayList<>();
        this.madRouteRepository.findAll().forEach( madRoute -> {
            MadRouteDTO madRouteDTO = madRouteMapper.modelToDto(madRoute, false);
            if (madRouteDTO != null) {
                madRouteDTOs.add(madRouteDTO);
            }
        });
        return madRouteDTOs;
    }

    @Override
    public MadRouteDTO getMadRoute(Long id) {
        return madRouteMapper.modelToDto(this.madRouteRepository.findOne(id), true);
    }
}
