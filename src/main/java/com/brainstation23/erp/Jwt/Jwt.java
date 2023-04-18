package com.brainstation23.erp.Jwt;

import com.brainstation23.erp.model.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Base64;
import java.util.Date;

public class Jwt {
    public Jwt()
    {

    }
    public static String getDecodedUserData(String authHeader)
    {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] chunks = authHeader.split("\\.");
        return new String(decoder.decode(chunks[1]));
    }
    public static String getToken(User user)
    {
        return
                Jwts.builder()
                        .setSubject(user.getEmail())
                        .claim("role", user.getRole())
                        .claim("id", user.getId())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 120000)) // Token will expire in 2 minutes
                        .signWith(SignatureAlgorithm.HS512, "secretkey")
                        .compact();
    }
}
