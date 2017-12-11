package com.maddob.madroute.parsers;

import java.util.HashMap;
import java.util.Map;

public class NMEAParser {


    interface SentenceParser {
        public boolean parse(String [] tokens, GPSPosition position);
    }

    // utils
    private static float Degrees2Decimal(String lat, String NS) {
        if (lat.isEmpty() || NS.isEmpty()) {
            return 0f;
        }
        String[] parts = lat.split(".");
        int split = lat.split("\\.")[0].length() - 2;
        float med = Float.parseFloat(lat.substring(split)) / 60.0f;
        med += Float.parseFloat(lat.substring(0, split));
        if (NS.equals("S") || NS.equals("W")) {
            med = -med;
        }
        return med;
    }
    // parsers
    class GPGGA implements SentenceParser {
        public boolean parse(String [] tokens, GPSPosition position) {
            if (!tokens[1].isEmpty()) {
                position.time = Float.parseFloat(tokens[1]);
            }

            if (!tokens[2].isEmpty() && !tokens[3].isEmpty()) {
                position.lat = Degrees2Decimal(tokens[2], tokens[3]);
            }

            if (!tokens[4].isEmpty() &&!tokens[5].isEmpty()) {
                position.lon = Degrees2Decimal(tokens[4], tokens[5]);
            }

            if (!tokens[6].isEmpty()) {
                position.quality = Integer.parseInt(tokens[6]);
            }

            if (!tokens[9].isEmpty()) {
                position.altitude = Float.parseFloat(tokens[9]);
            }
            return true;
        }
    }

    class GPGGL implements SentenceParser {
        public boolean parse(String [] tokens, GPSPosition position) {
            position.lat = Degrees2Decimal(tokens[1], tokens[2]);
            position.lon = Degrees2Decimal(tokens[3], tokens[4]);
            position.time = Float.parseFloat(tokens[5]);
            return true;
        }
    }

    class GPRMC implements SentenceParser {
        public boolean parse(String [] tokens, GPSPosition position) {
            if (!tokens[1].isEmpty()) {
                position.time = Float.parseFloat(tokens[1]);
            }

            if (!tokens[3].isEmpty() && !tokens[4].isEmpty()) {
                position.lat = Degrees2Decimal(tokens[3], tokens[4]);
            }

            if (!tokens[5].isEmpty() && !tokens[6].isEmpty()) {
                position.lon = Degrees2Decimal(tokens[5], tokens[6]);
            }

            if (!tokens[7].isEmpty()) {
                position.velocity = Float.parseFloat(tokens[7]);
            }

            if (!tokens[8].isEmpty()) {
                position.dir = Float.parseFloat(tokens[8]);
            }
            return true;
        }
    }

    class GPVTG implements SentenceParser {
        public boolean parse(String [] tokens, GPSPosition position) {
            position.dir = Float.parseFloat(tokens[3]);
            return true;
        }
    }

    class GPRMZ implements SentenceParser {
        public boolean parse(String [] tokens, GPSPosition position) {
            position.altitude = Float.parseFloat(tokens[1]);
            return true;
        }
    }

    public class GPSPosition {
        public float time = 0.0f;
        public float lat = 0.0f;
        public float lon = 0.0f;
        public boolean fixed = false;
        public int quality = 0;
        public float dir = 0.0f;
        public float altitude = 0.0f;
        public float velocity = 0.0f;

        public void updatefix() {
            fixed = quality > 0;
        }

        public String toString() {
            return String.format("POSITION: lat: %f, lon: %f, time: %f, Q: %d, dir: %f, alt: %f, vel: %f", lat, lon, time, quality, dir, altitude, velocity);
        }
    }

    GPSPosition position = new GPSPosition();

    private static final Map<String, SentenceParser> sentenceParsers = new HashMap<String, SentenceParser>();

    public NMEAParser() {
        //sentenceParsers.put("GPGGA", new GPGGA());
        sentenceParsers.put("GPGGL", new GPGGL());
        sentenceParsers.put("GPRMC", new GPRMC());
        sentenceParsers.put("GPRMZ", new GPRMZ());
        //only really good GPS devices have this sentence but ...
        sentenceParsers.put("GPVTG", new GPVTG());
    }

