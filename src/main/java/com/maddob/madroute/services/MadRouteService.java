package com.maddob.madroute.services;

import com.maddob.madroute.command.MadRouteCommand;
import com.maddob.madroute.domain.MadRoute;

import java.util.List;

public interface MadRouteService {
    List<MadRouteCommand> getAllMadRoutes();
    MadRouteCommand getRoute(Long id);
    void save(MadRoute routeToBeSaved);
}
