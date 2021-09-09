# Shownotes

__1. Create a service that makes HTTP calls to backend service__
  `new RestTemplate().getForEntity(backendUrl, String.class).getBody();`

__2. Create a CA:__
  - Generate key: `openssl genrsa -aes256 -out ca.key 2048`
  - Generate cert: `openssl req -new -x509 -sha256 -key ca.key -out ca.crt`

__3. Generate a CSR for business application service__
  - `openssl req -new -newkey rsa:2048 -sha256 -keyout business-application-server.key -out business-application-server.csr`

__4. Sign the CSR from the CA__
  - `openssl x509 -req -CA ../../ca.crt -CAkey ../../ca.key -in business-application-server.csr -out business-application-server.crt -days 365 -CAcreateserial -sha256`

__5. Create a .p12 file of the cert and key__
  - `openssl pkcs12 -export -in business-application-server.crt \
      -inkey business-application-server.key \
      -out business-application-server.p12 -name server-cert \
      -CAfile ../../ca.crt -caname root`

__6. Import the .p12 file to a java keystore__
  - `keytool -importkeystore \
        -destkeystore business-application-server.keystore \
        -srckeystore business-application-server.p12 -srcstoretype PKCS12 \
        -alias server-cert`

__7. Enable HTTPS on the server__
  - ```
    server.ssl.key-store-type=PKCS12
    server.ssl.key-store=classpath:mTLS/business-application-server.keystore
    server.ssl.key-store-password=password1234
    server.ssl.key-alias=server-cert
    server.ssl.enabled=true
    ```

__8. Create a truststore on API-GW application:__
  - `keytool -import -file business-application-server.crt -alias server-cert -keystore api-gw.truststore`

__9. Configure the client to use the truststore:__
  - ```
    trust.store=classpath:mTLS/api-gw.truststore
    trust.store.password=password1234
    ```
  - `implementation 'org.apache.httpcomponents:httpclient:4.5.13'`
  - Use the code from the finished example

__10. Configure the server to only accept valid certificates__
  - Install spring security: `implementation 'org.springframework.boot:spring-boot-starter-security'`
  - Implement code
  - Add configuration `server.ssl.client-auth=need`

__11. Create a valid certificate for the client__
  - CSR: `openssl req -new -newkey rsa:2048 -sha256 -keyout api-gw-client.key -out api-gw-client.csr`
  - Sign CSR: `openssl x509 -req -CA ../../ca.crt -CAkey ../../ca.key -in api-gw-client.csr -out api-gw-client.crt -days 365 -CAcreateserial -sha256`
  - .p12 file: `openssl pkcs12 -export -in api-gw-client.crt \
      -inkey api-gw-client.key \
      -out api-gw-client.p12 -name client-cert \
      -CAfile ../../ca.crt -caname root`
  - keystore file: `keytool -importkeystore \
        -destkeystore api-gw.keystore \
        -srckeystore api-gw-client.p12 -srcstoretype PKCS12 \
        -alias client-cert`

__12. Use the certificate with outgoing requests__
  - Add keystore-logic to the already created template

__13. Install client cert in trust store__
  - Create truststore: `keytool -import -file api-gw-client.crt -alias client-cert -keystore business-application.truststore`
  - Add truststore configuration:
    ```
    server.ssl.trust-store=classpath:mTLS/business-application.truststore
    server.ssl.trust-store-password=password1234
    server.ssl.client-auth=need
    ```
__14. Implement authentication provider__
  - Consult the code example

__15. Exercise__
1. Create a `.csr` for a new client cert
2. Email the `.csr` to `<email>`
3. Receive your client certificate and the root certificate in the response
4. Use your client cert when making outgoing requests
5. Install root-cert into trust-store on business-application
6. Make requests to a friend 😃
  