public class Main {
    public static void main(String[] args) throws Exception {
        
        Service.getInstance().initialize();

        Service.getInstance().Menu(); // main menu

        Service.getInstance().exit();     
    }
}
