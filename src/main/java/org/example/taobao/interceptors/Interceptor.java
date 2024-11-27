package org.example.taobao.interceptors;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.ExpiredJwtException;


import static org.example.taobao.utils.JWTUtil.*;

/**
 * @author 关岁安
 * 这个就是相当于在之前servlet里面的拦截器，当接入HandlerInterceptor这个接口的时候，
 * 所有的请求都会经过Interceptor这个类 然后执行preHandle里面的方法。
 * 在后端的拦截器一共有4个点
 * 1.短token没有过期，没有入侵，此时就直接让这个请求执行，返回true就代表可以执行了
 * 2.短token已经过期了，或者已经被入侵了，此时发信息给前端，让前端发送长token过来
 * 3.如果长token没有过期就重新生成一个长token和一个短token过去，让前端将长token和短token存在本地
 * 4.如果长token已经过期了，那么就让用户重新进行登录

 * 测试要求：
 * 1.当短token没有过期的时候，返回数据给前端，在Apifox里面可以看到返回的数据
 * 2.当短token已经过期了，要求前端返回一个长token过来，发给前端的信息必须是以状态码进行发送(401)，在Apifox里面可以看返回的状态码为401
 * 3.当接收到长token后对其进行检验，如果没有过期或者入侵就在后端重新生成长短token发给前端，在Apifox里面可以看到长短token和状态码403
 * 4.如果长token已经过期了，后端要求发送状态码为403，让前端返回登录界面，在Apifox里面可以看到状态码为404

 * 基础知识：在springboot项目中，接了HandlerInterceptor接口的类，返回true就是执行这个请求，相当于chain.doFilter(request, response);
 * 如果返回的是一个false，那么就需要返回对应的状态码
 */
@Component
public class Interceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            System.out.println("OPTIONS请求，放行");
            return true;
        }
       //这个Authorization是专门用来放token的地方
        Map<String,Object> claims = null;
        String token = request.getHeader("Authorization");
        System.out.println(token);
        if(token == null || "undefined".equals(token)){
            response.setStatus(404);
            return true;
        }
        try {
            claims = parseToken(token);
            System.out.println(claims);
//            Map<String, Object> user = (Map<String, Object>) claims.get("user");
            String whichToken = (String) claims.get("whichToken");
            if("short".equals(whichToken)){
                return true;
            }else if("long".equals(whichToken)){
                //没有报异常就说明，长token没有过期 那么发送状态码为403，并发送长短token过去
                Integer id = (Integer) claims.get("id");
                String username = (String) claims.get("username");
                String shortToken = createShortToken(id,username);
                String longToken = createLongToken(id,username);
                Map<String, String> doubleToken = new HashMap<>();
                System.out.println("当短token过期的时候返回的长短token是否一样");
                System.out.println("短：" + shortToken);
                System.out.println("长：" + longToken);
                doubleToken.put("shortToken",shortToken);
                doubleToken.put("longToken",longToken);
                //状态码返回403就要求前端将长短token存进本地
                response.setStatus(403);

                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(doubleToken);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);

                return false;
            }else{
                response.setStatus(404);
                return false;
            }
        }
        catch (TokenExpiredException e){

            try {
                // 尝试解码 token 以获取负载
                DecodedJWT decodedJWT = JWT.decode(token);
                Map<String, Object> userClaims = decodedJWT.getClaim("user").asMap();
                System.out.println(userClaims);
                String whichToken = (String) userClaims.get("whichToken");
                if("short".equals(whichToken)){
                    System.out.println("在TokenExpiredException这个异常中 短token已经过期了");
                    response.setStatus(401);
                    return false;
                }else if("long".equals(whichToken)){
                    System.out.println("在TokenExpiredException这个异常中 长token已经过期了");
                    response.setStatus(404);
                    return false;
                }
                return false;
            } catch (JWTDecodeException decodeException) {
                // 处理解码异常
                System.out.println("Token decoding failed: " + decodeException.getMessage());
                response.setStatus(404);
                return false;
            }
        }
    }
}
