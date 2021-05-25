import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

import model.Gender;
import model.ManagedAppointment;
import model.ManagedClient;
import model.ManagedMedic;
import orm.DBContext;
import orm.Pagination;
import orm.Predicate;
import orm.PredicateOperation;
import orm.SortDescriptor;

public class Service {
    private Service() {}

    private static Service shared;
    public static Service getInstance() {
        if (shared == null)
            shared = new Service();
  
        return shared;
    }

    public void Menu() {
        String[] operations = {"Adaugare client nou", "Adaugare medic nou", "Adaugare programare noua", "Afiseaza programari", 
                                "Cauta o programare", "Modifica o programare", "Sterge o programare", "Afiseaza clienti", "Cauta un client",
                                "Afiseaza doctori", "Cauta un doctor", "Updateaza datele unui client", "Updateaza datele unui medic", 
                                "Sterge un client", "Sterge un doctor", "Iesire"};

        for (Integer i = 0; i < operations.length; i++) {
            System.out.println("" + (i+1) + ". \t" + operations[i]);
        }
        System.out.println();
        System.out.print("Actiune: ");

        Integer i;

        Scanner scanner = new Scanner(System.in);
        i = scanner.nextInt();
        scanner.nextLine(); // thanks, java

        while (i != operations.length) {

            switch(i-1) {
                case 0: // new client
                addClient();
                break;

                case 1: // new medic
                addMedic();
                break;

                case 2: // new appointment
                addAppointment();
                break;

                case 3: // all appointments
                getAllAppointments();
                break;

                case 4: // search for appointment
                ManagedAppointment ma = getAppointment();
                if (ma != null) {
                    ma.print();
                } else {
                    System.out.println("Nicio programare gasita.");
                }
                break;

                case 5: // update appointment
                updateAppointment();
                break;

                case 6: // delete appointment
                deleteAppointment();
                break;

                case 7: // all clients
                getAllClients();
                break;

                case 8: // search for client
                System.out.println("Nume sau prenume?");
                String nume = scanner.nextLine();
                ManagedClient mc = getClient(nume);
                if (mc != null) {
                    mc.print();
                } else {
                    System.out.println("Client negasit.");
                }
                break;

                case 9: // all doctors
                getAllMedics();
                break;

                case 10: // search for doctor
                System.out.println("Nume sau prenume?");
                String numeMedic = scanner.nextLine();
                ManagedMedic mm = getMedic(numeMedic);
                if (mm != null) {
                    mm.print();
                } else {
                    System.out.println("Medic negasit");
                }
                break;

                case 11: // update client
                updateClient();
                break;

                case 12: // update medic
                updateMedic();
                break;

                case 13: // delete client
                deleteClient();
                break;

                case 14: // delete medic
                deleteMedic();
                break;

                case 15: // exit
                // pass. Write operation and exit.
                break;
            }

            String operation = operations[i-1];
            op_log(operation);
            System.out.print("[Enter]");
            scanner.nextLine();

            System.out.println();
            for (Integer idx = 0; idx < operations.length; idx++) {
                System.out.println("" + (idx+1) + ". " + operations[idx]);
            }
            System.out.println();
            System.out.print("Actiune: ");
            i = scanner.nextInt();
            scanner.nextLine(); // thanks, java
        }

        String operation = operations[i-1];
        op_log(operation);
        scanner.close();
        scanner = null;
    }

