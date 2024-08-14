package com.gminds.auth_server.controller;

import com.gminds.auth_server.config.RsaKeyProperties;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class JwkSetController {

    private final JWKSource<SecurityContext> jwkSource;
    private final RsaKeyProperties rsaKeys;

    @Autowired
    public JwkSetController(JWKSource<SecurityContext> jwkSource,   RsaKeyProperties rsaKeys) {
        this.jwkSource = jwkSource;
        this.rsaKeys  = rsaKeys;
    }

    @GetMapping("/jwks")
    public Map<String, Object> keys() {
        JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
        JWKSet jwkSet = new JWKSet(jwk);
        return jwkSet.toJSONObject();
    }
}
