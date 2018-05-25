package com.glookast.commons.timecode;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glookast.commons.timecode.json.TimecodeDeserializer;
import com.glookast.commons.timecode.json.TimecodeSerializer;

import java.io.Serializable;

@JsonSerialize(using = TimecodeSerializer.class)
@JsonDeserialize(using = TimecodeDeserializer.class)
public class Timecode extends AbstractTimecode implements Serializable
{
    private static final long serialVersionUID = 1L;

    public Timecode()
    {
    }

    public Timecode(int timecodeBase)
    {
        super(timecodeBase);
    }

    public Timecode(int timecodeBase, boolean dropFrame)
    {
        super(timecodeBase, dropFrame);
    }

    public Timecode(int timecodeBase, long frameNumber)
    {
        super(timecodeBase, frameNumber);
    }

    public Timecode(int timecodeBase, long frameNumber, boolean dropFrame)
    {
        super(timecodeBase, frameNumber, dropFrame);
    }

    public Timecode(int timecodeBase, int hours, int minutes, int seconds, int frames)
    {
        super(timecodeBase, hours, minutes, seconds, frames);
    }

    public Timecode(int timecodeBase, int hours, int minutes, int seconds, int frames, boolean dropFrame)
    {
        super(timecodeBase, hours, minutes, seconds, frames, dropFrame);
    }

    public Timecode(AbstractTimecode timecode)
    {
        super(timecode);
    }

    @Override
    protected long truncateFrameNumber(long value)
    {
        long mod = 24 * 6 * framesPerTenMinutes;
        return ((value % mod) + mod) % mod;
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
     * Returns a Timecode instance for given Timecode storage string. Will return an invalid timecode in case the storage string represents an invalid Timecode
     *
     * @param timecode
     * @return the timecode
     * @throws IllegalArgumentException
     */
    public static Timecode valueOf(String timecode) throws IllegalArgumentException
    {
        Timecode tc = new Timecode();
        return (Timecode) tc.parse(timecode);
    }

    /**
     * Returns a Timecode instance for given timecode string and timecode base. Acceptable inputs are
     * the normal representation HH:MM:SS:FF for non drop frame and HH:MM:SS:FF for drop frame
     *
     * @param timecode
     * @param timecodeBase
     * @return the timecode
     * @throws IllegalArgumentException
     */
    public static Timecode valueOf(String timecode, int timecodeBase) throws IllegalArgumentException
    {
        return valueOf(timecode, timecodeBase, StringType.Normal);
    }

    /**
     * Returns a Timecode instance for given timecode string and timecode base.
     * What is considered acceptable input varies per selected StringType
     *
     * @param timecode
     * @param timecodeBase
     * @param stringType
     *
     * @return the timecode
     * @throws IllegalArgumentException
     */
    public static Timecode valueOf(String timecode, int timecodeBase, StringType stringType) throws IllegalArgumentException
    {
        Timecode tc = new Timecode();
        return (Timecode) tc.parse(timecode, timecodeBase, stringType);
    }
}
