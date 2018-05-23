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
        long mod = 24 * 6 * myFramesPerTenMinutes;
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
     * Returns a Timecode instance for given Timecode storage string. Will return null in case the storage string represents a null Timecode
     *
     * @param timecode
     * @return the timecode
     * @throws IllegalArgumentException
     */
    public static Timecode valueOf(String timecode) throws IllegalArgumentException
    {
        Timecode tc = new Timecode();
        return (Timecode)tc.parse(timecode);
    }

    /**
     * Returns a Timecode instance for given timecode string and timecode base. Acceptable inputs are
     * the normal representation xx:xx:xx:xx for non drop frame and xx:xx:xx;xx for drop frame
     * and SMPTE331M representation which uses binary coded decimals
     *
     * @param timecode
     * @param timecodeBase
     * @return the timecode
     * @throws IllegalArgumentException
     */
    public static Timecode valueOf(String timecode, int timecodeBase) throws IllegalArgumentException
    {
        Timecode tc = new Timecode();
        return (Timecode)tc.parse(timecode, timecodeBase);
    }
}
