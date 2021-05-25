package model;

import orm.ManagedObject;
import orm.Pagination;
import orm.Predicate;
import orm.SortDescriptor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManagedMedic extends ManagedObject {
    public Integer id = null;
    public String firstName;
    public String lastName;
    public String email;
    public Integer cabinet;
    public String phoneNumber;
    public String specialty; 

    public ManagedMedic() {}

    public ManagedMedic(String firstName, String lastName,
                        String email, Integer cabinet, String phoneString,
                        String specialty) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.cabinet = cabinet;
        this.specialty = specialty;
        this.phoneNumber = phoneString;
    }

    public ManagedMedic(Integer id, String firstName, String lastName,
                        String email, Integer cabinet, String phoneString,
                        String specialty) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.cabinet = cabinet;
        this.specialty = specialty;
        this.phoneNumber = phoneString;
    }

    public static ManagedMedic read() {
        String _lastName, _firstName, _phoneNumber, _email, _specialty;
        Integer  _cabinet;
        
        System.out.print("Prenume medic: ");
        _firstName = ManagedMedic.sc.nextLine();
        
        System.out.print("Nume medic: ");
        _lastName = ManagedMedic.sc.nextLine();

        System.out.print("Numar de telefon: ");
        _phoneNumber = ManagedMedic.sc.nextLine();

        System.out.print("Email medic: ");
        _email = ManagedMedic.sc.nextLine();

        System.out.print("Specialitate: ");
        _specialty = ManagedMedic.sc.nextLine();

        System.out.print("Cabinet: ");
        _cabinet = ManagedMedic.sc.nextInt();
        ManagedMedic.sc.nextLine();

        return new ManagedMedic(_firstName, _lastName, _email, _cabinet, _phoneNumber, _specialty);
    }

    public void print() {
        System.out.println("Medic: Dr. " + this.firstName + " " + this.lastName);
        System.out.println("Numar de telefon: " + this.phoneNumber);
        System.out.println("Email: " + this.email);
        System.out.println("Cabinet: " + this.cabinet);
        System.out.println("Specialitate: " + this.specialty);
        System.out.println();
    }

    public static Boolean insert(ManagedMedic object) throws Exception {
        String sql = "INSERT INTO `medics` (firstName, lastName, email, cabinet, specialty, phoneNumber) VALUES (?, ?, ?, ?, ?, ?);";

        PreparedStatement ps = ManagedMedic.connection.prepareStatement(sql);
        ps.setString(1, object.firstName);
        ps.setString(2, object.lastName);
        ps.setString(3, object.email);
        ps.setInt(4, object.cabinet);
        ps.setString(5, object.specialty);
        ps.setString(6, object.phoneNumber);

        try {
            boolean res = ps.execute();

            sql = "SELECT id FROM medics WHERE firstName=? AND lastName=? AND email=? AND phoneNumber=?;";
            PreparedStatement select = ManagedMedic.connection.prepareStatement(sql);
            select.setString(1, object.firstName);
            select.setString(2, object.lastName);
            select.setString(3, object.email);
            select.setString(4, object.phoneNumber);

            ResultSet rs = select.executeQuery();
            while(rs.next()) {
                object.id = rs.getInt("id");
                rs.close();
                break;
            }

            return res;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public static ManagedMedic get(Integer id) throws Exception {
        ManagedMedic mm = null;

        String sql = "SELECT firstName, lastName, email, cabinet, specialty, phoneNumber FROM medics WHERE id=?;";

        PreparedStatement ps = ManagedMedic.connection.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            mm = new ManagedMedic(id, rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getInt("cabinet"), rs.getString("phoneNumber"), rs.getString("specialty"));
            rs.close();
            break;
        }

        return mm;
    }

    public static Boolean delete(ManagedMedic object) throws Exception {
        // Sanity check. Can't delete an item that was never initialized
        if (object.id == null) {
            return true; // No-op. It wasn't there to begin with
        }

        String sql = "DELETE FROM medics WHERE id=?;";

        PreparedStatement ps = ManagedMedic.connection.prepareStatement(sql);
        ps.setInt(1, object.id);
        
        try {
            ps.executeUpdate();
            return true;
        } catch (SQLException se) {
            System.out.println(se.getSQLState());
            return false;
        }
    }

    public static Boolean update(ManagedMedic object, String[] columns) throws Exception {
        
        // if the object was never inserted, we'll insert it now
        if (object.id == null) {
            return ManagedMedic.insert(object);
        }

        ArrayList<String> updates = new ArrayList<String>();
        for (String column : columns) {
            String upd = column + "=? ";
            updates.add(upd);
        }
        String updatesString = String.join(", ", updates);

        String sql = "UPDATE medics SET " + updatesString + "WHERE id=?";

        PreparedStatement ps = ManagedMedic.connection.prepareStatement(sql);
        Integer argumentIndices = columns.length + 1;

        for (Integer idx = 1; idx <= argumentIndices; idx++) {
            if (idx == argumentIndices) {
                ps.setInt(idx, object.id);
            } else {
                switch (columns[idx - 1].toLowerCase()) {
                    case "firstname":
                    ps.setString(idx, object.firstName);
                    break;

                    case "lastname":
                    ps.setString(idx, object.lastName);
                    break;

                    case "email":
                    ps.setString(idx, object.email);
                    break;

                    case "phonenumber":
                    ps.setString(idx, object.phoneNumber);
                    break;

                    case "cabinet":
                    ps.setInt(idx, object.cabinet);
                    break;

                    case "specialty":
                    ps.setString(idx, object.specialty);
                    break;

                    default:
                    throw new Exception("Unknown column '" + columns[idx - 1] + "' for ManagedMedic.");
                }
            }
        }

        int success = ps.executeUpdate();

        return success > 0 ? true : false;
    }

    public static ManagedMedic[] fetch(Predicate preds, SortDescriptor[] sds, Pagination p) throws Exception {
        String sql = "SELECT id, firstName, lastName, email, cabinet, specialty, phoneNumber FROM medics ";

        if (preds != null && preds.columns.length > 0) {
            sql += "WHERE ";
            ArrayList<String> predicates = new ArrayList<String>();
            for(Integer i = 0; i < preds.columns.length; i++) {
                if (preds.values[i] instanceof ArrayList) {
                    ArrayList<Object> arr = ((ArrayList<Object>)preds.values[i]);
                    String array_string = "(";
                    for(Integer j = 0 ; j < arr.size(); j++) {
                        if (j == arr.size() - 1) {
                            array_string += "\"" + arr.get(j).toString() + "\"";
                        } else {
                            array_string += "\"" + arr.get(j).toString() + "\", ";
                        }
                    }
                    array_string += ") ";

                    predicates.add(preds.columns[i] + preds.operations[i].value() + array_string);
                } else {
                    predicates.add(preds.columns[i] + preds.operations[i].value() + "\"" + preds.values[i].toString() + "\"");
                }
            }
            sql += (String.join(preds.interPredicateOperation.value(), predicates) + " ");
        }

        if (sds != null && sds.length > 0) {
            sql = sql + "ORDER BY";
            ArrayList<String> sortDescriptors = new ArrayList<String>();
            for(SortDescriptor s : sds) {
                sortDescriptors.add(" " + s.fieldName + (s.ascending ? " ASC" : " DESC"));
            }
            sql += String.join(", ", sortDescriptors);
        }

        if (p != null && p.limit != null) {
            sql = sql + " LIMIT " + p.limit;
            if (p.offset != null) {
                sql = sql + ", " + p.offset;
            }
        }

        System.out.println(sql); // DEBUG

        PreparedStatement ps = ManagedMedic.connection.prepareStatement(sql);
        
        ResultSet rs = ps.executeQuery();

        ArrayList<ManagedMedic> results = new ArrayList<ManagedMedic>();
        while(rs.next()) {
            ManagedMedic mm = new ManagedMedic(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getInt("cabinet"), rs.getString("phoneNumber"), rs.getString("specialty"));
            results.add(mm);
        }

        ManagedMedic[] mm_l = new ManagedMedic[results.size()];
        mm_l = results.toArray(mm_l);

        return mm_l;
    }
}
