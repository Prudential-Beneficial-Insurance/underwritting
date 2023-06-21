package cm.pruben.underwritting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/*

Cette classe ne fonctionne pas et génère l'erreur suivante :
nested exception is java.lang.IllegalStateException: No supported DataSource type found

 */


@Configuration
public class DataSourceConfig {
/*
    @Primary
    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.ibm.as400.access.AS400JDBCDriver");
        dataSourceBuilder.url("jdbc:as400://10.219.98.10/OLASCAPROD");
        dataSourceBuilder.username("STEKAMUSER");
        dataSourceBuilder.password("DSI14PX79R");
        return dataSourceBuilder.build(); portefeuilleagent
    }*/

    /*@Bean
    public Connection getAS400Connection() throws ClassNotFoundException, SQLException {
        String DRIVER = "com.ibm.as400.access.AS400JDBCDriver";//props.getProperty("driver");
        String URL = "jdbc:as400://10.219.98.10/OLASCAPROD";//OLASCAPROD
        Class.forName(DRIVER);
        return DriverManager.getConnection(URL, "STEKAMUSER", "DSI18PX79R");
    }*/

}
