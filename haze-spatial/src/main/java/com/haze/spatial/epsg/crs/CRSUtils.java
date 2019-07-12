package com.haze.spatial.epsg.crs;

import com.haze.spatial.epsg.postgresql.ThreadedPostgreSQLEpsgFactory;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.crs.DefaultProjectedCRS;
import org.geotools.referencing.operation.DefaultProjection;
import org.geotools.referencing.operation.projection.MapProjection;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.CartesianCS;
import org.opengis.referencing.cs.EllipsoidalCS;
import org.opengis.referencing.datum.GeodeticDatum;
import org.opengis.referencing.operation.OperationMethod;
import org.opengis.util.GenericName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public final class CRSUtils {

    private static final ThreadedPostgreSQLEpsgFactory threadedPostgreSQLEpsgFactory = new ThreadedPostgreSQLEpsgFactory();

    private static final int DEFAULT_NOT_FOUND_SRID = -1;

    private static final String[] ANSI = {
            "[Alias]", "epsg_alias",
            "[Area]", "epsg_area",
            "[Coordinate Axis]", "epsg_coordinateaxis",
            "[Coordinate Axis Name]", "epsg_coordinateaxisname",
            "[Coordinate_Operation]", "epsg_coordoperation",
            "[Coordinate_Operation Method]", "epsg_coordoperationmethod",
            "[Coordinate_Operation Parameter]", "epsg_coordoperationparam",
            "[Coordinate_Operation Parameter Usage]", "epsg_coordoperationparamusage",
            "[Coordinate_Operation Parameter Value]", "epsg_coordoperationparamvalue",
            "[Coordinate_Operation Path]", "epsg_coordoperationpath",
            "[Coordinate Reference System]", "epsg_coordinatereferencesystem",
            "[Coordinate System]", "epsg_coordinatesystem",
            "[Datum]", "epsg_datum",
            "[Ellipsoid]", "epsg_ellipsoid",
            "[Naming System]", "epsg_namingsystem",
            "[Prime Meridian]", "epsg_primemeridian",
            "[Supersession]", "epsg_supersession",
            "[Unit of Measure]", "epsg_unitofmeasure",
            "[Version History]", "epsg_versionhistory",
            "[ORDER]", "coord_axis_order" // a field in epsg_coordinateaxis
    };
    private static final Map map = new LinkedHashMap();

    private static String schema;

    static {
        for (int i = 0; i < ANSI.length; i++) {
            map.put(ANSI[i], ANSI[++i]);
        }
        try {
            threadedPostgreSQLEpsgFactory.getDataSource();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        schema = threadedPostgreSQLEpsgFactory.getSchema().trim();
        final int length = schema.length();
        if (length == 0) {
            throw new IllegalArgumentException(schema);
        }
        final char separator = schema.charAt(length - 1);
        if (separator != '.' && separator != '_') {
            schema += '.';
        } else if (length == 1) {
            throw new IllegalArgumentException(schema);
        }
        for (final Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
            final Map.Entry entry = (Map.Entry) it.next();
            final String tableName = (String) entry.getValue();
            entry.setValue(schema + tableName);
        }
    }

    /**
     * 获取坐标对应srid
     *
     * @param crs
     * @return
     */
    public static int getSrid(CoordinateReferenceSystem crs) {
        Integer srid = DEFAULT_NOT_FOUND_SRID;
        try {
            srid = CRS.lookupEpsgCode(crs, true);
            if (srid == null) {
                try (Connection connection = threadedPostgreSQLEpsgFactory.getDataSource().getConnection()) {
                    if (crs instanceof DefaultGeographicCRS) {
                        //查询地理坐标系统
                        srid = findGeographicCRS(connection, (DefaultGeographicCRS) crs);
                    } else if (crs instanceof DefaultProjectedCRS) {
                        //查询平面坐标系统
                        srid = findProjectCRS(connection, (DefaultProjectedCRS) crs);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (FactoryException e) {
            e.printStackTrace();
        }
        return srid;
    }

    private static Integer findGeographicCRS(Connection connection, DefaultGeographicCRS crs) throws SQLException {
        int srid = DEFAULT_NOT_FOUND_SRID;
        //PreparedStatement statement = null;
        List<String> queryParams = new ArrayList<>();
        String sql = "select cfs.coord_ref_sys_code from [Coordinate Reference System] cfs inner join [Coordinate System] cs on cfs.coord_sys_code=cs.coord_sys_code where cfs.coord_ref_sys_name = ? and cs.dimension=? and cs.coord_sys_type=? and cfs.deprecated=0";
        try (PreparedStatement statement = connection.prepareStatement(adaptSQL(sql))) {
            String coordSysType = "";
            int dimension = 0;
            DefaultGeographicCRS defaultGeographicCRS = (DefaultGeographicCRS) crs;
            GeodeticDatum datum = defaultGeographicCRS.getDatum();
            for (GenericName alias : datum.getAlias()) {
                queryParams.add(alias.name().toString());
            }
            EllipsoidalCS cs = defaultGeographicCRS.getCoordinateSystem();
            coordSysType = "ellipsoidal";
            dimension = cs.getDimension();
            for (String params : queryParams) {
                statement.setString(1, params);
                statement.setInt(2, dimension);
                statement.setString(3, coordSysType);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    srid = rs.getInt(1);
                }
            }
        }
        return srid;
    }

    private static Integer findProjectCRS(Connection connection, DefaultProjectedCRS crs) throws SQLException {
        int srid = DEFAULT_NOT_FOUND_SRID;
        List<String> queryParams = new ArrayList<>();
        GeodeticDatum datum = crs.getDatum();
        for (GenericName alias : datum.getAlias()) {
            queryParams.add(alias.name().toString());
        }
        CartesianCS cs = crs.getCoordinateSystem();
        DefaultProjection projection = (DefaultProjection) crs.getConversionFromBase();
        String coordSysType = "";
        int dimension = 0;
        coordSysType = "cartesian";
        dimension = cs.getDimension();
        //获取对应地理坐标信息
        int geoSrid = findGeographicCRS(connection, (DefaultGeographicCRS) projection.getSourceCRS());
        if (geoSrid != DEFAULT_NOT_FOUND_SRID) {
            OperationMethod operationMethod = projection.getMethod();
            int methodCode = getMethodCode(connection, operationMethod);
            if (methodCode != 0) {
                //遍历参数
                MapProjection mapProjection = CRS.getMapProjection(crs);
                mapProjection.getParameterValues();
                Map<Integer, Object> querys = getMethodParams(connection, mapProjection, methodCode);
                //根据method code和对应参数code查询投影code
                Set<Integer> projectCodes = queryProjectCode(connection, methodCode, querys);
                //根据projectCodes和geoSrid唯一平面坐标信息
                String sql = "select coord_ref_sys_code from [Coordinate Reference System] where source_geogcrs_code=? and projection_conv_code=? and deprecated=0";
                try (PreparedStatement statement = connection.prepareStatement(adaptSQL(sql))) {
                    //此处理论上来说zhi唯一
                    for (Integer projectCode : projectCodes) {
                        statement.setInt(1, geoSrid);
                        statement.setInt(2, projectCode);
                        ResultSet rs = statement.executeQuery();
                        while (rs.next()) {
                            srid = rs.getInt(1);
                        }
                    }
                }
            }
        }
        return srid;
    }

    private static int getMethodCode(Connection connection, OperationMethod operationMethod) {
        List<String> methodAliasList = new ArrayList<>();
        for (GenericName alias : operationMethod.getAlias()) {
            methodAliasList.add(alias.name().toString());
        }
        String sql = "SELECT * FROM [Coordinate_Operation Method] WHERE coord_op_method_name=?";
        int methodCode = 0;
        try (PreparedStatement statement = connection.prepareStatement(adaptSQL(sql));) {
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
        String sql = "select p.parameter_code, parameter_name from [Coordinate_Operation Parameter] p inner join [Coordinate_Operation Parameter Usage] u on p.parameter_code=u.parameter_code where u.coord_op_method_code=?";
        Map<Integer, String> params = new HashMap<>();
        try (PreparedStatement statement = connection.prepareStatement(adaptSQL(sql));) {
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

    private static Set<Integer> queryProjectCode(Connection connection, int methodCode, Map<Integer, Object> methodParams) {
        String sql = "SELECT coord_op_code,parameter_code, parameter_value FROM [Coordinate_Operation Parameter Value] WHERE coord_op_method_code=?";
        Set<Integer> projectCodes = new HashSet<>();
        Map<Integer, Map<Integer, Object>> map = new HashMap<>();
        try (PreparedStatement statement = connection.prepareStatement(adaptSQL(sql));) {
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
                    projectCodes.add(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projectCodes;
    }

    private static String adaptSQL(final String statement) {
        final StringBuilder modified = new StringBuilder(statement);
        for (final Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
            final Map.Entry entry = (Map.Entry) it.next();
            final String oldName = (String) entry.getKey();
            final String newName = (String) entry.getValue();
            int start = 0;
            while ((start = modified.indexOf(oldName, start)) >= 0) {
                modified.replace(start, start + oldName.length(), newName);
                start += newName.length();
            }
        }
        return modified.toString();
    }
}
