import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

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
                                "Cauta o programare", "Afiseaza clienti", "Cauta un client", "Afiseaza doctori", "Cauta un doctor", "Iesire"};

        for (Integer i = 0; i < operations.length; i++) {
            System.out.println("" + (i+1) + ". " + operations[i]);
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
                addMedic();
                break;

                case 3: // all appointments
                getAllProgramari();
                break;

                case 4: // search for appointment
                getProgramare();
                break;

                case 5: // all clients
                getAllClients();
                break;

                case 6: // search for client
                String  nume    = scanner.nextLine();
                getClient(nume).print();
                break;

                case 7: // all doctors
                getAllMedics();
                break;

                case 8: // search for doctor
                String  numeMedic = scanner.nextLine();
                getMedic(numeMedic).print();
                break;

                case 9: // exit
                // pass. Write operation and exit.
                break;
            }

            String operation = operations[i-1];
            op_log(operation);
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

    public void initialize() {
        Class[] cs = {Client.class, Medic.class, Programare.class};

        for (Class c : cs) {
            try {
                Queryable.readAll(c);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void saveState() {
        Class[] cs = {Client.class, Medic.class, Programare.class};

        for (Class c : cs) {
            try {
                Queryable.saveAll(c);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addClient() {
        Client.insert(Client.read());
    }

    public void addMedic() {
        Medic.insert(Medic.read());
    }

    public void addProgramare() {
        Programare.insert(Programare.read());
    }

    public Client getClient(String firstOrLastName) {
        Client c = null;

        for (var client: Client.getAll(Client.class)) {
            if (client.getFullName().contains(firstOrLastName)) {
                return client;
            }
        }

        return c;
    }

    public Medic getMedic(String firstOrLastName) {
        Medic c = null;

        for (var m: Medic.getAll(Medic.class)) {
            if (m.getFullName().contains(firstOrLastName)) {
                return m;
            }
        }

        return c;
    }

    public Programare getProgramare() {
        
        System.out.println("Cautare dupa client sau medic? m/c");
        
        Scanner scanner = new Scanner(System.in);
        String  r       = scanner.nextLine();


        System.out.println("Nume: ");
        String  nume    = scanner.nextLine();
        Client  c       = null;
        Medic   m       = null;

        String  ch      = r.toLowerCase();

        if (ch == "m") {
            m = getMedic(nume);
        } else if (ch == "c") {
            c = getClient(nume);
        }


        for (var prog : Programare.getAll(Programare.class)) {
            if (m != null && prog.getMedic().id == m.id) {
                scanner.close();
                scanner = null;

                return prog;
            }

            if (c != null && prog.getClient().id == c.id) {
                scanner.close();
                scanner = null;

                return prog;
            }
        }

        scanner.close();
        scanner = null;

        return null;
    }

    public void getAllProgramari() {
        for (var p: Programare.getAll(Programare.class)) {
            p.print();
        }
    }

    public void getAllMedics() {
        for (var p: Medic.getAll(Medic.class)) {
            p.print();
        }
    }

    public void getAllClients() {
        for (var c: Client.getAll(Client.class)) {
            c.print();
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
