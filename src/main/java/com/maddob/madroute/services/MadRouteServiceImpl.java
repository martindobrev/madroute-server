package com.maddob.madroute.services;

import com.maddob.madroute.command.MadRouteCommand;
import com.maddob.madroute.converters.MadRouteToCommandWithGpsData;
import com.maddob.madroute.converters.MadRouteToMadRouteCommand;
import com.maddob.madroute.domain.MadRoute;
import com.maddob.madroute.repositories.MadRouteRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MadRouteServiceImpl implements MadRouteService {

    private MadRouteRepository madRouteRepository;
    private MadRouteToMadRouteCommand madRouteToMadRouteCommandConverter;
    private MadRouteToCommandWithGpsData madRouteToMadRouteCommandWithGpsData;

    public MadRouteServiceImpl(MadRouteRepository madRouteRepository,
                               MadRouteToMadRouteCommand madRouteToMadRouteCommandConverter,
                               MadRouteToCommandWithGpsData madRouteToMadRouteCommandWithGpsData) {
        this.madRouteRepository = madRouteRepository;
        this.madRouteToMadRouteCommandConverter = madRouteToMadRouteCommandConverter;
        this.madRouteToMadRouteCommandWithGpsData = madRouteToMadRouteCommandWithGpsData;
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
}
