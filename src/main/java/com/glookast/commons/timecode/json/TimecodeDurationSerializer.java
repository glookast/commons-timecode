package com.glookast.commons.timecode.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.glookast.commons.timecode.AbstractTimecode;
import com.glookast.commons.timecode.TimecodeDuration;

import java.io.IOException;

public class TimecodeDurationSerializer extends StdSerializer<TimecodeDuration>
{
    protected TimecodeDurationSerializer()
    {
        super(TimecodeDuration.class);
    }

    @Override
    public void serialize(TimecodeDuration value, JsonGenerator gen, SerializerProvider provider) throws IOException
    {
        gen.writeString(value.toString(AbstractTimecode.StringType.STORAGE));
    }
}
