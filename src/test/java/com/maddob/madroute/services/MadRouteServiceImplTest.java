package com.maddob.madroute.services;


import com.maddob.madroute.command.MadRouteCommand;
import com.maddob.madroute.converters.MadRouteToCommandWithGpsData;
import com.maddob.madroute.converters.MadRouteToMadRouteCommand;
import com.maddob.madroute.domain.MadRoute;
import com.maddob.madroute.repositories.MadRouteRepository;
import javassist.bytecode.ByteArray;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MadRouteServiceImplTest {

    @Autowired
    MadRouteService madRouteService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetMadRoutes() {
        // given data already in the repositories

        // when
        List<MadRouteCommand> madRouteCommandList = madRouteService.getAllMadRoutes();

        // then
        assertNotNull("The returned list shall not be null", madRouteCommandList);
        assertEquals(2, madRouteCommandList.size());
        // assertEquals("", madRouteCommandList.get(0).getName());
    }

    @Test
    @Transactional
    public void testSaveMadRouteCalculatesAndStoresDistanceInformation() {
        // given
        final MadRoute routeToBeSaved = getTestRoute();

        // when
        madRouteService.save(routeToBeSaved);

        // then
        MadRouteCommand command = madRouteService.getRoute(3l);
        assertNotNull(command);
        assertNotNull(command.getDistance());
        assertThat(command.getDistance(), Matchers.closeTo(50, 10));

    }

    private MadRoute getTestRoute() {
        //List<MadRoute> madRouteList = new ArrayList<>();
        MadRoute testRouteBerlin = new MadRoute();
        testRouteBerlin.setId(3l);
        testRouteBerlin.setVideoId("INVALID_VIDEO_ID");
        testRouteBerlin.setName("Test Route Berlin");
        testRouteBerlin.setDescription("This is a test route for the service implementation");
        testRouteBerlin.setLocation("Berlin, Germany");

        try {
            testRouteBerlin.setGpsData(getResourceBytes("nmea/points_10_distance_5.nmea"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //madRouteList.add(testRouteBerlin);
        return testRouteBerlin;
    }

    private Byte[] getResourceBytes(final String filePath) throws IOException, URISyntaxException {
        final InputStream inputStream = this.getClass().getClassLoader()
                .getResourceAsStream(filePath);
        final Byte[] bytes = new Byte[inputStream.available()];
        int byteIndex = 0;
        int byteRead;
        while ((byteRead = inputStream.read()) != -1) {
            bytes[byteIndex++] = (byte) byteRead;
        }

        if (inputStream != null) {
            inputStream.close();
        }
        return bytes;
    }
}

