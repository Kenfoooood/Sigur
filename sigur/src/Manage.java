import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class Manage extends Thread implements Runnable {
    private String cid = "1";
    private String add = "0";
    private String update = "1";
    private int delay = 10000;
    private int counter = 0;
    public static class Commands {
        ArrayList<String[]> arr;
    }
    DBHandler manager = new DBHandler();
    @Override
    public void run() {
        while (true) {
            Gson gson = new Gson();
            String cmd;
            Personal person = new Personal();
            try {
                String data = manager.getCommands();
                Commands commands = gson.fromJson(data, new TypeToken<Commands>(){}.getType()); //достаем из строки инфу о команде
                for (String[] x: commands.arr) {
                    System.out.println("Получаю команду (добавить запись, отредактировать запись, ...)");
                    System.out.printf("%s %s %s\n", x[0], x[1], x[2]);
                    cmd = x[1];
                    if (cmd.equals(add)) {
                        Personal persProp = gson.fromJson(x[2], new TypeToken<Personal>(){}.getType()); //достаем из строки инфу про чела
                        manager.addPerson(persProp);
                    }
                    if (cmd.equals(update)) {
                        Personal persProp = gson.fromJson(x[2], new TypeToken<Personal>(){}.getType());
                        manager.updatePerson(persProp);
                    }
                }
                manager.showChanges();
                if (commands.arr.isEmpty()) {
                    counter++;
                    if (counter == 10) {
                        delay = 120000;
                    }
                }
                else {
                    delay = 10000;
                    counter = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Manage.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
