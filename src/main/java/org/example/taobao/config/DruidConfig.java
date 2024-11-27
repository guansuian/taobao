//package org.example.taobao.config;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import com.alibaba.druid.support.http.StatViewServlet;
//import com.alibaba.druid.support.http.WebStatFilter;
//import jakarta.servlet.Filter;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//import java.util.HashMap;
//
///**
// * @author zhhao
// * @date 2024-01-30 12:02
// * @describe
// */
//@Configuration
//public class DruidConfig {
//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource")//和配置文件绑定
//    public DataSource druidDataSource(){
//        return new DruidDataSource();
//    }
//
//    //获取后台监控
//    @Bean
//    //由于SpringBoot默认是以jar包的方式启动嵌入式的Servlet容器来启动SpringBoot的web应用，没有web.xml文件。
//    // 所以想用使用Servlet功能，就必须要借用SpringBoot提供的ServletRegistrationBean接口。
//    public ServletRegistrationBean servletRegistration(){
//        //StatViewServlet用于展示Druid的统计信息
//        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet(),"/druid/*");
//
//        //设置后台登录的账户和密码
//        HashMap<String, String> initParameters = new HashMap<>();
//
//		// IP白名单
//        initParameters.put("allow","");
//        // IP黑名单(共同存在时，deny优先于allow)
//        initParameters.put("deny","");
//
//        //增加配置 登录的key是固定的
//        initParameters.put("loginUsername","admin");
//        initParameters.put("loginPassword","123456");
//
//        //设置谁可以访问
//        //任何人都可以访问
//        initParameters.put("allow","");
//
//        //设置初始化参数
//        bean.setInitParameters(initParameters);
//
//        return bean;
//    }
//
//    /**
//     * 用于配置Web和Druid数据源之间的管理关联监控统计
//     * @return
//     */
//    @Bean
//    public FilterRegistrationBean webStartFilter(){
//        FilterRegistrationBean bean = new FilterRegistrationBean();
//        bean.setFilter((Filter) new WebStatFilter());
//
//        //可以过滤的请求
//        HashMap<String, String> initParameters = new HashMap<>();
//
//        //排除一些不必要的url
//        initParameters.put("exclusions","*.js,*.css,/druid/*");
//        //设置初始化参数
//        bean.setInitParameters(initParameters);
//        return bean;
//    }
//
//}
