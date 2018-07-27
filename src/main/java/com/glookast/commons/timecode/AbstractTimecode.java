package com.glookast.commons.timecode;

import java.io.Serializable;

public abstract class AbstractTimecode implements Serializable
{
    private static final long serialVersionUID = 1L;

    protected int timecodeBase;
    protected boolean dropFrame;
    protected long frameNumber;

    protected int adjustmentPerMinute;
    protected int framesPerMinute;
    protected int framesPerMinuteDropFrame;
    protected int framesPerTenMinutes;

    protected int hours;
    protected int minutes;
    protected int seconds;
    protected int frames;

    public AbstractTimecode()
    {
        this(0);
    }

    public AbstractTimecode(int timecodeBase)
    {
        this(timecodeBase, false);
    }

    public AbstractTimecode(int timecodeBase, boolean dropFrame)
    {
        if (timecodeBase < 0) {
            timecodeBase = 0;
        }

        this.timecodeBase = timecodeBase;

        innerSetDropFrame(dropFrame);
    }

    public AbstractTimecode(int timecodeBase, long frameNumber)
    {
        this(timecodeBase, false);
        innerSetFrameNumber(frameNumber);
    }

    public AbstractTimecode(int timecodeBase, long frameNumber, boolean dropFrame)
    {
        this(timecodeBase, dropFrame);
        innerSetFrameNumber(frameNumber);
    }

    public AbstractTimecode(int timecodeBase, int hours, int minutes, int seconds, int frames)
    {
        this(timecodeBase, false);
        innerSetHMSF(hours, minutes, seconds, frames);
    }

    public AbstractTimecode(int timecodeBase, int hours, int minutes, int seconds, int frames, boolean dropFrame)
    {
        this(timecodeBase, dropFrame);
        innerSetHMSF(hours, minutes, seconds, frames);
    }

    public AbstractTimecode(AbstractTimecode timecode)
    {
        this(timecode.getTimecodeBase(), timecode.isDropFrame());
        innerSetFrameNumber(timecode.getFrameNumber());
    }

    /**
     * Returns whether this timecode represents a valid / initialized.
     *
     * @return isValid
     */
    public boolean isValid()
    {
        return timecodeBase != 0;
    }

    /**
     * Returns whether this timecode represents a invalid / uninitialized / null timecode
     *
     * @return isInvalid
     */
    public boolean isInvalid()
    {
        return timecodeBase == 0;
    }

    public int getTimecodeBase()
    {
        return timecodeBase;
    }

    public boolean isDropFrame()
    {
        return dropFrame;
    }

    protected void innerSetDropFrame(boolean dropFrame)
    {
        if (timecodeBase == 0 || timecodeBase / 30 * 30 != timecodeBase) {
            dropFrame = false;
        }

        this.dropFrame = dropFrame;
        adjustmentPerMinute = (this.dropFrame) ? (timecodeBase / 15) : 0;
        framesPerMinute = 60 * timecodeBase;
        framesPerMinuteDropFrame = framesPerMinute - adjustmentPerMinute;
        framesPerTenMinutes = framesPerMinute + 9 * framesPerMinuteDropFrame;

        this.innerSetHMSF(this.hours, this.minutes, this.seconds, this.frames);
    }

    /**
     * Gets the frame number for current timecode
     *
     * @return Frame number
     */
    public long getFrameNumber()
    {
        return frameNumber;
    }

    protected void innerSetFrameNumber(long frameNumber)
    {
        this.frameNumber = frameNumber;
        calculateHMSF();
    }

    protected int limit(int value, int min, int max)
    {
        return (value < min) ? min : ((value > max) ? max : value);
    }

    protected int limitFrames(int frames)
    {
        return limit(frames, (dropFrame && (minutes % 10) != 0 && ((seconds % 60) == 0)) ? adjustmentPerMinute : 0, timecodeBase - 1);
    }

    protected void innerSetHMSF(int hours, int minutes, int seconds, int frames)
    {
        this.hours = limit(hours, 0, Integer.MAX_VALUE);
        this.minutes = limit(minutes, 0, 59);
        this.seconds = limit(seconds, 0, 59);
        this.frames = limitFrames(frames);
        calculateFrameNumber();
    }

    public int getHours()
    {
        return hours;
    }

    public int getMinutes()
    {
        return minutes;
    }

    public int getSeconds()
    {
        return seconds;
    }

