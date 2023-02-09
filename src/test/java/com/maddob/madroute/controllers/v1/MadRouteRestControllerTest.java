package com.maddob.madroute.controllers.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maddob.madroute.api.v1.model.MadRouteDTO;
import com.maddob.madroute.services.MadRouteService;
import com.maddob.madroute.util.DataUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {MadRouteRestController.class})
@Import(MadRouteRestController.class)
public class MadRouteRestControllerTest {


    @MockBean
    MadRouteService madRouteServiceMock;
    
    @MockBean
    DataUtils dataUtils;

    @Autowired
    MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    public void setUp() {
        final MadRouteDTO madRouteOneDTO = createMadRouteDTO();

        final List<MadRouteDTO> madRouteDTOList = new ArrayList<>();
        madRouteDTOList.add(madRouteOneDTO);

        given(madRouteServiceMock.getMadRoutes()).willReturn(madRouteDTOList);
        given(madRouteServiceMock.getMadRoute(any())).willReturn(madRouteOneDTO);
        given(madRouteServiceMock.saveDto(any())).willReturn(madRouteOneDTO);
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

    @Test
    public void testCreateNewRoute()  throws Exception {
        String jsonMadRouteDTO = objectMapper.writeValueAsString(createMadRouteDTO());
        mockMvc.perform(post("/api/v1/routes").contentType(MediaType.MULTIPART_FORM_DATA).content(jsonMadRouteDTO))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo(12)));
    }



    private MadRouteDTO createMadRouteDTO() {
        final MadRouteDTO dto = new MadRouteDTO();
        dto.setId(12L);
        dto.setName("DUMMY");
        dto.setLocation("New Berlin");
        dto.setDuration(100L);
        dto.setVideoId("fhdlsju34kjhfsd");
        dto.setDistance(125.70);
        dto.setDescription("This is just a test");
        return dto;
    }
}
