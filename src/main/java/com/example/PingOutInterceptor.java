package com.example;

import java.util.HashMap;
import java.util.Map;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;

/**
 * TODO Javadoc.
 *
 * @author {@literal David Lakatos <david.lakatos@great-it.com>}
 *
 */
public class PingOutInterceptor extends WSS4JOutInterceptor {

  public PingOutInterceptor() {
    super(createProps());
  }

  static Map<String, Object> createProps() {
    Map<String, Object> outProps = new HashMap<String, Object>();
    outProps.put("action", "Signature");
    outProps.put("passwordCallbackClass", UtPasswordCallback.class.getName());

    outProps.put("signatureUser", "mykey");
    outProps.put("signaturePropFile", "META-INF/Server_Sign.properties");
    outProps.put("signatureKeyIdentifier", "DirectReference");
    outProps.put("signatureParts", "{Element}{http://schemas.xmlsoap.org/soap/envelope/}Body");
    outProps.put("encryptionKeyTransportAlgorithm",
        "http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p");
    outProps.put("signatureAlgorithm", "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256");

    return outProps;
  }

}
