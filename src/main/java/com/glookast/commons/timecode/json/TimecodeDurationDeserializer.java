package com.glookast.commons.timecode.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.glookast.commons.timecode.TimecodeDuration;

import java.io.IOException;

public class TimecodeDurationDeserializer extends StdDeserializer<TimecodeDuration>
{
    protected TimecodeDurationDeserializer()
    {
        super(TimecodeDuration.class);
    }

    @Override
    public TimecodeDuration deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException
    {
        return TimecodeDuration.valueOf(p.getText());
    }
}
