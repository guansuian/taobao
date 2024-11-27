package org.example.taobao;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.example.taobao.utils.JWTUtil.createShortToken;
import static org.example.taobao.utils.JWTUtil.parseToken;

@SpringBootTest
class TaobaoApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(parseToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoxLCJ3aGljaFRva2VuIjoic2hvcnQiLCJ1c2VybmFtZSI6IuWFs-WygeWuiSJ9LCJleHAiOjE3MjYzNjc4MTd9.DbDX-w0gDRzNLsTMcc9e4txka7UVopic1bvm5E4hrd4"));
    }
    @Test
   void createToken(){
       Integer a = 1000000;
       Integer b = 2000000;
       Integer c = 1000000;
       System.out.println(a+c==b);
       System.out.println( System.identityHashCode(b));
       System.out.println( System.identityHashCode(a+c));


   }

}
