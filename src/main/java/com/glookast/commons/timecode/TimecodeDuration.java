package com.glookast.commons.timecode;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glookast.commons.timecode.json.TimecodeDurationDeserializer;
import com.glookast.commons.timecode.json.TimecodeDurationSerializer;

import java.io.Serializable;

@JsonSerialize(using = TimecodeDurationSerializer.class)
@JsonDeserialize(using = TimecodeDurationDeserializer.class)
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
        return (TimecodeDuration) td.parse(timecode);
    }

    /**
     * Returns a TimecodeDuration instance for given timecode string and timecode base. Acceptable inputs are
     * the normal representation HH:MM:SS:FF for non drop frame and HH:MM:SS:FF for drop frame
     *
     * @param timecode
     * @param timecodeBase
     * @return the TimecodeDuration
     * @throws IllegalArgumentException
     */
    public static TimecodeDuration valueOf(String timecode, int timecodeBase) throws IllegalArgumentException
    {
        return valueOf(timecode, timecodeBase, StringType.Normal);
    }

    /**
     * Returns a TimecodeDuration instance for given timecode string and timecode base.
     * What is considered acceptable input varies per selected StringType
     *
     * @param timecode
     * @param timecodeBase
     * @param stringType
     * @return the TimecodeDuration
     * @throws IllegalArgumentException
     */
    public static TimecodeDuration valueOf(String timecode, int timecodeBase, StringType stringType) throws IllegalArgumentException
    {
        TimecodeDuration tc = new TimecodeDuration();
        return (TimecodeDuration) tc.parse(timecode, timecodeBase, stringType);
    }
}
