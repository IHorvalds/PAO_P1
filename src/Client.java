// import java.io.File;
// import java.io.FileWriter;

public class Client extends Queryable {
    private String lastName, firstName, phoneNumber;

    static Integer lastId = 0;

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
        Client.update(this, Client.class);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String newLastName) {
        this.lastName = newLastName;
        Client.update(this, Client.class);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String newPhoneNumber) {
        this.phoneNumber = newPhoneNumber;
        Client.update(this, Client.class);
    }

    // TODO: Maybe next stage
    // public String serialize() {
    //     String s = "{";
    //     s = s.concat("id: " + id + ",");
    //     s = s.concat("first_name: " + firstName + ",");
    //     s = s.concat("last_name: " + firstName + ",");
    //     s = s.concat("phone_number: " + firstName + ",");
    //     return "";
    // }

    // public void save() {
    //     String className = Client.class.getSimpleName();

    //     // Serializing all objects
    //     String json_start = "{objects:[";
    //     for (var ob : _store) {
    //         json_start = json_start.concat(ob.serialize());
    //     }
    //     String json_end = "]}";

    //     json_start = json_start.concat(json_end);

    //     // Try to write it out
    //     try {
    //         File f = new File(className + ".json");

    //         if (!f.exists()) {
    //             f.createNewFile();
    //         }

    //         FileWriter fw = new FileWriter(f);

    //         fw.write(json_start);
    //         fw.close();
    //     } catch (Exception e) {
    //         System.out.println("An error occurred.");
    //         e.printStackTrace();
    //     }

    // }
}