    public GPSPosition parse(String line) {
        if(line.startsWith("$")) {
            String nmea = line.substring(1);
            String[] tokens = nmea.split(",");
            String type = tokens[0];
            //TODO check crc
            if(sentenceParsers.containsKey(type)) {
                sentenceParsers.get(type).parse(tokens, position);
            } else {
                return null;
            }
            position.updatefix();
        }

        return position;
    }

    /*
    public static void main(String[] args) {
        NMEAParser nmea = new NMEAParser();
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("/Users/martindobrev/Desktop/test.txt", "UTF-8");
            for (String coord : VITOSHA_RECORDS) {
                GPSPosition pos = nmea.parse(coord);
                if (pos != null) {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("new GpsPosition(").append(pos.lat).append(", ")
                            .append(pos.lon).append(", ").append(pos.altitude).append(", ")
                            .append(pos.velocity).append(", ").append(pos.time).append("),");
                    //System.out.println(stringBuffer.toString());
                    writer.println(stringBuffer.toString());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }
    */

    public static final String[] REAL_SONY_ACTIONCAM_DATA = {
            "@Sonygps/ver5.0/wgs-84/20171105133445.000/",
            "@Sonygpsoption/0/20171105133446.000/20171105133446.178/",
            "$GPGGA,133446.178,4237.4355,N,2322.0657,E,1,0,,,M,,M,,*4B",
            "$GPRMC,133446.178,A,4237.4355,N,2322.0657,E,12.17,,051117,,,A*7F",
            "$GPGGA,133447.180,4237.4356,N,2322.0619,E,1,0,,,M,,M,,*44",
            "$GPRMC,133447.180,A,4237.4356,N,2322.0619,E,9.73,,051117,,,A*48",
            "$GPGGA,133448.180,4237.4348,N,2322.0579,E,1,0,,,M,,M,,*41",
            "$GPRMC,133448.180,A,4237.4348,N,2322.0579,E,9.08,,051117,,,A*41",
            "$GPGGA,133449.181,4237.4339,N,2322.0539,E,1,0,,,M,,M,,*43",
            "$GPRMC,133449.181,A,4237.4339,N,2322.0539,E,9.62,,051117,,,A*4F",
            "$GPGGA,133450.180,4237.4339,N,2322.0501,E,1,0,,,M,,M,,*41",
            "$GPRMC,133450.180,A,4237.4339,N,2322.0501,E,9.93,,051117,,,A*43",
            "$GPGGA,133451.180,4237.4341,N,2322.0461,E,1,0,,,M,,M,,*48",
            "$GPRMC,133451.180,A,4237.4341,N,2322.0461,E,10.03,,051117,,,A*7B",
            "$GPGGA,133452.180,4237.4343,N,2322.0422,E,1,0,,,M,,M,,*4E",
            "$GPRMC,133452.180,A,4237.4343,N,2322.0422,E,9.95,,051117,,,A*4A",
            "$GPGGA,133453.180,4237.4345,N,2322.0383,E,1,0,,,M,,M,,*45",
            "$GPRMC,133453.180,A,4237.4345,N,2322.0383,E,9.71,,051117,,,A*4B",
            "$GPGGA,133454.179,4237.4350,N,2322.0344,E,1,0,,,M,,M,,*4B",
            "$GPRMC,133454.179,A,4237.4350,N,2322.0344,E,9.76,,051117,,,A*42",
            "$GPGGA,133455.179,4237.4352,N,2322.0305,E,1,0,,,M,,M,,*4D",
            "$GPRMC,133455.179,A,4237.4352,N,2322.0305,E,9.98,,051117,,,A*44",
            "$GPGGA,133456.180,4237.4352,N,2322.0271,E,1,0,,,M,,M,,*4A",
            "$GPRMC,133456.180,A,4237.4352,N,2322.0271,E,10.19,,051117,,,A*72",
            "$GPGGA,133458.000,4237.4355,N,2322.0202,E,1,0,,,M,,M,,*4E",
            "$GPRMC,133458.000,A,4237.4355,N,2322.0202,E,10.14,,051117,,,A*7B",
            "$GPGGA,133459.000,4237.4357,N,2322.0162,E,1,0,,,M,,M,,*48",
            "$GPRMC,133459.000,A,4237.4357,N,2322.0162,E,10.32,,051117,,,A*79",
            "$GPGGA,133500.000,4237.4358,N,2322.0124,E,1,0,,,M,,M,,*48",
            "$GPRMC,133500.000,A,4237.4358,N,2322.0124,E,10.06,,051117,,,A*7E",
            "$GPGGA,133501.000,4237.4359,N,2322.0086,E,1,0,,,M,,M,,*41",
            "$GPRMC,133501.000,A,4237.4359,N,2322.0086,E,9.90,,051117,,,A*40",
            "$GPGGA,133502.000,4237.4359,N,2322.0048,E,1,0,,,M,,M,,*40",
            "$GPRMC,133502.000,A,4237.4359,N,2322.0048,E,10.04,,051117,,,A*74",
            "$GPGGA,133503.000,4237.4361,N,2322.0011,E,1,0,,,M,,M,,*46",
            "$GPRMC,133503.000,A,4237.4361,N,2322.0011,E,10.01,,051117,,,A*77",
            "$GPGGA,133504.000,4237.4363,N,2321.9972,E,1,0,,,M,,M,,*45",
            "$GPRMC,133504.000,A,4237.4363,N,2321.9972,E,10.04,,051117,,,A*71",
            "$GPGGA,133505.000,4237.4364,N,2321.9934,E,1,0,,,M,,M,,*41",
            "$GPRMC,133505.000,A,4237.4364,N,2321.9934,E,10.22,,051117,,,A*71",
            "$GPGGA,133506.000,4237.4366,N,2321.9895,E,1,0,,,M,,M,,*4A",
            "$GPRMC,133506.000,A,4237.4366,N,2321.9895,E,10.26,,051117,,,A*7E",
            "$GPGGA,133507.000,4237.4367,N,2321.9856,E,1,0,,,M,,M,,*45",
            "$GPRMC,133507.000,A,4237.4367,N,2321.9856,E,10.25,,051117,,,A*72",
            "$GPGGA,133508.000,4237.4368,N,2321.9818,E,1,0,,,M,,M,,*4F",
            "$GPRMC,133508.000,A,4237.4368,N,2321.9818,E,10.22,,051117,,,A*7F",
            "$GPGGA,133509.000,4237.4369,N,2321.9779,E,1,0,,,M,,M,,*47",
            "$GPRMC,133509.000,A,4237.4369,N,2321.9779,E,10.22,,051117,,,A*77",
            "$GPGGA,133510.000,4237.4370,N,2321.9740,E,1,0,,,M,,M,,*4D",
            "$GPRMC,133510.000,A,4237.4370,N,2321.9740,E,10.26,,051117,,,A*79",
            "$GPGGA,133511.000,4237.4372,N,2321.9701,E,1,0,,,M,,M,,*4B",
            "$GPRMC,133511.000,A,4237.4372,N,2321.9701,E,10.35,,051117,,,A*7D",
            "$GPGGA,133512.000,4237.4374,N,2321.9662,E,1,0,,,M,,M,,*4A",
            "$GPRMC,133512.000,A,4237.4374,N,2321.9662,E,10.30,,051117,,,A*79",
            "$GPGGA,133513.000,4237.4375,N,2321.9623,E,1,0,,,M,,M,,*4F",
            "$GPRMC,133513.000,A,4237.4375,N,2321.9623,E,10.32,,051117,,,A*7E",
            "$GPGGA,133514.000,4237.4376,N,2321.9584,E,1,0,,,M,,M,,*45",
            "$GPRMC,133514.000,A,4237.4376,N,2321.9584,E,10.32,,051117,,,A*74",
            "$GPGGA,133515.000,4237.4377,N,2321.9546,E,1,0,,,M,,M,,*4B",
            "$GPRMC,133515.000,A,4237.4377,N,2321.9546,E,10.35,,051117,,,A*7D",
            "$GPGGA,133516.000,4237.4377,N,2321.9507,E,1,0,,,M,,M,,*4D",
            "$GPRMC,133516.000,A,4237.4377,N,2321.9507,E,10.39,,051117,,,A*77",
            "$GPGGA,133517.000,4237.4378,N,2321.9468,E,1,0,,,M,,M,,*4B",
            "$GPRMC,133517.000,A,4237.4378,N,2321.9468,E,10.40,,051117,,,A*7F",
            "$GPGGA,133518.000,4237.4379,N,2321.9429,E,1,0,,,M,,M,,*40",
            "$GPRMC,133518.000,A,4237.4379,N,2321.9429,E,10.42,,051117,,,A*76",
            "$GPGGA,133519.000,4237.4380,N,2321.9389,E,1,0,,,M,,M,,*4A",
            "$GPRMC,133519.000,A,4237.4380,N,2321.9389,E,10.48,,051117,,,A*76",
            "$GPGGA,133520.000,4237.4382,N,2321.9349,E,1,0,,,M,,M,,*4E",
            "$GPRMC,133520.000,A,4237.4382,N,2321.9349,E,10.51,,051117,,,A*7A",
            "$GPGGA,133521.000,4237.4383,N,2321.9309,E,1,0,,,M,,M,,*4A",
            "$GPRMC,133521.000,A,4237.4383,N,2321.9309,E,10.57,,051117,,,A*78",
            "$GPGGA,133522.000,4237.4385,N,2321.9269,E,1,0,,,M,,M,,*48",
            "$GPRMC,133522.000,A,4237.4385,N,2321.9269,E,10.73,,051117,,,A*7C",
            "$GPGGA,133523.000,4237.4387,N,2321.9229,E,1,0,,,M,,M,,*4F",
            "$GPRMC,133523.000,A,4237.4387,N,2321.9229,E,10.73,,051117,,,A*7B",
            "$GPGGA,133524.000,4237.4388,N,2321.9189,E,1,0,,,M,,M,,*4E",
            "$GPRMC,133524.000,A,4237.4388,N,2321.9189,E,10.71,,051117,,,A*78",
            "$GPGGA,133525.000,4237.4390,N,2321.9148,E,1,0,,,M,,M,,*4B",
            "$GPRMC,133525.000,A,4237.4390,N,2321.9148,E,10.72,,051117,,,A*7E",
            "$GPGGA,133526.000,4237.4392,N,2321.9108,E,1,0,,,M,,M,,*4E",
            "$GPRMC,133526.000,A,4237.4392,N,2321.9108,E,10.69,,051117,,,A*71",
            "$GPGGA,133527.000,4237.4394,N,2321.9068,E,1,0,,,M,,M,,*4E",
            "$GPRMC,133527.000,A,4237.4394,N,2321.9068,E,10.51,,051117,,,A*7A",
            "$GPGGA,133528.000,4237.4396,N,2321.9029,E,1,0,,,M,,M,,*46",
            "$GPRMC,133528.000,A,4237.4396,N,2321.9029,E,10.56,,051117,,,A*75",
            "$GPGGA,133529.000,4237.4397,N,2321.8988,E,1,0,,,M,,M,,*45",
            "$GPRMC,133529.000,A,4237.4397,N,2321.8988,E,10.59,,051117,,,A*79",
            "$GPGGA,133530.000,4237.4398,N,2321.8948,E,1,0,,,M,,M,,*4E",
            "$GPRMC,133530.000,A,4237.4398,N,2321.8948,E,10.62,,051117,,,A*7A",
            "$GPGGA,133531.000,4237.4399,N,2321.8907,E,1,0,,,M,,M,,*45",
            "$GPRMC,133531.000,A,4237.4399,N,2321.8907,E,10.80,,051117,,,A*7D",
            "$GPGGA,133532.000,4237.4400,N,2321.8867,E,1,0,,,M,,M,,*46",
            "$GPRMC,133532.000,A,4237.4400,N,2321.8867,E,10.81,,051117,,,A*7F",
            "$GPGGA,133533.000,4237.4402,N,2321.8826,E,1,0,,,M,,M,,*40",
            "$GPRMC,133533.000,A,4237.4402,N,2321.8826,E,10.83,,051117,,,A*7B",
            "$GPGGA,133534.000,4237.4404,N,2321.8785,E,1,0,,,M,,M,,*47",
            "$GPRMC,133534.000,A,4237.4404,N,2321.8785,E,10.85,,051117,,,A*7A",
            "$GPGGA,133535.000,4237.4405,N,2321.8744,E,1,0,,,M,,M,,*4A",
            "$GPRMC,133535.000,A,4237.4405,N,2321.8744,E,11.14,,051117,,,A*7E",
            "$GPGGA,133536.000,4237.4407,N,2321.8702,E,1,0,,,M,,M,,*49",
            "$GPRMC,133536.000,A,4237.4407,N,2321.8702,E,11.32,,051117,,,A*79",
            "$GPGGA,133537.000,4237.4409,N,2321.8659,E,1,0,,,M,,M,,*49",
            "$GPRMC,133537.000,A,4237.4409,N,2321.8659,E,11.35,,051117,,,A*7E",
            "$GPGGA,133538.000,4237.4410,N,2321.8615,E,1,0,,,M,,M,,*46",
            "$GPRMC,133538.000,A,4237.4410,N,2321.8615,E,11.56,,051117,,,A*74",
            "$GPGGA,133539.000,4237.4411,N,2321.8571,E,1,0,,,M,,M,,*47",
            "$GPRMC,133539.000,A,4237.4411,N,2321.8571,E,11.54,,051117,,,A*77",
            "$GPGGA,133540.000,4237.4412,N,2321.8528,E,1,0,,,M,,M,,*46",
            "$GPRMC,133540.000,A,4237.4412,N,2321.8528,E,11.51,,051117,,,A*73",
            "$GPGGA,133541.000,4237.4413,N,2321.8485,E,1,0,,,M,,M,,*40",
            "$GPRMC,133541.000,A,4237.4413,N,2321.8485,E,11.51,,051117,,,A*75",
            "$GPGGA,133542.000,4237.4414,N,2321.8442,E,1,0,,,M,,M,,*4F",
            "$GPRMC,133542.000,A,4237.4414,N,2321.8442,E,11.48,,051117,,,A*72",
            "$GPGGA,133543.000,4237.4416,N,2321.8398,E,1,0,,,M,,M,,*4C",
            "$GPRMC,133543.000,A,4237.4416,N,2321.8398,E,11.56,,051117,,,A*7E",
            "$GPGGA,133544.000,4237.4418,N,2321.8354,E,1,0,,,M,,M,,*45",
            "$GPRMC,133544.000,A,4237.4418,N,2321.8354,E,11.75,,051117,,,A*76",
            "$GPGGA,133545.000,4237.4420,N,2321.8310,E,1,0,,,M,,M,,*4F",
            "$GPRMC,133545.000,A,4237.4420,N,2321.8310,E,11.79,,051117,,,A*70",
            "$GPGGA,133546.000,4237.4422,N,2321.8265,E,1,0,,,M,,M,,*4D",
            "$GPRMC,133546.000,A,4237.4422,N,2321.8265,E,11.83,,051117,,,A*77",
            "$GPGGA,133547.000,4237.4423,N,2321.8221,E,1,0,,,M,,M,,*4D",
            "$GPRMC,133547.000,A,4237.4423,N,2321.8221,E,11.84,,051117,,,A*70",
            "$GPGGA,133548.000,4237.4425,N,2321.8178,E,1,0,,,M,,M,,*4B",
            "$GPRMC,133548.000,A,4237.4425,N,2321.8178,E,11.81,,051117,,,A*73",
            "$GPGGA,133549.000,4237.4428,N,2321.8135,E,1,0,,,M,,M,,*4E",
            "$GPRMC,133549.000,A,4237.4428,N,2321.8135,E,11.77,,051117,,,A*7F"
    };

}

