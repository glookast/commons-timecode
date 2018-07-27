package com.glookast.commons.timecode;

public final class MutableTimecodeDuration extends AbstractMutableTimecode
{
    public MutableTimecodeDuration()
    {
    }

    public MutableTimecodeDuration(int timecodeBase)
    {
        super(timecodeBase);
    }

    public MutableTimecodeDuration(int timecodeBase, boolean dropFrame)
    {
        super(timecodeBase, dropFrame);
    }

    public MutableTimecodeDuration(int timecodeBase, long frameNumber)
    {
        super(timecodeBase, frameNumber);
    }

    public MutableTimecodeDuration(int timecodeBase, long frameNumber, boolean dropFrame)
    {
        super(timecodeBase, frameNumber, dropFrame);
    }

    public MutableTimecodeDuration(int timecodeBase, int hours, int minutes, int seconds, int frames)
    {
        super(timecodeBase, hours, minutes, seconds, frames);
    }

    public MutableTimecodeDuration(int timecodeBase, int hours, int minutes, int seconds, int frames, boolean dropFrame)
    {
        super(timecodeBase, hours, minutes, seconds, frames, dropFrame);
    }

    public MutableTimecodeDuration(AbstractTimecode timecode)
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
     * Returns a UnmodifiableTimecodeDuration instance for given TimecodeDuration storage string. Will return null in case the storage string represents a null TimecodeDuration
     *
     * @param timecode
     * @return the TimecodeDuration
     * @throws IllegalArgumentException
     */
    public static MutableTimecodeDuration valueOf(String timecode) throws IllegalArgumentException
    {
        MutableTimecodeDuration td = new MutableTimecodeDuration();
        return (MutableTimecodeDuration) td.parse(timecode);
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
    public static MutableTimecodeDuration valueOf(String timecode, int timecodeBase) throws IllegalArgumentException
    {
        return valueOf(timecode, timecodeBase, StringType.NORMAL);
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
    public static MutableTimecodeDuration valueOf(String timecode, int timecodeBase, StringType stringType) throws IllegalArgumentException
    {
        MutableTimecodeDuration tc = new MutableTimecodeDuration();
        return (MutableTimecodeDuration) tc.parse(timecode, timecodeBase, stringType);
    }

    /**
     * Helper function to test whether the timecode duration is valid, which means it is not null and valid
     *
     * @param timecodeDuration
     * @return whether timecode is initialized and valid
     */
    public static boolean isValid(TimecodeDuration timecodeDuration)
    {
        return timecodeDuration != null && timecodeDuration.isValid();
    }
}
