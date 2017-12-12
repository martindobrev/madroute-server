package com.maddob.madroute.bootstrap;

import com.maddob.madroute.domain.MadRoute;
import com.maddob.madroute.repositories.MadRouteRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BootstrapSomeData implements ApplicationListener<ContextRefreshedEvent> {

    private final String[] validNmeaRecords = new String[] {
            "$GPRMC,133446.178,A,4237.4355,N,2322.0657,E,12.17,,051117,,,A*7F",
            "$GPGGA,133447.180,4237.4356,N,2322.0619,E,1,0,,,M,,M,,*44",
            "$GPRMC,133447.180,A,4237.4356,N,2322.0619,E,9.73,,051117,,,A*48",
            "$GPGGA,133448.180,4237.4348,N,2322.0579,E,1,0,,,M,,M,,*41",
            "$GPRMC,133448.180,A,4237.4348,N,2322.0579,E,9.08,,051117,,,A*41"
    };


    private final MadRouteRepository madRouteRepository;



    public BootstrapSomeData(MadRouteRepository madRouteRepository) {
        this.madRouteRepository = madRouteRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (String someValidRecord : validNmeaRecords) {
            stringBuilder.append(someValidRecord).append(System.lineSeparator());
        }

        Byte[] byteArray = new Byte[stringBuilder.toString().getBytes().length];
        int i = 0;
        for (Byte wrappedByte : stringBuilder.toString().getBytes()) {
            byteArray[i++] = wrappedByte;
        }

        final MadRoute madRoute = new MadRoute();
        //madRoute.setId(253l);
        madRoute.setDescription("DUMMY MAD ROUTE");
        madRoute.setLocation("Testland");
        madRoute.setName("DUMMY");
        madRoute.setGpsData(byteArray);
        madRouteRepository.save(madRoute);
    }
}
