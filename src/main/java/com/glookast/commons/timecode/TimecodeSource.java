
package com.glookast.commons.timecode;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

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
 *     &lt;enumeration value="LTC"/&gt;
 *     &lt;enumeration value="VITC"/&gt;
 *     &lt;enumeration value="TOD"/&gt;
 *     &lt;enumeration value="RS422"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 */
@XmlType(name = "TimecodeSource", namespace = "http://timecode.commons.glookast.com")
@XmlEnum
public enum TimecodeSource
{

    LTC("LTC"),
    VITC("VITC"),
    TOD("TOD"),
    @XmlEnumValue("RS422")
    RS_422("RS422");
    private final String value;

    TimecodeSource(String v)
    {
        value = v;
    }

    @JsonValue()
    public String value()
    {
        return value;
    }

    @JsonCreator
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
