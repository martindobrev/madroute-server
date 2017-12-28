package com.maddob.madroute.services;


import com.maddob.madroute.api.v1.model.MadRouteDTO;
import com.maddob.madroute.domain.MadRoute;
import com.maddob.madroute.util.DataUtils;
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
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MadRouteServiceImplTest {

    private static final String NMEA_POINTS_10_DISTANCE_5_NMEA = "nmea/points_10_distance_5.nmea";

    @Autowired
    MadRouteService madRouteService;

    @Autowired
    DataUtils dataUtils;

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
    public void testSaveMadRouteDto() throws Exception {
        // given
        final MadRouteDTO madRouteDto = new MadRouteDTO();
        madRouteDto.setName("BASE64 GPS DATA");
        madRouteDto.setDescription("Just a simple test");
        madRouteDto.setLocation("DUMMYLAND");
        madRouteDto.setVideoId("someNotExistingID");
        madRouteDto.setBase64GpsData(dataUtils.byteArrayAsBase64String(dataUtils.getResourceBytes(NMEA_POINTS_10_DISTANCE_5_NMEA)));

        // when
        MadRouteDTO savedMadRouteDto = madRouteService.saveDto(madRouteDto);

        // then
        assertNotNull(savedMadRouteDto.getId());
        MadRouteDTO dtoExtractedFromDb = madRouteService.getMadRoute(savedMadRouteDto.getId());
        assertNotNull(dtoExtractedFromDb);
        assertNotNull(dtoExtractedFromDb.getDistance());
        assertNotNull(dtoExtractedFromDb.getDuration());
        assertNotNull(dtoExtractedFromDb.getGpsData());
        assertEquals(10, dtoExtractedFromDb.getGpsData().size());
    }

    @Test
    @Transactional
    public void testSaveMadRouteCalculatesAndStoresDurationInformation() {
        // given
        final MadRoute routeToBeSaved = getTestRoute();

        // when
        MadRoute savedMadRoute = madRouteService.save(routeToBeSaved);

        // then
        MadRouteDTO madRouteDTO = madRouteService.getMadRoute(savedMadRoute.getId());
        assertNotNull(madRouteDTO);
        assertNotNull(madRouteDTO.getDuration());
        assertThat(madRouteDTO.getDuration(), Matchers.equalTo(9L));
    }

    private MadRoute getTestRoute() {
        //List<MadRoute> madRouteList = new ArrayList<>();
        MadRoute testRouteBerlin = new MadRoute();
        testRouteBerlin.setVideoId("INVALID_VIDEO_ID");
        testRouteBerlin.setName("Test Route Berlin");
        testRouteBerlin.setDescription("This is a test route for the service implementation");
        testRouteBerlin.setLocation("Berlin, Germany");

        try {
            testRouteBerlin.setGpsData(dataUtils.getResourceBytes(NMEA_POINTS_10_DISTANCE_5_NMEA));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return testRouteBerlin;
    }
}

