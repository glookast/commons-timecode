package com.glookast.commons.timecode;

import com.glookast.commons.timecode.AbstractTimecode.StringType;
import org.junit.*;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TimecodeTest
{

    public TimecodeTest()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    private void testIllegalTimecode(Timecode timecode, int timecodeBase, boolean dropFrame)
    {
        int adjustment = (dropFrame) ? ((timecodeBase == 60) ? 4 : 2) : 0;

        int h = timecode.getHours();
        int m = timecode.getMinutes();
        int s = timecode.getSeconds();
        int f = timecode.getFrames();

        int minFrames = 0;
        if ((m % 10) != 0 && s == 0) {
            minFrames = adjustment;
        }

        assertTrue(h >= 0 && h < 24);
        assertTrue(m >= 0 && m < 60);
        assertTrue(s >= 0 && s < 60);
        assertTrue(f >= minFrames && s < 60);
    }

    private void testConsecutiveTimecode(Timecode t1, Timecode t2)
    {
        assertTrue(t2.getTimecodeBase() == t1.getTimecodeBase());
        assertTrue(t2.isDropFrame() == t1.isDropFrame());

        int tb = t1.getTimecodeBase();
        boolean df = t1.isDropFrame();
        int adjustment = (df) ? ((tb == 60) ? 4 : 2) : 0;

        int h1 = t1.getHours();
        int m1 = t1.getMinutes();
        int s1 = t1.getSeconds();
        int f1 = t1.getFrames();

        int h2 = t2.getHours();
        int m2 = t2.getMinutes();
        int s2 = t2.getSeconds();
        int f2 = t2.getFrames();

        int minFrames = 0;
        if ((m2 % 10) != 0 && s2 == 0) {
            minFrames = adjustment;
        }

        if (f2 > minFrames) {
            assertTrue(h1 == h2);
            assertTrue(m1 == m2);
            assertTrue(s1 == s2);
            assertTrue(f1 == f2 - 1);
        } else {
            assertTrue(f2 == minFrames);
            if (s2 > 0) {
                assertTrue(h1 == h2);
                assertTrue(m1 == m2);
                assertTrue(s1 == s2 - 1);
                assertTrue(f1 == tb - 1);
            } else {
                assertTrue(s2 == 0);
                if (m2 > 0) {
                    assertTrue(h1 == h2);
                    assertTrue(m1 == m2 - 1);
                    assertTrue(s1 == 59);
                    assertTrue(f1 == tb - 1);
                } else {
                    assertTrue(m2 == 0);
                    if (h2 > 0) {
                        assertTrue(h1 == h2 - 1);
                        assertTrue(m1 == 59);
                        assertTrue(s1 == 59);
                        assertTrue(f1 == tb - 1);
                    } else {
                        assertTrue(h2 == 0);
                        assertTrue(h1 == 23);
                        assertTrue(m1 == 59);
                        assertTrue(s1 == 59);
                        assertTrue(f1 == tb - 1);
                    }
                }
            }
        }
    }

    private void testTimecodeEquality(Timecode tc1, Timecode tc2)
    {
        assertTrue(tc1.getTimecodeBase() == tc2.getTimecodeBase());
        assertTrue(tc1.isDropFrame() == tc2.isDropFrame());
        assertTrue(tc1.getFrameNumber() == tc2.getFrameNumber());
        assertTrue(tc1.getHours() == tc2.getHours());
        assertTrue(tc1.getMinutes() == tc2.getMinutes());
        assertTrue(tc1.getSeconds() == tc2.getSeconds());
        assertTrue(tc1.getFrames() == tc2.getFrames());
    }

    private void testStorageFormat(Timecode tc1)
    {
        Timecode tc2 = Timecode.valueOf(tc1.toString(StringType.Storage));
        testTimecodeEquality(tc1, tc2);
    }

    private void testOldStorageFormat(Timecode tc1, int timecodeBase, boolean dropFrame)
    {
        int numerator, denominator;

        switch (timecodeBase) {
            case 0:
                return;
            case 24:
                numerator = 24000;
                denominator = 1001;
                break;
            case 25:
                numerator = 25;
                denominator = 1;
                break;
            case 30:
                numerator = 30000;
                denominator = 1001;
                break;
            case 48:
                numerator = 48000;
                denominator = 1001;
                break;
            case 50:
                numerator = 50;
                denominator = 1;
                break;
            case 60:
                numerator = 60000;
                denominator = 1001;
                break;
            default:
                fail();
                return;
        }

        String oldStorageFormat = tc1.toString(StringType.Normal).replace(';', ':') + ";" + numerator + ";" + denominator + ";" + dropFrame;
        Timecode tc2 = Timecode.valueOf(oldStorageFormat);
        testTimecodeEquality(tc1, tc2);
    }

    private void testParsingFormat(Timecode tc1, int timecodeBase)
    {
        String tc2Str = tc1.toString();
        Timecode tc2 = Timecode.valueOf(tc2Str, timecodeBase);
        testTimecodeEquality(tc1, tc2);

        String tc3Str = tc1.toString(StringType.Normal);
        Timecode tc3 = Timecode.valueOf(tc3Str, timecodeBase, StringType.Normal);
        testTimecodeEquality(tc1, tc3);

        String tc4Str = tc1.toString(StringType.Milliseconds);
        Timecode tc4 = Timecode.valueOf(tc4Str, timecodeBase, StringType.Milliseconds);
        tc4.setDropFrame(tc1.isDropFrame());
        testTimecodeEquality(tc1, tc4);

        String tc5Str = tc1.toString(StringType.SMPTE_HIGH_FRAME_RATE);
        Timecode tc5 = Timecode.valueOf(tc5Str, timecodeBase, StringType.SMPTE_HIGH_FRAME_RATE);
        testTimecodeEquality(tc1, tc5);

        String tc6Str = tc1.toString(StringType.SMPTE_ST_12M_BINARY_CODED_DECIMALS);
        Timecode tc6 = Timecode.valueOf(tc6Str, timecodeBase, StringType.SMPTE_ST_12M_BINARY_CODED_DECIMALS);
        testTimecodeEquality(tc1, tc6);

        String tc7Str = tc1.toString(StringType.SMPTE_ST_258);
        Timecode tc7 = Timecode.valueOf(tc7Str, timecodeBase, StringType.SMPTE_ST_258);
        testTimecodeEquality(tc1, tc7);
    }

    /**
     * Tests for a timecode base + drop frame flag if a whole day from 00:00:00:00 to next day 00:00:00:00 (inclusive) is proceeding correctly
     * It tests for:
     * - illegal values for hours, minutes, seconds and frames
     * - timecodes being correctly consecutive
     * - if converting to storage format and back works
     * - parsing old storage format works
     * - if parsing timecode with given timecode base works
     *
     * @param timecodeBase
     * @param dropFrame
     */
    private void testTimecode(int timecodeBase, boolean dropFrame)
    {
        long totalNumberFramesInADay;

        if (dropFrame) {
            totalNumberFramesInADay = 24 * 6 * (1800 + 9 * 1798);
            if (timecodeBase == 60) {
                totalNumberFramesInADay *= 2;
            }
        } else {
            totalNumberFramesInADay = 24 * 60 * 60 * timecodeBase;
        }

        System.out.println("Timecode base " + timecodeBase + ((dropFrame) ? " Drop Frame" : " Non Drop Frame"));

        Timecode prev = new Timecode(timecodeBase, 0, dropFrame);
        assertTrue(prev.getFrameNumber() == 0);

        testIllegalTimecode(prev, timecodeBase, dropFrame);
        testStorageFormat(prev);
        testOldStorageFormat(prev, timecodeBase, dropFrame);
        testParsingFormat(prev, timecodeBase);

        for (long frameNumber = 1; frameNumber <= totalNumberFramesInADay; frameNumber++) {
            Timecode tc = new Timecode(timecodeBase, frameNumber, dropFrame);

            testIllegalTimecode(tc, timecodeBase, dropFrame);
            testStorageFormat(tc);
            testOldStorageFormat(tc, timecodeBase, dropFrame);
            testParsingFormat(tc, timecodeBase);
            testConsecutiveTimecode(prev, tc);

            prev = tc;
        }

        assertTrue(prev.getFrameNumber() == 0);
    }

    @Test
    public void testInvalidTimecode()
    {
        testTimecode(0, false);
    }

    @Test
    public void testTimecodeBase30DF()
    {
        testTimecode(30, true);
    }

    @Test
    public void testTimecodeBase60DF()
    {
        testTimecode(60, true);
    }

    @Test
    public void testTimecodeBase24NDF()
    {
        testTimecode(24, false);
    }

    @Test
    public void testTimecodeBase25NDF()
    {
        testTimecode(25, false);
    }

    @Test
    public void testTimecodeBase30NDF()
    {
        testTimecode(30, false);
    }

    @Test
    public void testTimecodeBase48NDF()
    {
        testTimecode(48, false);
    }

    @Test
    public void testTimecodeBase50NDF()
    {
        testTimecode(50, false);
    }

    @Test
    public void testTimecodeBase60NDF()
    {
        testTimecode(60, false);
    }

    @Test
    public void testInPointOutPointDuration()
    {
        Timecode inPoint = new Timecode(30, 10, 0, 0, 0, true);
        Timecode outPoint = new Timecode(30, 5, 0, 0, 0, true);

        TimecodeDuration duration = Timecode.calculateDuration(inPoint, outPoint);

        assertTrue(duration.hours == 19);
        assertTrue(duration.minutes == 0);
        assertTrue(duration.seconds == 0);
        assertTrue(duration.frames == 0);

        Timecode inPoint2 = Timecode.calculateInPoint(outPoint, duration);
        Timecode outPoint2 = Timecode.calculateOutPoint(inPoint, duration);

        testTimecodeEquality(inPoint, inPoint2);
        testTimecodeEquality(outPoint, outPoint2);
    }

    @Test
    public void testTimecodeConversion()
    {
        Timecode tc1 = new Timecode(30, 0, 1, 0, 0, false);
        tc1.setDropFrame(true);
        assertTrue(tc1.hours == 0);
        assertTrue(tc1.minutes == 1);
        assertTrue(tc1.seconds == 0);
        assertTrue(tc1.frames == 2);

        Timecode tc2 = new Timecode(30, 10, 1, 5, 29, true);
        Timecode tc3 = new Timecode(tc2);

        tc2.setTimecodeBase(25);
        assertTrue(tc2.hours == 10);
        assertTrue(tc2.minutes == 1);
        assertTrue(tc2.seconds == 5);
        assertTrue(tc2.frames == 24);

        tc2.setTimecodeBase(30);
        tc2.setDropFrame(true);

        assertTrue(tc2.hours == 10);
        assertTrue(tc2.minutes == 1);
        assertTrue(tc2.seconds == 5);
        assertTrue(tc2.frames == 29);

        testTimecodeEquality(tc2, tc3);
    }

}
