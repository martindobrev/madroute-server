package com.maddob.madroute.services;


import com.maddob.madroute.command.MadRouteCommand;
import com.maddob.madroute.domain.MadRoute;
import com.maddob.madroute.repositories.MadRouteRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MadRouteServiceImplTest {

    @Autowired
    MadRouteService madRouteService;

    @Autowired
    MadRouteRepository madRouteRepository;

    @Test
    @Transactional
    public void testGetMadRoutes() {
        // given - already setup in Bootstrap

        // when
        List<MadRouteCommand> madRouteCommandList = madRouteService.getAllMadRoutes();

        // then
        assertNotNull("The returned list shall not be null", madRouteCommandList);
        assertEquals(1, madRouteCommandList.size());
        assertNotNull(madRouteCommandList.get(0).getGpsData());
        assertEquals(3, madRouteCommandList.get(0).getGpsData().size());
        assertEquals(new Double(12.17), madRouteCommandList.get(0).getGpsData().get(0).getVelocity());
    }
}

