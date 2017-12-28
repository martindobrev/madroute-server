package com.maddob.madroute.bootstrap;

import com.maddob.madroute.domain.MadRoute;
import com.maddob.madroute.repositories.MadRouteRepository;
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
    private Resource gpsDataRide2Work;

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
        final MadRoute sofiaRide = new MadRoute();
        sofiaRide.setDescription("First test ride in Sofia");
        sofiaRide.setLocation("Sofia, Bulgaria");
        sofiaRide.setName("Up to Vitosha Mountain");
        sofiaRide.setVideoId("1savMPQRWvg");

        try {
            sofiaRide.setGpsData(dataUtils.getResourceBytes(gpsDataTestRide));
        } catch (IOException exception) {
            log.warn("Cannot set gps data of test ride 1");
        }
        madRouteService.save(sofiaRide);

        final MadRoute ride2work = new MadRoute();
        ride2work.setDescription("Just a rest ride to work on my first fixie");
        ride2work.setLocation("Sofia, Bulgaria");
        ride2work.setName("Ride to work");
        ride2work.setVideoId("iAqC3FNJboo");
        try {
            ride2work.setGpsData(dataUtils.getResourceBytes(gpsDataRide2Work));
        } catch (IOException exception) {
            log.warn("Cannot set gps data of test ride 2");
        }
        madRouteService.save(ride2work);

    }


}
