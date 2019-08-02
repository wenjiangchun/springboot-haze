package com.haze.spatial.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "haze.spatial")
public class SpatialProperties {

    private String name;

    private EPSG epsg;

    private RemoteGeoServer geoServer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EPSG getEpsg() {
        return epsg;
    }

    public void setEpsg(EPSG epsg) {
        this.epsg = epsg;
    }

    public RemoteGeoServer getGeoServer() {
        return geoServer;
    }

    public void setGeoServer(RemoteGeoServer geoServer) {
        this.geoServer = geoServer;
    }

    public static class EPSG {
       private String dbType;
       private String serverName;
       private String databaseName;
       private String databasePort;
       private String schema;
       private String userName;
       private String password;

       public String getDbType() {
           return dbType;
       }

       public void setDbType(String dbType) {
           this.dbType = dbType;
       }

       public String getServerName() {
           return serverName;
       }

       public void setServerName(String serverName) {
           this.serverName = serverName;
       }

       public String getDatabaseName() {
           return databaseName;
       }

       public void setDatabaseName(String databaseName) {
           this.databaseName = databaseName;
       }

       public String getDatabasePort() {
           return databasePort;
       }

       public void setDatabasePort(String databasePort) {
           this.databasePort = databasePort;
       }

       public String getSchema() {
           return schema;
       }

       public void setSchema(String schema) {
           this.schema = schema;
       }

       public String getUserName() {
           return userName;
       }

       public void setUserName(String userName) {
           this.userName = userName;
       }

       public String getPassword() {
           return password;
       }

       public void setPassword(String password) {
           this.password = password;
       }
   }
   public static class RemoteGeoServer {

       private String url;
       private String userName;
       private String password;

       public String getUrl() {
           return url;
       }

       public void setUrl(String url) {
           this.url = url;
       }

       public String getUserName() {
           return userName;
       }

       public void setUserName(String userName) {
           this.userName = userName;
       }

       public String getPassword() {
           return password;
       }

       public void setPassword(String password) {
           this.password = password;
       }
   }
}
