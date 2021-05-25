package model;

import orm.ManagedObject;

import orm.ManagedObject;
import orm.Pagination;
import orm.Predicate;
import orm.PredicateOperation;
import orm.SortDescriptor;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class ManagedAppointment extends ManagedObject {
    public Integer clientId = null;
    public Integer medicId = null;
    public Timestamp appointmentDate;
    public String description;

    public ManagedAppointment() {}

    public ManagedAppointment(Integer clientId, Integer medicId, Timestamp appointmentDate, String description) {
        this.clientId = clientId;
        this.medicId = medicId;
        this.appointmentDate = appointmentDate;
        this.description = description;
    }

    public static ManagedAppointment read() {
        String _clientName, _medicName, _dateString, _description;
        java.util.Date _date;
        Timestamp _sqlDate;
        
        System.out.print("Nume sau prenume client: ");
        _clientName = ManagedAppointment.sc.nextLine();
        
        System.out.print("Nume sau prenume medic: ");
        _medicName = ManagedAppointment.sc.nextLine();

        // FETCH OPERATION
        SortDescriptor[] sds = {};
        Pagination p = null;

        Predicate clientPred = new Predicate();
        String[] columns = {"firstName", "lastName"};
        Object[] values = {_clientName, _clientName};

        PredicateOperation[] pred_ops = {PredicateOperation.LIKE, PredicateOperation.LIKE};

        clientPred.columns = columns;
        clientPred.values = values;
        clientPred.operations = pred_ops;
        clientPred.interPredicateOperation = PredicateOperation.OR;

        Predicate medicPred = new Predicate();
        Object[] medicValues = {_medicName, _medicName};

        medicPred.columns = Arrays.copyOf(columns, columns.length);
        medicPred.values = medicValues;
        medicPred.operations = Arrays.copyOf(pred_ops, pred_ops.length);
        medicPred.interPredicateOperation = PredicateOperation.OR;

        ManagedClient[] mcs;
        ManagedMedic[] mms;
        try {
            mcs = ManagedClient.fetch(clientPred, sds, p);
            mms = ManagedMedic.fetch(medicPred, sds, p);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }

        if (mcs.length == 0 || mms.length == 0) {
            System.out.println("Client or Medic not in the system. Check who is missing and add them.");
            return null;
        }

        // choose which one
        System.out.println("Care din acesti clienti?");
        for (Integer i = 0; i < mcs.length; i++) {
            System.out.println("" + (i+1) + ". " + mcs[i].firstName + " " + mcs[i].lastName + ": " + mcs[i].email);
        }
        Integer chosen;
        chosen = ManagedAppointment.sc.nextInt();
        ManagedAppointment.sc.nextLine();
        ManagedClient mc = mcs[chosen - 1]; // client is chosen

        System.out.println("Care din acesti medici?");
        for (Integer i = 0; i < mms.length; i++) {
            System.out.println("" + (i+1) + ". Dr. " + mms[i].firstName + " " + mms[i].lastName + ": " + mms[i].email);
        }
        chosen = ManagedAppointment.sc.nextInt();
        ManagedAppointment.sc.nextLine();
        ManagedMedic mm = mms[chosen - 1]; // client is chosen

        System.out.print("Data programarii (DD-MM-YYYY HH:mm): ");
        _dateString = ManagedAppointment.sc.nextLine();

        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        try {
            _date = formatter.parse(_dateString);
            _sqlDate = new Timestamp(_date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        
        
        System.out.print("Descriere: ");
        _description = ManagedAppointment.sc.nextLine();

        return new ManagedAppointment(mc.id, mm.id, _sqlDate, _description);
    }

    public void print() {
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        ManagedClient mc = getClient();
        ManagedMedic mm = getMedic();

        System.out.println("Programare pe data " + formatter.format(appointmentDate));
        System.out.println("-------------------------------------------------------");
        System.out.println("Pacient: \t" + mc.firstName + " " + mc.lastName + ": " + mc.email);
        System.out.println("Medic: \t\tDr. " + mm.firstName + " " + mm.lastName + ": " + mm.email);
        System.out.println("Cabinet: \t" + mm.cabinet);
        System.out.println("Descriere: \t" + this.description);
        System.out.println();
    }

    public ManagedClient getClient() {
        try {
            return ManagedClient.get(clientId);
        } catch (Exception e) {
            return null;
        }
    }

    public ManagedMedic getMedic() {
        try {
            return ManagedMedic.get(medicId);
        } catch (Exception e) {
           return null;
        }
    }

    public ManagedAppointment[] getAppointments(ManagedClient mc) throws Exception {

        if (mc.id == null) {
            System.out.println("Inserati intai clientul in baza de date.");
            return null;
        }

        String sql = "SELECT clientId, medicId, appointmentDate, description FROM appointments WHERE clientId=?";
        PreparedStatement ps = ManagedAppointment.connection.prepareStatement(sql);
        ps.setInt(1, mc.id);

        ResultSet rs = ps.executeQuery();

        ArrayList<ManagedAppointment> ma_l = new ArrayList<ManagedAppointment>();
        while(rs.next()) {
            ma_l.add(new ManagedAppointment(rs.getInt("clientId"), rs.getInt("medicId"), rs.getTimestamp("appointmentDate"), rs.getString("description")));
        }

        ManagedAppointment[] ma_a = new ManagedAppointment[ma_l.size()];
        ma_a = ma_l.toArray(ma_a);

        return ma_a;
    }

    public ManagedAppointment[] getAppointments(ManagedMedic mm) throws Exception {

        if (mm.id == null) {
            System.out.println("Inserati intai medicul in baza de date.");
            return null;
        }

        String sql = "SELECT clientId, medicId, appointmentDate, description FROM appointments WHERE medicId=?";
        PreparedStatement ps = ManagedAppointment.connection.prepareStatement(sql);
        ps.setInt(1, mm.id);

        ResultSet rs = ps.executeQuery();

        ArrayList<ManagedAppointment> ma_l = new ArrayList<ManagedAppointment>();
        while(rs.next()) {
            ma_l.add(new ManagedAppointment(rs.getInt("clientId"), rs.getInt("medicId"), rs.getTimestamp("appointmentDate"), rs.getString("description")));
        }

        ManagedAppointment[] ma_a = new ManagedAppointment[ma_l.size()];
        ma_a = ma_l.toArray(ma_a);

        return ma_a;
    }

    public static Boolean insert(ManagedAppointment object) throws Exception {
        if (object == null) {
            return false;
        }

        String sql = "INSERT INTO `appointments` (clientId, medicId, appointmentDate, description) VALUES (?, ?, ?, ?);";

        PreparedStatement ps = ManagedMedic.connection.prepareStatement(sql);
        ps.setInt(1, object.clientId);
        ps.setInt(2, object.medicId);
        ps.setTimestamp(3, object.appointmentDate);
        ps.setString(4, object.description);

        try {
            boolean res = ps.execute();
            return res;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public static Boolean updateDate(ManagedAppointment ma, Timestamp newDate) throws Exception {
        String sql = "UPDATE appointments SET appointmentDate=? WHERE clientId=? AND medicId=? AND appointmentDate=?;";
        PreparedStatement ps = ManagedAppointment.connection.prepareStatement(sql);
        ps.setTimestamp(1, newDate);
        ps.setInt(2, ma.clientId);
        ps.setInt(3, ma.medicId);
        ps.setTimestamp(4, ma.appointmentDate);

        int success = ps.executeUpdate();
        ma.appointmentDate = newDate;

        return success > 0 ? true : false;
    }

    public static Boolean delete(ManagedAppointment ma) throws Exception {
        String sql = "DELETE FROM appointments WHERE clientId=? AND medicId=? AND appointmentDate=?;";
        PreparedStatement ps = ManagedAppointment.connection.prepareStatement(sql);
        ps.setInt(1, ma.clientId);
        ps.setInt(2, ma.medicId);
        ps.setTimestamp(3, ma.appointmentDate);

        int success = ps.executeUpdate();

        return success > 0 ? true : false;
    }

    public static ManagedAppointment[] fetch(Predicate preds, SortDescriptor[] sds, Pagination p) throws Exception {
        String sql = "SELECT clientId, medicId, appointmentDate, description FROM appointments ";

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

        PreparedStatement ps = ManagedAppointment.connection.prepareStatement(sql);

        
        ResultSet rs = ps.executeQuery();

        ArrayList<ManagedAppointment> results = new ArrayList<ManagedAppointment>();
        while(rs.next()) {
            ManagedAppointment ma = new ManagedAppointment(rs.getInt("clientId"), rs.getInt("medicId"), rs.getTimestamp("appointmentDate"), rs.getString("description"));
            results.add(ma);
        }

        ManagedAppointment[] ma_l = new ManagedAppointment[results.size()];
        ma_l = results.toArray(ma_l);

        return ma_l;
    }
}
