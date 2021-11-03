import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUI extends JFrame{
    //private ArrayList<String> items = new ArrayList<>();
    private JButton textFieldButton = new JButton("Добавить");
    private JTextField inputCollege = new JTextField("", 30);
    private JButton comboBoxButton = new JButton("Старт");
    private JTextField inputLoginadm = new JTextField("", 30);
    private JTextField inputPasswordadm = new JTextField("", 30);
    private JTextField inputLoginsec = new JTextField("", 30);
    private JTextField inputPasswordsec = new JTextField("", 30);
    private JLabel mainLabel = new JLabel("Добавьте новое название колледжа или выберите существующее:");
    private JLabel colLabel = new JLabel("Колледж: ");
    private JLabel logLabeladm = new JLabel("Логин (Администратора): ");
    private JLabel passLabeladm = new JLabel("Пароль (Администратора):");
    private JLabel logLabelsec = new JLabel("Логин (Охраны): ");
    private JLabel passLabelsec = new JLabel("Пароль (Охраны):");
    private JLabel comboLabel = new JLabel("Выберите колледж");
    private JComboBox comboBox = new JComboBox();
    Manage.Commands commands = new Manage.Commands();


    public GUI () {
        super("Sms info");
        this.setSize(500,500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        final String imgPath = "gELQDi1xIaA.png";
//        this.setIconImage(Toolkit.getDefaultToolkit().getImage(GUI.class.getResource(imgPath)));
        getContentPane().add(create());
        getList();
        textFieldButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InputData inputData = new InputData();
                inputData.college = inputCollege.getText();
                inputData.login = inputLoginadm.getText();
                inputData.password = inputPasswordadm.getText();
                inputData.sec_login = inputLoginsec.getText();
                inputData.sec_password = inputPasswordsec.getText();
                Gson gson = new Gson();
                String url1 = DBHandler.url + "colleges.php";
                String json = gson.toJson(inputData, new TypeToken<InputData>(){}.getType());
                try {
                    SendHandler.post(url1, json);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                getList();
            }
        });

        comboBoxButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DBHandler.cid = commands.arr.get(comboBox.getSelectedIndex())[0];
                System.out.println(DBHandler.cid);
                getIdLevel();
                Main.manage.start();
            }
        });


    }

    private JPanel create() {
        // Создание панели для размещение компонентов
        JPanel panel = BoxLayoutUtils.createVerticalPanel();
        // Определение отступов от границ ранели. Для этого используем пустую рамку
        panel.setBorder (BorderFactory.createEmptyBorder(12,12,12,12));
        // Создание панели для размещения метки и текстового поля логина
        JPanel label = BoxLayoutUtils.createHorizontalPanel();
        label.add(mainLabel);
        JPanel college = BoxLayoutUtils.createHorizontalPanel();
        college.add(colLabel);
        college.add(Box.createHorizontalStrut(20));
        college.add(inputCollege);
        JPanel login = BoxLayoutUtils.createHorizontalPanel();
        login.add(logLabeladm);
        login.add(Box.createHorizontalStrut(20));
        login.add(inputLoginadm);
        JPanel password = BoxLayoutUtils.createHorizontalPanel();
        password.add(passLabeladm);
        password.add(Box.createHorizontalStrut(20));
        password.add(inputPasswordadm);

        JPanel sec_login = BoxLayoutUtils.createHorizontalPanel();
        sec_login.add(logLabelsec);
        sec_login.add(Box.createHorizontalStrut(20));
        sec_login.add(inputLoginsec);
        JPanel sec_password = BoxLayoutUtils.createHorizontalPanel();
        sec_password.add(passLabelsec);
        sec_password.add(Box.createHorizontalStrut(20));
        sec_password.add(inputPasswordsec);

        JPanel start1 = BoxLayoutUtils.createHorizontalPanel();
        start1.add(textFieldButton);
        JPanel comboboxdata = BoxLayoutUtils.createHorizontalPanel();
        comboboxdata.add(comboLabel);
        comboboxdata.add(Box.createHorizontalStrut(20));
        comboboxdata.add(comboBox);
        JPanel start2 = BoxLayoutUtils.createHorizontalPanel();
        start2.add(comboBoxButton);

        // Выравнивание вложенных панелей по горизонтали
        BoxLayoutUtils.setGroupAlignmentX(new JComponent[] { label, college, login, password, sec_login, sec_password, start1, comboboxdata, start2 },
                Component.LEFT_ALIGNMENT);
        // Выравнивание вложенных панелей по вертикали
        BoxLayoutUtils.setGroupAlignmentY(new JComponent[] { label, college, login, password, sec_login, sec_password, start1, comboboxdata, start2},
                Component.CENTER_ALIGNMENT);
        // Определение размеров надписей к текстовым полям
//        GUITools.makeSameSize(new JComponent[] { nameLabel, passwrdLabel } );
//        // Определение стандартного вида для кнопок
//        GUITools.createRecommendedMargin(new JButton[] { btnOk, btnCancel } );
        // Устранение "бесконечной" высоты текстовых полей
        GUITools.fixTextFieldSize(inputCollege);
        GUITools.fixTextFieldSize(inputLoginadm);
        GUITools.fixTextFieldSize(inputPasswordadm);
        GUITools.fixTextFieldSize(inputLoginsec);
        GUITools.fixTextFieldSize(inputPasswordsec);
        GUITools.fixComboBoxSize(comboBox);


        // Сборка интерфейса
        panel.add(label);
        panel.add(Box.createVerticalStrut(12));
        panel.add(college);
        panel.add(Box.createVerticalStrut(12));
        panel.add(login);
        panel.add(Box.createVerticalStrut(12));
        panel.add(password);
        panel.add(Box.createVerticalStrut(12));
        panel.add(sec_login);
        panel.add(Box.createVerticalStrut(12));
        panel.add(sec_password);
        panel.add(Box.createVerticalStrut(20));
        panel.add(start1);
        panel.add(Box.createVerticalStrut(20));
        panel.add(comboboxdata);
        panel.add(Box.createVerticalStrut(20));
        panel.add(start2);

        // готово
        return panel;
    }

    public void getList() {
        comboBox.removeAllItems();
        Gson gson1 = new Gson();
        try {
            String response = SendHandler.post(DBHandler.url + "comboboxinfo.php", "");
            commands = gson1.fromJson(response, new TypeToken<Manage.Commands>(){}.getType());
            for(String[] x: commands.arr) {
                System.out.println("Получаю cid колледжа");
                System.out.println(DBHandler.cid);
                comboBox.addItem(x[1]);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getIdLevel() {
        String url1 = DBHandler.url + "checkidlevel.php?cid=" + DBHandler.cid;
        try {
            DBHandler.idlevel = SendHandler.post(url1, "");
            System.out.println("Получаю idlevel");
            System.out.println(DBHandler.idlevel);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
