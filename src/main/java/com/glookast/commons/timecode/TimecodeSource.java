
package com.glookast.commons.timecode;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TimecodeSource.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TimecodeSource"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="ltc"/&gt;
 *     &lt;enumeration value="vitc"/&gt;
 *     &lt;enumeration value="tod"/&gt;
 *     &lt;enumeration value="rs422"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 */
@XmlType(name = "TimecodeSource")
@XmlEnum
public enum TimecodeSource
{

    @XmlEnumValue("ltc")
    LTC("ltc"),
    @XmlEnumValue("vitc")
    VITC("vitc"),
    @XmlEnumValue("tod")
    TOD("tod"),
    @XmlEnumValue("rs422")
    RS_422("rs422");
    private final String value;

    TimecodeSource(String v)
    {
        value = v;
    }

    public String value()
    {
        return value;
    }

    public static TimecodeSource fromValue(String v)
    {
        for (TimecodeSource c : TimecodeSource.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
