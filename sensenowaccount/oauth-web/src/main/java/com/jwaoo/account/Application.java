package com.jwaoo.account;

import com.alibaba.fastjson.JSONObject;
import com.jwaoo.account.model.Account;
import com.jwaoo.account.utils.GenderEnum;
import com.jwaoo.account.web.rest.dto.UserDTO;
import com.jwaoo.common.core.config.Global;
import com.jwaoo.common.core.geoip.GeoIpUtils;
import com.jwaoo.common.core.mongo.MongoDBUtil;
import com.jwaoo.common.core.redis.JedisManager;
import com.jwaoo.common.core.utils.BeanMapDozer;
import com.jwaoo.common.security.dto.ClientDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import java.net.UnknownHostException;

@ComponentScan(basePackages="com.jwaoo")
@SpringBootApplication
@ImportResource({"classpath:dubbo.xml"})
public class Application extends SpringBootServletInitializer {

    private final static Logger log = LoggerFactory.getLogger(Application.class);


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class).showBanner(false);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        WebApplicationContext rootAppContext = createRootApplicationContext(servletContext);
        if (rootAppContext != null) {
            servletContext.addListener(new ContextLoaderListener(rootAppContext) {
                @Override
                public void contextInitialized(ServletContextEvent event) {
                    // no-op because the application context is already initialized
                    log.info("redis ping: " + JedisManager.getInstance().ping());
                    String zkcfg = Global.getConfigGlobalCfg("mongcfg:oa.dbname", "");
                    log.info("zkcfg: " + zkcfg);
                    boolean collectionExist = MongoDBUtil.collectionExists(zkcfg, ClientDto.TBL_CLIENT);
                    log.info("mongo is exists: "+ collectionExist);
                    log.info("geoip city: " + GeoIpUtils.getInstance().getCityByIp("180.169.167.166"));
                    Account acct = new Account();
                    acct.setNickname("Jack");
                    acct.setGender(GenderEnum.FEMALE.getValue());
                    log.info(JSONObject.toJSON(BeanMapDozer.map(acct, UserDTO.class)).toString());
                }
            });
        }
        else {
            log.debug("No ContextLoaderListener registered, as createRootApplicationContext() did not return an application context");
        }
    }

    /**
     * Main method, used to run the application.
     * @throws java.net.UnknownHostException
     */
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(Application.class);
        app.setShowBanner(false);
        app.run(args);
    }

}
