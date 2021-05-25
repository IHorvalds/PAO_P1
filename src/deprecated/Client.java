package deprecated;
import java.lang.reflect.InvocationTargetException;

// import java.io.File;
// import java.io.FileWriter;

public class Client extends Queryable {
    private String lastName, firstName, phoneNumber;

    static Integer lastId = 0;

    public Client() {
        Queryable.addNewStore(Client.class);
    }

    public Client(String firstName, String lastName, String phoneString) {
        this.id = lastId;
        lastId += 1;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneString;

        Queryable.addNewStore(Client.class);
    }
    
    public static Client read() {
        String _lastName, _firstName, _phoneNumber;
        
        System.out.print("Prenume client: ");
        _firstName = Queryable.sc.nextLine();
        
        System.out.print("Nume client: ");
        _lastName = Queryable.sc.nextLine();

        System.out.print("Numar de telefon: ");
        _phoneNumber = Queryable.sc.nextLine();

        return new Client(_firstName, _lastName, _phoneNumber);
    }

    public void print() {
        System.out.println("Client: " + getFullName());
        System.out.println("Numar de telefon: " + getPhoneNumber());
        System.out.println();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String newFirstName) {
        this.firstName = newFirstName;
        Client.update(this);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String newLastName) {
        this.lastName = newLastName;
        Client.update(this);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String newPhoneNumber) {
        this.phoneNumber = newPhoneNumber;
        Client.update(this);
    }
}
