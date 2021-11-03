package com.maddob.madroute.controllers.v1;

import com.maddob.madroute.api.v1.model.MadRouteDTO;
import com.maddob.madroute.services.MadRouteService;
import com.maddob.madroute.util.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
public class MadRouteRestController {

    @Autowired
    private final MadRouteService madRouteService;

    @Autowired
    private final DataUtils dataUtils;

    public MadRouteRestController(MadRouteService madRouteService, DataUtils dataUtils) {
        this.madRouteService = madRouteService;
        this.dataUtils = dataUtils;
    }

    @GetMapping("routes")
    public Map<String, List<MadRouteDTO>> getRoutes() {
        return Collections.singletonMap("madRoutes", madRouteService.getMadRoutes());
    }

    @GetMapping("route/{id}")
    public MadRouteDTO getRoute(@PathVariable String id) {
        return madRouteService.getMadRoute(Long.valueOf(id));
    }

    @PostMapping("routes")
    @ResponseStatus(HttpStatus.CREATED)
    public MadRouteDTO createNewRoute(@RequestParam("file") MultipartFile file, @RequestParam("name") String name, @RequestParam("location") String location) throws IOException {

        final MadRouteDTO dto = new MadRouteDTO();
        dto.setName(name);
        dto.setLocation(location);
        dto.setBase64GpsData(dataUtils.byteArrayAsBase64String(file.getBytes()));

        return madRouteService.saveDto(dto);
    }
}
