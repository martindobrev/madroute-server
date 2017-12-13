package com.maddob.madroute.controllers;

import com.maddob.madroute.command.MadRouteCommand;
import com.maddob.madroute.services.MadRouteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class MadRouteRestController {

    private MadRouteService madRouteService;

    public MadRouteRestController(MadRouteService madRouteService) {
        this.madRouteService = madRouteService;
    }

    @GetMapping("api/routes")
    public Map<String, List<MadRouteCommand>> getRoutes() {
        return Collections.singletonMap("madRoutes", madRouteService.getAllMadRoutes());
    }

    @GetMapping("api/route/{id}")
    public MadRouteCommand getRoute(@PathVariable String id) {
        return madRouteService.getRoute(Long.valueOf(id));
    }
}
