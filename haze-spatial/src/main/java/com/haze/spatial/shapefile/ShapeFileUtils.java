package com.haze.spatial.shapefile;

import org.geotools.data.DataStoreFinder;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCFeatureStore;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
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
            //;fileDataStore.getSchema().
            fileDataStore.setCharset(Charset.forName("UTF-8"));
            fileDataStore.setNamespaceURI(null);
            String typeName = fileDataStore.getTypeNames()[0];
           /* FeatureSource<SimpleFeatureType, SimpleFeature> source =
                    fileDataStore.getFeatureSource(typeName);*/
            ContentFeatureSource source = fileDataStore.getFeatureSource();
            Filter filter = Filter.INCLUDE;
            FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(filter);
            //System.out.println(CRSUtils.getSrid(source.getSchema().getCoordinateReferenceSystem()));
            try {
                System.out.println(CRS.lookupEpsgCode(source.getSchema().getCoordinateReferenceSystem(), true));
                //System.out.println(CRS.lookupIdentifier(source.getSchema().getGeometryDescriptor().getCoordinateReferenceSystem(), true));
            } catch (FactoryException e) {
                e.printStackTrace();
            }
            //createTable(databaseParams, fileDataStore.getSchema(), collection);
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
               // int srid = getSrid(dataStore, schema.getCoordinateReferenceSystem());
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
        }
    }
}
