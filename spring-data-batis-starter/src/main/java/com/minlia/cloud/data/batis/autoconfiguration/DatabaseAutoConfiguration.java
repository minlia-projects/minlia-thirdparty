package com.minlia.cloud.data.batis.autoconfiguration;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mybatis.domains.AuditDateAware;
import org.springframework.data.mybatis.replication.transaction.ReadWriteManagedTransactionFactory;
import org.springframework.data.mybatis.repository.config.EnableMybatisRepositories;
import org.springframework.data.mybatis.support.SqlSessionFactoryBean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


@Configuration
@EnableConfigurationProperties(MybatisProperties.class)

@EntityScan(basePackages = {".**.domain", ".**.model"})
@EnableMybatisRepositories(value = {".**.dao"}, mapperLocations = {"classpath*:**/dao/*Dao.xml", "classpath*:**/dao/*DaoCustom.xml"}, transactionManagerRef = "batisTransactionManager")
@Slf4j
public class DatabaseAutoConfiguration implements ResourceLoaderAware {

    private ResourceLoader resourceLoader;


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


    //defined in rebecca project
//    @Bean
//    public AuditorAware<String> auditorAware() {
//        return new AuditorAware<String>() {
//            @Override
//            public String getCurrentAuditor() {
//                return DefaultGuidHolder.getGuid();
//            }
//        };
//    }

    @Bean
    @ConditionalOnMissingBean
    public AuditDateAware<LocalDateTime> auditDateAware() {
        return new AuditDateAware<LocalDateTime>() {
            @Override
            public LocalDateTime getCurrentDate() {
                return LocalDateTime.now();
            }
        };
    }


    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

}
