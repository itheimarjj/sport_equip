package com.example.util;


import com.example.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.SignatureException;
import java.util.Date;

public class JWTUtils {
    private static final String SECRET = "hefeijingjixueyuanaVeryLongSecretKeyWithAtLeast64CharactersToEnsureSecurityForHS512Algorithm1234567890";
    private static final long EXPIRATION = 86400000; // 24小时

    public static String generateToken(User user) {
        return Jwts.builder()
                // 主题（通常是用户唯一标识）
                .setSubject(String.valueOf(user.getUserid()))
                // 自定义 Claims（存储其他用户信息）
                .claim("userid", user.getUserid())
                .claim("username", user.getUsername())
                .claim("role", user.getRole())
                // 过期时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                // 签名算法和密钥
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public static User parseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            // 从 Claims 中提取用户信息
            User user = new User();
            user.setUserid(Integer.parseInt(claims.getSubject())); // Subject 存储 userid
            user.setUsername(claims.get("username", String.class));
            user.setRole(claims.get("role", String.class));
            return user;
        } catch (ExpiredJwtException e) {
            // Token 已过期
            return null;
        } catch (Exception e) {
            // 其他解析错误（如格式错误）
            return null;
        }
    }
}
