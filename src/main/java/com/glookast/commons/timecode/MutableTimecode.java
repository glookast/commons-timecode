package com.glookast.commons.timecode;

public final class MutableTimecode extends AbstractMutableTimecode
{
    public MutableTimecode()
    {
    }

    public MutableTimecode(int timecodeBase)
    {
        super(timecodeBase);
    }

    public MutableTimecode(int timecodeBase, boolean dropFrame)
    {
        super(timecodeBase, dropFrame);
    }

    public MutableTimecode(int timecodeBase, long frameNumber)
    {
        super(timecodeBase, frameNumber);
    }

    public MutableTimecode(int timecodeBase, long frameNumber, boolean dropFrame)
    {
        super(timecodeBase, frameNumber, dropFrame);
    }

    public MutableTimecode(int timecodeBase, int hours, int minutes, int seconds, int frames)
    {
        super(timecodeBase, hours, minutes, seconds, frames);
    }

    public MutableTimecode(int timecodeBase, int hours, int minutes, int seconds, int frames, boolean dropFrame)
    {
        super(timecodeBase, hours, minutes, seconds, frames, dropFrame);
    }

    public MutableTimecode(AbstractTimecode timecode)
    {
        super(timecode);
    }

    @Override
    protected long truncateFrameNumber(long value)
    {
        long mod = 24 * 6 * framesPerTenMinutes;
        return ((value % mod) + mod) % mod;
    }

    /**
     * Returns a MutableTimecode instance for given Timecode storage string. Will return an invalid timecode in case the storage string represents an invalid Timecode
     *
     * @param timecode
     * @return the timecode
     * @throws IllegalArgumentException
     */
    public static MutableTimecode valueOf(String timecode) throws IllegalArgumentException
    {
        MutableTimecode tc = new MutableTimecode();
        return (MutableTimecode) tc.parse(timecode);
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
    public static MutableTimecode valueOf(String timecode, int timecodeBase) throws IllegalArgumentException
    {
        return valueOf(timecode, timecodeBase, StringType.NORMAL);
    }

    /**
     * Returns a Timecode instance for given timecode string and timecode base.
     * What is considered acceptable input varies per selected StringType
     *
     * @param timecode
     * @param timecodeBase
     * @param stringType
     * @return the timecode
     * @throws IllegalArgumentException
     */
    public static MutableTimecode valueOf(String timecode, int timecodeBase, StringType stringType) throws IllegalArgumentException
    {
        MutableTimecode tc = new MutableTimecode();
        return (MutableTimecode) tc.parse(timecode, timecodeBase, stringType);
    }
}
