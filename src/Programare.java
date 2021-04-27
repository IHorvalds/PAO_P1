import java.text.*;
import java.util.*;

public class Programare extends Queryable {
    
    private Integer clientId, medicId;
    private Date appointmentDate;

    static Integer lastId = 0;

    public Programare() {
        Queryable.addNewStore(Programare.class);
    }

    public Programare(Client c, Medic m, Date d) {
        this.id                 = lastId;
        lastId                  += 1;
        this.clientId           = c.id;
        this.medicId            = m.id;
        this.appointmentDate    = d;

        Queryable.addNewStore(Programare.class);
    }

    public static Programare read() {
        String _clientName, _medicName, _dateString;
        Date _date;
        
        System.out.print("Nume sau prenume client: ");
        _clientName = Queryable.sc.nextLine();
        
        System.out.print("Nume sau prenume medic: ");
        _medicName = Queryable.sc.nextLine();

        System.out.print("Data programarii (DD-MM-YYYY HH:mm): ");
        _dateString = Queryable.sc.nextLine();

        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        try {
            _date = formatter.parse(_dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        Client c = Service.getInstance().getClient(_clientName);
        Medic m = Service.getInstance().getMedic(_medicName);

        if (c == null || m == null) {
            System.out.println("Client or Medic not in the system. Check who is missing and add them.");
            return null;
        }

        return new Programare(c, m, _date);
    }

    public void print() {
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        System.out.println("Programare pe data " + formatter.format(appointmentDate));
        System.out.println("-------------------------------------------------------");
        System.out.println("Pacient: \t" + getClient().getFullName());
        System.out.println("Medic: \t\t" + getMedic().getFullName());
        System.out.println("Cabinet: \t" + getMedic().getCabinetNumber());
        System.out.println();
    }

    public Client getClient() {
        try {
            return (Client)Client.get(clientId, Client.class);
        } catch (Error e) {
            return null;
        }
    }

    public Medic getMedic() {
        try {
            return (Medic)Medic.get(medicId, Medic.class);
        } catch (Error e) {
           return null;
        }
    }

    public Date getAppointDate() {
        return appointmentDate;
    }

    public void setAppointDate(Date newDate) {
        appointmentDate = newDate;

        Programare.update(this);
    }
}
