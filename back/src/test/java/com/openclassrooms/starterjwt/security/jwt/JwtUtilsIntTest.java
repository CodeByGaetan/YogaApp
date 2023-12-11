package com.openclassrooms.starterjwt.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@SpringBootTest
public class JwtUtilsIntTest {

    @Value("${oc.app.jwtSecret}")
    private String jwtSecret;

    @Value("${oc.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Autowired
    JwtUtils jwtUtilsUnderTest;

    String username = "test@studio.com";

    @Test
    public void generateValidate_jwt_returnValidToken() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().username(username).password("testOfPassword").build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        String token = jwtUtilsUnderTest.generateJwtToken(authentication);

        String testName = jwtUtilsUnderTest.getUserNameFromJwtToken(token);
        Boolean testIsValid = jwtUtilsUnderTest.validateJwtToken(token);

        assertFalse(token.isEmpty());
        assertEquals(username, testName);
        assertEquals(true, testIsValid);
    }

    @Test
    public void validate_MalformedJwt_returnFalse() {
        Boolean testIsValid = jwtUtilsUnderTest.validateJwtToken("jwt");

        assertEquals(false, testIsValid);
    }

    @Test
    public void validate_BadSignedJwt_returnFalse() {

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, "WrongSignature")
                .compact();

        Boolean testIsValid = jwtUtilsUnderTest.validateJwtToken(token);

        assertEquals(false, testIsValid);
    }

    @Test
    public void validate_ExpiredJwt_returnFalse() {

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime()))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        Boolean testIsValid = jwtUtilsUnderTest.validateJwtToken(token);

        assertEquals(false, testIsValid);
    }

}
