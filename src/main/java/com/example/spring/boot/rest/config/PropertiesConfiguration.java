package com.example.spring.boot.rest.config;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.example.spring.boot.rest.db.connector.DmDatabaseConnector;

/**
 * @author mlahariya
 * @version 1.0, Dec 2016
 */

@Configuration
@EnableTransactionManagement
public class PropertiesConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(PropertiesConfiguration.class);

    @Autowired
    private Environment env;

    @Autowired
    private ResourceLoader resourceLoader;

    @Bean(name = "MessageSource")
    public ResourceBundleMessageSource messageSource() {

        ResourceBundleMessageSource obj = new ResourceBundleMessageSource();
        obj.setBasenames("bundles/Messages");
        return obj;
    }

    @Bean(name = { "dmDataSource" })
    @ConfigurationProperties(prefix = "datasource.dm")
    public DataSource dmDataSource() {

        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dmJdbcTemplate")
    public JdbcOperations dmJdbcTemplate(@Qualifier("dmDataSource") DataSource ds) {

        LOG.info("\n\n\n\n jdbcTeamplatedm BEAN " + ds + " \n\n\n");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        return jdbcTemplate;
    }

    @Bean(name = "dmDao")
    @Autowired
    public DmDatabaseConnector dmDao(@Qualifier("dmJdbcTemplate") JdbcOperations jop) {

        DmDatabaseConnector obj = new DmDatabaseConnector();
        obj.setJdbcTemplate(jop);
        try {
            obj.getJdbcTemplate().execute("select 1 from dual");
            LOG.info("DmDatabaseConnector Database sucessfully connected. ");
        } catch (Exception e) {
            LOG.error(" Error while connecting to DmDatabaseConnector.", e);
        }
        return obj;
    }

    protected Integer propInt(String property, int defaultVal) {
        return toInteger(env.getProperty(property), defaultVal);
    }

    protected Integer toInteger(String val, int defaultVal) {

        if (this.isNull(val)) {
            return defaultVal;
        } else
            return toInteger(val);
    }

    protected Integer toInteger(String val) {
        return Integer.valueOf(val);
    }

    private boolean isNull(String str) {
        return !isNotNull(str);
    }

    private boolean isNotNull(String str) {
        return StringUtils.isNotBlank(str);
    }

}
