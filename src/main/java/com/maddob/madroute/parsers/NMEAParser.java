package com.maddob.madroute.parsers;

import com.maddob.madroute.domain.GpsPosition;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class NMEAParser {

    private static final String GPRMC = "$GPRMC";

    public List<GpsPosition> parse(MultipartFile file) {
        List<GpsPosition> gpsPositions = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                GpsPosition position = this.parseNMEALine(line);
                if (position != null) {
                    gpsPositions.add(position);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gpsPositions;
    }

    public List<GpsPosition> parse(Byte[] bytes) {
        List<GpsPosition> gpsPositions = new ArrayList<>();
        if (bytes.length > 0) {
            byte[] byteArray = new byte[bytes.length];
            int i = 0;
            for (Byte wrappedByte : bytes) {
                byteArray[i++] = wrappedByte;
            }

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(byteArray)));
                String line;
                while ((line = in.readLine()) != null) {
                    GpsPosition position = this.parseNMEALine(line);
                    if (position != null) {
                        gpsPositions.add(position);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return gpsPositions;
    }

    private GpsPosition parseNMEALine(final String line) {
        if (line.startsWith(GPRMC)) {
            String[] parts = line.split(",");
            if (parts.length != 13) {
                return null;
            }

            final GpsPosition gpsPosition = new GpsPosition();
            if (!parts[1].isEmpty()) {
                if (parts[1].length() > 5) {
                    gpsPosition.setTime(LocalTime.of(
                            Integer.parseInt(parts[1].substring(0, 2)),
                            Integer.parseInt(parts[1].substring(2, 4)),
                            Integer.parseInt(parts[1].substring(4, 6))
                        )
                    );
                }
            }

            if (!parts[3].isEmpty() && !parts[4].isEmpty()) {
                gpsPosition.setLatitude(degrees2Decimal(parts[3], parts[4]));
            }

            if (!parts[5].isEmpty() && !parts[6].isEmpty()) {
                gpsPosition.setLongitude(degrees2Decimal(parts[5], parts[6]));
            }

            if (!parts[7].isEmpty()) {
                gpsPosition.setVelocity(Double.parseDouble(parts[7]));
            }

            if (!parts[9].isEmpty()) {
                if (parts[9].length() > 5) {
                    gpsPosition.setDate(LocalDate.of(
                            2000 + Integer.parseInt(parts[9].substring(4, 6)),
                            Integer.parseInt(parts[9].substring(2, 4)),
                            Integer.parseInt(parts[9].substring(0, 2))
                            )
                    );
                }
            }
            return gpsPosition;
        }
        return null;
    }

    private double degrees2Decimal(String degrees, String location) {
        if (degrees.isEmpty() || location.isEmpty()) {
            return 0;
        }
        int split = degrees.split("\\.")[0].length() - 2;
        double med = Double.parseDouble(degrees.substring(split)) / 60.0;
        med += Double.parseDouble(degrees.substring(0, split));
        if (location.equals("S") || location.equals("W")) {
            med = -med;
        }
        return med;
    }
}

