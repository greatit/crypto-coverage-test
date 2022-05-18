package com.example;

import invalid.test.ping.ObjectFactory;
import invalid.test.ping.Ping;
import invalid.test.ping.ReqType;
import invalid.test.ping.RspType;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 * TODO Javadoc.
 *
 * @author {@literal David Lakatos <david.lakatos@great-it.com>}
 *
 */
@WebService(name = "Ping", serviceName = "PingSvc", portName = "PingPort",
    targetNamespace = "http://ping.test.invalid", wsdlLocation = "WEB-INF/wsdl/pingsvc.wsdl",
    endpointInterface = "invalid.test.ping.Ping")
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP11HTTP_BINDING)
public class PingImpl implements Ping {
  private final ObjectFactory factory = new ObjectFactory();

  @Override
  public RspType ping(ReqType parameters) {
    RspType response = factory.createRspType();
    response.setParam0(parameters.getParam0());
    response.setParam1(parameters.getParam1());
    response.setParam2(parameters.getParam2());
    return response;
  }

}
