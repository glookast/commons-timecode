package com.glookast.commons.timecode;

public abstract class AbstractMutableTimecode extends AbstractTimecode
{
    public AbstractMutableTimecode()
    {
    }

    public AbstractMutableTimecode(int timecodeBase)
    {
        super(timecodeBase);
    }

    public AbstractMutableTimecode(int timecodeBase, boolean dropFrame)
    {
        super(timecodeBase, dropFrame);
    }

    public AbstractMutableTimecode(int timecodeBase, long frameNumber)
    {
        super(timecodeBase, frameNumber);
    }

    public AbstractMutableTimecode(int timecodeBase, long frameNumber, boolean dropFrame)
    {
        super(timecodeBase, frameNumber, dropFrame);
    }

    public AbstractMutableTimecode(int timecodeBase, int hours, int minutes, int seconds, int frames)
    {
        super(timecodeBase, hours, minutes, seconds, frames);
    }

    public AbstractMutableTimecode(int timecodeBase, int hours, int minutes, int seconds, int frames, boolean dropFrame)
    {
        super(timecodeBase, hours, minutes, seconds, frames, dropFrame);
    }

    public AbstractMutableTimecode(AbstractTimecode timecode)
    {
        super(timecode);
    }

    /**
     * Invalidates the timecode.
     */
    public void setInvalid()
    {
        setTimecodeBase(0);
    }

    /**
     * Changes timecode base of the timecode
     *
     * @param timecodeBase
     */
    public void setTimecodeBase(int timecodeBase)
    {
        if (timecodeBase < 0) {
            timecodeBase = 0;
        }

        if (this.timecodeBase > 0 && timecodeBase != this.timecodeBase) {
            this.frames = (int) Math.round((double) this.frames * timecodeBase / this.timecodeBase);
        }
        this.timecodeBase = timecodeBase;

        innerSetDropFrame(this.dropFrame);
    }

    /**
     * Changes dropframe flag of the timecode. Note that this only works for timecode bases that are a multiple of 30.
     * @param dropFrame
     */
    public void setDropFrame(boolean dropFrame)
    {
        innerSetDropFrame(dropFrame);
    }

    /**
     * Sets the timecode to the current frame number
     *
     * @param frameNumber Frame number
     */
    public void setFrameNumber(long frameNumber)
    {
        innerSetFrameNumber(frameNumber);
    }

    /**
     * Sets the timecode to the provided hours, minutes, seconds and frames
     *
     * @param hours
     * @param minutes
     * @param seconds
     * @param frames
     */
    public void setHMSF(int hours, int minutes, int seconds, int frames)
    {
        innerSetHMSF(hours, minutes, seconds, frames);
    }

    /**
     * Sets the hour component of the timecode
     *
     * @param hours
     */
    public void setHours(int hours)
    {
        this.hours = limit(hours, 0, Integer.MAX_VALUE);
        calculateFrameNumber();
    }

    /**
     * Sets the minute component of the timecode
     *
     * @param minutes
     */
    public void setMinutes(int minutes)
    {
        this.minutes = limit(minutes, 0, 59);
        frames = limitFrames(frames);
        calculateFrameNumber();
    }

    /**
     * Sets the second component of the timecode
     *
     * @param seconds
     */
    public void setSeconds(int seconds)
    {
        this.seconds = limit(seconds, 0, 59);
        frames = limitFrames(frames);
        calculateFrameNumber();
    }

    /**
     * Sets the frames component of the timecode
     *
     * @param frames
     */
    public void setFrames(int frames)
    {
        this.frames = limitFrames(frames);
        calculateFrameNumber();
    }

    /**
     * Adds indicated number of hours to the timecode
     *
     * @param hours
     */
    public void addHours(int hours)
    {
        this.hours += hours;
        calculateFrameNumber();
    }

    /**
     * Adds indicated number of minutes to the timecode
     *
     * @param minutes
     */
    public void addMinutes(int minutes)
    {
        this.minutes += minutes;
        frames = limitFrames(frames);
        calculateFrameNumber();
    }

    /** Adds indicated number of seconds to the timecode
     *
     * @param seconds
     */
    public void addSeconds(int seconds)
    {
        this.seconds += seconds;
        frames = limitFrames(frames);
        calculateFrameNumber();
    }

    /** Adds indicated number of frames to the timecode
     *
     * @param frames
     */
    public void addFrames(long frames)
    {
        frameNumber += frames;
        calculateHMSF();
    }
}
