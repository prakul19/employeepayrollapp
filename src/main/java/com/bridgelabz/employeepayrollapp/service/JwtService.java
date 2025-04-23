//package com.bridgelabz.employeepayrollapp.service;
//
//import com.bridgelabz.employeepayrollapp.dto.UserDTO;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.stereotype.Service;
//
//import java.security.Key;
//import java.util.Date;
//
//@Service
//public class JwtService {
//
//    private static final String SECRET_KEY = "your_secret_key";  // You should store it securely, not in code.
//    private static final long EXPIRATION_TIME = 86400000;  // 24 hours
//
//    private Key getSigningKey() {
//        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
//    }
//
//    // Method to generate JWT Token
//    public String generateToken(UserDTO userDTO) {
//        return Jwts.builder()
//                .setSubject(userDTO.getEmail())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    // Method to validate JWT Token
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(getSigningKey())
//                    .build()
//                    .parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    // Method to extract the username (email) from JWT Token
//    public String extractUsername(String token) {
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//        return claims.getSubject();
//    }
//}
