package com.maddob.madroute.controllers.v1;

import com.maddob.madroute.api.v1.model.MadRouteDTO;
import com.maddob.madroute.services.MadRouteService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = MadRouteRestController.class)
public class MadRouteRestControllerTest {


    @MockBean
    MadRouteService madRouteServiceMock;

    @Autowired
    MockMvc mockMvc;


    @Before
    public void setUp() {
        final MadRouteDTO madRouteOneDTO = createMadRouteDTO();

        final List<MadRouteDTO> madRouteDTOList = new ArrayList<>();
        madRouteDTOList.add(madRouteOneDTO);

        given(madRouteServiceMock.getMadRoutes()).willReturn(madRouteDTOList);
        given(madRouteServiceMock.getMadRoute(any())).willReturn(madRouteOneDTO);
    }


    @Test
    public void testGetAllRoutes() throws Exception {
        mockMvc.perform(get("/api/v1/routes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.madRoutes", hasSize(1)));
    }

    @Test
    public void testGetMadRoute() throws Exception {
        mockMvc.perform(get("/api/v1/route/12").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(createMadRouteDTO().getName())));
    }



    private MadRouteDTO createMadRouteDTO() {
        final MadRouteDTO dto = new MadRouteDTO();
        dto.setId(12l);
        dto.setName("DUMMY");
        dto.setLocation("New Berlin");
        dto.setDuration(Duration.ofSeconds(100));
        dto.setVideoId("fhdlsju34kjhfsd");
        dto.setDistance(125.70);
        dto.setDescription("This is just a test");
        return dto;
    }
}
