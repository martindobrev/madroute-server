package com.maddob.madroute.bootstrap;

import com.maddob.madroute.domain.MadRoute;
import com.maddob.madroute.repositories.MadRouteRepository;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class BootstrapSomeData implements ApplicationListener<ContextRefreshedEvent> {

    @Value(value = "classpath:bootstrap/17110502.LOG")
    private Resource gpsDataTestRide;

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
        sofiaRide.setVideoUrl("1savMPQRWvg");

        try {
            final int length = (int) gpsDataTestRide.contentLength();
            final Byte[] bytes = new Byte[(int) gpsDataTestRide.contentLength()];
            final InputStream inputStream = gpsDataTestRide.getInputStream();

            int byteIndex = 0;
            int byteRead;
            while ((byteRead = inputStream.read()) != -1) {
                bytes[byteIndex++] = (byte) byteRead;
            }
            sofiaRide.setGpsData(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }
        madRouteRepository.save(sofiaRide);
    }
}
