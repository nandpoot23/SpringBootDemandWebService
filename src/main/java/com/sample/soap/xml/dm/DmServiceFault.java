package com.sample.soap.xml.dm;

import javax.xml.ws.WebFault;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "serviceFault", targetNamespace = "http://dm.xml.soap.sample.com/")
public class DmServiceFault extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private ExceptionType faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public DmServiceFault(String message, ExceptionType faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public DmServiceFault(String message, ExceptionType faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return returns fault bean: com.comcast.xml.types.ExceptionType
     */
    public ExceptionType getFaultInfo() {
        return faultInfo;
    }

}
