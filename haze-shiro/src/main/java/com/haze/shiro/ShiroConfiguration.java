package com.haze.shiro;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/config/shiro/application.properties")
public class ShiroConfiguration {

    @Bean
    public Realm realm() {
        return new ShiroRealm();
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        /*chainDefinition.addPathDefinition("/login", "anon");
        chainDefinition.addPathDefinition("/res/**", "anon");
        chainDefinition.addPathDefinition("/resources/**", "anon");
        chainDefinition.addPathDefinition("/k/**", "authc,perms[mvn:install]");
        chainDefinition.addPathDefinition("/kettle/**", "anon");
        chainDefinition.addPathDefinition("/swagger-ui.html", "anon");
        chainDefinition.addPathDefinition("/swagger-resources/**", "anon");
        chainDefinition.addPathDefinition("/swagger-resources", "anon");
        chainDefinition.addPathDefinition("/v2/**", "anon");
        chainDefinition.addPathDefinition("/crsf", "anon");
        chainDefinition.addPathDefinition("/webjars/**", "anon");*/
        chainDefinition.addPathDefinition("/**", "anon");
        chainDefinition.addPathDefinition("/", "anon");
        chainDefinition.addPathDefinitions(shiroFilterChainDBDefinition().getFilterChainDefinitions());
        return chainDefinition;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator=new DefaultAdvisorAutoProxyCreator();
        /**
         * setUsePrefix(false)用于解决一个奇怪的bug。在引入spring aop的情况下。
         * 在@Controller注解的类的方法中加入@RequiresRole等shiro注解，会导致该方法无法映射请求，导致返回404。
         * 加入这项配置能解决这个bug
         */
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /*@Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/");
        shiroFilterFactoryBean.setUnauthorizedUrl("/");
        //shiroFilterFactoryBean.setFilterChainDefinitionMap(chainDefinitionSectionMetaSource().getObject());
        return shiroFilterFactoryBean;
    }*/

    @Bean
    public ShiroFilterChainDBDefinition shiroFilterChainDBDefinition() {
        return new ShiroFilterChainDBDefinition();
    }
}
