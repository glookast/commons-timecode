package com.glookast.commons.timecode.xml;

import com.glookast.commons.timecode.AbstractTimecode;
import com.glookast.commons.timecode.Timecode;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class XmlAdapterTimecode extends XmlAdapter<String, Timecode>
{

    @Override
    public Timecode unmarshal(String value) throws Exception
    {
        try {
            return (Timecode.valueOf(value));
        } catch (Exception ex) {
        }
        return null;
    }

    @Override
    public String marshal(Timecode value) throws Exception
    {
        return (AbstractTimecode.toString(value, AbstractTimecode.StringType.STORAGE));
    }

}