    /**
     * Gets the frames component of the timecode
     *
     * @return frames
     */
    public int getFrames()
    {
        return frames;
    }

    protected void calculateFrameNumber()
    {
        int tenMinutes = hours * 6 + minutes / 10;
        int minutes = this.minutes % 10;

        frameNumber = tenMinutes * framesPerTenMinutes +
                      minutes * framesPerMinuteDropFrame +
                      seconds * timecodeBase +
                      frames;

        calculateHMSF();
    }

    protected void calculateHMSF()
    {
        if (timecodeBase == 0) {
            frameNumber = 0;
            frames = 0;
            seconds = 0;
            minutes = 0;
            hours = 0;
            return;
        }

        frameNumber = truncateFrameNumber(frameNumber);

        long frameNumber = this.frameNumber;

        if (dropFrame) {
            long tenMinutesFrames = frameNumber / framesPerTenMinutes;
            long remainingMinutes = ((frameNumber % framesPerTenMinutes) - adjustmentPerMinute) / framesPerMinuteDropFrame;
            long dropIncidents = 9 * tenMinutesFrames + remainingMinutes;
            frameNumber += adjustmentPerMinute * dropIncidents;
        }

        frames = (int) (frameNumber % timecodeBase);
        frameNumber = (frameNumber / timecodeBase);
        seconds = (int) (frameNumber % 60);
        frameNumber = (frameNumber / 60);
        minutes = (int) (frameNumber % 60);
        hours = (int) (frameNumber / 60);
    }

    protected abstract long truncateFrameNumber(long value);

    public enum StringType
    {
        STORAGE,
        NORMAL,
        NO_FRAMES,
        MILLISECONDS,
        SMPTE_HIGH_FRAME_RATE,
        SMPTE_ST_12M_BINARY_CODED_DECIMALS,
        SMPTE_ST_258
    }

    @Override
    public String toString()
    {
        return toString(this, StringType.NORMAL);
    }

    public String toString(StringType stringType)
    {
        return toString(this, stringType);
    }

    public static String toString(AbstractTimecode timecode, StringType stringType)
    {
        if (timecode == null || timecode.isInvalid()) {
            switch (stringType) {
                case STORAGE:
                    return "null-timecode";
                case MILLISECONDS:
                    return "--:--:--.---";
                case NORMAL:
                case NO_FRAMES:
                case SMPTE_HIGH_FRAME_RATE:
                case SMPTE_ST_258:
                    return "--:--:--:--";
                case SMPTE_ST_12M_BINARY_CODED_DECIMALS:
                    return "FFFFFFFF";
                default:
                    throw new IllegalArgumentException("Unknown String type '" + stringType.toString() + "'");
            }
        } else {
            int correctedFrames = timecode.frames;
            boolean firstField = true;
            if (timecode.timecodeBase >= 40) {
                correctedFrames /= 2;
                firstField = timecode.frames % 2 == 0;
            }

            switch (stringType) {
                case STORAGE:
                    return String.format("%02d:%02d:%02d%c%02d/%d", timecode.hours, timecode.minutes, timecode.seconds, ((timecode.dropFrame) ? ';' : ':'), timecode.frames, timecode.timecodeBase);
                case NORMAL:
                    return String.format("%02d:%02d:%02d%c%02d", timecode.hours, timecode.minutes, timecode.seconds, ((timecode.dropFrame) ? ';' : ':'), timecode.frames);
                case NO_FRAMES:
                    return String.format("%02d:%02d:%02d%c--", timecode.hours, timecode.minutes, timecode.seconds, ((timecode.dropFrame) ? ';' : ':'));
                case MILLISECONDS:
                    return String.format("%02d:%02d:%02d.%03d", timecode.hours, timecode.minutes, timecode.seconds, Math.round(timecode.frames * 1000.0 / timecode.timecodeBase));
                case SMPTE_ST_12M_BINARY_CODED_DECIMALS:
                    long value = 0;
                    value |= timecode.hours % 10 + ((timecode.hours / 10) << 4);
                    value |= (timecode.minutes % 10 + ((timecode.minutes / 10) << 4)) << 8;
                    value |= (timecode.seconds % 10 + ((timecode.seconds / 10) << 4)) << 16;
                    value |= (correctedFrames % 10 + ((correctedFrames / 10) << 4)) << 24;
                    if (timecode.dropFrame) {
                        value |= 0x40000000;
                    }
                    if (!firstField) {
                        if (timecode.timecodeBase == 48 || timecode.timecodeBase == 60) {
                            value |= 0x00800000;
                        } else if (timecode.timecodeBase == 50) {
                            value |= 0x00000080;
                        }
                    }
                    return String.format("%08X", value);
                case SMPTE_ST_258:
                case SMPTE_HIGH_FRAME_RATE:
                    if (timecode.timecodeBase < 40) {
                        return String.format("%02d:%02d:%02d%c%02d", timecode.hours, timecode.minutes, timecode.seconds, ((timecode.dropFrame) ? ';' : ':'), timecode.frames);
                    } else {
                        if (stringType == StringType.SMPTE_ST_258) {
                            if (firstField) {
                                return String.format("%02d:%02d:%02d%c%02d", timecode.hours, timecode.minutes, timecode.seconds, ((timecode.dropFrame) ? ',' : '.'), correctedFrames);
                            } else {
                                return String.format("%02d:%02d:%02d%c%02d", timecode.hours, timecode.minutes, timecode.seconds, ((timecode.dropFrame) ? ';' : ':'), correctedFrames);
                            }
                        } else {
                            return String.format("%02d:%02d:%02d%c%02d.%d", timecode.hours, timecode.minutes, timecode.seconds, ((timecode.dropFrame) ? ';' : ':'), correctedFrames, (firstField ? 0 : 1));
                        }
                    }
                default:
                    throw new UnsupportedOperationException("Timecode.toString() with StringType '" + stringType + "' not implemented");
            }
        }
    }

