package com.example;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import invalid.test.ping.ObjectFactory;
import invalid.test.ping.Ping;
import invalid.test.ping.PingSvc;
import invalid.test.ping.ReqType;
import invalid.test.ping.RspType;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * TODO Javadoc.
 *
 * @author {@literal David Lakatos <david.lakatos@great-it.com>}
 *
 */
public class PingTest {
  private static QName serviceName;
  private static URL wsdlSingleCoverageUrl;
  private static URL wsdlDualCoverageUrl;
  private static PingSvc pingSingleCoverageSvc;
  private static PingSvc pingDualCoverageSvc;
  private static Ping pingSingleCoveragePort;
  private static Ping pingDualCoveragePort;
  private static Map<String, Object> defaultInProps = new HashMap<String, Object>();
  private static Map<String, Object> defaultOutProps = new HashMap<String, Object>();
  private static ReqType request;

  /**
   * TODO Javadoc.
   *
   * @throws MalformedURLException TODO
   */
  @BeforeAll
  public static void setup() throws MalformedURLException {
    serviceName = new QName("http://ping.test.invalid", "PingSvc");
    wsdlSingleCoverageUrl =
        new URL("http://localhost:8080/crypto-coverage-test/singlecoverage?wsdl");
    wsdlDualCoverageUrl = new URL("http://localhost:8080/crypto-coverage-test/dualcoverage?wsdl");
    pingSingleCoverageSvc = new PingSvc(wsdlSingleCoverageUrl, serviceName);
    pingDualCoverageSvc = new PingSvc(wsdlDualCoverageUrl, serviceName);
    pingSingleCoveragePort = pingSingleCoverageSvc.getPingPort();
    pingDualCoveragePort = pingDualCoverageSvc.getPingPort();

    // generic
    defaultInProps.put("action", "Signature");
    defaultInProps.put("passwordCallbackClass", UtPasswordCallback.class.getName());
    // signature
    defaultInProps.put("signaturePropFile", "META-INF/Client_SignVerf.properties");
    defaultInProps.put("signatureKeyIdentifier", "DirectReference");
    defaultInProps.put("signatureAlgorithm", "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256");
    // encryption
    defaultInProps.put("encryptionKeyTransportAlgorithm",
        "http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p");

    // generic
    defaultOutProps.put("action", "Signature Encrypt");
    defaultOutProps.put("passwordCallbackClass", UtPasswordCallback.class.getName());
    // signature
    defaultOutProps.put("signatureUser", "mykey");
    defaultOutProps.put("signaturePropFile", "META-INF/Client_Sign.properties");
    defaultOutProps.put("signatureKeyIdentifier", "DirectReference");
    defaultOutProps.put("signatureParts",
        "{Element}{http://schemas.xmlsoap.org/soap/envelope/}Body");
    defaultOutProps.put("signatureAlgorithm", "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256");
    // encryption
    defaultOutProps.put("encryptionUser", "server");
    defaultOutProps.put("encryptionPropFile", "META-INF/Client_Encrypt.properties");
    defaultOutProps.put("encryptionKeyIdentifier", "IssuerSerial");
    defaultOutProps.put("encryptionKeyTransportAlgorithm",
        "http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p");

    // call Web service
    ObjectFactory factory = new ObjectFactory();
    request = factory.createReqType();
    String param0 = "Hello";
    String param1 = "World!";
    String param2 = "!";
    request.setParam0(param0);
    request.setParam1(param1);
    request.setParam2(param2);
  }

  /**
   * Remove all WSS4J interceptors.
   */
  @BeforeEach
  public void clearInterceptors() {
    List<Interceptor<? extends Message>> inInterceptors =
        ClientProxy.getClient(pingSingleCoveragePort).getInInterceptors();
    List<Interceptor<? extends Message>> outInterceptors =
        ClientProxy.getClient(pingSingleCoveragePort).getOutInterceptors();

    for (Interceptor<? extends Message> i : inInterceptors) {
      if (i instanceof WSS4JInInterceptor) {
        inInterceptors.remove(i);
      }
    }

    for (Interceptor<? extends Message> i : outInterceptors) {
      if (i instanceof WSS4JOutInterceptor) {
        outInterceptors.remove(i);
      }
    }
  }

  /**
   * Signs and encrypts request's param1 XML tag.
   */
  @Test
  public void signAndEncryptSingleCoverage() {
    // add outbound encryption and signing policy
    ClientProxy.getClient(pingSingleCoveragePort).getInInterceptors()
        .add(new WSS4JInInterceptor(defaultInProps));

    // prepare outbound encryption and signing policy
    Map<String, Object> outProps = new HashMap<String, Object>(defaultOutProps);
    // this would not trigger the WSS4J bug if the server only expected param1 to be encrypted
    outProps.put("encryptionParts", "{Element}{http://ping.test.invalid}param1");
    // add outbound encryption and signing policy
    ClientProxy.getClient(pingSingleCoveragePort).getOutInterceptors()
        .add(new WSS4JOutInterceptor(outProps));

    // evaluate response
    assertDoesNotThrow(() -> {
      RspType response = pingSingleCoveragePort.ping(request);
      assertEquals(request.getParam0(), response.getParam0());
      assertEquals(request.getParam1(), response.getParam1());
      assertEquals(request.getParam2(), response.getParam2());
    });
  }

  /**
   * Signs and encrypts request's param1 and param2 XML tags.
   */
  @Test
  public void signAndEncryptDualCoverage() {
    // add outbound encryption and signing policy
    ClientProxy.getClient(pingDualCoveragePort).getInInterceptors()
        .add(new WSS4JInInterceptor(defaultInProps));

    // prepare outbound encryption and signing policy
    Map<String, Object> outProps = new HashMap<String, Object>(defaultOutProps);
    // this does trigger the WSS4J bug
    outProps.put("encryptionParts",
        "{Element}{http://ping.test.invalid}param1;{Element}{http://ping.test.invalid}param2");
    // add outbound encryption and signing policy
    ClientProxy.getClient(pingSingleCoveragePort).getOutInterceptors()
        .add(new WSS4JOutInterceptor(outProps));

    // evaluate response
    assertDoesNotThrow(() -> {
      RspType response = pingSingleCoveragePort.ping(request);
      assertEquals(request.getParam0(), response.getParam0());
      assertEquals(request.getParam1(), response.getParam1());
      assertEquals(request.getParam2(), response.getParam2());
    });
  }
}
