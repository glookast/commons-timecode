package com.glookast.commons.timecode.xml;

import com.glookast.commons.timecode.AbstractTimecode;
import com.glookast.commons.timecode.TimecodeDuration;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class XmlAdapterTimecodeDuration extends XmlAdapter<String, TimecodeDuration>
{

    @Override
    public TimecodeDuration unmarshal(String value) throws Exception
    {
        try {
            return (TimecodeDuration.valueOf(value));
        } catch (Exception ex) {
        }
        return null;
    }

    @Override
    public String marshal(TimecodeDuration value) throws Exception
    {
        return (AbstractTimecode.toString(value, AbstractTimecode.StringType.STORAGE));
    }

}
