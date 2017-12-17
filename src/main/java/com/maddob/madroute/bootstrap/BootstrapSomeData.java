package com.maddob.madroute.bootstrap;

import com.maddob.madroute.domain.MadRoute;
import com.maddob.madroute.repositories.MadRouteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;

@Component
public class BootstrapSomeData implements ApplicationListener<ContextRefreshedEvent> {

    @Value(value = "classpath:bootstrap/17110502.LOG")
    private Resource gpsDataTestRide;

    @Value(value = "classpath:bootstrap/17111003.LOG")
    private Resource gpsDataRide2Work;

    private final MadRouteRepository madRouteRepository;

    public BootstrapSomeData(MadRouteRepository madRouteRepository) {
        this.madRouteRepository = madRouteRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        final MadRoute sofiaRide = new MadRoute();
        sofiaRide.setDescription("First test ride in Sofia");
        sofiaRide.setLocation("Sofia, Bulgaria");
        sofiaRide.setName("Up to Vitosha Mountain");
        sofiaRide.setVideoId("1savMPQRWvg");

        sofiaRide.setGpsData(getResourceBytes(gpsDataTestRide));
        madRouteRepository.save(sofiaRide);

        final MadRoute ride2work = new MadRoute();
        ride2work.setDescription("Just a rest ride to work on my first fixie");
        ride2work.setLocation("Sofia, Bulgaria");
        ride2work.setName("Ride to work");
        ride2work.setVideoId("iAqC3FNJboo");
        ride2work.setGpsData(getResourceBytes(gpsDataRide2Work));
        madRouteRepository.save(ride2work);

    }

    private Byte[] getResourceBytes(final Resource resource) {
        try {
            final int length = (int) resource.contentLength();
            final Byte[] bytes = new Byte[(int) resource.contentLength()];
            final InputStream inputStream = resource.getInputStream();

            int byteIndex = 0;
            int byteRead;
            while ((byteRead = inputStream.read()) != -1) {
                bytes[byteIndex++] = (byte) byteRead;
            }
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
