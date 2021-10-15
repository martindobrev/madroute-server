package com.maddob.madroute.parsers;

import com.maddob.madroute.domain.GpsPosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.*;


public class NMEAParserTest {

    private NMEAParser parser;

    @BeforeEach
    public void setUp() {
        parser = new NMEAParser();
    }

    @Test
    public void testParserReturnsEmptyListWhenNoGPRMCRecordsFound() {
        // given no valid GPRMC NMEA record is contained in the file
        MockMultipartFile file = new MockMultipartFile("test", "test.txt",
                "text/plain", "@Sonygpsoption/0/20171105133446.000/20171105133446.178/".getBytes());

        // when
        List<GpsPosition> gpsPositionList = parser.parse(file);

        // then
        assertTrue(gpsPositionList.isEmpty());
    }

    @Test
    public void testParserCorrectlyParsesSingleGPRMCRecord() {
        // given a valid single GPRMC NMEA record is contained in the file
        MockMultipartFile file = new MockMultipartFile("test", "test.txt", "text/plain",
                "$GPRMC,133446.178,A,4237.4355,N,2322.0657,E,12.17,,051117,,,A*7F".getBytes());

        // when
        List<GpsPosition> gpsPositionList = parser.parse(file);

        // then
        assertFalse("The list shall not be empty", gpsPositionList.isEmpty());
        assertEquals("The list shall contain only 1 GpsPosition object", 1, gpsPositionList.size());
        GpsPosition gpsPosition = gpsPositionList.get(0);
        assertNotNull("The GpsPosition object shall not be null", gpsPosition);
        assertTrue(0.0001 > gpsPosition.getLatitude() - 42.623925);
        assertTrue(0.0001 > gpsPosition.getLongitude() - 23.367762);
        assertEquals("", gpsPosition.getVelocity(), new Double(12.17));
        assertEquals("", LocalTime.of(13, 34, 46), gpsPosition.getTime());
        assertEquals("", LocalDate.of(2017, 11, 5), gpsPosition.getDate());
    }

    @Test
    public void testParserCorrectlyParsesMultipleGPRMCRecords() {
        // given a valid multiple GPRMC NMEA records are contained in the file
        String[] someValidRecords = new String[] {
            "$GPRMC,133446.178,A,4237.4355,N,2322.0657,E,12.17,,051117,,,A*7F",
            "$GPGGA,133447.180,4237.4356,N,2322.0619,E,1,0,,,M,,M,,*44",
            "$GPRMC,133447.180,A,4237.4356,N,2322.0619,E,9.73,,051117,,,A*48",
            "$GPGGA,133448.180,4237.4348,N,2322.0579,E,1,0,,,M,,M,,*41",
            "$GPRMC,133448.180,A,4237.4348,N,2322.0579,E,9.08,,051117,,,A*41"
        };

        StringBuilder stringBuffer = new StringBuilder();
        for (String someValidRecord : someValidRecords) {
            stringBuffer.append(someValidRecord).append(System.lineSeparator());
        }

        MockMultipartFile file = new MockMultipartFile("test", "test.txt", "text/plain",
                stringBuffer.toString().getBytes());

        // when
        List<GpsPosition> gpsPositionList = parser.parse(file);

        // then
        assertFalse("The list shall not be empty", gpsPositionList.isEmpty());
        assertEquals("The list shall contain 3 GpsPosition objects", 3, gpsPositionList.size());
    }

    @Test
    public void testParserWithByteArray() {
        // given a valid single GPRMC NMEA record is contained in the byte array
        byte[] bytes = "$GPRMC,133446.178,A,4237.4355,N,2322.0657,E,12.17,,051117,,,A*7F".getBytes();
        Byte[] wrappedBytes = new Byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            wrappedBytes[i] = bytes[i];
        }

        // when
        List<GpsPosition> gpsPositionList = parser.parse(wrappedBytes);

        // then
        assertFalse("The list shall not be empty", gpsPositionList.isEmpty());
        assertEquals("The list shall contain only 1 GpsPosition object", 1, gpsPositionList.size());
        GpsPosition gpsPosition = gpsPositionList.get(0);
        assertNotNull("The GpsPosition object shall not be null", gpsPosition);
        assertTrue(0.0001 > gpsPosition.getLatitude() - 42.623925);
        assertTrue(0.0001 > gpsPosition.getLongitude() - 23.367762);
        assertEquals("", gpsPosition.getVelocity(), new Double(12.17));
        assertEquals("", LocalTime.of(13, 34, 46), gpsPosition.getTime());
        assertEquals("", LocalDate.of(2017, 11, 5), gpsPosition.getDate());
    }

    @Test
    public void testParserWithGeneratedNMEAFromNmeagenDotOrg() {
        // given a valid single GPRMC NMEA record generated from nmeagen.org
        byte[] bytes = "$GPRMC,184559.342,A,5230.180,N,01324.996,E,009.7,041.3,181217,000.0,W*70".getBytes();
        Byte[] wrappedBytes = new Byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            wrappedBytes[i] = bytes[i];
        }

        // when
        List<GpsPosition> gpsPositionList = parser.parse(wrappedBytes);

        // then
        assertFalse("The list shall not be empty", gpsPositionList.isEmpty());
        assertEquals("The list shall contain only 1 GpsPosition object", 1, gpsPositionList.size());
        GpsPosition gpsPosition = gpsPositionList.get(0);
        assertNotNull("The GpsPosition object shall not be null", gpsPosition);
        //assertTrue(0.0001 > gpsPosition.getLatitude() - 42.623925);
        //assertTrue(0.0001 > gpsPosition.getLongitude() - 23.367762);
        assertEquals("", gpsPosition.getVelocity(), new Double(9.7));
        assertEquals("", LocalTime.of(18, 45, 59), gpsPosition.getTime());
        assertEquals("", LocalDate.of(2017, 12, 18), gpsPosition.getDate());
    }
}
