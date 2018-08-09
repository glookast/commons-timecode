package com.glookast.commons.timecode;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.glookast.commons.timecode.xml.XmlAdapterTimecode;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Objects;


/**
 * <p>Java class for TimecodeCollection complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TimecodeCollection"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ltc" type="{http://timecode.commons.glookast.com}Timecode" minOccurs="0"/&gt;
 *         &lt;element name="vitc" type="{http://timecode.commons.glookast.com}Timecode" minOccurs="0"/&gt;
 *         &lt;element name="tod" type="{http://timecode.commons.glookast.com}Timecode" minOccurs="0"/&gt;
 *         &lt;element name="rs422" type="{http://timecode.commons.glookast.com}Timecode" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TimecodeCollection", namespace = "http://timecode.commons.glookast.com", propOrder = {
    "ltc",
    "vitc",
    "tod",
    "rs422"
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, defaultImpl = TimecodeCollection.class)
public class TimecodeCollection implements Serializable
{

    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(XmlAdapterTimecode.class)
    protected Timecode ltc;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(XmlAdapterTimecode.class)
    protected Timecode vitc;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(XmlAdapterTimecode.class)
    protected Timecode tod;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(XmlAdapterTimecode.class)
    protected Timecode rs422;

    /**
     * Default no-arg constructor
     */
    public TimecodeCollection()
    {
        this(null, null, null, null);
    }

    /**
     * Fully-initialising value constructor
     */
    public TimecodeCollection(final Timecode ltc, final Timecode vitc, final Timecode tod, final Timecode rs422)
    {
        this.ltc = ltc == null ? new Timecode() : ltc;
        this.vitc = vitc == null ? new Timecode() : vitc;
        this.tod = tod == null ? new Timecode() : tod;
        this.rs422 = rs422 == null ? new Timecode() : rs422;
    }

    public TimecodeCollection(TimecodeCollection timecodeCollection)
    {
        this.ltc = timecodeCollection.ltc;
        this.vitc = timecodeCollection.vitc;
        this.tod = timecodeCollection.tod;
        this.rs422 = timecodeCollection.rs422;
    }

    /**
     * Gets the value of the ltc property.
     *
     * @return possible object is
     * {@link String }
     */
    public Timecode getLtc()
    {
        return ltc;
    }

    /**
     * Sets the value of the ltc property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLtc(Timecode value)
    {
        this.ltc = value == null ? new Timecode() : value;
    }

    /**
     * Gets the value of the vitc property.
     *
     * @return possible object is
     * {@link String }
     */
    public Timecode getVitc()
    {
        return vitc;
    }

    /**
     * Sets the value of the vitc property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVitc(Timecode value)
    {
        this.vitc = value == null ? new Timecode() : value;
    }

    /**
     * Gets the value of the tod property.
     *
     * @return possible object is
     * {@link String }
     */
    public Timecode getTod()
    {
        return tod;
    }

    /**
     * Sets the value of the tod property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTod(Timecode value)
    {
        this.tod = value == null ? new Timecode() : value;
    }

    /**
     * Gets the value of the rs422 property.
     *
     * @return possible object is
     * {@link String }
     */
    public Timecode getRs422()
    {
        return rs422;
    }

    /**
     * Sets the value of the rs422 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRs422(Timecode value)
    {
        this.rs422 = value == null ? new Timecode() : value;
    }

    public Timecode get(TimecodeSource timecodeSource)
    {
        switch (timecodeSource) {
            case LTC:
                return ltc;
            case VITC:
                return vitc;
            case TOD:
                return tod;
            case RS_422:
                return rs422;
        }
        return new Timecode();
    }

    @Override
    public String toString()
    {
        return "TimecodeCollection{" +
               "ltc=" + ltc +
               ", vitc=" + vitc +
               ", tod=" + tod +
               ", rs422=" + rs422 +
               '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimecodeCollection that = (TimecodeCollection) o;
        return Objects.equals(ltc, that.ltc) &&
               Objects.equals(vitc, that.vitc) &&
               Objects.equals(tod, that.tod) &&
               Objects.equals(rs422, that.rs422);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(ltc, vitc, tod, rs422);
    }
}
