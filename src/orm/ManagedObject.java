package orm;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class ManagedObject {
    
    // public static <T extends ManagedObject> ManagedObject[] fetch(Class<T> entityClass, Predicate pred, SortDescriptor[] sds, Pagination p) {
    //     // assume the connector will throw errors if syntax is incorrect.
    //     try (Connection c = DBContext.getDatabaseConnection()) {
    //         var sql = "SELECT * FROM " + entityClass.getSimpleName();
    //         if (pred != null) {
    //             sql = sql + " WHERE " + pred.p;
    //         }

    //         if (sds != null && sds.length > 0) {
    //             sql = sql + " SORT BY";
    //             ArrayList<String> sortDescriptors = new ArrayList<String>();
    //             for(SortDescriptor s : sds) {
    //                 sortDescriptors.add(" " + s.fieldName + (s.ascending ? " ASC" : " DESC"));
    //             }
    //             sql += String.join(", ", sortDescriptors);
    //         }

    //         if (p != null && p.limit != null) {
    //             sql = sql + " LIMIT " + p.limit;
    //             if (p.offset != null) {
    //                 sql = sql + ", " + p.offset;
    //             }
    //         }

    //         System.out.println(sql); // DEBUG

    //         PreparedStatement ps = c.prepareStatement(sql);
    //         for(int i = 0; i < pred.parameters.length; i++) {
    //             var param = pred.parameters[i];
    //             switch (param.getClass().getSimpleName())
    //             {
    //                 case "String":
    //                 ps.setString(i+1, (String)param);
    //                 break;
    
    //                 case "Double":
    //                 ps.setDouble(i+1, (Double)param);
    //                 break;
    
    //                 case "Integer":
    //                 ps.setInt(i+1, (Integer)param);
    //                 break;
    
    //                 case "Date":
    //                 ps.setDate(i+1, (Date)param);
    //                 break;
    //             } 
    //         }
            
    //         ResultSet rs = ps.executeQuery();

    //         ArrayList<ManagedObject> results = new ArrayList<ManagedObject>(0);
    //         while(rs.next()) {
    //             // results.add(rs.)
    //             // TODO: Find a way to map this to any object or just hard code it for each object.
    //             // IDEA: use the serialize/deserialize methods for queryable.
    //         }

    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return null;
    //     }
    // }

    public static Connection connection = DBContext.getDatabaseConnection();
    public static Scanner sc = new Scanner(System.in);

    public static <T extends ManagedObject> T[] fetch(Class<T> entityClass, Predicate pred, SortDescriptor[] sds, Pagination p) throws Exception {
        throw new Exception("Method not implemented");
    }

    public static <T extends ManagedObject> T get(Integer id) throws Exception {
        throw new Exception("Method not implemented");
    }

    public static <T extends ManagedObject> Boolean insert(T object) throws Exception {
        throw new Exception("Method not implemented");
    }

    public static <T extends ManagedObject> Boolean delete(T object) throws Exception {
        throw new Exception("Method not implemented");
    }

    public static <T extends ManagedObject> Boolean update(T object, String[] columns) throws Exception {
        throw new Exception("Method not implemented");
    }

}
