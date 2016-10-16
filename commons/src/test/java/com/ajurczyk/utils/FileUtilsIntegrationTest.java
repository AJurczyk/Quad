package com.ajurczyk.utils;

import org.testng.Assert;
import org.testng.annotations.*;

import java.io.*;

/**
 * Used to test class {@link FileUtils}.
 *
 * @author aleksander.jurczyk@seedlabs.io
 */
@Test(groups = {"integrationTests"})
public class FileUtilsIntegrationTest {

    private static final String TEST_FILE_PATH1 = "src/test/resources/testFile";
    private static final String TEST_FILE_PATH2 = "src/test/resources/testFile2";
    private static final String TEST_STRING_1 = "ABC";
    private static final String TEST_STRING_2 = "GHI";
    private static final String TEST_STRING_3 = "JKL";
    private static final String TEST_STRING_4 = "MNO";

    /**
     * Create file used by tests.
     *
     * @throws UnsupportedEncodingException thrown if the named charset is not supported
     * @throws FileNotFoundException        thrown if the file doesn't exist
     */

    @BeforeMethod
    public final void createTestFiles() throws IOException {
        createFile(TEST_FILE_PATH1, TEST_STRING_1);
        createFile(TEST_FILE_PATH2, "Some data");
    }

    /**
     * Delete test file.
     */
    @AfterMethod
    public final void deleteTestFiles() {
        new File(TEST_FILE_PATH1).delete();
        new File(TEST_FILE_PATH2).delete();
    }

    /**
     * Read from file, make ascii byte[].
     *
     * @throws IOException if an I/O error occurs reading from the stream
     */
    @Test
    public final void fileToAsciiByteArray() throws IOException {
        //when
        final byte[] asciiByteArray = FileUtils.readAsByteArray(TEST_FILE_PATH1);

        //then
        Assert.assertEquals(asciiByteArray, new byte[]{65, 66, 67});
    }

    /**
     * Create/replace file with content of ascii byte[].
     *
     * @throws IOException if an I/O error occurs writing to file
     */
    @Test
    public final void asciiByteArrayToFile() throws IOException {
        //given
        new File(TEST_FILE_PATH1).delete();
        final byte[] asciiByteArray = new byte[]{68, 69, 70};

        //when
        FileUtils.save(asciiByteArray, TEST_FILE_PATH1, false);

        //then
        final byte[] fileContent = FileUtils.readAsByteArray(TEST_FILE_PATH1);
        Assert.assertEquals(fileContent, new byte[]{68, 69, 70});
    }

    @Test
    public final void stringToFile() throws IOException {
        //when
        new File(TEST_FILE_PATH1).delete();
        FileUtils.save(TEST_STRING_1, TEST_FILE_PATH1, false);

        //then
        final byte[] fileContent = FileUtils.readAsByteArray(TEST_FILE_PATH1);
        Assert.assertEquals(fileContent, new byte[]{65, 66, 67});
    }

    @Test
    public final void appendStringToFile() throws IOException {
        //given
        new File(TEST_FILE_PATH1).delete();
        createFile(TEST_FILE_PATH1, TEST_STRING_2);

        //when
        FileUtils.save(TEST_STRING_3, TEST_FILE_PATH1, true);

        //then
        final byte[] fileContent = FileUtils.readAsByteArray(TEST_FILE_PATH1);
        Assert.assertEquals(fileContent, new byte[]{71, 72, 73, 74, 75, 76});
    }

    @Test
    public final void appendByteArrayToFile() throws IOException {
        //given
        new File(TEST_FILE_PATH1).delete();
        createFile(TEST_FILE_PATH1, TEST_STRING_4);
        final byte[] asciiByteArray = new byte[]{80, 81, 82};

        //when
        FileUtils.save(asciiByteArray, TEST_FILE_PATH1, true);

        //then
        final byte[] fileContent = FileUtils.readAsByteArray(TEST_FILE_PATH1);
        Assert.assertEquals(fileContent, new byte[]{77, 78, 79, 80, 81, 82});
    }

    @Test
    public final void verifyPathsPositive() throws FileNotFoundException, UnsupportedEncodingException {
        ///given
        final String[] paths = new String[]{TEST_FILE_PATH1, TEST_FILE_PATH2};

        //when
        FileUtils.verifyPaths(paths);
    }

    @Test(expectedExceptions = FileNotFoundException.class)
    public final void verifyPathsNegative() throws FileNotFoundException, UnsupportedEncodingException {
        ///given
        final String[] paths = new String[]{TEST_FILE_PATH1, TEST_FILE_PATH2, "SomeInvalidPath"};

        //when
        FileUtils.verifyPaths(paths);
    }

    private final void createFile(String path, String content) throws IOException {
        final FileOutputStream fos = new FileOutputStream(path);
        try {
            fos.write(content.getBytes("UTF-8"));
        } finally {
            fos.close();
        }
    }
}
