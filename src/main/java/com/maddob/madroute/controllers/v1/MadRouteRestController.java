package com.maddob.madroute.controllers.v1;

import com.maddob.madroute.api.v1.model.MadRouteDTO;
import com.maddob.madroute.services.MadRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
public class MadRouteRestController {

    @Autowired
    private final MadRouteService madRouteService;

    public MadRouteRestController(MadRouteService madRouteService) {
        this.madRouteService = madRouteService;
    }

    @GetMapping("routes")
    public Map<String, List<MadRouteDTO>> getRoutes() {
        return Collections.singletonMap("madRoutes", madRouteService.getMadRoutes());
    }

    @GetMapping("route/{id}")
    public MadRouteDTO getRoute(@PathVariable String id) {
        return madRouteService.getMadRoute(Long.valueOf(id));
    }
}
