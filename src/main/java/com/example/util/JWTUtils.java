package com.example.util;


import com.example.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JWTUtils {
    private static final String SECRET = "hefeijingjixueyuanaVeryLongSecretKeyWithAtLeast64CharactersToEnsureSecurityForHS512Algorithm1234567890";
    private static final long EXPIRATION = 86400000; // 24小时

    public static String generateToken(User user) {

        return Jwts.builder()
                .setSubject(String.valueOf(user.getUserid()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public static String parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}
