public class Main {
    public static void main(String[] args) throws Exception {
        // System.out.println("Add a few clients");
        // Service.getInstance().addClient();
        // Service.getInstance().addClient();
        // System.out.println();

        Service.getInstance().initialize();

        Service.getInstance().Menu(); // main menu

        Service.getInstance().saveState();

        // System.out.println("Here they are");
        // Service.getInstance().getAllClients();
        // Service.getInstance().saveClients();
        // System.out.println();

        // System.out.println("Add a few medics");
        // Service.getInstance().addMedic();
        // Service.getInstance().addMedic();
        // System.out.println();

        // System.out.println("Here they are");
        // Service.getInstance().getAllMedics();
        // System.out.println();

        // System.out.println("And finally some appointments");
        // Service.getInstance().addProgramare();
        // Service.getInstance().addProgramare();
        // System.out.println();

        // System.out.println("Here they are");
        // Service.getInstance().getAllProgramari();
        // System.out.println();
    }
}