    protected AbstractTimecode parse(String timecode)
    {
        if (timecode == null || timecode.contains("null") || timecode.contains("--:--:--") || timecode.contains("FFFFFFFF")) {
            return this;
        }

        try {
            String hmsf;
            int timecodeBase;
            int hours;
            int minutes;
            int seconds;
            int frames;
            boolean dropFrame;

            String[] parts = timecode.split("/");

            if (parts.length == 2) {
                hmsf = parts[0];
                dropFrame = hmsf.contains(";");
                timecodeBase = Integer.valueOf(parts[1]);
            } else {
                // supporting the old timecode storage format
                parts = timecode.split(";");

                if (parts.length == 4) {
                    hmsf = parts[0];
                    int numerator = Integer.valueOf(parts[1]);
                    int denominator = Integer.valueOf(parts[2]);
                    dropFrame = Boolean.valueOf(parts[3]);
                    timecodeBase = (numerator + (denominator / 2)) / denominator;
                } else {
                    // if not old format then just parse it regularly and assume timecode base of 30 / 60 / 120 / etc;
                    parts = timecode.split("[:;.,]");
                    if (parts.length == 4) {
                        hmsf = timecode;
                        dropFrame = hmsf.contains(";");
                        frames = Integer.valueOf(parts[3]);
                        timecodeBase = 30;
                        while (timecodeBase < frames) {
                            timecodeBase *= 2;
                        }
                    } else {
                        throw new IllegalArgumentException(timecode + " is not a parsable timecode");
                    }
                }
            }

            parts = hmsf.split("[:;.,]");
            if (parts.length != 4) {
                throw new IllegalArgumentException(timecode + " is not a parsable timecode");
            }

            hours = Integer.valueOf(parts[0]);
            minutes = Integer.valueOf(parts[1]);
            seconds = Integer.valueOf(parts[2]);
            frames = Integer.valueOf(parts[3]);

            return init(timecodeBase, hours, minutes, seconds, frames, dropFrame);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(timecode + " is not a parsable timecode");
        }
    }

