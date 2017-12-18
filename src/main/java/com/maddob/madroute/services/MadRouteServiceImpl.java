package com.maddob.madroute.services;

import com.maddob.madroute.command.MadRouteCommand;
import com.maddob.madroute.converters.MadRouteToCommandWithGpsData;
import com.maddob.madroute.converters.MadRouteToMadRouteCommand;
import com.maddob.madroute.domain.GpsPosition;
import com.maddob.madroute.domain.MadRoute;
import com.maddob.madroute.parsers.NMEAParser;
import com.maddob.madroute.repositories.MadRouteRepository;
import com.maddob.madroute.util.GeoUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MadRouteServiceImpl implements MadRouteService {

    private MadRouteRepository madRouteRepository;
    private MadRouteToMadRouteCommand madRouteToMadRouteCommandConverter;
    private MadRouteToCommandWithGpsData madRouteToMadRouteCommandWithGpsData;
    private NMEAParser nmeaParser;

    public MadRouteServiceImpl(MadRouteRepository madRouteRepository,
                               MadRouteToMadRouteCommand madRouteToMadRouteCommandConverter,
                               MadRouteToCommandWithGpsData madRouteToMadRouteCommandWithGpsData,
                               NMEAParser nmeaParser) {
        this.madRouteRepository = madRouteRepository;
        this.madRouteToMadRouteCommandConverter = madRouteToMadRouteCommandConverter;
        this.madRouteToMadRouteCommandWithGpsData = madRouteToMadRouteCommandWithGpsData;
        this.nmeaParser = nmeaParser;
    }

    @Override
    public List<MadRouteCommand> getAllMadRoutes() {
        List<MadRouteCommand> madRouteCommands = new ArrayList<>();
        this.madRouteRepository.findAll().forEach( madRoute -> {
            MadRouteCommand madRouteCommand = madRouteToMadRouteCommandConverter.convert(madRoute);
            if (madRouteCommand != null) {
                madRouteCommands.add(madRouteCommand);
            }
        });
        return madRouteCommands;
    }

    @Override
    public MadRouteCommand getRoute(Long id) {
        return madRouteToMadRouteCommandWithGpsData.convert(madRouteRepository.findOne(id));
    }

    @Override
    public void save(final MadRoute routeToBeSaved) {
        if (routeToBeSaved.getGpsData() != null) {
            List<GpsPosition> gpsPositionList = nmeaParser.parse(routeToBeSaved.getGpsData());

            double distance = 0;
            if (gpsPositionList.size() > 1) {
                for (int i = 1; i < gpsPositionList.size(); i++) {
                    distance += GeoUtils.distance(gpsPositionList.get(i - 1), gpsPositionList.get(i));
                }
            }
            routeToBeSaved.setDistance(distance);

        }

        madRouteRepository.save(routeToBeSaved);
    }


}
