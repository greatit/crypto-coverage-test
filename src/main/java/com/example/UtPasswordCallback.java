package com.example;

import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.wss4j.common.ext.WSPasswordCallback;

/**
 * TODO Javadoc.
 *
 * @author {@literal David Lakatos <david.lakatos@great-it.com>}
 *
 */
public class UtPasswordCallback implements CallbackHandler {

  public static final String PASSWORD = "secret";

  /**
   * TODO Javadoc.
   *
   * @param callbacks TODO
   */
  @Override
  public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
    for (int i = 0; i < callbacks.length; i++) {
      WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];
      pc.setPassword(PASSWORD);
    }
  }
}
