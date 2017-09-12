package com.glookast.commons.timecode;

import java.io.Serializable;

public abstract class AbstractTimecode implements Serializable
{
    private static final long serialVersionUID = 1L;

    protected int myTimecodeBase;
    protected boolean myDropFrame;
    protected long myFrameNumber;

    protected int myAdjustmentPerMinute;
    protected int myFramesPerMinute;
    protected int myFramesPerMinuteDropFrame;
    protected int myFramesPerTenMinutes;

    protected int myHours;
    protected int myMinutes;
    protected int mySeconds;
    protected int myFrames;

    public AbstractTimecode()
    {
        this(30);
    }

    public AbstractTimecode(int timecodeBase)
    {
        this(timecodeBase, false);
    }

    public AbstractTimecode(int timecodeBase, boolean dropFrame)
    {
        if (timecodeBase < 1)
            timecodeBase = 1;

        myTimecodeBase = timecodeBase;

        if (timecodeBase != 30 && timecodeBase != 60) {
            dropFrame = false;
        }

        setDropFrame(dropFrame);
    }

    public AbstractTimecode(int timecodeBase, long frameNumber)
    {
        this(timecodeBase, false);
        setFrameNumber(frameNumber);
    }

    public AbstractTimecode(int timecodeBase, long frameNumber, boolean dropFrame)
    {
        this(timecodeBase, dropFrame);
        setFrameNumber(frameNumber);
    }

    public AbstractTimecode(int timecodeBase, int hours, int minutes, int seconds, int frames)
    {
        this(timecodeBase, false);
        setHMSF(hours, minutes, seconds, frames);
    }

    public AbstractTimecode(int timecodeBase, int hours, int minutes, int seconds, int frames, boolean dropFrame)
    {
        this(timecodeBase, dropFrame);
        setHMSF(hours, minutes, seconds, frames);
    }

    public AbstractTimecode(AbstractTimecode timecode)
    {
        this(timecode.getTimecodeBase(), timecode.isDropFrame());
        setFrameNumber(timecode.getFrameNumber());
    }

    private void setDropFrame(boolean dropFrame)
    {
        myDropFrame = dropFrame;
        myAdjustmentPerMinute = (myDropFrame) ? ((myTimecodeBase == 60) ? 4 : 2) : 0;
        myFramesPerMinute = 60 * myTimecodeBase;
        myFramesPerMinuteDropFrame = myFramesPerMinute - myAdjustmentPerMinute;
        myFramesPerTenMinutes = myFramesPerMinute + 9 * myFramesPerMinuteDropFrame;
    }

    public final int getTimecodeBase()
    {
        return myTimecodeBase;
    }

    public final boolean isDropFrame()
    {
        return myDropFrame;
    }

    /**
     * Gets the frame number for current timecode
     * @return Frame number
     */
    public final long getFrameNumber()
    {
        return myFrameNumber;
    }

    /**
     * Sets the timecode to the current frame number
     * @param frameNumber Frame number
     */
    public final void setFrameNumber(long frameNumber)
    {
        myFrameNumber = frameNumber;
        calculateHMSF();
    }

    private int limit(int value, int min, int max)
    {
        return (value < min) ? min : ((value > max) ? max : value);
    }

    private int limitFrames(int frames)
    {
        return limit(frames, (myDropFrame && (myMinutes % 10) != 0 && ((mySeconds % 60) == 0)) ? myAdjustmentPerMinute : 0, myTimecodeBase - 1);
    }

    /**
     * Sets the timecode to the provided hours, minutes, seconds and frames
     * @param hours
     * @param minutes
     * @param seconds
     * @param frames
     */
    public final void setHMSF(int hours, int minutes, int seconds, int frames)
    {
        myHours = limit(hours, 0, Integer.MAX_VALUE);
        myMinutes = limit(minutes, 0, 59);
        mySeconds = limit(seconds, 0, 59);
        myFrames = limitFrames(frames);
        calculateFrameNumber();
    }

    public final int getHours()
    {
        return myHours;
    }

    public final void setHours(int hours)
    {
        myHours = limit(hours, 0, Integer.MAX_VALUE);
        calculateFrameNumber();
    }

    public final int getMinutes()
    {
        return myMinutes;
    }

    public final void setMinutes(int minutes)
    {
        myMinutes = limit(minutes, 0, 59);
        myFrames = limitFrames(myFrames);
        calculateFrameNumber();
    }

    public final int getSeconds()
    {
        return mySeconds;
    }

    public final void setSeconds(int seconds)
    {
        mySeconds = limit(seconds, 0, 59);
        myFrames = limitFrames(myFrames);
        calculateFrameNumber();
    }

    /**
     * Gets the frames component of the timecode
     * @return frames
     */
    public final int getFrames()
    {
        return myFrames;
    }

    /**
     * Sets the Frames component of the timecode
     * @param frames
     */
    public final void setFrames(int frames)
    {
        myFrames = limitFrames(frames);
        calculateFrameNumber();
    }

