package utils.archiever;

import exceptions.ZIPFormatException;
import org.junit.Test;
import receiver.asynchronous.Mock;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

/**
 * Created by Logitech on 08.05.15.
 */
public class DecompresserTest {
    InputStream report = DecompresserTest.class.getClassLoader().getResourceAsStream("archive_sample.gz");

    @Test
    public void testDecompress() throws Exception {
        try {
            assertEquals(new String(Decompresser.decompress(report).toByteArray(), "UTF-8"), Mock.report);
        } catch (ZIPFormatException | UnsupportedEncodingException z) {

        }
    }
}