package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.cxf.ws.security.wss4j.CryptoCoverageUtil.CoverageType;
import org.apache.cxf.ws.security.wss4j.DefaultCryptoCoverageChecker;

/**
 * TODO Javadoc.
 *
 * @author {@literal David Lakatos <david.lakatos@great-it.com>}
 *
 */
public class PingCryptoCoverageChecker extends DefaultCryptoCoverageChecker {

  /**
   * TODO Javadoc.
   *
   * @param coverageCnt cover 1 or 2 parameters with encryption
   *
   * @throws IllegalArgumentException coverage count is not 1 or 2
   */
  public PingCryptoCoverageChecker(int coverageCnt) {
    if (coverageCnt < 1 || coverageCnt > 2) {
      throw new IllegalArgumentException("coverage count must be 1 or 2");
    }

    // ping prefix registration
    Map<String, String> pingPrefixes = new HashMap<>();
    pingPrefixes.put("ping", "http://ping.test.invalid");
    addPrefixes(pingPrefixes);

    setSignBody(true);
    setSignTimestamp(false);
    setEncryptBody(false);
    setSignAddressingHeaders(false);
    setEncryptUsernameToken(false);

    // expressions to check
    List<XPathExpression> xpaths = new ArrayList<XPathExpression>();
    // root XPath of all expressions
    String xpath = "/soapenv:Envelope/soapenv:Body/ping:req";
    if (coverageCnt > 0) {
      xpaths.add(new XPathExpression(xpath + "/ping:param1", CoverageType.ENCRYPTED));
    }
    if (coverageCnt > 1) {
      xpaths.add(new XPathExpression(xpath + "/ping:param2", CoverageType.ENCRYPTED));
    }
    addXPaths(xpaths);
  }
}
