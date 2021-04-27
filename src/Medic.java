public class Medic extends Queryable {
    private String lastName, firstName, phoneNumber, email;
    private Integer startingHour, endOfDay, cabinetNumber;

    static Integer lastId = 0;

    public Medic() {
        Queryable.addNewStore(Medic.class);
    }

    public Medic(String firstName, String lastName, String phoneString, String email,
                 Integer start, Integer end, Integer cabinet) {
        this.id = lastId;
        lastId += 1;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneString;
        this.email = email;
        this.startingHour = start;
        this.endOfDay = end;
        this.cabinetNumber = cabinet;

        Queryable.addNewStore(Medic.class);
    }

    public static Medic read() {
        String _lastName, _firstName, _phoneNumber, _email;
        Integer _startingHour, _endOfDay, _cabinet;
        
        System.out.print("Prenume medic: ");
        _firstName = Queryable.sc.nextLine();
        
        System.out.print("Nume medic: ");
        _lastName = Queryable.sc.nextLine();

        System.out.print("Numar de telefon: ");
        _phoneNumber = Queryable.sc.nextLine();

        System.out.print("Email medic: ");
        _email = Queryable.sc.nextLine();

        System.out.print("Ora de inceput: ");
        _startingHour = Queryable.sc.nextInt();

        System.out.print("Ora de sfarsit: ");
        _endOfDay = Queryable.sc.nextInt();

        System.out.print("Cabinet: ");
        _cabinet = Queryable.sc.nextInt();
        Queryable.sc.nextLine(); // consume the last \n. Because of course it doesn't by default....
        // Such a wonderful and consistent API...

        return new Medic(_firstName, _lastName, _phoneNumber, _email, _startingHour, _endOfDay, _cabinet);
    }

    public void print() {
        System.out.println("Medic: " + getFullName());
        System.out.println("Numar de telefon: " + getPhoneNumber());
        System.out.println("Email: " + getEmail());
        System.out.println("Cabinet: " + getCabinetNumber());
        System.out.println("Ora de inceput: " + getStartingTime());
        System.out.println("Ora de final: " + getLastHour());
        System.out.println();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String newFirstName) {
        this.firstName = newFirstName;
        Medic.update(this);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String newLastName) {
        this.lastName = newLastName;
        Medic.update(this);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String newPhoneNumber) {
        this.phoneNumber = newPhoneNumber;
        Medic.update(this);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String newEmail) {
        this.email = newEmail;
        Medic.update(this);
    }

    public Integer getStartingTime() {
        return startingHour;
    }

    public void setStartingTime(Integer newStartingTime) {
        this.startingHour = newStartingTime;
        Medic.update(this);
    }

    public Integer getLastHour() {
        return endOfDay;
    }

    public void setEndingTime(Integer newEndTime) {
        this.endOfDay = newEndTime;
        Medic.update(this);
    }

    public Integer getScheduleLength() {
        return endOfDay - startingHour;
    }

    public Integer getCabinetNumber() {
        return cabinetNumber;
    }

    public void setCabinetNumber(Integer newCabinet) {
        this.cabinetNumber = newCabinet;
        Medic.update(this);
    }
}