    private void deleteMedic() {
        System.out.println("Care medic?");
        SortDescriptor[] sds = {};
        Pagination p = null;

        Predicate pred = null;
        ManagedMedic[] mms;
        try {
            mms = ManagedMedic.fetch(pred, sds, p);
            for (Integer i = 0; i < mms.length; i++) {
                System.out.println("" + (i+1) + ". Dr. " + mms[i].firstName + " " + mms[i].lastName + ": " + mms[i].email);
            }
            Integer chosen;
            chosen = ManagedMedic.sc.nextInt();
            ManagedMedic.sc.nextLine();
            ManagedMedic mm = mms[chosen - 1]; // client is chosen
            ManagedMedic.delete(mm);
            mm = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteClient() {
        System.out.println("Care client?");
        SortDescriptor[] sds = {};
        Pagination p = null;

        Predicate pred = null;
        ManagedClient[] mcs;
        try {
            mcs = ManagedClient.fetch(pred, sds, p);
            for (Integer i = 0; i < mcs.length; i++) {
                System.out.println("" + (i+1) + ". " + mcs[i].firstName + " " + mcs[i].lastName + ": " + mcs[i].email);
            }
            Integer chosen;
            chosen = ManagedClient.sc.nextInt();
            ManagedClient.sc.nextLine();
            ManagedClient mc = mcs[chosen - 1]; // client is chosen
            ManagedClient.delete(mc);
            mc = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateMedic() {

        Scanner _scanner = new Scanner(System.in);

        System.out.println("Nume sau prenume?");
        String numeMedic = _scanner.nextLine();
        ManagedMedic mm = getMedic(numeMedic);
        if (mm != null) {

            mm.print();
            
            System.out.println("Ce campuri doriti sa actualizati? (Separate cu virgula)");
            String[] options = {"Nume", "Prenume", "Email", "Specialitate", "Cabinet", "Numar de telefon"};
            String[] columns = {"lastName", "firstName", "email", "specialty", "cabinet", "phoneNumber"};
            for(Integer i = 1; i <= options.length; i++) {
                System.out.println("" + i + ". " + options[i-1]);
            }

            String _chosenIndices = _scanner.nextLine();
            Integer[] _chosen = Arrays.stream(_chosenIndices.split(","))
            .map(idx -> {
                return Integer.valueOf(idx);
            })
            .map(_idx -> {
                return _idx-1;
            }).toArray(Integer[]::new);

            String[] _updatedColumns = Arrays.stream(_chosen).map(optionIdx -> {
                return columns[optionIdx];
            }).toArray(String[]::new);

            for(Integer j = 0; j < _chosen.length; j++) {
                System.out.print(options[_chosen[j]] + ": ");
                String newVal = _scanner.nextLine();
                switch (columns[_chosen[j]].toLowerCase()) {
                    case "lastname":
                    mm.lastName = newVal;
                    break;
                    
                    case "firstname":
                    mm.firstName = newVal;
                    break;

                    case "email":
                    mm.email = newVal;
                    break;

                    case "specialty":
                    mm.specialty = newVal;
                    break;

                    case "phonenumber":
                    mm.phoneNumber = newVal;
                    break;

                    case "cabinet":
                    mm.cabinet = Integer.valueOf(newVal);
                    break;
                }
            }

            try {
                ManagedMedic.update(mm, _updatedColumns);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Medic negasit");
        }

        _scanner = null;
    }

    private void updateClient() {
        Scanner _scanner = new Scanner(System.in);

        System.out.println("Nume sau prenume?");
        String numeClient = _scanner.nextLine();
        ManagedClient mm = getClient(numeClient);
        if (mm != null) {

            mm.print();
            
            System.out.println("Ce campuri doriti sa actualizati? (Separate cu virgula)");
            String[] options = {"Nume", "Prenume", "Email", "Numar de telefon", "Data de nastere", "Gen"};
            String[] columns = {"lastName", "firstName", "email", "phoneNumber", "dateOfBirth", "gender"};
            for(Integer i = 1; i <= options.length; i++) {
                System.out.println("" + i + ". " + options[i-1]);
            }

            String _chosenIndices = _scanner.nextLine();
            Integer[] _chosen = Arrays.stream(_chosenIndices.split(","))
            .map(idx -> {
                return Integer.valueOf(idx);
            })
            .map(_idx -> {
                return _idx-1;
            }).toArray(Integer[]::new);

            String[] _updatedColumns = Arrays.stream(_chosen).map(optionIdx -> {
                return columns[optionIdx];
            }).toArray(String[]::new);

            for(Integer j = 0; j < _chosen.length; j++) {
                if (columns[_chosen[j]].toLowerCase().equals("gender")) {
                    System.out.print(options[_chosen[j]] + "(M/F/Other): ");
                } else if (columns[_chosen[j]].toLowerCase().equals("dateofbirth")) {
                    System.out.print(options[_chosen[j]] + "(DD-MM-YYYY): ");
                } else {
                    System.out.print(options[_chosen[j]] + ": ");
                }
                String newVal = _scanner.nextLine();
                switch (columns[_chosen[j]].toLowerCase()) {
                    case "lastname":
                    mm.lastName = newVal;
                    break;
                    
                    case "firstname":
                    mm.firstName = newVal;
                    break;

                    case "email":
                    mm.email = newVal;
                    break;

                    case "phonenumber":
                    mm.phoneNumber = newVal;
                    break;

                    case "dateofbirth":
                    java.util.Date _date;
                    java.sql.Date _sqlDate;

                    DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    try {
                        _date = formatter.parse(newVal);
                        _sqlDate = new java.sql.Date(_date.getTime());
                        mm.dateOfBirth = _sqlDate;
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Reincercati.");
                        _scanner = null;
                        return;
                    }
                    break;

                    case "gender":
                    Gender g;
                    switch (newVal.toLowerCase()) {
                        case "m":
                        g = Gender.M;
                        break;

                        case "f":
                        g = Gender.F;
                        break;

                        default:
                        g = Gender.Other;
                        break;
                    }
                    mm.gender = g;
                    break;
                }
            }

            try {
                ManagedClient.update(mm, _updatedColumns);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Client negasit");
        }

        _scanner = null;
    }

    private void deleteAppointment() {
        System.out.println("Care programare?");
        SortDescriptor[] sds = {};
        Pagination p = null;

        Predicate pred = null;
        ManagedAppointment[] ma;
        try {
            ma = ManagedAppointment.fetch(pred, sds, p);
            for (Integer i = 0; i < ma.length; i++) {
                System.out.println("" + (i+1) + ". ");
                ma[i].print();
            }
            Integer chosen;
            chosen = ManagedAppointment.sc.nextInt();
            ManagedAppointment.sc.nextLine();
            ManagedAppointment mc = ma[chosen - 1]; // client is chosen
            ManagedAppointment.delete(mc);
            mc = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateAppointment() {
        ManagedAppointment ma = getAppointment();
        if (ma == null) {
            return;
        }

        String _dateString;
        java.util.Date _date;
        Timestamp _sqlDate;

        System.out.print("Noua data a programarii (DD-MM-YYYY HH:mm): ");
        _dateString = ManagedAppointment.sc.nextLine();

        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        try {
            _date = formatter.parse(_dateString);
            _sqlDate = new Timestamp(_date.getTime());

            ManagedAppointment.updateDate(ma, _sqlDate);

            System.out.println();
            System.out.println("Noua programare");
            System.out.println();
            ma.print();

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public void initialize() {
        DBContext.getDatabaseConnection();
    }

    public void exit() {
        DBContext.closeDatabaseConnection();
    }

    public void addClient() {
        try {
            ManagedClient.insert(ManagedClient.read());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addMedic() {
        try {
            ManagedMedic.insert(ManagedMedic.read());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addAppointment() {
        try {
            ManagedAppointment.insert(ManagedAppointment.read());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ManagedClient getClient(String firstOrLastName) {
        ManagedClient c = null;

        Predicate preds = new Predicate();
        String[] columns = {"firstName", "lastName"};
        String[] values = {firstOrLastName, firstOrLastName};
        PredicateOperation[] p_ops = {PredicateOperation.LIKE, PredicateOperation.LIKE};
        preds.columns = columns;
        preds.values = values;
        preds.operations = p_ops;
        preds.interPredicateOperation = PredicateOperation.OR;

        SortDescriptor[] sds = {};
        Pagination p = new Pagination();
        p.limit = 1;

        ManagedClient[] mc;
        try {
            mc = ManagedClient.fetch(preds, sds, p);
            if (mc.length > 0) {
                c = mc[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    public ManagedMedic getMedic(String firstOrLastName) {
        ManagedMedic c = null;

        Predicate preds = new Predicate();
        String[] columns = {"firstName", "lastName"};
        String[] values = {firstOrLastName, firstOrLastName};
        PredicateOperation[] p_ops = {PredicateOperation.LIKE, PredicateOperation.LIKE};
        preds.columns = columns;
        preds.values = values;
        preds.operations = p_ops;
        preds.interPredicateOperation = PredicateOperation.OR;

        SortDescriptor[] sds = {};
        Pagination p = new Pagination();
        p.limit = 1;

        ManagedMedic[] mc;
        try {
            mc = ManagedMedic.fetch(preds, sds, p);
            if (mc.length > 0) {
                c = mc[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    public ManagedAppointment getAppointment() {
        
        System.out.println("Cautare dupa client sau medic? m/c");
        
        Scanner _scanner = new Scanner(System.in);
        String  r       = _scanner.nextLine();


        System.out.println("Nume: ");
        String  nume    = _scanner.nextLine();
        ManagedClient  c       = null;
        ManagedMedic   m       = null;

        String  ch      = r.toLowerCase();

        if (ch.equals("m")) {
            m = getMedic(nume);
        } else if (ch.equals("c")) {
            c = getClient(nume);
        }

        SortDescriptor[] sds = {};
        Pagination p = null;
        Predicate pred = null;
        ManagedAppointment[] ma;
        try {
            ma = ManagedAppointment.fetch(pred, sds, p);
        } catch (Exception e) {
            e.printStackTrace();
            _scanner = null;
            return null;
        }

        ArrayList<ManagedAppointment> ma_l = new ArrayList<ManagedAppointment>();
        for (ManagedAppointment prog : ma) {
            if (m != null && prog.getMedic().id == m.id) {
                ma_l.add(prog);
            }

            if (c != null && prog.getClient().id == c.id) {
                ma_l.add(prog);
            }
        }

        if (ma_l.size() == 0) {
            _scanner = null;
            return null;
        }

        if (ma_l.size() == 1) {
            _scanner = null;
            return ma_l.get(0);
        }

        System.out.println("Pe care dintre programari vreti sa o selectati?");

        for(Integer i = 0; i < ma_l.size(); i++) {
            System.out.println("" + (i+1) + ".");
            ma_l.get(i).print();
            System.out.println();
        }

        Integer chosen;
        chosen = _scanner.nextInt();
        _scanner.nextLine();
        _scanner = null;
        return ma_l.get(chosen - 1);
    }

    public void getAllAppointments() {
        SortDescriptor[] sds = {};
        Pagination p = null;
        Predicate pred = null;
        try {
            for (var ma: ManagedAppointment.fetch(pred, sds, p)) {
                ma.print();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getAllMedics() {
        SortDescriptor[] sds = {};
        Pagination p = null;
        Predicate pred = null;
        try {
            for (var mm: ManagedMedic.fetch(pred, sds, p)) {
                mm.print();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getAllClients() {
        SortDescriptor[] sds = {};
        Pagination p = null;
        Predicate pred = null;
        try {
            for (var c: ManagedClient.fetch(pred, sds, p)) {
                c.print();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void op_log(String operation) {
        File f = new File(System.getProperty("user.dir") + "/data/log.csv");
        
        if (!f.exists()) {
            try {
                f.getParentFile().mkdirs();
                f.createNewFile();

                FileWriter fw = new FileWriter(f, true);

                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                String s = operation + ", " + formatter.format(new Date()) + "\n";
                fw.append(s);
                fw.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            try{
                FileWriter fw = new FileWriter(f, true);

                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                String s = operation + ", " + formatter.format(new Date()) + "\n";
                fw.append(s);
                fw.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
