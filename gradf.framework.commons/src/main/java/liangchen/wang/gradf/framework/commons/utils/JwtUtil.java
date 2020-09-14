package liangchen.wang.gradf.framework.commons.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.*;
import io.jsonwebtoken.impl.compression.DefaultCompressionCodecResolver;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import io.jsonwebtoken.lang.Strings;
import liangchen.wang.gradf.framework.commons.exception.InfoException;
import liangchen.wang.gradf.framework.commons.validator.Assert;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.Map;

public enum JwtUtil {
    /**
     * instance
     */
    INSTANCE;

    private ObjectMapper objectMapper = new ObjectMapper();
    private CompressionCodecResolver compressionCodecResolver = new DefaultCompressionCodecResolver();
    private Clock clock = DefaultClock.INSTANCE;

    public Jws<Claims> parse(String jwt) {
        Assert.INSTANCE.notBlank(jwt, "jwt字符串不能为空");
        String[] split = jwt.split("\\.");
        Assert.INSTANCE.isTrue(split.length == 3, "jwt字符串格式错误");
        String base64UrlEncodedHeader = split[0];
        String base64UrlEncodedPayload = split[1];
        String base64UrlEncodedDigest = split[2];

        // =============== Header =================
        String origValue = TextCodec.BASE64URL.decodeToString(base64UrlEncodedHeader);
        Map<String, Object> m = readValue(origValue);
        Header header = new DefaultJwsHeader(m);
        // =============== Body =================
        CompressionCodec compressionCodec = compressionCodecResolver.resolveCompressionCodec(header);
        String payload;
        if (compressionCodec != null) {
            byte[] decompressed = compressionCodec.decompress(TextCodec.BASE64URL.decode(base64UrlEncodedPayload));
            payload = new String(decompressed, Strings.UTF_8);
        } else {
            payload = TextCodec.BASE64URL.decodeToString(base64UrlEncodedPayload);
        }
        Claims claims = null;
        // likely to be json, parse it:
        if (payload.charAt(0) == '{' && payload.charAt(payload.length() - 1) == '}') {
            Map<String, Object> claimsMap = readValue(payload);
            claims = new DefaultClaims(claimsMap);
        }
        claims.put("header", base64UrlEncodedHeader);
        claims.put("payload", base64UrlEncodedPayload);
        return new DefaultJws<>((JwsHeader) header, claims, base64UrlEncodedDigest);
    }

    public void validate(Jws<Claims> jws, String keyString) {
        validate(jws, keyString, 0);
    }

    public void validate(Jws<Claims> jws, String keyString, int allowedClockSkewMillis) {
        JwsHeader jwsHeader = jws.getHeader();
        Claims claims = jws.getBody();

        //校验是否过期 考虑时钟偏移
        Date expiration = claims.getExpiration();
        long nowTime = clock.now().getTime();
        if (null != expiration) {
            ;
            long maxTime = nowTime - allowedClockSkewMillis;
            Date max = new Date(maxTime);
            if (max.after(expiration)) {
                throw new InfoException("Jwt 过期");
            }
        }

        Date notBefore = claims.getNotBefore();
        if (null != notBefore) {
            long minTime = nowTime + allowedClockSkewMillis;
            Date min = new Date(minTime);
            if (min.before(notBefore)) {
                throw new InfoException("Jwt 失效时间错误");
            }
        }


        //校验签名
        String alg = jwsHeader.getAlgorithm();
        SignatureAlgorithm algorithm = SignatureAlgorithm.forName(alg);
        byte[] keyBytes = TextCodec.BASE64.decode(keyString);
        Key key = new SecretKeySpec(keyBytes, algorithm.getJcaName());
        DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(algorithm, key);
        String jwtWithoutSignature = claims.get("header") + "." + claims.get("payload");
        if (!validator.isValid(jwtWithoutSignature, jws.getSignature())) {
            throw new InfoException("Jwt key 错误");
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> readValue(String val) {
        try {
            return objectMapper.readValue(val, Map.class);
        } catch (IOException e) {
            throw new MalformedJwtException("Unable to read JSON value: " + val, e);
        }
    }

}
