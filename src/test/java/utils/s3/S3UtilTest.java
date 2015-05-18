package utils.s3;


import exceptions.s3.NoFileInBucketException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Logitech on 08.05.15.
 */
public class S3UtilTest {
    private static final String TEST_BUCKET_NAME = "logitech-analytics-ksolod-s3test";
    private static final String TEST_OBJECT_KEY = "testKey";
    private static final byte[] MOCK_OBJECT = hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d");
    private static final InputStream TEST_OBJECT = new ByteArrayInputStream(MOCK_OBJECT);


    @BeforeClass
    public static void init() {
        assertFalse(S3Util.isExist(TEST_BUCKET_NAME));
        S3Util.createBucket(TEST_BUCKET_NAME);
        assertTrue(S3Util.isExist(TEST_BUCKET_NAME));
    }


    @AfterClass
    public static void testDeleteBucket() throws Exception {
      S3Util.cleanBucket(TEST_BUCKET_NAME);
        S3Util.deleteBucket(TEST_BUCKET_NAME);
        S3Util.isExist(TEST_BUCKET_NAME);
    }


    @Test
    public void testPutFileOnBucket() throws Exception {
        S3Util.cleanBucket(TEST_BUCKET_NAME);
        S3Util.putFileOnBucket(TEST_BUCKET_NAME, TEST_OBJECT_KEY, TEST_OBJECT);
        assertTrue(S3Util.isFileExist(TEST_BUCKET_NAME,TEST_OBJECT_KEY));
    }


    @Test
    public void testGetFileFromBucket() throws Exception {
        S3Util.cleanBucket(TEST_BUCKET_NAME);
        S3Util.putFileOnBucket(TEST_BUCKET_NAME, TEST_OBJECT_KEY, TEST_OBJECT);
        InputStream getObj = S3Util.getFileFromBucket(TEST_BUCKET_NAME, TEST_OBJECT_KEY).getObjectContent();
        assertTrue(isEqualStreams(getObj, TEST_OBJECT));
    }


    @Test(expected = NoFileInBucketException.class)
    public void testDeleteFileFromBucket() throws Exception {

        S3Util.deleteFileFromBucket(TEST_BUCKET_NAME, TEST_OBJECT_KEY);
        assertNull(S3Util.getFileFromBucket(TEST_BUCKET_NAME, TEST_OBJECT_KEY));
    }

    @Test
    public void testGetAllObjectSummaries() throws Exception {
       S3Util.cleanBucket(TEST_BUCKET_NAME);
        final int count = 10;
        for (int i = 0; i < count; i++) {
            S3Util.putFileOnBucket(TEST_BUCKET_NAME, TEST_OBJECT_KEY + i, TEST_OBJECT);
        }
        List summariesList = S3Util.getAllObjectSummaries(TEST_BUCKET_NAME);
        assertEquals(count,summariesList.size());
       //TODO
        for (int i = 0; i < 10; i++) {

        }
    }

    @Test
    public void testCleanBucket() throws Exception {
        final int count = 15;
        for (int i = 0; i < count; i++) {
            S3Util.putFileOnBucket(TEST_BUCKET_NAME, TEST_OBJECT_KEY + i, TEST_OBJECT);
        }

        assertEquals(S3Util.getAllObjectSummaries(TEST_BUCKET_NAME).size(), count);
        S3Util.cleanBucket(TEST_BUCKET_NAME);
        assertEquals(S3Util.getAllObjectSummaries(TEST_BUCKET_NAME).size(), 0);

    }


    @Test
    public void testGetBuckettsList() throws Exception {

    }

    @Test
    public void testSetPublicPermissions() throws Exception {

    }

    @Test
    public void testSetNotificationConfiguration() throws Exception {

    }


    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    private static boolean isEqualStreams(InputStream i1, InputStream i2)
            throws IOException {

        ReadableByteChannel ch1 = Channels.newChannel(i1);
        ReadableByteChannel ch2 = Channels.newChannel(i2);

        ByteBuffer buf1 = ByteBuffer.allocateDirect(1024);
        ByteBuffer buf2 = ByteBuffer.allocateDirect(1024);

        try {
            while (true) {

                int n1 = ch1.read(buf1);
                int n2 = ch2.read(buf2);

                if (n1 == -1 || n2 == -1) return n1 == n2;

                buf1.flip();
                buf2.flip();

                for (int i = 0; i < Math.min(n1, n2); i++) {
                    assertEquals(buf1.get(), buf2.get());
                    if (buf1.get() != buf2.get())
                        return false;
                }


                buf1.compact();
                buf2.compact();
            }
        } finally {
            if (i1 != null) i1.close();
            if (i2 != null) i2.close();
        }
    }

    public static void fillTheBucket(int count) {

    }
}