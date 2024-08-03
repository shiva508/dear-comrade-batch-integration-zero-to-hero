package com.comrade.config;

import javax.sql.DataSource;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class StudentpoolDatasourceConfig {
	@Primary
	@Bean(name = "datasourceOld")
	@ConfigurationProperties(prefix = "spring.datasourceOld")
	public DataSource datasourceOld() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "datasourceLatest")
	@ConfigurationProperties(prefix = "spring.datasourceLatest")
	public DataSource datasourceLatest() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name = "datasourceOldEntityManagerFactory")
	@Primary
	public EntityManagerFactory datasourceOldEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean beanOld=new LocalContainerEntityManagerFactoryBean();
		beanOld.setDataSource(datasourceOld());
		beanOld.setPackagesToScan("com.comrade.entity.db.old");
		beanOld.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		beanOld.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		beanOld.afterPropertiesSet();
		return beanOld.getObject();
	}

	@Bean(name = "datasourceLatestEntityManagerFactory")
	public EntityManagerFactory datasourceLatestEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean beanLatest=new LocalContainerEntityManagerFactoryBean();
		beanLatest.setDataSource(datasourceLatest());
		beanLatest.setPackagesToScan("com.comrade.entity.db.latest");
		beanLatest.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		beanLatest.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		beanLatest.afterPropertiesSet();
		return beanLatest.getObject();
	}
	
	@Primary
	@Bean
	public JpaTransactionManager jpaTransactionManager() {
		 JpaTransactionManager jpaTransactionManager=new JpaTransactionManager();
		 jpaTransactionManager.setDataSource(datasourceOld());
		 jpaTransactionManager.setEntityManagerFactory(datasourceOldEntityManagerFactory());
		 return jpaTransactionManager;
	}
	
	
}
