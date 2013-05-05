/*
 * Copyright 2013 bankinterface.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bankinterface.util;

import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509KeyManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * KeyManager used to specify a certificate alias
 */
public class AliasKeyManager implements X509KeyManager {
    private static final Logger logger     = LoggerFactory.getLogger(AliasKeyManager.class);
    protected X509KeyManager    keyManager = null;
    protected String            alias      = null;

    protected AliasKeyManager() {
    }

    public AliasKeyManager(X509KeyManager keyManager, String alias) {
        this.keyManager = keyManager;
        this.alias = alias;
    }

    // this is where the customization comes in
    public String chooseClientAlias(String[] keyTypes, Principal[] issuers, Socket socket) {
        for (String keyType : keyTypes) {
            String[] aliases = keyManager.getClientAliases(keyType, null);
            if (aliases != null && aliases.length > 0) {
                for (String alias : aliases) {
                    if (this.alias.equals(alias)) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("chooseClientAlias for keyType [" + keyType + "] got alias " + this.alias);
                        }
                        return this.alias;
                    }
                }
            }
        }
        return null;
    }

    // these just pass through the keyManager
    public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
        return keyManager.chooseServerAlias(keyType, issuers, socket);
    }

    // these just pass through the keyManager
    public String chooseServerAlias(String keyType, Socket socket, Principal... issuers) {
        return keyManager.chooseServerAlias(keyType, issuers, socket);
    }

    public X509Certificate[] getCertificateChain(String alias) {
        X509Certificate[] certArray = keyManager.getCertificateChain(alias);
        if (logger.isDebugEnabled()) {
            logger.debug("getCertificateChain for alias [" + alias + "] got " + certArray.length + " results");
        }
        return certArray;
    }

    public String[] getClientAliases(String keyType, Principal[] issuers) {
        return keyManager.getClientAliases(keyType, issuers);
    }

    public PrivateKey getPrivateKey(String alias) {
        PrivateKey pk = keyManager.getPrivateKey(alias);
        if (logger.isDebugEnabled()) {
            String result = pk != null ? "[alg:" + pk.getAlgorithm() + ";format:" + pk.getFormat() + "]"
                    : "[Not Found!]";
            logger.debug("getPrivateKey for alias [" + alias + "] got " + result);
        }
        return pk;
    }

    public String[] getServerAliases(String keyType, Principal[] issuers) {
        return keyManager.getServerAliases(keyType, issuers);
    }
}
