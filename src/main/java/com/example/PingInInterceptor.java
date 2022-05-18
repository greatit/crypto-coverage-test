package com.example;

import java.util.HashMap;
import java.util.Map;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;

/**
 * TODO Javadoc.
 *
 * @author {@literal David Lakatos <david.lakatos@great-it.com>}
 *
 */
public class PingInInterceptor extends WSS4JInInterceptor {

  /**
   * TODO Javadoc.
   */
  public PingInInterceptor() {
    super(createProps());
  }

  /**
   * TODO Javadoc.
   *
   * @return TODO
   */
  static Map<String, Object> createProps() {
    Map<String, Object> inProps = new HashMap<String, Object>();

    // common
    inProps.put("action", "Signature Encrypt");
    inProps.put("passwordCallbackClass", UtPasswordCallback.class.getName());

    // signature
    inProps.put("signaturePropFile", "META-INF/Server_SignVerf.properties");
    inProps.put("signatureKeyIdentifier", "DirectReference");
    inProps.put("signatureAlgorithm", "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256");

    // encryption
    inProps.put("decryptionPropFile", "META-INF/Server_Decrypt.properties");
    inProps.put("encryptionKeyIdentifier", "IssuerSerial");
    inProps.put("encryptionKeyTransportAlgorithm",
        "http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p");

    return inProps;
  }
}
