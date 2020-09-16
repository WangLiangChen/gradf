package liangchen.wang.gradf.framework.web.jwts;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import liangchen.wang.gradf.framework.commons.utils.JwtUtil;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author LiangChen.Wang
 * Header.Payload.Signature
 */
public enum AccessTokenUtil {
    /**
     *
     */
    INSTANCE;
    public final static String DEFAULT_KEY = "crdf_43821_*(&^$%#!";

    public String create(AccessToken accessToken, String key) {
        Date date = new Date();
        Claims claims = new DefaultClaims();
        claims.setIssuedAt(date);
        claims.setExpiration(DateUtils.addDays(date, 7));
        claims.putAll(accessToken.getMap());
        String compactJws = Jwts.builder().setClaims(claims).compressWith(CompressionCodecs.DEFLATE).signWith(SignatureAlgorithm.HS512, key).compact();
        return compactJws;
    }

    public AccessToken parse(String jwt, String key) {
        Jws<Claims> parse = JwtUtil.INSTANCE.parse(jwt);
        JwtUtil.INSTANCE.validate(parse, key);
        Claims claims = parse.getBody();
        Set<Entry<String, Object>> entrySet = claims.entrySet();
        AccessToken accessToken = AccessToken.newInstance();
        entrySet.forEach(entity -> {
            accessToken.put(entity.getKey(), entity.getValue());
        });
        return accessToken;
    }

    public Jws<Claims> parse(String jwt) {
        Jws<Claims> parse = JwtUtil.INSTANCE.parse(jwt);
        return parse;
    }

    public void validate(Jws<Claims> jws, String key) {
        JwtUtil.INSTANCE.validate(jws, key);
    }
}
