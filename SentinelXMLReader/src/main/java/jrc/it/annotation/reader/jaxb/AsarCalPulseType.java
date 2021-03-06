//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.05 at 04:36:39 PM CET 
//


package jrc.it.annotation.reader.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Annotation record for ASAR calibration pulses.
 * 
 * <p>Java class for asarCalPulseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="asarCalPulseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="maxAmpP1" type="{}float"/>
 *         &lt;element name="maxAmpP2" type="{}float"/>
 *         &lt;element name="maxAmpP3" type="{}float"/>
 *         &lt;element name="avgAmpP1" type="{}float"/>
 *         &lt;element name="avgAmpP2" type="{}float"/>
 *         &lt;element name="avgAmpP3" type="{}float"/>
 *         &lt;element name="calPulse" type="{}float"/>
 *         &lt;element name="phaseP1" type="{}float"/>
 *         &lt;element name="phaseP1A" type="{}float"/>
 *         &lt;element name="phaseP2" type="{}float"/>
 *         &lt;element name="phaseP3" type="{}float"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "asarCalPulseType", propOrder = {
    "maxAmpP1",
    "maxAmpP2",
    "maxAmpP3",
    "avgAmpP1",
    "avgAmpP2",
    "avgAmpP3",
    "calPulse",
    "phaseP1",
    "phaseP1A",
    "phaseP2",
    "phaseP3"
})
public class AsarCalPulseType {

    @XmlElement(required = true)
    protected Float maxAmpP1;
    @XmlElement(required = true)
    protected Float maxAmpP2;
    @XmlElement(required = true)
    protected Float maxAmpP3;
    @XmlElement(required = true)
    protected Float avgAmpP1;
    @XmlElement(required = true)
    protected Float avgAmpP2;
    @XmlElement(required = true)
    protected Float avgAmpP3;
    @XmlElement(required = true)
    protected Float calPulse;
    @XmlElement(required = true)
    protected Float phaseP1;
    @XmlElement(required = true)
    protected Float phaseP1A;
    @XmlElement(required = true)
    protected Float phaseP2;
    @XmlElement(required = true)
    protected Float phaseP3;

    /**
     * Gets the value of the maxAmpP1 property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getMaxAmpP1() {
        return maxAmpP1;
    }

    /**
     * Sets the value of the maxAmpP1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setMaxAmpP1(Float value) {
        this.maxAmpP1 = value;
    }

    /**
     * Gets the value of the maxAmpP2 property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getMaxAmpP2() {
        return maxAmpP2;
    }

    /**
     * Sets the value of the maxAmpP2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setMaxAmpP2(Float value) {
        this.maxAmpP2 = value;
    }

    /**
     * Gets the value of the maxAmpP3 property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getMaxAmpP3() {
        return maxAmpP3;
    }

    /**
     * Sets the value of the maxAmpP3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setMaxAmpP3(Float value) {
        this.maxAmpP3 = value;
    }

    /**
     * Gets the value of the avgAmpP1 property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getAvgAmpP1() {
        return avgAmpP1;
    }

    /**
     * Sets the value of the avgAmpP1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setAvgAmpP1(Float value) {
        this.avgAmpP1 = value;
    }

    /**
     * Gets the value of the avgAmpP2 property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getAvgAmpP2() {
        return avgAmpP2;
    }

    /**
     * Sets the value of the avgAmpP2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setAvgAmpP2(Float value) {
        this.avgAmpP2 = value;
    }

    /**
     * Gets the value of the avgAmpP3 property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getAvgAmpP3() {
        return avgAmpP3;
    }

    /**
     * Sets the value of the avgAmpP3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setAvgAmpP3(Float value) {
        this.avgAmpP3 = value;
    }

    /**
     * Gets the value of the calPulse property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getCalPulse() {
        return calPulse;
    }

    /**
     * Sets the value of the calPulse property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setCalPulse(Float value) {
        this.calPulse = value;
    }

    /**
     * Gets the value of the phaseP1 property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getPhaseP1() {
        return phaseP1;
    }

    /**
     * Sets the value of the phaseP1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setPhaseP1(Float value) {
        this.phaseP1 = value;
    }

    /**
     * Gets the value of the phaseP1A property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getPhaseP1A() {
        return phaseP1A;
    }

    /**
     * Sets the value of the phaseP1A property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setPhaseP1A(Float value) {
        this.phaseP1A = value;
    }

    /**
     * Gets the value of the phaseP2 property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getPhaseP2() {
        return phaseP2;
    }

    /**
     * Sets the value of the phaseP2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setPhaseP2(Float value) {
        this.phaseP2 = value;
    }

    /**
     * Gets the value of the phaseP3 property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getPhaseP3() {
        return phaseP3;
    }

    /**
     * Sets the value of the phaseP3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setPhaseP3(Float value) {
        this.phaseP3 = value;
    }

}
