package com.glookast.commons.timecode;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TimecodeSequenceCollectionTest
{
    public TimecodeSequenceCollectionTest()
    {
    }

    public void assertTimecodeSequenceCovered(TimecodeSequenceCollection tsc, TimecodeSequence ts)
    {
        Assert.assertEquals(ts.timecode, tsc.getTimecode(ts.getTimecodeSource(), ts.position));
        Assert.assertEquals(ts.position, (long) tsc.getPosition(ts.getTimecodeSource(), ts.timecode, ts.position).get());
    }

    @Test
    public void testAddAndClear()
    {
        TimecodeSequenceCollection tsc = new TimecodeSequenceCollection();

        TimecodeSequence timecodeSequence = new TimecodeSequence(TimecodeSource.LTC, new Timecode(25, 0), 0);
        tsc.addTimecode(timecodeSequence);

        assertTimecodeSequenceCovered(tsc, timecodeSequence);

        List<TimecodeSequence> timecodeSequences = tsc.getTimecodeSequences();

        Assert.assertEquals(1, timecodeSequences.size());
        Assert.assertEquals(timecodeSequence, timecodeSequences.get(0));

        tsc.clear();

        Assert.assertEquals(0, tsc.getTimecodeSequences().size());
    }

    @Test
    public void testAddMatchingTimecodesInOrder()
    {
        TimecodeSequenceCollection tsc = new TimecodeSequenceCollection();

        TimecodeSequence timecodeSequence1 = new TimecodeSequence(TimecodeSource.LTC, new Timecode(25, 100), 0);
        TimecodeSequence timecodeSequence2 = new TimecodeSequence(TimecodeSource.LTC, new Timecode(25, 110), 10);

        tsc.addTimecode(timecodeSequence1);
        tsc.addTimecode(timecodeSequence2);

        assertTimecodeSequenceCovered(tsc, timecodeSequence1);
        assertTimecodeSequenceCovered(tsc, timecodeSequence2);

        List<TimecodeSequence> timecodeSequences = tsc.getTimecodeSequences();

        Assert.assertEquals(1, timecodeSequences.size());
        Assert.assertEquals(timecodeSequence1, timecodeSequences.get(0));
    }

    @Test
    public void testAddMatchingTimecodesOutOfOrder()
    {
        TimecodeSequenceCollection tsc = new TimecodeSequenceCollection();

        TimecodeSequence timecodeSequence2 = new TimecodeSequence(TimecodeSource.LTC, new Timecode(25, 110), 10);
        TimecodeSequence timecodeSequence1 = new TimecodeSequence(TimecodeSource.LTC, new Timecode(25, 100), 0);

        tsc.addTimecode(timecodeSequence2);
        tsc.addTimecode(timecodeSequence1);

        assertTimecodeSequenceCovered(tsc, timecodeSequence1);
        assertTimecodeSequenceCovered(tsc, timecodeSequence2);

        List<TimecodeSequence> timecodeSequences = tsc.getTimecodeSequences();

        Assert.assertEquals(1, timecodeSequences.size());
        Assert.assertEquals(timecodeSequence1, timecodeSequences.get(0));
    }

    @Test
    public void testNonMatchingTimecodesInOrder()
    {
        TimecodeSequenceCollection tsc = new TimecodeSequenceCollection();

        List<TimecodeSequence> list = new ArrayList<>();
        list.add(new TimecodeSequence(TimecodeSource.LTC, new Timecode(25, 100), 0));
        list.add(new TimecodeSequence(TimecodeSource.LTC, new Timecode(25, 105), 5)); // matching first sequence
        list.add(new TimecodeSequence(TimecodeSource.LTC, new Timecode(25, 120), 10));
        list.add(new TimecodeSequence(TimecodeSource.LTC, new Timecode(25, 120), 20));

        list.forEach(tsc::addTimecode);
        list.forEach(ts -> assertTimecodeSequenceCovered(tsc, ts));

        List<TimecodeSequence> timecodeSequences = tsc.getTimecodeSequences();
        Assert.assertEquals(3, timecodeSequences.size());
    }

    @Test
    public void testNonMatchingTimecodesOutOfOrder()
    {
        TimecodeSequenceCollection tsc = new TimecodeSequenceCollection();

        List<TimecodeSequence> list = new ArrayList<>();
        list.add(new TimecodeSequence(TimecodeSource.LTC, new Timecode(25, 105), 5)); // matching first sequence
        list.add(new TimecodeSequence(TimecodeSource.LTC, new Timecode(25, 120), 10));
        list.add(new TimecodeSequence(TimecodeSource.LTC, new Timecode(25, 120), 20));
        list.add(new TimecodeSequence(TimecodeSource.LTC, new Timecode(25, 100), 0));

        list.forEach(tsc::addTimecode);
        list.forEach(ts -> assertTimecodeSequenceCovered(tsc, ts));

        List<TimecodeSequence> timecodeSequences = tsc.getTimecodeSequences();
        Assert.assertEquals(3, timecodeSequences.size());
    }

    @Test
    public void testTruncate()
    {
        TimecodeSequenceCollection tsc = new TimecodeSequenceCollection();

        List<TimecodeSequence> list = new ArrayList<>();
        list.add(new TimecodeSequence(TimecodeSource.LTC, new Timecode(25, 105), 5)); // matching first sequence
        list.add(new TimecodeSequence(TimecodeSource.LTC, new Timecode(25, 120), 10));
        list.add(new TimecodeSequence(TimecodeSource.LTC, new Timecode(25, 120), 20));
        list.add(new TimecodeSequence(TimecodeSource.LTC, new Timecode(25, 100), 0));

        list.forEach(tsc::addTimecode);

        tsc.truncate(15);

        List<TimecodeSequence> timecodeSequences = tsc.getTimecodeSequences();
        Assert.assertEquals(2, timecodeSequences.size());
    }

    @Test
    public void testCopyConstructor()
    {
        TimecodeSequenceCollection tsc = new TimecodeSequenceCollection();

        List<TimecodeSequence> list = new ArrayList<>();
        list.add(new TimecodeSequence(TimecodeSource.LTC, new Timecode(25, 105), 5)); // matching first sequence
        list.add(new TimecodeSequence(TimecodeSource.LTC, new Timecode(25, 120), 10));
        list.add(new TimecodeSequence(TimecodeSource.LTC, new Timecode(25, 120), 20));
        list.add(new TimecodeSequence(TimecodeSource.LTC, new Timecode(25, 100), 0));

        list.forEach(tsc::addTimecode);

        tsc.truncate(15);

        List<TimecodeSequence> timecodeSequences = tsc.getTimecodeSequences();
        Assert.assertEquals(2, timecodeSequences.size());
    }
}
