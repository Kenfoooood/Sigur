public class Main {
    static Thread manage = new Manage();
    public static void main(String[] args) {
        try {
            GUI gui = new GUI();
            gui.setVisible(true);
            System.out.println("Получаю cid в мэйне");
            System.out.println(DBHandler.cid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
