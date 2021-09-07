package se.omegapoint.kompetensdag.apigw;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Configuration
public class HttpsTemplate {

    private final URL trustStoreLocation;
    private final String trustStorePassword;
    private final String keyStoreType;
    private final String keystore;
    private final char[] keystorePass;

    public HttpsTemplate(
            @Value("${trust.store}") String trustStorePath,
            @Value("${trust.store.password}") String trustStorePassword,
            @Value("${key.store}") String keystore,
            @Value("${key.store.password}") String keystorePass,
            @Value("${key.store.type}") String keyStoreType
    ) {
        try {
            trustStoreLocation = new URL(trustStorePath);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        this.trustStorePassword = trustStorePassword;
        this.keyStoreType = keyStoreType;
        this.keystore = keystore;
        this.keystorePass = keystorePass.toCharArray();
    }

    public RestTemplate createRestTemplate() {
        try {
            var sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(trustStoreLocation, trustStorePassword.toCharArray())
                    .loadKeyMaterial(keystore(), keystorePass)
                    .build();
            var socketFactory = new SSLConnectionSocketFactory(sslContext);
            var httpClient = HttpClients.custom()
                    .setSSLSocketFactory(socketFactory)
                    .build();
            var factory = new HttpComponentsClientHttpRequestFactory(httpClient);
            return new RestTemplate(factory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private KeyStore keystore() throws KeyStoreException, FileNotFoundException {
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        File key = ResourceUtils.getFile(keystore);
        try (InputStream in = new FileInputStream(key)) {
            keyStore.load(in, keystorePass);
        } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new RuntimeException(e);
        }

        return keyStore;
    }
}
