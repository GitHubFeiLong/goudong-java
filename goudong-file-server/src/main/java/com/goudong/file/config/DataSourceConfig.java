// package com.goudong.file.config;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
// import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
// import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.boot.jdbc.DataSourceBuilder;
// import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Primary;
// import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
// import org.springframework.orm.jpa.JpaTransactionManager;
// import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
// import org.springframework.transaction.PlatformTransactionManager;
// import org.springframework.transaction.annotation.EnableTransactionManagement;
//
// import javax.persistence.EntityManager;
// import javax.sql.DataSource;
// import java.util.Map;
//
// /**
//  * 类描述：
//  * 数据源配置
//  * @author cfl
//  * @version 1.0
//  * @date 2022/11/7 16:02
//  */
// // @Configuration
// @Deprecated
// public class DataSourceConfig {
//
//     @Primary
//     @Bean
//     @ConfigurationProperties(prefix = "spring.datasource.file")
//     public DataSource fileDataSource() {
//         return DataSourceBuilder.create().build();
//     }
//
//
//     @Bean
//     @ConfigurationProperties(prefix = "spring.datasource.user")
//     public DataSource userDataSource() {
//         return DataSourceBuilder.create().build();
//     }
//
//     /**
//      * 类描述：
//      * file数据源配置
//      * @author cfl
//      * @version 1.0
//      * @date 2022/11/7 16:08
//      */
//     @Configuration
//     @EnableTransactionManagement
//     @EnableJpaRepositories(
//             entityManagerFactoryRef="entityManagerFactoryFile",
//             transactionManagerRef="transactionManagerFile",
//             basePackages = {"com.goudong.file.repository.file"}
//     )
//     public static class FileDataSourceConfig {
//         @Autowired
//         @Qualifier("fileDataSource")
//         private DataSource fileDataSource;
//
//         @Autowired
//         private JpaProperties jpaProperties;
//         @Autowired
//         private HibernateProperties hibernateProperties;
//
//         private Map<String, Object> getVendorProperties() {
//             return hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings());
//         }
//
//         @Primary
//         @Bean(name = "entityManagerFile")
//         public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
//             return entityManagerFactoryPrimary(builder).getObject().createEntityManager();
//         }
//
//         @Primary
//         @Bean(name = "entityManagerFactoryFile")
//         public LocalContainerEntityManagerFactoryBean entityManagerFactoryPrimary (EntityManagerFactoryBuilder builder) {
//             return builder.dataSource(fileDataSource)
//                     .packages("com.goudong.file.po.file") //设置实体类所在位置
//                     .persistenceUnit("filePersistenceUnit")
//                     .properties(getVendorProperties())
//                     .build();
//         }
//
//         @Primary
//         @Bean(name = "transactionManagerFile")
//         public PlatformTransactionManager transactionManagerPrimary(EntityManagerFactoryBuilder builder) {
//             return new JpaTransactionManager(entityManagerFactoryPrimary(builder).getObject());
//         }
//     }
//
//     /**
//      * 类描述：
//      * 用户数据源
//      * @author cfl
//      * @version 1.0
//      * @date 2022/11/7 17:48
//      */
//     @Configuration
//     @EnableTransactionManagement
//     @EnableJpaRepositories(
//             entityManagerFactoryRef="entityManagerFactoryUser",
//             transactionManagerRef="transactionManagerUser",
//             basePackages= { "com.goudong.file.repository.user" }) //设置Repository所在位置
//     public static class UserDataSourceConfig {
//
//         @Autowired
//         @Qualifier("userDataSource")
//         private DataSource userDataSource;
//
//         @Autowired
//         private JpaProperties jpaProperties;
//         @Autowired
//         private HibernateProperties hibernateProperties;
//
//         private Map<String, Object> getVendorProperties() {
//             return hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings());
//         }
//
//         @Bean(name = "entityManagerUser")
//         public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
//             return entityManagerFactorySecondary(builder).getObject().createEntityManager();
//         }
//
//         @Bean(name = "entityManagerFactoryUser")
//         public LocalContainerEntityManagerFactoryBean entityManagerFactorySecondary (EntityManagerFactoryBuilder builder) {
//             return builder.dataSource(userDataSource)
//                     .packages("com.goudong.file.po.user") //设置实体类所在位置
//                     .persistenceUnit("userPersistenceUnit")
//                     .properties(getVendorProperties())
//                     .build();
//         }
//
//         @Bean(name = "transactionManagerUser")
//         public PlatformTransactionManager transactionManagerSecondary(EntityManagerFactoryBuilder builder) {
//             return new JpaTransactionManager(entityManagerFactorySecondary(builder).getObject());
//         }
//     }
// }
