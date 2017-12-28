package com.maddob.madroute.util;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

@Component
public class DataUtils {

    public Byte[] getResourceBytes(final String filePath) throws IOException, URISyntaxException {
        final InputStream inputStream = this.getClass().getClassLoader()
                .getResourceAsStream(filePath);
        final Byte[] bytes = new Byte[inputStream.available()];
        int byteIndex = 0;
        int byteRead;
        while ((byteRead = inputStream.read()) != -1) {
            bytes[byteIndex++] = (byte) byteRead;
        }
        inputStream.close();
        return bytes;
    }

    public Byte[] getResourceBytes(final Resource resource) throws IOException {
        final Byte[] bytes = new Byte[(int) resource.contentLength()];
        final InputStream inputStream = resource.getInputStream();
        int byteIndex = 0;
        int byteRead;
        while ((byteRead = inputStream.read()) != -1) {
            bytes[byteIndex++] = (byte) byteRead;
        }
        inputStream.close();

        return bytes;
    }

    public String byteArrayAsBase64String(final Byte[] byteArray) {
        return DatatypeConverter.printBase64Binary(unwrapByteArray(byteArray));
    }

    public Byte[] base64StringToByteArray(final String base64String) {
        return wrapByteArray(DatatypeConverter.parseBase64Binary(base64String));
    }

    public byte[] unwrapByteArray(final Byte[] byteObjects) {
        final byte[] byteArray = new byte[byteObjects.length];
        int byteIndex = 0;
        for(byte b: byteObjects) {
            byteArray[byteIndex++] = b;
        }
        return byteArray;
    }

    public Byte[] wrapByteArray(final byte[] byteArray) {
        final Byte[] byteObjects = new Byte[byteArray.length];
        int byteIndex = 0;
        for (Byte b : byteArray) {
            byteObjects[byteIndex++] = b;
        }
        return byteObjects;
    }
}
