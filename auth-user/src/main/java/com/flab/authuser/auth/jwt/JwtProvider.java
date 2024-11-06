package com.flab.authuser.auth.jwt;

import com.flab.authuser.auth.jwt.domain.RefreshToken;
import com.flab.authuser.auth.jwt.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {
    public static final long ACCESSTOKEN_TIME = 1000 * 60 * 30; // 30분
    public static final long REFRESHTOKEN_TIME = 1000 * 60 * 60 * 24; // 1일
    public static final String ACCESS_PREFIX_STRING = "Bearer ";
    public static final String ACCESS_HEADER_STRING = "Authorization";
    public static final String REFRESH_HEADER_STRING = "RefreshToken";


    private static String key;
    private static String keyBase64Encoded; // properties에 정의된 값
    private static SecretKey signingKey;

    private final RefreshTokenRepository refreshTokenRepository;

    public JwtProvider(@Value("${jwt.secret_key}") String keyParam,
                       RefreshTokenRepository refreshTokenRepository) {
        key = keyParam;
        signingKey = Keys.hmacShaKeyFor(key.getBytes());
        this.refreshTokenRepository = refreshTokenRepository;

    }

    public String getKey() {
        return key;
    }

    //==SigningKey==//
    public static SecretKey getSigningKey() {
        return signingKey;
    }


    public String createAccessToken(Long userId, String nickname, Boolean isAdmin) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("userId", userId);
        claims.put("nickname", nickname);
        claims.put("isAdmin", Boolean.toString(isAdmin));

        Date expiration = new Date(System.currentTimeMillis() + ACCESSTOKEN_TIME);
        System.out.println("expiration = " + expiration);

        String accessToken = ACCESS_PREFIX_STRING + Jwts.builder()
                .subject(Long.toString(userId))
                .claims(claims)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();

        // 액세스 토큰을 DB에 저장

        return accessToken;
    }

    /**
     * //질문 : 외부에서 호출될 수 없습니다. -> 이런게 필요한 이유가 뭔지? 좋은 코딩 인가요?
     * @return 리프레시 토큰
     */
    public String createRefreshToken(Long userId) {
        Date expiration = new Date(System.currentTimeMillis() + REFRESHTOKEN_TIME);

        String refreshTokenValue = Jwts.builder()
                .subject(Long.toString(userId))
                .claim("userId", userId)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();

        // 리프레시 토큰을 DB에 저장
        refreshTokenRepository.deleteByUserId(userId);
        refreshTokenRepository.save(new RefreshToken(refreshTokenValue, userId, expiration));

        return refreshTokenValue;
    }

    /**
     * 토큰의 클레임들을 가져옵니다.
     * @param token 액세스 또는 리프레시 토큰
     * @return 해당 토큰의 클레임들. parse에 실패할 경우 null을 반환합니다.
     */
    public Jws<Claims> parseClaims(String token) {
        Jws<Claims> claimsJws;
        try {

            claimsJws = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);

        } catch(ExpiredJwtException ex) {
            return null; // 만료되었음
        } catch(JwtException ex) {
            throw new IllegalArgumentException("잘못된 토큰입니다.");
        }

        return claimsJws;
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return parseClaims(token).getPayload().getSubject();
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token) {
        return (verifySignature(token) && !isTokenExpired(token));
    }


    public static boolean verifySignature(String token) {

        String[] parts = token.split("\\."); // Split by the period character

        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid JWT token format.");
        }

        try {
            // part1 is header, part2 is payload
            String dataToSign = parts[0] + "." + parts[1];
            SecretKey keySpec = getSigningKey();

            Mac mac = Mac.getInstance(keySpec.getAlgorithm());
            mac.init(keySpec);

            byte[] computedSignatureBytes = mac.doFinal(dataToSign.getBytes(StandardCharsets.UTF_8));
            String newlyComputedSignature = Base64.getUrlEncoder().withoutPadding().encodeToString(computedSignatureBytes);

            // Compare the computed signature with the provided signature
            return newlyComputedSignature.equals(parts[2]);
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify JWT signature", e);
        }
    }



}
