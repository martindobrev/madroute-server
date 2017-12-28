package com.maddob.madroute.services;

import com.maddob.madroute.api.v1.model.MadRouteDTO;
import com.maddob.madroute.domain.MadRoute;

import java.util.List;

public interface MadRouteService {
    MadRoute save(MadRoute routeToBeSaved);
    MadRouteDTO saveDto(MadRouteDTO madRouteDTO);
    List<MadRouteDTO> getMadRoutes();
    MadRouteDTO getMadRoute(Long id);
}