    protected AbstractTimecode parse(String timecode, int timecodeBase, StringType stringType)
    {
        if (timecodeBase <= 0 || timecode == null || timecode.contains("null") || timecode.contains("--:--:--") || timecode.contains("FFFFFFFF")) {
            return this;
        }

        String[] parts;
        String temp = timecode;

        int hours;
        int minutes;
        int seconds;
        int frames;
        boolean dropFrame;

        try {
            switch (stringType) {
                case STORAGE:
                    parts = timecode.split("/");
                    if (parts.length != 2) {
                        throw new IllegalArgumentException(timecode + " is not a valid format for string type '" + stringType + "'");
                    }
                    temp = parts[0];
                case NORMAL:
                case NO_FRAMES:
                case MILLISECONDS:
                    parts = temp.split("[:;.]");
                    if (parts.length != 4) {
                        throw new IllegalArgumentException(timecode + " is not a valid format for string type '" + stringType + "'");
                    }
                    hours = Integer.valueOf(parts[0]);
                    minutes = Integer.valueOf(parts[1]);
                    seconds = Integer.valueOf(parts[2]);
                    switch (stringType) {
                        case STORAGE:
                        case NORMAL:
                            frames = Integer.valueOf(parts[3]);
                            break;
                        case MILLISECONDS:
                            frames = Math.round(Integer.valueOf(parts[3], 10) * timecodeBase / 1000.0f);
                            break;
                        default:
                            frames = 0;
                            break;
                    }
                    dropFrame = temp.contains(";");
                    break;
                case SMPTE_ST_12M_BINARY_CODED_DECIMALS:
                    long value = Long.parseLong(timecode, 16);
                    hours = (int) ((((value & 0x00000030) >> 4) * 10) + (value & 0x0000000F));
                    minutes = (int) ((((value & 0x00007000) >> 12) * 10) + ((value & 0x00000F00) >> 8));
                    seconds = (int) ((((value & 0x00700000) >> 20) * 10) + ((value & 0x000F0000) >> 16));
                    frames = (int) ((((value & 0x30000000) >> 28) * 10) + ((value & 0x0F000000) >> 24));
                    dropFrame = ((value & 0x40000000) != 0);

                    if (timecodeBase >= 40) {
                        frames *= 2;

                        if (timecodeBase == 48 || timecodeBase == 60) {
                            if ((value & 0x00800000) != 0) {
                                frames += 1;
                            }
                        } else if (timecodeBase == 50) {
                            if ((value & 0x00000080) != 0) {
                                frames += 1;
                            }
                        }
                    }
                    break;
                case SMPTE_ST_258:
                    parts = timecode.split("[:;.,]");
                    if (parts.length != 4) {
                        throw new IllegalArgumentException(timecode + " is not a valid format for string type '" + stringType + "'");
                    }

                    hours = Integer.valueOf(parts[0]);
                    minutes = Integer.valueOf(parts[1]);
                    seconds = Integer.valueOf(parts[2]);
                    frames = Integer.valueOf(parts[3]);
                    dropFrame = timecode.contains(";") || timecode.contains(",");

                    if (timecodeBase >= 40) {
                        frames *= 2;

                        boolean firstField = timecode.contains(",") || timecode.contains(".");

                        if (!firstField) {
                            frames += 1;
                        }
                    }
                    break;
                case SMPTE_HIGH_FRAME_RATE:
                    parts = timecode.split("[:;.]");
                    if (parts.length != 4 && parts.length != 5) {
                        throw new IllegalArgumentException(timecode + " is not a valid format for string type '" + stringType + "'");
                    }

                    hours = Integer.valueOf(parts[0]);
                    minutes = Integer.valueOf(parts[1]);
                    seconds = Integer.valueOf(parts[2]);
                    frames = Integer.valueOf(parts[3]);
                    dropFrame = timecode.contains(";");

                    if (timecodeBase >= 40) {
                        frames *= 2;

                        boolean firstField = parts.length == 5 && Integer.valueOf(parts[4]) == 0;

                        if (!firstField) {
                            frames += 1;
                        }
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("Timecode.valueOf() with StringType '" + stringType + "' not implemented");
            }
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(timecode + " is not a valid format for string type '" + stringType + "'");
        }

        return init(timecodeBase, hours, minutes, seconds, frames, dropFrame);
    }

    private AbstractTimecode init(int timecodeBase, int hours, int minutes, int seconds, int frames, boolean dropFrame)
    {
        if (timecodeBase < 0) {
            timecodeBase = 0;
        }

        this.timecodeBase = timecodeBase;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.frames = frames;

        this.innerSetDropFrame(dropFrame);

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
        if (timecodeBase != other.timecodeBase) {
            return false;
        }
        if (dropFrame != other.dropFrame) {
            return false;
        }
        if (frameNumber != other.frameNumber) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 31 * hash + this.timecodeBase;
        hash = 31 * hash + (this.dropFrame ? 1 : 0);
        hash = 31 * hash + (int) (this.frameNumber ^ (this.frameNumber >>> 32));
        return hash;
    }

    public static int compare(AbstractTimecode t1, AbstractTimecode t2)
    {
        if (t1 == null && t2 == null) {
            return 0;
        }

        if (Timecode.isInvalid(t1)) {
            return -1;
        } else if (Timecode.isInvalid(t2)) {
            return 1;
        }

        int s1 = t1.getHours() * 3600 + t1.getMinutes() * 60 + t1.getSeconds();
        int s2 = t2.getHours() * 3600 + t2.getMinutes() * 60 + t2.getSeconds();

        if (s1 < s2) {
            return -1;
        } else if (s1 > s2) {
            return 1;
        }

        return t1.getFrames() - t2.getFrames();
    }

    /**
     * Calculates duration between given inPoint and outPoint.
     * In case outPoint does not have the same Timecode base and/or dropFrame flag
     * it will convert it to the same Timecode base and dropFrame flag of the inPoint
     *
     * @param inPoint
     * @param outPoint
     * @return duration
     */
    public static TimecodeDuration calculateDuration(Timecode inPoint, Timecode outPoint)
    {
        if (!inPoint.isCompatible(outPoint)) {
            MutableTimecode mutableTimecode = new MutableTimecode(outPoint);
            mutableTimecode.setTimecodeBase(inPoint.getTimecodeBase());
            mutableTimecode.setDropFrame(inPoint.isDropFrame());
            outPoint = new Timecode(mutableTimecode);
        }

        long frameNumber = outPoint.getFrameNumber() - inPoint.getFrameNumber();
        if (frameNumber < 0) {
            frameNumber += (24 * 6 * inPoint.framesPerTenMinutes);
        }

        return new TimecodeDuration(inPoint.getTimecodeBase(), frameNumber, inPoint.isDropFrame());
    }

    /**
     * Calculates inPoint of a given outPoint and duration.
     * In case duration does not have the same Timecode base and/or dropFrame flag
     * it will convert it to the same Timecode base and dropFrame flag of the outPoint
     *
     * @param outPoint
     * @param duration
     * @return inPoint
     */
    public static Timecode calculateInPoint(Timecode outPoint, TimecodeDuration duration)
    {
        if (!outPoint.isCompatible(duration)) {
            MutableTimecodeDuration mutableTimecodeDuration = new MutableTimecodeDuration(duration);
            mutableTimecodeDuration.setTimecodeBase(outPoint.getTimecodeBase());
            mutableTimecodeDuration.setDropFrame(outPoint.isDropFrame());
            duration = new TimecodeDuration(mutableTimecodeDuration);
        }

        MutableTimecode inPoint = new MutableTimecode(outPoint);
        inPoint.addFrames(-duration.getFrameNumber());
        return new Timecode(inPoint);
    }

    /**
     * Calculates outPoint of a given inPoint and duration.
     * In case duration does not have the same Timecode base and/or dropFrame flag
     * it will convert it to the same Timecode base and dropFrame flag of the inPoint
     *
     * @param inPoint
     * @param duration
     * @return outPoint
     */
    public static Timecode calculateOutPoint(Timecode inPoint, TimecodeDuration duration)
    {
        if (!inPoint.isCompatible(duration)) {
            MutableTimecodeDuration mutableTimecodeDuration = new MutableTimecodeDuration(duration);
            mutableTimecodeDuration.setTimecodeBase(inPoint.getTimecodeBase());
            mutableTimecodeDuration.setDropFrame(inPoint.isDropFrame());
            duration = new TimecodeDuration(mutableTimecodeDuration);
        }

        MutableTimecode outPoint = new MutableTimecode(inPoint);
        outPoint.addFrames(duration.getFrameNumber());
        return new Timecode(outPoint);
    }

    public boolean isCompatible(AbstractTimecode other)
    {
        return isCompatible(this, other);
    }

    public static boolean isCompatible(AbstractTimecode t1, AbstractTimecode t2)
    {
        return t1 != null &&
               t2 != null &&
               t1.isValid() &&
               t2.isValid() &&
               t1.getTimecodeBase() == t2.getTimecodeBase() &&
               t1.isDropFrame() == t2.isDropFrame();
    }

    /**
     * Helper function to test whether the timecode is valid, which means it is not null and valid
     *
     * @param timecode
     * @return whether timecode is initialized and valid
     */
    public static boolean isValid(AbstractTimecode timecode)
    {
        return timecode != null && timecode.isValid();
    }

    /**
     * Helper function to test whether a the timecode is invalid, which means it is either null or invalid.
     *
     * @param timecode
     * @return whether timecode is not initialized or invalid
     */
    public static boolean isInvalid(AbstractTimecode timecode)
    {
        return timecode == null || timecode.isInvalid();
    }

}
