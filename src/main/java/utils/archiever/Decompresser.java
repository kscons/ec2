package utils.archiever;

import exceptions.ZIPFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.GZIPInputStream;

/**
 * This class contains the two static methods one of which extracts the data.
 * Another method helps it to do this processing the GZIPInputStream .
 */
public class Decompresser {


    private static final Logger LOG = LoggerFactory.getLogger(Decompresser.class);

    /**
     * Method decompresses data and returns data as OutputStream.
     * It creates GZIPInputStream that is formed on the basis of InputStream which we define.
     *
     * @param in -data which we need to decompress
     * @return decompresses data in a ByteArrayOutputStream
     */
    public static ByteArrayOutputStream decompress(final InputStream in) throws ZIPFormatException {
        try (GZIPInputStream gis = new GZIPInputStream(in);) {
            return doConverting(gis);

        } catch (ZIPFormatException | IOException ioe) {
            ioe.printStackTrace();
            throw new ZIPFormatException("Decompress failed");
        }
    }

    /**
     * That Method performs GZIPInputStream conversion to ByteArrayOutputStream that is an actual process of decompression.
     * It's used only for decompress method.
     *
     * @param is -GZIPInputStream  which was formed in decompress method
     * @return ByteArrayOutputStream decompressed data
     */
    private static ByteArrayOutputStream doConverting(final InputStream is) throws ZIPFormatException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {

            int oneByte;
            while ((oneByte = is.read()) != -1) {
                outputStream.write(oneByte);
            }
            return outputStream;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new ZIPFormatException("Conversion failed");
        }
    }


}

