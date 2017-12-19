package com.maddob.madroute.api.v1.mapper;

import com.maddob.madroute.api.v1.model.MadRouteDTO;
import com.maddob.madroute.domain.GpsPosition;
import com.maddob.madroute.domain.MadRoute;
import com.maddob.madroute.parsers.NMEAParser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;

public class MadRouteMapperTest {

    private MadRouteMapper madRouteMapper;

    private final Long id = 12382173l;
    private final String name = "TEST ROUTE";
    private final String location = "TESTORDIA";
    private final String description = "THIS IS JUST A TEST DESCRIPTION";
    private final String videoId = "oz7632nknf98jf";
    private final Double distance = 1235.45;
    private final Duration duration = Duration.ofSeconds(100);

    @Mock
    private GpsPositionMapper gpsPositionMapperMock;

    @Mock
    private NMEAParser nmeaParserMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        madRouteMapper = new MadRouteMapper(gpsPositionMapperMock, nmeaParserMock);
    }

    @Test
    public void testModelToDtoNoGps() {
        // given
        final MadRoute madRoute = createMadRoute();

        // when
        final MadRouteDTO madRouteDTO = madRouteMapper.modelToDto(madRoute, false);

        // then
        assertNotNull(madRouteDTO);
        assertEquals(id, madRouteDTO.getId());
        assertEquals(name, madRouteDTO.getName());
        assertEquals(location, madRouteDTO.getLocation());
        assertEquals(description, madRouteDTO.getDescription());
        assertEquals(videoId, madRouteDTO.getVideoId());
        assertEquals(distance, madRouteDTO.getDistance());
        assertEquals(duration, madRouteDTO.getDuration());
    }

    @Test
    public void testModelToDtoWithGps() {
        // given
        final MadRoute madRoute = createMadRoute();
        final Byte[] byteArray = new Byte[10];
        madRoute.setGpsData(byteArray);

        final List<GpsPosition> mockGpsPositions = new ArrayList<>();
        mockGpsPositions.add(new GpsPosition());
        mockGpsPositions.add(new GpsPosition());

        given(nmeaParserMock.parse(byteArray)).willReturn(mockGpsPositions);

        // when
        final MadRouteDTO madRouteDTO = madRouteMapper.modelToDto(madRoute, true);

        assertNotNull(madRouteDTO.getGpsData());
        assertEquals(2, madRouteDTO.getGpsData().size());
    }

    @Test
    public void testDtoToModel() {
        // given
        MadRouteDTO madRouteDTO = createMadRouteDTO();

        // when
        final MadRoute madRoute = madRouteMapper.dtoToModel(madRouteDTO);

        // then
        assertNotNull(madRoute);
        assertEquals(id, madRoute.getId());
        assertEquals(name, madRoute.getName());
        assertEquals(location, madRoute.getLocation());
        assertEquals(description, madRoute.getDescription());
        assertEquals(videoId, madRoute.getVideoId());
        assertEquals(distance, madRoute.getDistance());
        assertEquals(duration, madRoute.getDuration());
    }


    private MadRoute createMadRoute() {
        final MadRoute madRoute = new MadRoute();
        madRoute.setId(id);
        madRoute.setName(name);
        madRoute.setLocation(location);
        madRoute.setDescription(description);
        madRoute.setVideoId(videoId);
        madRoute.setDistance(distance);
        madRoute.setDuration(duration);
        return madRoute;
    }

    private MadRouteDTO createMadRouteDTO() {
        MadRouteDTO dto = new MadRouteDTO();
        dto.setId(id);
        dto.setName(name);
        dto.setLocation(location);
        dto.setDescription(description);
        dto.setVideoId(videoId);
        dto.setDistance(distance);
        dto.setDuration(duration);
        return dto;
    }
}
