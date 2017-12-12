package com.maddob.madroute.controllers;

import com.maddob.madroute.command.MadRouteCommand;
import com.maddob.madroute.services.MadRouteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MadRouteRestController {

    private MadRouteService madRouteService;

    public MadRouteRestController(MadRouteService madRouteService) {
        this.madRouteService = madRouteService;
    }

    @GetMapping("routes")
    public List<MadRouteCommand> getRoutes() {
        return madRouteService.getAllMadRoutes();
    }
}