    public final void addHours(int hours)
    {
        myHours += hours;
        calculateFrameNumber();
    }

    public final void addMinutes(int minutes)
    {
        myMinutes += minutes;
        myFrames = limitFrames(myFrames);
        calculateFrameNumber();
    }

    public final void addSeconds(int seconds)
    {
        mySeconds += seconds;
        myFrames = limitFrames(myFrames);
        calculateFrameNumber();
    }

    public final void addFrames(long frames)
    {
        myFrameNumber += frames;
        calculateHMSF();
    }

    private void calculateFrameNumber()
    {
        int tenMinutes = myHours * 6 + myMinutes / 10;
        int minutes = myMinutes % 10;

        myFrameNumber = tenMinutes * myFramesPerTenMinutes +
                        minutes * myFramesPerMinuteDropFrame +
                        mySeconds * myTimecodeBase +
                        myFrames;

        calculateHMSF();
    }

    private void calculateHMSF()
    {
        myFrameNumber = truncateFrameNumber(myFrameNumber);

        long frameNumber = myFrameNumber;

        if (myDropFrame) {
            long tenMinutesFrames = frameNumber / myFramesPerTenMinutes;
            long remainingMinutes = ((frameNumber % myFramesPerTenMinutes) - myAdjustmentPerMinute) / myFramesPerMinuteDropFrame;
            long dropIncidents = 9 * tenMinutesFrames + remainingMinutes;
            frameNumber += myAdjustmentPerMinute * dropIncidents;
        }

        myFrames = (int)(frameNumber % myTimecodeBase);
        frameNumber = (frameNumber / myTimecodeBase);
        mySeconds = (int)(frameNumber % 60);
        frameNumber = (frameNumber / 60);
        myMinutes = (int)(frameNumber % 60);
        myHours = (int)(frameNumber / 60);
    }

    protected abstract long truncateFrameNumber(long value);

    public enum StringType
    {
        Storage,
        Normal,
        NoFrames,
        Milliseconds
    }

    @Override
    public String toString()
    {
        return toString(this, StringType.Normal);
    }

    public String toString(StringType stringType)
    {
        return toString(this, stringType);
    }

    public static String toString(AbstractTimecode timecode, StringType stringType)
    {
        if (timecode == null) {
            switch (stringType) {
                case Storage:
                    return "null-timecode";
                case Milliseconds:
                    return "--:--:--.---";
                case Normal:
                case NoFrames:
                    return "--:--:--:--";
                default:
                    throw new IllegalArgumentException("Unknown String type '" + stringType.toString() + "'");
            }
        } else {
            switch (stringType) {
                case Storage:
                    return String.format("%02d:%02d:%02d%c%02d/%d", timecode.myHours, timecode.myMinutes, timecode.mySeconds, ((timecode.myDropFrame) ? ';' : ':'), timecode.myFrames, timecode.myTimecodeBase);
                case Normal:
                    return String.format("%02d:%02d:%02d%c%02d", timecode.myHours, timecode.myMinutes, timecode.mySeconds, ((timecode.myDropFrame) ? ';' : ':'), timecode.myFrames);
                case NoFrames:
                    return String.format("%02d:%02d:%02d%c--", timecode.myHours, timecode.myMinutes, timecode.mySeconds, ((timecode.myDropFrame) ? ';' : ':'));
                case Milliseconds:
                    return String.format("%02d:%02d:%02d.%03d", timecode.myHours, timecode.myMinutes, timecode.mySeconds, timecode.myFrames * 1000 / timecode.myTimecodeBase);
                default:
                    throw new IllegalArgumentException("Unknown String type '" + stringType.toString() + "'");
            }
        }
    }

    protected AbstractTimecode parse(String timecode)
    {
        if (timecode == null || timecode.contains("null")) {
            return null;
        }

        try {
            int timecodeBase;

            String[] parts = timecode.split("/");
            if (parts.length == 2) {
                timecodeBase = Integer.valueOf(parts[1]);
            } else {
                // supporting the old timecode storage format
                parts = timecode.split(";");
                if (parts.length == 4) {
                    int numerator = Integer.valueOf(parts[1]);
                    int denominator = Integer.valueOf(parts[2]);
                    boolean dropFrame = Boolean.valueOf(parts[3]);
                    timecodeBase = (numerator + (denominator / 2)) / denominator;
                    if (dropFrame) {
                        int index = parts[0].lastIndexOf(':');
                        if (index >= 0) {
                            parts[0] = parts[0].substring(0, index) + ";" + parts[0].substring(index + 1, parts[0].length());
                        }
                    }
                } else {
                    throw new IllegalArgumentException(timecode + " is not a valid timecode");
                }
            }
            return parse(parts[0], timecodeBase);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(timecode + " is not a valid timecode", ex);
        }
    }

