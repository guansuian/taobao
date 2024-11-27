package org.example.taobao.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 关岁安
 */
public class JWTUtil {

    private static final String key = "zhengmeilingwozhendehaoxihuanniwoshizhendehenxiangniyoudeshihouwoyebuzhidaowodeyoudiandanshiwojiushixianghenizaiyiqinishuonibuxiangzaizheliwoyebuxiangzaizhelidantianheidetaikuaigengbenglaibujiwoainikeshiguanxibainchengmeiguanxi";


    //生成短token
    public static String createShortToken(Integer id,String username){
        Map<String , Object> claims = new HashMap<>();
        claims.put("id",id);
        claims.put("username",username);
        claims.put("whichToken","short");
        return JWT.create()
                .withClaim("user",claims)
                //代表需要12个小时 1000*60*60*12
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000*60*60*12))
                .sign(Algorithm.HMAC256(key));
    }
    //生成长token
    public static String createLongToken(Integer id,String username){
        Map<String,Object> claims = new HashMap<>();
        claims.put("id",id);
        claims.put("username",username);
        claims.put("whichToken","long");
        return JWT.create()
                .withClaim("user",claims)
                //代表需要6天 1000*60*60*24*6
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000*60*60*24*6))
                .sign(Algorithm.HMAC256(key));
    }
    //验证token
    public static Map<String,Object> parseToken(String token){
        return JWT.require(Algorithm.HMAC256(key))
                .build()
                .verify(token)
                .getClaim("user")
                .asMap();
    }


}
