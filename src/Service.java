public class Service {
    private Service() {}

    private static Service shared;
    public static Service getInstance() {
        if (shared == null)
            shared = new Service();
  
        return shared;
    }

    public void addClient() {
        Client.insert(Client.read(), Client.class);
    }

    public void addMedic() {
        Medic.insert(Medic.read(), Medic.class);
    }

    public void addProgramare() {
        Programare.insert(Programare.read(), Programare.class);
    }

    public Client getClient(String firstOrLastName) {
        Client c = null;

        for (var client: Client.getAll(Client.class)) {
            Client cl = (Client)client;
            if (cl.getFullName().contains(firstOrLastName)) {
                c = cl;
                return c;
            }
        }

        return c;
    }

    public Medic getMedic(String firstOrLastName) {
        Medic c = null;

        for (var m: Medic.getAll(Medic.class)) {
            Medic md = (Medic)m;
            if (md.getFullName().contains(firstOrLastName)) {
                c = md;
                return c;
            }
        }

        return c;
    }

    public void getAllProgramari() {
        for (var p: Programare.getAll(Programare.class)) {
            Programare pr = (Programare)p;
            pr.print();
        }
    }

    public void getAllMedics() {
        for (var p: Medic.getAll(Medic.class)) {
            Medic m = (Medic)p;
            m.print();
        }
    }

    public void getAllClients() {
        for (var c: Client.getAll(Client.class)) {
            Client cl = (Client)c;
            cl.print();
        }
    }
}
