package com.example.spring.boot.test.util;

import static com.google.common.collect.Lists.newArrayList;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import org.jose4j.base64url.Base64;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;

/**
 * @author mlahariya
 * @version 1.0, Dec 2016
 */

public class TokenHelper {

    private static final String PRIVATE_KEY_BASE64 = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCCa7ttjLUmx6C03KWHLTx9M/un+sf808jOsGbJKiPznxFtcj9O+v0glv+McqITBKethCiNYJFPAZVBwqCNGhTo9kEZ//kSfOuoxPUWIe+eBAeo9ZzEKTsVYNAzJS8Eags6YuVGpxB3yD1GW3HD2BXeluwQxu4zumsgFi9Flzg2epYC7+mnO15+lIkaEGahiTAZl4J0es0qyb6HWNk5fCGyofcLllwb+ulK+jxsuAu1b3BDyfQq7lRkMHdBVrGpV86gsLo6Law2fS1IAAq+uNdPhWGB+Zcdm+EOtmCINeo3IcNLLhAygJbb3MIUpe/i26RzaafZogJMTZ3/raaZ043DAgMBAAECggEAPXA0zimf2NHn0euzipdzzGNlpL9N4Lt/+GY/NXrFjumbUxyj2DgCcIzRe78wvJbPyrzcQaWBLl/HiT//VBk6SnhfOLlOAziX51xJPJDmCWFKnnx9yibGL4745W8Z1BGEko+uBHeFGXhZ7T/8us6bls0wDcM4yWODPY1V3YmcMIUoltz1M69VFJRYvX+MV91kyUgvk3PzEv4UFcbnIbanFJWEVSHDhrs6n1yYyIt6+EL0bi3kSpD1bJFUKkXpWdmGER1Ggc+7uOspnSiv/fentTeLMsT026ucoqChfBqDCB2PJu5JHY5FTGSAgZGE3JznDoSngZDu7vnHTmTLG95qSQKBgQDCJW19ElVPJ6jxZMys3AFwq6scAVzrRKSTpD86jUXMA+RF2sPSS5XqqyZ0b+BLOPv6jNTKc554/FE5lT0Wv31K2wYN0RazFtQQWYsDEAr/9+qjws0YNjgckey9BD7H0/k3QsSohNKQ8AjBtz9zlq28ObAi8xBJcTIgBhlzU4gZPQKBgQCr+NxAUDx3Ad2G06cISzM2ny2o4VJiGkakTv8C7TT+O5njSSrCmAK9Rzd94W1YH+3aUiHUB9m0NHalu3kKkVZOpefFt5JlSbGh/9fR76zKcNwjFMVtxo7CWrrlAhh1egO+ptPTGEMsVSv7Eu3qcIiXcOTchVEMYd83Bt7GUe6y/wKBgF/18fU0/5edScLvCL+UCt4u6+duwQfEqPgU4e/F7i7V6ZmhdNLX62TPbbV1qs5jfyCrRW4/0Z+JT3h/zG8W/Q3nzkk8BNVGPlFKgQxlfJpiZsbEvWzvN7Lfab1PgNxyaKcoyF1nMVDUY++j7KfsXTlA5k+QBofWfGkZurCnJAkdAoGBAI0Li+F3rU/SCNXHC8zimHtpLHLBcuchM+UZIVBOKtOFIJ7oaJi3Qi/plBq7ZAoODe+swy/6zFRfXxxo2UqQjx/sNF7WQ8ytF3KNun3lvGtBC1v+cjqpynafIYAoKtVXIHEitMMCuD1JEcxX5ygL6guDPUGcWi/0qbWg/d/cXRETAoGBAJbVTkVZaKqhiWKqSqyS4cMdmKZNcmQnT7bMd6Z2nJQlXqHC10W3FVkqEClFIztOepr/4qWatXl5wdqmSBoSzoM9oOpe9N+h5Y3J2YpCYNmPwvoTrkU6Z4g/MYQV1NwAuavXitZoHVDKd4DuwqDm8pB150iN1fWyMxyjKjgi9rjS";

    public static void main(String[] args) {
        System.out.println("Token:" + getToken());
    }

    public static String getToken() {
        try {
            JwtClaims claims = new JwtClaims();
            // who creates the token and signs it
            claims.setIssuer("PingFederate");
            // time when the token will expire (10 minutes from now)
            claims.setExpirationTimeMinutesInTheFuture(1000);
            // a unique identifier for the token
            // claims.setGeneratedJwtId();
            // when the token was issued/created (now)
            // claims.setIssuedAtToNow();
            // time before which the token is not yet valid (2 minutes ago)
            // claims.setNotBeforeMinutesInThePast(2);
            // the subject/principal is whom the token is about
            // claims.setSubject("subject");
            // additional claims/attributes about the subject can be added
            claims.setClaim("client_id", "cima");
            // additional claims/attributes about the subject can be added
            claims.setStringListClaim("scope", newArrayList("gsp"));
            // List<String> groups = Arrays.asList("group-one", "other-group",
            // "group-three");
            // claims.setStringListClaim("groups", groups); // multi-valued
            // claims work too and will end up as a JSON array

            // A JWT is a JWS and/or a JWE with JSON claims as the payload.
            // In this example it is a JWS so we create a JsonWebSignature
            // object.
            JsonWebSignature jws = new JsonWebSignature();

            // The payload of the JWS is JSON content of the JWT Claims
            jws.setPayload(claims.toJson());

            // The JWT is signed using the private key
            jws.setKey(getPrivateKey());

            // Set the Key ID (kid) header because it's just the polite thing to
            // do.
            // We only have one key in this example but a using a Key ID helps
            // facilitate a smooth key rollover process
            jws.setKeyIdHeaderValue("JWTSigningCert-2016");

            // Set the signature algorithm on the JWT/JWS that will integrity
            // protect the claims
            jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

            // Sign the JWS and produce the compact serialization or the
            // complete JWT/JWS
            // representation, which is a string consisting of three dot ('.')
            // separated
            // base64url-encoded parts in the form Header.Payload.Signature
            // If you wanted to encrypt it, you can simply set this jwt as the
            // payload
            // of a JsonWebEncryption object and set the cty (Content Type)
            // header to "jwt".
            return jws.getCompactSerialization();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] byteKey = Base64.decode(PRIVATE_KEY_BASE64);
        PKCS8EncodedKeySpec X509privateKey = new PKCS8EncodedKeySpec(byteKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        return kf.generatePrivate(X509privateKey);
    }
}
