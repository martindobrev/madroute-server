package com.maddob.madroute.services;

import com.maddob.madroute.command.MadRouteCommand;

import java.util.List;

public interface MadRouteService {
    List<MadRouteCommand> getAllMadRoutes();
}
