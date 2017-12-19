package com.maddob.madroute.services;


import com.maddob.madroute.api.v1.model.MadRouteDTO;
import com.maddob.madroute.command.MadRouteCommand;
import com.maddob.madroute.domain.MadRoute;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.*;

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
        List<MadRouteDTO> madRouteDTOList = madRouteService.getMadRoutes();

        // then
        assertNotNull("The returned list shall not be null", madRouteDTOList);
        assertEquals(2, madRouteDTOList.size());
    }

    @Test
    @Transactional
    public void testSaveMadRouteCalculatesAndStoresDistanceInformation() {
        // given
        final MadRoute routeToBeSaved = getTestRoute();

        // when
        MadRoute savedMadRoute = madRouteService.save(routeToBeSaved);

        // then
        MadRouteDTO command = madRouteService.getMadRoute(savedMadRoute.getId());
        assertNotNull(command);
        assertNotNull(command.getDistance());
        assertThat(command.getDistance(), Matchers.closeTo(50, 10));
    }

    @Test
    @Transactional
    public void testSaveMadRouteCalculatesAndStoresDurationInformation() {
        // given
        final MadRoute routeToBeSaved = getTestRoute();

        // when
        MadRoute savedMadRoute = madRouteService.save(routeToBeSaved);

        // then
        MadRouteDTO command = madRouteService.getMadRoute(savedMadRoute.getId());
        assertNotNull(command);
        assertNotNull(command.getDuration());
        assertThat(new Double(command.getDuration().toMillis()), Matchers.closeTo(9000, 100));
    }

    private MadRoute getTestRoute() {
        //List<MadRoute> madRouteList = new ArrayList<>();
        MadRoute testRouteBerlin = new MadRoute();
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

