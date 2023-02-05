package com.maddob.madroute.services;

import com.maddob.madroute.api.v1.mapper.MadRouteMapper;
import com.maddob.madroute.api.v1.model.MadRouteDTO;
import com.maddob.madroute.domain.GpsPosition;
import com.maddob.madroute.domain.MadRoute;
import com.maddob.madroute.parsers.GPXParser;
import com.maddob.madroute.parsers.NMEAParser;
import com.maddob.madroute.repositories.MadRouteRepository;
import com.maddob.madroute.util.GeoUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MadRouteServiceImpl implements MadRouteService {

    private final MadRouteRepository madRouteRepository;
    private final NMEAParser nmeaParser;
    private final GPXParser gpxParser;
    private final MadRouteMapper madRouteMapper;
    private final GeoUtils geoUtils;

    public MadRouteServiceImpl(MadRouteRepository madRouteRepository, NMEAParser nmeaParser, GPXParser gpxParser, MadRouteMapper madRouteMapper, GeoUtils geoUtils) {
        this.madRouteRepository = madRouteRepository;
        this.nmeaParser = nmeaParser;
        this.gpxParser = gpxParser;
        this.madRouteMapper = madRouteMapper;
        this.geoUtils = geoUtils;
    }

    @Override
    public MadRoute save(final MadRoute routeToBeSaved) {
        if (routeToBeSaved.getGpsData() != null) {

            List<GpsPosition> gpsPositionList;
            try {
                gpsPositionList = gpxParser.parse(routeToBeSaved.getGpsData());
            } catch (IOException ioException) {
                // data is not GPX formatted, try NMEA
                gpsPositionList = nmeaParser.parse(routeToBeSaved.getGpsData());
            }

            double distance = 0;
            if (gpsPositionList.size() > 1) {
                for (int i = 1; i < gpsPositionList.size(); i++) {
                    distance += geoUtils.distance(gpsPositionList.get(i - 1), gpsPositionList.get(i));
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
        }

        return madRouteRepository.save(routeToBeSaved);
    }

    @Override
    public MadRouteDTO saveDto(MadRouteDTO madRouteDTO) {
        return madRouteMapper.modelToDto(save(madRouteMapper.dtoToModel(madRouteDTO)), true);
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
        Optional<MadRoute> madRoute = this.madRouteRepository.findById(id);
        if (!madRoute.isPresent()) {
            return null;
        }
        return madRouteMapper.modelToDto(madRoute.get(), true);
    }
}
