package com.glookast.commons.timecode;

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
        return (AbstractTimecode.toString(value, AbstractTimecode.StringType.Storage));
    }

}
