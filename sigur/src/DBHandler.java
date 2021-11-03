import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;




public class DBHandler {
    static String url = "http://smsinfb9.beget.tech/";
    //static String url = "http://iothach01.000webhostapp.com/sigur/";
    Connection conn = null;
    boolean isConnected = false;
    static String idlevel = "";
    static String cid = "";
    public DBHandler() {
        //this.main = main;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3305/tc-db-main?characterEncoding=utf8";
            conn = DriverManager.getConnection(url,"root","");
            System.out.println("Connection to mysql has been established.");
            isConnected = true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addPerson(Personal personal) {
        if (!isConnected) {
            return;
        }
            try {
                Statement statement = conn.createStatement();
                personal.status = "AVAILABLE";
                personal.type = "EMP";
                byte[] bytes = Base64.getDecoder().decode(personal.name);
                personal.name = new String(bytes, StandardCharsets.UTF_8);
                String str = "INSERT INTO `personal` (`PARENT_ID`, `TYPE`, `NAME`, `DESCRIPTION`, `SMS_TARGETNUMBER`, `STATUS`, `CODEKEY`, `NTFY_PASS_ENABLED`) VALUES ('"+personal.parent_id+"'," +
                        "'"+personal.type+"', '"+personal.name+"', '"+personal.description+"', '"+personal.sms_targetnumber+"', '"+personal.status+"', CAST(0x00 AS BINARY(8)), '1')";
                System.out.println("SQL query: " + str);
                PreparedStatement preState = conn.prepareStatement(str);
                preState.executeUpdate();
                //ResultSet resultSet = statement.executeQuery(str);
            } catch (SQLException e) {
                e.printStackTrace();
            }

    }
//    public void activatePerson(String key, String id) {
//        if (!isConnected) {
//            return;
//        }
//            try (Statement statement = conn.createStatement()) {
//                //long left = key/100000;
//                //long right = key%100000;
//                //long digit = (((0x18 << 8) + left << 16) + right) << 32;
//                //String.format("0x%016x", key)
//                String str = "UPDATE `personal` SET `CODEKEY` = CAST("+key+" AS BINARY(8)), `STATUS` = 'AVAILABLE' WHERE id = '"+id+"'";
//                PreparedStatement preState = conn.prepareStatement(str);
//                //preState.setBytes(1, new byte[]{0x18, 0x55,0,0,0,0,0,0});
//                preState.executeUpdate();
//                //ResultSet resultSet = statement.executeQuery(str);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//    }
//
//    public void deactivatePerson(String id) {
//        if (!isConnected) {
//            return;
//        }
//            try {
//                Statement statement = conn.createStatement();
//                String str = "UPDATE `personal` SET `STATUS` = 'FIRED' WHERE id = '"+id+"'";
//                ResultSet resultSet = statement.executeQuery(str);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//    }

    enum Myenum{
        AVAILABLE,
        FIRED
    }

    public void updatePerson(Personal pers) {
        if (!isConnected) {
            return;
        }
        try {
            Statement statement = conn.createStatement();
            long key = Long.parseLong(pers.codekey);
//            long left = key/100000;
//            long right = key%100000;
//            long digit = (((0x18 << 8) + left << 16) + right) << 32;
            pers.codekey = String.format("0x%016x", key);
            byte[] bytes = Base64.getDecoder().decode(pers.name);
            pers.name = new String(bytes, StandardCharsets.UTF_8);
            String str = "UPDATE `personal` SET `NAME` = '"+pers.name+"', `STATUS` = '"+pers.status+"', `SMS_TARGETNUMBER` = '"+pers.sms_targetnumber+"', `CODEKEY` = CAST("+pers.codekey+" AS BINARY(8)), `NTFY_PASS_ENABLED` = '1' WHERE ID = '"+pers.id+"'";
            System.out.println("SQL query1: " + str);
            PreparedStatement preState = conn.prepareStatement(str);
            Myenum obj = Myenum.AVAILABLE;
            //preState.setObject(1, obj);
            preState.executeUpdate();
            //ResultSet resultSet = statement.executeQuery(str);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showChanges() throws Exception {
        if (!isConnected) {
            return;
        }
        String str = "SELECT ID, PARENT_ID, TYPE, NAME, DESCRIPTION, SMS_TARGETNUMBER, STATUS, CODEKEY FROM `personal` WHERE ID > '"+idlevel+"' order by id asc";
        ArrayList<Personal> arr = new ArrayList<>();
        try (Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(str);
            while (resultSet.next()) {
                Personal pers = new Personal();
                pers.id = resultSet.getString("ID");
                pers.parent_id = resultSet.getString("PARENT_ID");
                pers.type = resultSet.getString("TYPE");
                pers.name = resultSet.getString("NAME");
                if (resultSet.getString("DESCRIPTION") == null || resultSet.getString("DESCRIPTION").isEmpty()) {
                    pers.description = "null";
                }
                else {
                    pers.description = resultSet.getString("DESCRIPTION");
                }
                if (resultSet.getString("SMS_TARGETNUMBER") == null || resultSet.getString("SMS_TARGETNUMBER").isEmpty()) {
                    pers.sms_targetnumber = "null";
                }
                else {
                    pers.sms_targetnumber = resultSet.getString("SMS_TARGETNUMBER");
                }
                pers.status = resultSet.getString("STATUS");
                if (resultSet.getString("CODEKEY") == null || resultSet.getString("CODEKEY").isEmpty()) {
                    pers.codekey = "null";
                }
                else {
                    byte[] bytes = resultSet.getBytes("CODEKEY");
                    long tmp = 0;
                    for (byte x: bytes) {
                        tmp <<= 8;
                        short a = (short)((short)0xff&(short)x);
                        tmp |= a;
                    }
//                    pers.codekey = String.format("0x%016x", tmp);
                    pers.codekey = String.valueOf(tmp);
                }
                arr.add(pers);
                idlevel = pers.id;
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String url1 = url + "sigur.php?cid=" + cid;
        //String cid = "1";
        //String url = "http://localhost:8080/sigur.php?cid=" + cid;
        if (!arr.isEmpty()) {
            SendHandler.sendUpdate(url1, arr);
            System.out.println("Скачал  записи с сигура:");
            System.out.println(arr.toString());
        }

    }


    public String getCommands() throws Exception{
        String data;
        //String cid = "1";
        String url2 = url + "sigcomp.php?cid=" + cid;
        data = SendHandler.post(url2, "");
        System.out.println("Получаю команду и данные");
        System.out.println(data);
        return data;
    }




}
