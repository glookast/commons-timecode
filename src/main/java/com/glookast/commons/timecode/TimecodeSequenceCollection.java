package com.glookast.commons.timecode;

import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TimecodeSequenceCollection implements Serializable
{
    private Map<TimecodeSource, List<TimecodeSequence>> timecodeSequenceMap;

    public TimecodeSequenceCollection()
    {
        timecodeSequenceMap = new LinkedHashMap<>();
    }

    public TimecodeSequenceCollection(TimecodeSequenceCollection timecodeSequenceCollection)
    {
        timecodeSequenceMap = new LinkedHashMap<>();
        setTimecodeSequences(timecodeSequenceCollection.getTimecodeSequences());
    }

    public void setTimecodeSequences(List<TimecodeSequence> timecodeSequences)
    {
        timecodeSequences.clear();
        timecodeSequences.forEach(this::addTimecode);
    }

    public List<TimecodeSequence> getTimecodeSequences()
    {
        return Collections.unmodifiableList(timecodeSequenceMap.values()
                                                               .stream()
                                                               .flatMap(Collection::stream)
                                                               .map(TimecodeSequence::new)
                                                               .collect(Collectors.toList()));
    }

    public void addTimecode(TimecodeSequence timecodeSequence)
    {
        this.addTimecode(timecodeSequence.timecodeSource, timecodeSequence.timecode, timecodeSequence.position);
    }

    public void addTimecode(TimecodeSource timecodeSource, Timecode timecode, long position)
    {
        List<TimecodeSequence> timecodeSequences = timecodeSequenceMap.computeIfAbsent(timecodeSource, ts -> new ArrayList<>());

        // inserting the timecode sequence in correct place
        // finding the first timecode sequence that doesn't match
        // on that position we must insert the new timecode sequence
        int idx = binarySearch(timecodeSequences, (ts) -> ts.position > position);

        if (idx > 0) {
            // check the previous timecode sequence
            TimecodeSequence prevTimecodeSequence = timecodeSequences.get(idx - 1);

            if (prevTimecodeSequence.position == position) {
                // in case the position is equal we are replacing the timecode sequence, so we must remove the existing one.
                idx--;
                timecodeSequences.remove(idx);
            } else if (prevTimecodeSequence.timecode.getTimecodeBase() == timecode.getTimecodeBase() && prevTimecodeSequence.timecode.isDropFrame() == timecode.isDropFrame()) {
                // check if new sequence fit's in previous one. in that case we can skip the insertion.
                long diff = position - prevTimecodeSequence.position;
                Timecode clone = new Timecode(prevTimecodeSequence.timecode.getTimecodeBase(), prevTimecodeSequence.timecode.getFrameNumber() + diff, prevTimecodeSequence.timecode.isDropFrame());

                if (timecode.equals(clone)) {
                    return;
                }
            }
        }

        // inserting it add that position and push the rest forward
        timecodeSequences.add(idx, new TimecodeSequence(timecodeSource, new Timecode(timecode), position));

        if (timecodeSequences.size() > idx + 1) {
            // check if the next timecode sequence matches the newly inserted one (in that case we can remove the next timecode sequence)
            TimecodeSequence nextTimecodeSequence = timecodeSequences.get(idx + 1);

            long diff = nextTimecodeSequence.position - position;
            Timecode clone = new Timecode(timecode.getTimecodeBase(), timecode.getFrameNumber() + diff, timecode.isDropFrame());

            if (nextTimecodeSequence.timecode.equals(clone)) {
                timecodeSequences.remove(idx + 1);
            }
        }
    }

    public void truncate(long minimumPosition)
    {
        timecodeSequenceMap.values().forEach(timecodeSequences -> {
            // as soon as the minimum position is being handled by the second timecode sequence
            // we can drop the first timecode sequence as it is no longer needed
            while (timecodeSequences.size() > 1 && minimumPosition >= timecodeSequences.get(1).position) {
                timecodeSequences.remove(0);
            }

            // to allow seeking to future timecodes we also must update the position in the timecode sequence
            // such that it doesn't match timecodes to past frame numbers.
            if (timecodeSequences.size() > 0) {
                TimecodeSequence timecodeSequence = timecodeSequences.get(0);

                long truncateSize = minimumPosition - timecodeSequence.position;

                if (truncateSize > 0) {
                    timecodeSequence.position += truncateSize;
                    timecodeSequence.timecode = new Timecode(timecodeSequence.timecode.getTimecodeBase(), timecodeSequence.timecode.getFrameNumber() + truncateSize, timecodeSequence.timecode.isDropFrame());
                }
            }
        });
    }

    public void clear()
    {
        timecodeSequenceMap.clear();
    }

    public Optional<Long> getPosition(TimecodeSource timecodeSource, Timecode timecode)
    {
        return getPosition(timecodeSource, timecode, 0);
    }

    public Optional<Long> getPosition(TimecodeSource timecodeSource, Timecode timecode, long fromPosition)
    {
        Optional<Long> position = Optional.empty();

        List<TimecodeSequence> timecodeSequences = this.timecodeSequenceMap.get(timecodeSource);
        if (timecodeSequences != null) {
            // iterating through all timecode sequences to find the first occurrence of provided timecode
            for (int i = 0; i < timecodeSequences.size(); i++) {
                TimecodeSequence timecodeSequence = timecodeSequences.get(i);

                // if we found a position and
                //    the position is bigger or equal the from position and
                //    the next timecode sequence doesn't cover that position
                // then it means we found the right timecode sequence and position.
                if (position.isPresent() && position.get() >= fromPosition && position.get() < timecodeSequence.position) {
                    break;
                }

                // if provided timecode matches with timecode sequence we calculate position of timecode in that sequence.
                if (timecodeSequence.timecode.getTimecodeBase() == timecode.getTimecodeBase() && timecodeSequence.timecode.isDropFrame() == timecode.isDropFrame()) {
                    position = Optional.of(timecodeSequence.position + Timecode.calculateDuration(timecodeSequence.timecode, timecode).getFrameNumber());
                } else {
                    position = Optional.empty();
                }
            }
        }

        return position;
    }

    public Timecode getTimecode(TimecodeSource timecodeSource, long position)
    {
        List<TimecodeSequence> timecodeSequences = this.timecodeSequenceMap.get(timecodeSource);
        if (timecodeSequences != null) {
            // finding the first timecode sequence that doesn't match
            int idx = this.binarySearch(timecodeSequences, (ts) -> ts.position > position);

            if (idx > 0) {
                // using the previous timecode sequence
                TimecodeSequence timecodeSequence = timecodeSequences.get(idx - 1);

                if (timecodeSequence.timecode.isValid()) {
                    long diff = position - timecodeSequence.position;
                    return new Timecode(timecodeSequence.timecode.getTimecodeBase(), timecodeSequence.timecode.getFrameNumber() + diff, timecodeSequence.timecode.isDropFrame());
                }
            }
        }

        return new Timecode();
    }


    private <T> int binarySearch(List<T> list, Predicate<T> pred)
    {
        int lo = -1;
        int hi = list.size();
        while (1 + lo != hi) {
            final int mi = lo + ((hi - lo) >>> 1);
            if (pred.test(list.get(mi))) {
                hi = mi;
            } else {
                lo = mi;
            }
        }
        return hi;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimecodeSequenceCollection that = (TimecodeSequenceCollection) o;
        return Objects.equals(timecodeSequenceMap, that.timecodeSequenceMap);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(timecodeSequenceMap);
    }

    @Override
    public String toString()
    {
        return "TimecodeSequenceCollection{[\n   " +
               timecodeSequenceMap.values()
                                  .stream()
                                  .flatMap(Collection::stream)
                                  .map(TimecodeSequence::toString)
                                  .collect(Collectors.joining(",\n   ")) +
               "\n]}";
    }
}
