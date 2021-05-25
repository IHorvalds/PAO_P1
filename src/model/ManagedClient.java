package model;

import orm.ManagedObject;
import orm.Pagination;
import orm.Predicate;
import orm.SortDescriptor;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ManagedClient extends ManagedObject {

    public Integer id = null;
    public String firstName;
    public String lastName;
    public String email;
    public Date dateOfBirth;
    public String phoneNumber;
    public Gender gender; 

    public ManagedClient() {}

    public ManagedClient(String firstName, String lastName,
                        String email, Date dateOfBirth, String phoneString,
                        Gender gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneString;
    }

    public ManagedClient(Integer id, String firstName, String lastName,
                        String email, Date dateOfBirth, String phoneString,
                        Gender gender) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneString;
    }
    
    public static ManagedClient read() {
        String _lastName, _firstName, _phoneNumber, _email, _gender, _dateString;
        java.util.Date _date;
        java.sql.Date _sqlDate;
        
        System.out.print("Prenume client: ");
        _firstName = ManagedClient.sc.nextLine();
        
        System.out.print("Nume client: ");
        _lastName = ManagedClient.sc.nextLine();

        System.out.print("Gen (M/F/Other): ");
        _gender = ManagedClient.sc.nextLine();

        System.out.print("Numar de telefon: ");
        _phoneNumber = ManagedClient.sc.nextLine();

        System.out.print("Email: ");
        _email = ManagedClient.sc.nextLine();

        System.out.print("Data de nastere (DD-MM-YYYY): ");
        _dateString = ManagedClient.sc.nextLine();

        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            _date = formatter.parse(_dateString);
            _sqlDate = new java.sql.Date(_date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        Gender g;
        switch (_gender.toLowerCase()) {
            case "m":
            g = Gender.M;
            break;

            case "f":
            g = Gender.F;
            break;

            default:
            g = Gender.Other;
        }

        return new ManagedClient(_firstName, _lastName, _email, _sqlDate, _phoneNumber, g);
    }

    public void print() {
        System.out.println("Client: " + this.firstName + " " + this.lastName);
        System.out.println("Numar de telefon: " + this.phoneNumber);
        System.out.println("Email: " + this.email);
        System.out.println("Gen: " + this.gender.value());
        System.out.println("Data nasterii: " + this.dateOfBirth.toLocalDate());
        System.out.println();
    }

    public static Boolean insert(ManagedClient object) throws Exception {
        String sql = "INSERT INTO `clients` (firstName, lastName, email, dateOfBirth, phoneNumber, gender) VALUES (?, ?, ?, ?, ?, ?);";

        PreparedStatement ps = ManagedClient.connection.prepareStatement(sql);
        ps.setString(1, object.firstName);
        ps.setString(2, object.lastName);
        ps.setString(3, object.email);
        ps.setDate(4, object.dateOfBirth);
        ps.setString(5, object.phoneNumber);
        ps.setString(6, object.gender.value());

        try {
            boolean res = ps.execute();

            sql = "SELECT id FROM clients WHERE firstName=? AND lastName=? AND email=? AND phoneNumber=?;";
            PreparedStatement select = ManagedClient.connection.prepareStatement(sql);
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

    public static ManagedClient get(Integer id) throws Exception {
        ManagedClient mc = null;

        String sql = "SELECT firstName, lastName, email, dateOfBirth, phoneNumber, gender FROM clients WHERE id=?;";

        PreparedStatement ps = ManagedClient.connection.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            Gender g;
            String g_s = rs.getString("gender");
            switch (g_s.toLowerCase()) {
                case "m":
                g = Gender.M;
                break;

                case "f":
                g = Gender.F;
                break;

                default:
                g = Gender.Other;
            }
            mc = new ManagedClient(id, rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getDate("dateOfBirth"), rs.getString("phoneNumber"), g);
            rs.close();
            break;
        }

        return mc;
    }

    public static Boolean delete(ManagedClient object) throws Exception {
        // Sanity check. Can't delete an item that was never initialized
        if (object.id == null) {
            return true; // No-op. It wasn't there to begin with
        }

        String sql = "DELETE FROM clients WHERE id=?;";

        PreparedStatement ps = ManagedClient.connection.prepareStatement(sql);
        ps.setInt(1, object.id);
        
        try {
            ps.executeUpdate();
            return true;
        } catch (SQLException se) {
            System.out.println(se.getSQLState());
            return false;
        }
    }

    public static Boolean update(ManagedClient object, String[] columns) throws Exception {
        
        // if the object was never inserted, we'll insert it now
        if (object.id == null) {
            return ManagedClient.insert(object);
        }

        ArrayList<String> updates = new ArrayList<String>();
        for (String column : columns) {
            String upd = column + "=? ";
            updates.add(upd);
        }
        String updatesString = String.join(", ", updates);

        String sql = "UPDATE clients SET " + updatesString + "WHERE id=?";

        PreparedStatement ps = ManagedClient.connection.prepareStatement(sql);
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

                    case "gender":
                    ps.setString(idx, object.gender.value());
                    break;

                    case "dateofbirth":
                    ps.setDate(idx, object.dateOfBirth);
                    break;

                    default:
                    throw new Exception("Unknown column '" + columns[idx - 1] + "' for ManagedClient.");
                }
            }
        }

        int success = ps.executeUpdate();

        return success > 0 ? true : false;
    }

    public static ManagedClient[] fetch(Predicate preds, SortDescriptor[] sds, Pagination p) throws Exception {
        String sql = "SELECT id, firstName, lastName, email, dateOfBirth, phoneNumber, gender FROM clients ";

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

        PreparedStatement ps = ManagedClient.connection.prepareStatement(sql);

        
        ResultSet rs = ps.executeQuery();

        ArrayList<ManagedClient> results = new ArrayList<ManagedClient>();
        while(rs.next()) {
            Gender g;
            String g_s = rs.getString("gender");
            switch (g_s.toLowerCase()) {
                case "m":
                g = Gender.M;
                break;

                case "f":
                g = Gender.F;
                break;

                default:
                g = Gender.Other;
            }
            ManagedClient mc = new ManagedClient(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"),
                                                 rs.getString("email"), rs.getDate("dateOfBirth"), rs.getString("phoneNumber"),
                                                 g);
            results.add(mc);
        }

        ManagedClient[] mc_l = new ManagedClient[results.size()];
        mc_l = results.toArray(mc_l);

        return mc_l;
    }
}
