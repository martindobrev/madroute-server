package com.maddob.madroute.parsers;

import com.maddob.madroute.domain.GpsPosition;
import com.maddob.madroute.util.DataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;


public class GPXParserTest {

    private GPXParser gpxParser;
    private DataUtils dataUtils;

    @BeforeEach
    public void setUp() {
        dataUtils = new DataUtils();
        gpxParser = new GPXParser(dataUtils);
    }

    @Test
    public void testGpxValidParser() throws Exception {
        // given
        Byte[] testGpxData = dataUtils.getResourceBytes("gpx/simple_gpx_data.gpx");

        // when
        List<GpsPosition> parsedPositions = gpxParser.parse(testGpxData);

        // then
        assertNotNull("Parsed positions shall not be null", parsedPositions);
        assertEquals("Parsed positions shall be exactly 3", 3, parsedPositions.size());
    }

//    @Test(expected = IOException.class)
//    public void testGpxInvalidData() throws Exception {
//        // given
//        byte[] testNotGpxData = "This is not a GPX data".getBytes();
//
//        // when
//        List<GpsPosition> parsedPositions = gpxParser.parse(dataUtils.wrapByteArray(testNotGpxData));
//
//        // then - test case shall not reach this statement
//        TestCase.fail();
//    }
}
