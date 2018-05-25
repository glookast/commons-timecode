package com.glookast.commons.timecode.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.glookast.commons.timecode.Timecode;

import java.io.IOException;

public class TimecodeDeserializer extends StdDeserializer<Timecode>
{
    protected TimecodeDeserializer()
    {
        super(Timecode.class);
    }

    @Override
    public Timecode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException
    {
        return Timecode.valueOf(p.getText());
    }
}
