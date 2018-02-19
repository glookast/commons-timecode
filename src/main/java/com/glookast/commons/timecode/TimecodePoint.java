
package com.glookast.commons.timecode;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;


/**
 * <p>Java class for TimecodePoint complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TimecodePoint"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="timecodeSource" type="{http://timecode.commons.glookast.com}TimecodeSource"/&gt;
 *         &lt;element name="timecode" type="{http://timecode.commons.glookast.com}Timecode"/&gt;
 *         &lt;element name="position" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TimecodePoint", propOrder = {
    "timecodeSource",
    "timecode",
    "position"
})
public class TimecodePoint implements Serializable
{

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected TimecodeSource timecodeSource;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(XmlAdapterTimecode.class)
    protected Timecode timecode;
    protected long position;

    /**
     * Default no-arg constructor
     */
    public TimecodePoint()
    {
        super();
    }

    /**
     * Fully-initialising value constructor
     */
    public TimecodePoint(final TimecodeSource timecodeSource, final Timecode timecode, final long position)
    {
        this.timecodeSource = timecodeSource;
        this.timecode = timecode;
        this.position = position;
    }

    /**
     * Gets the value of the timecodeSource property.
     *
     * @return possible object is
     * {@link TimecodeSource }
     */
    public TimecodeSource getTimecodeSource()
    {
        return timecodeSource;
    }

    /**
     * Sets the value of the timecodeSource property.
     *
     * @param value allowed object is
     *              {@link TimecodeSource }
     */
    public void setTimecodeSource(TimecodeSource value)
    {
        this.timecodeSource = value;
    }

    /**
     * Gets the value of the timecode property.
     *
     * @return possible object is
     * {@link String }
     */
    public Timecode getTimecode()
    {
        return timecode;
    }

    /**
     * Sets the value of the timecode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTimecode(Timecode value)
    {
        this.timecode = value;
    }

    /**
     * Gets the value of the position property.
     */
    public long getPosition()
    {
        return position;
    }

    /**
     * Sets the value of the position property.
     */
    public void setPosition(long value)
    {
        this.position = value;
    }

}
