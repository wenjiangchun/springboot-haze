package com.haze.spatial.shapefile;

import org.geotools.data.*;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCFeatureStore;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.crs.DefaultProjectedCRS;
import org.geotools.referencing.operation.DefaultCylindricalProjection;
import org.geotools.referencing.operation.projection.MapProjection;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.parameter.*;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.CartesianCS;
import org.opengis.referencing.cs.EllipsoidalCS;
import org.opengis.referencing.datum.GeodeticDatum;
import org.opengis.referencing.operation.OperationMethod;
import org.opengis.util.GenericName;
import org.opengis.util.ScopedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ShapeFileUtils {

    private static final Logger logger = LoggerFactory.getLogger(ShapeFileUtils.class);

    public static void importToDatabase(Map<String, Object> databaseParams, String filePath) {
        ShapefileDataStore fileDataStore = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                logger.error("文件不存在");
                throw new FileNotFoundException("文件不存在");
            }
            FileDataStoreFactorySpi factory = new ShapefileDataStoreFactory();
            //factory.createDataStore(file.toURI().toURL());
            fileDataStore = (ShapefileDataStore) factory.createDataStore(file.toURI().toURL());
            ;
            fileDataStore.setCharset(Charset.forName("UTF-8"));
            fileDataStore.setNamespaceURI(null);
            String typeName = fileDataStore.getTypeNames()[0];
           /* FeatureSource<SimpleFeatureType, SimpleFeature> source =
                    fileDataStore.getFeatureSource(typeName);*/
            ContentFeatureSource source = fileDataStore.getFeatureSource();
            Filter filter = Filter.INCLUDE;
            FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(filter);
            createTable(databaseParams, fileDataStore.getSchema(), collection);
            fileDataStore.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileDataStore != null) {
                fileDataStore.dispose();
            }
        }
    }

    private static void createTable(Map<String, Object> databaseParams, SimpleFeatureType schema, FeatureCollection<SimpleFeatureType, SimpleFeature> features) {
        Connection connection = null;
        Transaction transaction = null;
        JDBCDataStore dataStore = null;
        try {
            dataStore = (JDBCDataStore) DataStoreFinder.getDataStore(databaseParams);
            //if (dataStore.getSchema(schema.getName().toString()) == null) {
            SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
            List<AttributeDescriptor> attributeDescriptorList = schema.getAttributeDescriptors();
            List<AttributeDescriptor> attributeDescriptors = new ArrayList<>();
            GeometryDescriptor geometryDescriptor;
            System.out.println(schema.getCoordinateReferenceSystem().toWKT());
            String wkt = "GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4326\"]]";
            try {
                int srid = getSrid(dataStore, schema.getCoordinateReferenceSystem());
                //int espg = CRS.lookupEpsgCode(schema.getCoordinateReferenceSystem(), true);
                int espg = CRS.lookupEpsgCode(DefaultGeographicCRS.WGS84, true);
                CoordinateReferenceSystem referenceSystem = schema.getCoordinateReferenceSystem();
                referenceSystem.getCoordinateSystem().getName();
            } catch (FactoryException e) {
                e.printStackTrace();
            }
            for (AttributeDescriptor attributeDescriptor : attributeDescriptorList) {
                if (attributeDescriptor instanceof GeometryDescriptor) {
                    GeometryDescriptor gd = (GeometryDescriptor) attributeDescriptor;
                    geometryDescriptor = new GeometryDescriptor() {
                        @Override
                        public GeometryType getType() {
                            return gd.getType();
                        }

                        @Override
                        public CoordinateReferenceSystem getCoordinateReferenceSystem() {
                            //return schema.getCoordinateReferenceSystem();
                            return DefaultGeographicCRS.WGS84;
                        }

                        @Override
                        public String getLocalName() {
                            return gd.getLocalName();
                        }

                        @Override
                        public Object getDefaultValue() {
                            return gd.getDefaultValue();
                        }

                        @Override
                        public Name getName() {
                            return gd.getName();
                        }

                        @Override
                        public int getMinOccurs() {
                            return gd.getMinOccurs();
                        }

                        @Override
                        public int getMaxOccurs() {
                            return gd.getMaxOccurs();
                        }

                        @Override
                        public boolean isNillable() {
                            return gd.isNillable();
                        }

                        @Override
                        public Map<Object, Object> getUserData() {
                            Map<Object, Object> data = new HashMap<>();
                            data.putAll(gd.getUserData());
                            data.put(JDBCDataStore.JDBC_NATIVE_SRID, 4326);
                            return data;
                        }
                    };
                    attributeDescriptors.add(geometryDescriptor);
                } else {
                    attributeDescriptors.add(attributeDescriptor);
                }
            }
            builder.setAttributes(attributeDescriptors);
            builder.setName(schema.getName().toString());
            builder.setCRS(schema.getCoordinateReferenceSystem());
            dataStore.createSchema(builder.buildFeatureType());
            //dataStore.createSchema(schema);
            // }
            connection = dataStore.getDataSource().getConnection();
            transaction = dataStore.buildTransaction(connection);
            //插入数据
            SimpleFeatureSource simpleFeatureSource = dataStore.getFeatureSource(schema.getName().toString());
            if (simpleFeatureSource instanceof JDBCFeatureStore) {
                JDBCFeatureStore featureStore = (JDBCFeatureStore) simpleFeatureSource;
                featureStore.setTransaction(transaction);
                featureStore.addFeatures(features);
                transaction.commit();
            }
        } catch (IOException | SQLException e) {
            try {
                if (transaction != null) {
                    transaction.rollback();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (transaction != null) {
                    transaction.close();
                }
                if (connection != null) {
                    connection.close();
                }
                if (dataStore != null) {
                    dataStore.dispose();
                }
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void exportFromDatabase(Map<String, Object> databaseParams, String schemaName, String filePath) {
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        ShapefileDataStore newDataStore = null;
        JDBCDataStore dataStore = null;
        Transaction transaction = null;
        try {
            Map<String, Serializable> params = new HashMap<>();
            params.put("url", Paths.get(filePath + schemaName + ".shp").toUri().toURL());
            params.put("create spatial index", Boolean.TRUE);
            newDataStore =
                    (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
            dataStore = (JDBCDataStore) DataStoreFinder.getDataStore(databaseParams);
            SimpleFeatureType schema = dataStore.getSchema(schemaName);
            schema.getGeometryDescriptor().getCoordinateReferenceSystem();
            SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
            List<AttributeDescriptor> attributeDescriptorList = schema.getAttributeDescriptors();
            List<AttributeDescriptor> attributeDescriptors = new ArrayList<>();
            GeometryDescriptor geometryDescriptor = null;
            int i = 0;
            for (AttributeDescriptor attributeDescriptor : attributeDescriptorList) {
                if (attributeDescriptor instanceof GeometryDescriptor) {
                    i = attributeDescriptorList.indexOf(attributeDescriptor);
                    GeometryDescriptor gd = (GeometryDescriptor) attributeDescriptor;
                    geometryDescriptor = new GeometryDescriptor() {
                        @Override
                        public GeometryType getType() {
                            return gd.getType();
                        }

                        @Override
                        public CoordinateReferenceSystem getCoordinateReferenceSystem() {
                            return DefaultGeographicCRS.WGS84;
                        }

                        @Override
                        public String getLocalName() {
                            return gd.getLocalName();
                        }

                        @Override
                        public Object getDefaultValue() {
                            return gd.getDefaultValue();
                        }

                        @Override
                        public Name getName() {
                            return gd.getName();
                        }

                        @Override
                        public int getMinOccurs() {
                            return gd.getMinOccurs();
                        }

                        @Override
                        public int getMaxOccurs() {
                            return gd.getMaxOccurs();
                        }

                        @Override
                        public boolean isNillable() {
                            return gd.isNillable();
                        }

                        @Override
                        public Map<Object, Object> getUserData() {
                            Map<Object, Object> data = new HashMap<>();
                            data.putAll(gd.getUserData());
                            data.put(JDBCDataStore.JDBC_NATIVE_SRID, 4326);
                            return data;
                        }
                    };
                    attributeDescriptors.add(geometryDescriptor);
                } else {
                    attributeDescriptors.add(attributeDescriptor);
                }
            }
            builder.setAttributes(attributeDescriptors);
            builder.setName(schemaName);
            transaction = new DefaultTransaction("create");
            newDataStore.createSchema(builder.buildFeatureType());
            SimpleFeatureSource featureSource = newDataStore.getFeatureSource(schemaName);
            if (featureSource instanceof SimpleFeatureStore) {
                SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
                featureStore.addFeatures(dataStore.getFeatureSource(schemaName).getFeatures(Filter.INCLUDE));
                featureStore.setTransaction(transaction);
                transaction.commit();
            }
        } catch (Exception e) {
            //
            e.printStackTrace();
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            try {
                if (transaction != null) {
                    transaction.close();
                }
                if (dataStore != null) {
                    dataStore.dispose();
                }
                if (newDataStore != null) {
                    newDataStore.dispose();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void createTable(Map<String, Object> databaseParams) {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("Location");
        builder.setCRS(DefaultGeographicCRS.WGS84); // <- Coordinate reference system

        // add attributes in order
        builder.add("geom", Point.class);
        builder.length(15).add("name", String.class); // <- 15 chars width for name field
        builder.add("number", Integer.class);
        // build the type
        final SimpleFeatureType location = builder.buildFeatureType();
        try {
            JDBCDataStore dataStore = (JDBCDataStore) DataStoreFinder.getDataStore(databaseParams);
            dataStore.createSchema(location);
        } catch (IOException e) {
            logger.error("创建失败【{}】", e.getCause().getMessage());
            //e.printStackTrace();
        }
    }

    public static int getSrid(JDBCDataStore dataStore, CoordinateReferenceSystem crs) {
        PreparedStatement statement = null;
        try {
            Integer srid = CRS.lookupEpsgCode(crs, true);
            if (srid == null) {
                List<String> queryParams = new ArrayList<>();
                String sql = "select cfs.coord_ref_sys_code from crs.epsg_coordinatereferencesystem cfs inner join  crs.epsg_coordinatesystem cs on cfs.coord_sys_code=cs.coord_sys_code where cfs.coord_ref_sys_name = ? and cs.dimension=? and cs.coord_sys_type=?";
                Connection connection = dataStore.getDataSource().getConnection();
                statement = connection.prepareStatement(sql);
                String coordSysType = "";
                int dimension = 0;
                if (crs instanceof DefaultGeographicCRS) {
                    DefaultGeographicCRS defaultGeographicCRS = (DefaultGeographicCRS) crs;
                    GeodeticDatum datum = defaultGeographicCRS.getDatum();
                    for (GenericName alias : datum.getAlias()) {
                        queryParams.add(alias.name().toString());
                    }
                    EllipsoidalCS cs = defaultGeographicCRS.getCoordinateSystem();
                    coordSysType = "ellipsoidal";
                    dimension = cs.getDimension();

                } else if (crs instanceof DefaultProjectedCRS) {
                    DefaultProjectedCRS defaultCRS = (DefaultProjectedCRS) crs;
                    GeodeticDatum datum = defaultCRS.getDatum();
                    for (GenericName alias : datum.getAlias()) {
                        queryParams.add(alias.name().toString());
                    }
                    CartesianCS cs = defaultCRS.getCoordinateSystem();
                    DefaultCylindricalProjection projection = (DefaultCylindricalProjection) defaultCRS.getConversionFromBase();
                    coordSysType = "cartesian";
                    dimension = cs.getDimension();

                    OperationMethod operationMethod = defaultCRS.getConversionFromBase().getMethod();
                    //首先查詢投影方法code
                    int methodCode = getMethodCode(connection, operationMethod);
                    if (methodCode != 0) {
                        //遍历参数
                        MapProjection mapProjection = CRS.getMapProjection(defaultCRS);
                        mapProjection.getParameterValues();
                        Map<Integer, Object> querys = getMethodParams(connection, mapProjection, methodCode);
                        //根据method code和对应参数code查询投影code
                        int projectCode = queryProjectCode(connection, methodCode, querys);
                        System.out.println("=====================projectCode" + projectCode);
                    }
                }
                for (String params : queryParams) {
                    statement.setString(1, params);
                    statement.setInt(2, dimension);
                    statement.setString(3, coordSysType);
                    ResultSet rs = statement.executeQuery();
                    while (rs.next()) {
                        srid = rs.getInt(1);
                    }
                    if (srid != null) {
                        return srid;
                    }
                }
            }
        } catch (FactoryException | SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    private static int getMethodCode(Connection connection, OperationMethod operationMethod) {
        List<String> methodAliasList = new ArrayList<>();
        for (GenericName alias : operationMethod.getAlias()) {
            methodAliasList.add(alias.name().toString());
        }
        String projectMethodNameSql = "SELECT * FROM crs.epsg_coordoperationmethod WHERE coord_op_method_name=?";
        int methodCode = 0;
        try(PreparedStatement statement = connection.prepareStatement(projectMethodNameSql);) {
            for (String methodAlias : methodAliasList) {
                statement.setString(1, methodAlias);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    methodCode = rs.getInt(1);
                }
                if (methodCode != 0) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return methodCode;
    }

    private static Map<Integer, Object> getMethodParams(Connection connection, MapProjection operationMethod, int methodCode) {
        Map<Integer, Object> methodParams = new HashMap<>();
        ParameterValueGroup valueGroup = operationMethod.getParameterValues();
        Map<Integer, String> params = getMethodParamsCode(connection, methodCode);
        for (GeneralParameterValue parameterValue : valueGroup.values()) {
            //parameterValue.
            if (parameterValue instanceof org.geotools.parameter.Parameter) {
                org.geotools.parameter.Parameter parameter = (org.geotools.parameter.Parameter) parameterValue;
                double value = parameter.doubleValue();
                //查询对应code
                //int paramCode = getMethodParamsCode(connection, methodCode);
                for (GenericName alias : parameterValue.getDescriptor().getAlias()) {
                    String paramName = alias.name().toString();
                    for (Integer key : params.keySet()) {
                        if (params.get(key).equalsIgnoreCase(paramName)) {
                            methodParams.put(key, value);
                            break;
                        }
                    }
                }
            }
        }
        return methodParams;
    }

    private static Map<Integer, String> getMethodParamsCode(Connection connection, int methodCode) {
        //String projectMethodNameSql = "SELECT parameter_code FROM crs.epsg_coordoperationparam WHERE parameter_name=?";
        String projectMethodNameSql = "select p.parameter_code, parameter_name from crs.epsg_coordoperationparam p inner join crs.epsg_coordoperationparamusage u on p.parameter_code=u.parameter_code where u.coord_op_method_code=?";
        Map<Integer, String> params = new HashMap<>();
        try(PreparedStatement statement = connection.prepareStatement(projectMethodNameSql);) {
            statement.setInt(1, methodCode);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                params.put(rs.getInt(1), rs.getString(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

    private static int queryProjectCode(Connection connection, int methodCode, Map<Integer, Object> methodParams) {
        String projectCodeSql = "SELECT coord_op_code,parameter_code, parameter_value FROM crs.epsg_coordoperationparamvalue WHERE coord_op_method_code=?";
        int code = 0;
        Map<Integer, Map<Integer, Object>> map = new HashMap<>();
        try(PreparedStatement statement = connection.prepareStatement(projectCodeSql);) {
            statement.setInt(1, methodCode);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int projectCode = rs.getInt(1);
                if (!map.containsKey(projectCode)) {
                    Map<Integer, Object> params = new HashMap<>();
                    map.put(rs.getInt(1), params);
                    params.put(rs.getInt(2), rs.getDouble(3));
                } else {
                    Map<Integer, Object> params = map.get(projectCode);
                    params.put(rs.getInt(2), rs.getDouble(3));
                }
            }
            //处理map
            for (Integer key : map.keySet()) {
                Map<Integer, Object> params = map.get(key);
                if (params.keySet().containsAll(methodParams.keySet()) && params.values().containsAll(methodParams.values())) {
                    code = key;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }
}
