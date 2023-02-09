package com.maddob.madroute.bootstrap;

import com.maddob.madroute.domain.MadRoute;
import com.maddob.madroute.services.MadRouteService;
import com.maddob.madroute.util.DataUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@Component
public class BootstrapSomeData implements ApplicationListener<ContextRefreshedEvent> {

    @Value(value = "classpath:bootstrap/17110502.LOG")
    private Resource gpsDataTestRide;

    @Value(value = "classpath:bootstrap/17111003.LOG")
    private Resource gpsDataRide2WorkSofiaRing;

    @Value(value = "classpath:bootstrap/2018-01-26_26493116_bike-tour_export.gpx")
    private Resource gpsDataRide2WorkSofiaCenter;

    @Autowired
    private final MadRouteService madRouteService;

    @Autowired
    private final DataUtils dataUtils;

    public BootstrapSomeData(MadRouteService madRouteService, DataUtils dataUtils) {
        this.madRouteService = madRouteService;
        this.dataUtils = dataUtils;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        createRoute("Ftest ride in Sofia", "Uirst p to Vitosha Mountain", "Sofia, Bulgaria",
                "1savMPQRWvg", gpsDataTestRide);
        createRoute("Ride to work", "My usual ride to work",
                "Sofia, Bulgaria","iAqC3FNJboo", gpsDataRide2WorkSofiaRing);
        createRoute("Another ride to work", "This time through the city center",
                "Sofia, Bulgaria", "lIv4TpPML-0", gpsDataRide2WorkSofiaCenter);
    }

    private void createRoute(final String name, final String description, final String location,
                           final String videoId, final Resource gpsDataResource) {

        final MadRoute route = new MadRoute();
        route.setName(name);
        route.setDescription(description);
        route.setLocation(location);
        route.setVideoId(videoId);
        try {
            route.setGpsData(dataUtils.getResourceBytes(gpsDataResource));
        } catch (IOException exception) {
            log.warn("Cannot set gps data of test ride 1");
        }
        madRouteService.save(route);
    }


}
