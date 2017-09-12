package com.glookast.commons.timecode;

import java.io.Serializable;

public class TimecodeDuration extends AbstractTimecode implements Serializable
{
    private static final long serialVersionUID = 1L;

    public TimecodeDuration()
    {
    }

    public TimecodeDuration(int timecodeBase)
    {
        super(timecodeBase);
    }

    public TimecodeDuration(int timecodeBase, boolean dropFrame)
    {
        super(timecodeBase, dropFrame);
    }

    public TimecodeDuration(int timecodeBase, long frameNumber)
    {
        super(timecodeBase, frameNumber);
    }

    public TimecodeDuration(int timecodeBase, long frameNumber, boolean dropFrame)
    {
        super(timecodeBase, frameNumber, dropFrame);
    }

    public TimecodeDuration(int timecodeBase, int hours, int minutes, int seconds, int frames)
    {
        super(timecodeBase, hours, minutes, seconds, frames);
    }

    public TimecodeDuration(int timecodeBase, int hours, int minutes, int seconds, int frames, boolean dropFrame)
    {
        super(timecodeBase, hours, minutes, seconds, frames, dropFrame);
    }

    public TimecodeDuration(AbstractTimecode timecode)
    {
        super(timecode);
    }

    @Override
    protected long truncateFrameNumber(long value)
    {
        if (value < 0) {
            return 0;
        }
        return value;
    }

    /**
     * Returns a TimecodeDuration instance for given TimecodeDuration storage string. Will return null in case the storage string represents a null TimecodeDuration
     *
     * @param timecode
     * @return the TimecodeDuration
     * @throws IllegalArgumentException
     */
    public static TimecodeDuration valueOf(String timecode) throws IllegalArgumentException
    {
        TimecodeDuration td = new TimecodeDuration();
        return (TimecodeDuration)td.parse(timecode);
    }

    /**
     * Returns a TimecodeDuration instance for given TimecodeDuration string and TimecodeDuration base. Acceptable inputs are
     * the normal representation xx:xx:xx:xx for non drop frame and xx:xx:xx;xx for drop frame
     * and SMPTE331M representation which uses binary coded decimals
     *
     * @param timecode
     * @param timecodeBase
     * @return the TimecodeDuration
     * @throws IllegalArgumentException
     */
    public static TimecodeDuration valueOf(String timecode, int timecodeBase) throws IllegalArgumentException
    {
        TimecodeDuration td = new TimecodeDuration();
        return (TimecodeDuration)td.parse(timecode, timecodeBase);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return super.equals(obj);
    }
}