    protected AbstractTimecode parse(String timecode, int timecodeBase)
    {
        if (timecodeBase <= 0) {
            throw new IllegalArgumentException(timecodeBase + " is not a valid timecodebase");
        }

        myTimecodeBase = timecodeBase;

        String parts[] = timecode.split(";|:");

        try {
            boolean dropFrame;

            if (parts.length == 1) {
                long value = Long.parseLong(timecode, 16);
                myHours = (int) ((((value & 0x00000030) >> 4) * 10) + (value & 0x0000000F));
                myMinutes = (int) ((((value & 0x00007000) >> 12) * 10) + ((value & 0x00000F00) >> 8));
                mySeconds = (int) ((((value & 0x00700000) >> 20) * 10) + ((value & 0x000F0000) >> 16));
                myFrames = (int) ((((value & 0x30000000) >> 28) * 10) + ((value & 0x0F000000) >> 24));
                if (myTimecodeBase == 50 || myTimecodeBase == 60) {
                    myFrames *= 2;
                }
                dropFrame = ((value & 0x40000000) != 0);
            } else if (parts.length == 4) {
                myHours = Integer.valueOf(parts[0]);
                myMinutes = Integer.valueOf(parts[1]);
                mySeconds = Integer.valueOf(parts[2]);
                myFrames = Integer.valueOf(parts[3]);
                dropFrame = timecode.contains(";");
            } else {
                throw new IllegalArgumentException(timecode + " is not a valid timecode");
            }

            setDropFrame(dropFrame && (myTimecodeBase == 30 || myTimecodeBase == 60));

            if (myHours < 0) {
                throw new IllegalArgumentException(timecode + " is not a valid timecode");
            }
            if (myMinutes < 0 || myMinutes >= 60) {
                throw new IllegalArgumentException(timecode + " is not a valid timecode");
            }
            if (mySeconds < 0 || mySeconds >= 60) {
                throw new IllegalArgumentException(timecode + " is not a valid timecode");
            }
            if (myFrames < ((myDropFrame && (myMinutes % 10) != 0 && ((mySeconds % 60) == 0)) ? myAdjustmentPerMinute : 0) || myFrames >= myTimecodeBase) {
                throw new IllegalArgumentException(timecode + " is not a valid timecode for timecode base " + myTimecodeBase + (myDropFrame ? " DF" : " NDF"));
            }

            calculateFrameNumber();
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(timecode + " is not a valid timecode", ex);
        }

        return this;
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
        final AbstractTimecode other = (AbstractTimecode) obj;
        if (myTimecodeBase != other.myTimecodeBase) {
            return false;
        }
        if (myDropFrame != other.myDropFrame) {
            return false;
        }
        if (myFrameNumber != other.myFrameNumber) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 31 * hash + this.myTimecodeBase;
        hash = 31 * hash + (this.myDropFrame ? 1 : 0);
        hash = 31 * hash + (int) (this.myFrameNumber ^ (this.myFrameNumber >>> 32));
        return hash;
    }

    public static int compare(AbstractTimecode t1, AbstractTimecode t2)
    {
        if (t1 == null && t2 == null)
            return 0;

        if (t1 == null)
            return -1;

        if (t2 == null)
            return 1;

        long diff = t1.myFrameNumber - t2.myFrameNumber;
        if (diff > 0) {
            return 1;
        } else if (diff < 0) {
            return -1;
        }
        return 0;
    }

    public static TimecodeDuration calculateDuration(Timecode inPoint, Timecode outPoint)
    {
        if (inPoint.getTimecodeBase() != outPoint.getTimecodeBase() || inPoint.isDropFrame() != outPoint.isDropFrame()) {
            throw new IllegalArgumentException("Incompatible timecodes");
        }
        long frameNumber = outPoint.getFrameNumber() - inPoint.getFrameNumber();
        if (frameNumber < 0) {
            frameNumber += (24 * 6 * inPoint.myFramesPerTenMinutes);
        }
        frameNumber++;

        return new TimecodeDuration(inPoint.getTimecodeBase(), frameNumber, inPoint.isDropFrame());
    }

    public static Timecode calculateInPoint(Timecode outPoint, TimecodeDuration duration)
    {
        if (outPoint.getTimecodeBase() != duration.getTimecodeBase() || outPoint.isDropFrame() != duration.isDropFrame()) {
            throw new IllegalArgumentException("Incompatible timecodes");
        }
        Timecode inPoint = new Timecode(outPoint);
        inPoint.addFrames(1 - duration.getFrameNumber());
        return inPoint;
    }

    public static Timecode calculateOutPoint(Timecode inPoint, TimecodeDuration duration)
    {
        if (inPoint.getTimecodeBase() != duration.getTimecodeBase() || inPoint.isDropFrame() != duration.isDropFrame()) {
            throw new IllegalArgumentException("Incompatible timecodes");
        }
        Timecode outPoint = new Timecode(inPoint);
        outPoint.addFrames(duration.getFrameNumber() - 1);
        return outPoint;
    }
}
