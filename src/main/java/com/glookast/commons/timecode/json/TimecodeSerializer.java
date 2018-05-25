package com.glookast.commons.timecode.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.glookast.commons.timecode.AbstractTimecode;
import com.glookast.commons.timecode.Timecode;

import java.io.IOException;

public class TimecodeSerializer extends StdSerializer<Timecode>
{
    protected TimecodeSerializer()
    {
        super(Timecode.class);
    }

    @Override
    public void serialize(Timecode value, JsonGenerator gen, SerializerProvider provider) throws IOException
    {
        gen.writeString(value.toString(AbstractTimecode.StringType.Storage));
    }
}
