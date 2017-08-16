package com.minlia.cloud.data.batis.autoconfiguration;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.data.mybatis.domains.AuditDateAware;
import org.springframework.data.mybatis.replication.transaction.ReadWriteManagedTransactionFactory;
import org.springframework.data.mybatis.repository.config.EnableMybatisRepositories;
import org.springframework.data.mybatis.repository.dialect.Dialect;
import org.springframework.data.mybatis.repository.dialect.MySQLDialect;
import org.springframework.data.mybatis.support.HierarchicalResourceLoader;
import org.springframework.data.mybatis.support.HierarchicalSqlSessionFactoryBean;
import org.springframework.data.mybatis.support.SqlSessionFactoryBean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Configuration
@EnableConfigurationProperties(MybatisProperties.class)

@EntityScan(basePackages = {".**.domain", ".**.model"})
@EnableMybatisRepositories(value = {".**.dao"}, mapperLocations = {"classpath*:**/dao/*Dao.xml","classpath*:**/dao/*DaoCustom.xml"}, transactionManagerRef = "batisTransactionManager" )
@Slf4j
public class DatabaseAutoConfiguration implements ResourceLoaderAware {

    private ResourceLoader    resourceLoader;

//    @Autowired
//    private MybatisProperties properties;
//
//
//    private HierarchicalResourceLoader getLoader(
//            Class<? extends Object> baseClazz,
//            Class<? extends Object> clazz) throws Throwable
//    {
//        HierarchicalResourceLoader loader = new HierarchicalResourceLoader();
//        loader.setDialectBaseClass(baseClazz.getName());
//        loader.setDialectClass(clazz.getName());
//        loader.afterPropertiesSet();
//        return loader;
//    }
//
//
//    @Bean
//    public HierarchicalResourceLoader hierarchicalResourceLoader() throws Throwable{
//        return getLoader(Dialect.class,MySQLDialect.class);
//    }
//    @Bean
////    @ConditionalOnMissingBean
//    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws Throwable {
//        HierarchicalSqlSessionFactoryBean factoryBean = new HierarchicalSqlSessionFactoryBean();
//        factoryBean.setDataSource(dataSource);
//        factoryBean.setVfs(SpringBootVFS.class);
//        factoryBean.setResourceLoader(hierarchicalResourceLoader());
//
//
//        if (null != properties.getBeforeMapperLocations() && properties.getBeforeMapperLocations().length > 0) {
//            Set<Resource> set = new HashSet<Resource>();
//            for (String s : properties.getBeforeMapperLocations()) {
//                Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(this.resourceLoader).getResources(s);
//                if (null != resources && resources.length > 0) {
//                    set.addAll(Arrays.asList(resources));
//                }
//            }
//            if (!set.isEmpty()) {
//                factoryBean.setMapperLocations(set.toArray(new Resource[set.size()]));
//            }
//        }
//
//        String handlers = "ir.boot.autoconfigure.data.mybatis.handlers";
//        if (null != properties.getHandlerPackages() && properties.getHandlerPackages().length > 0) {
//            for (String s : properties.getHandlerPackages()) {
//                if (StringUtils.isEmpty(s)) {
//                    continue;
//                }
//                handlers += "," + s;
//            }
//
//        }
//
//        factoryBean.setTypeHandlersPackage(handlers);
//
//        org.apache.ibatis.session.Configuration configuration = factoryBean.getObject().getConfiguration();
//        configuration.setMapUnderscoreToCamelCase(true);
//        if (null != properties.getDefaultScriptingLanguage()) {
//            configuration.setDefaultScriptingLanguage(properties.getDefaultScriptingLanguage());
//        }
//        return factoryBean;
//    }


    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setVfs(SpringBootVFS.class);
        factoryBean.setDataSource(dataSource);
        ReadWriteManagedTransactionFactory factory = new ReadWriteManagedTransactionFactory();
        factoryBean.setTransactionFactory(factory);

        return factoryBean;
    }


    @Bean
    public PlatformTransactionManager batisTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

//    @Bean
//    public AuditorAware<Long> auditorAware() {
//        return new AuditorAware<Long>() {
//            @Override
//            public Long getCurrentAuditor() {
//                return 1001L;
//            }
//        };
//    }

    @Bean
    @ConditionalOnMissingBean
    public AuditDateAware<Date> auditDateAware() {
        return new AuditDateAware<Date>() {
            @Override
            public Date getCurrentDate() {
                return new Date();
            }
        };
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

}
