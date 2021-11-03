import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class SendHandler {

    public static String post(String url, String param) throws Exception {
        final String charset = "UTF-8";
        final byte[] data0 = param.getBytes(StandardCharsets.UTF_8);
        URLConnection connection = new URL(url).openConnection();
        connection.setDoOutput(true); // Triggers POST.
        connection.setRequestProperty("Accept-Charset", charset);
        connection.setRequestProperty("Content-Type", "application/json;charset=" + charset);
        connection.setRequestProperty("Content-Length", String.valueOf(data0.length));
        try (OutputStream output = connection.getOutputStream()) {
            output.write(data0);
        }
        //System.out.println(param);

        String data = "";
        InputStream iStream = connection.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(iStream, StandardCharsets.UTF_8));
        StringBuffer sb = new StringBuffer();
        String line = "";

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        data = sb.toString();

        return data;
    }


    public static void sendUpdate(String url, ArrayList<Personal> pers) {
        Gson gson = new Gson();
            //String json = gson.fromJson(String.valueOf(pers), new TypeToken<List<Personal>>(){}.getType());
            StringBuilder persons = new StringBuilder();
            persons.append("{\"name\":[");
            int size = 11;//было 13
            int counter = 0;
            for (Personal x: pers) {
                String json = gson.toJson(x, new TypeToken<Personal>(){}.getType());
                byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
                if ((size + bytes.length) > (512*1024)) {
                    String tmp = persons.append("]}").toString();
                    System.out.println("Отправил полный пакет:");
                    System.out.println(tmp);
                    try {
                        String answer = post(url, tmp);
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    persons.delete(9, persons.length());
                    size = 11;
                    counter = 0;
                    continue;
                }
                if (persons.length() > 9) {
                    persons.append(",");
                    size += 1;
                }
                persons.append(json);
                size += bytes.length;
                counter++;
            }
        if (counter > 0) { //100% я тут обосрался
            String tmp = persons.append("]}").toString();
            System.out.println("Отправил неполный пакет:");
            System.out.println(tmp);
            try {
                String answer = post(url, tmp);
                Thread.sleep(1000);
                //System.out.println(answer);
            } catch (Exception e) {
                e.printStackTrace();
            }
            persons.delete(9, persons.length());
        }
    }

}
