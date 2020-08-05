package gg.codie.mineonline.api;

import gg.codie.mineonline.Globals;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.LinkedList;

public class MinecraftAPI {

    public static String getMpPass (String sessionId, String serverIP, String serverPort) {
        HttpURLConnection connection = null;

        try {
            String parameters = "sessionId=" + URLEncoder.encode(sessionId, "UTF-8") + "&serverIP=" + URLEncoder.encode(serverIP, "UTF-8") + "&serverPort=" + URLEncoder.encode(serverPort, "UTF-8");
            URL url = new URL("http://" + Globals.API_HOSTNAME + "/mineonline/mppass.jsp?" + parameters);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.connect();

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            String mpPass = rd.readLine();

            rd.close();

            return mpPass;
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        } finally {

            if (connection != null)
                connection.disconnect();
        }
    }

    public static boolean removecloak(String sessionId) {
        HttpURLConnection connection = null;

        try {
            String parameters = "sessionId=" + URLEncoder.encode(sessionId, "UTF-8");
            URL url = new URL("http://" + Globals.API_HOSTNAME + "/mineonline/removecloak.jsp?" + parameters);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.connect();

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            String res = rd.readLine();

            rd.close();

            return res.equals("ok");
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        } finally {

            if (connection != null)
                connection.disconnect();
        }
    }

    public static boolean uploadSkin(String uuid, String sessionId, InputStream skinFile) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL("http://" + Globals.API_HOSTNAME + "/mineonline/player/" + uuid + "/skin");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.getOutputStream().write(ByteBuffer.allocate(2).putShort((short)sessionId.length()).array());
            connection.getOutputStream().write(sessionId.getBytes(Charset.forName("UTF-8")));

            int skinSize = skinFile.available();

            connection.getOutputStream().write(ByteBuffer.allocate(4).putInt(skinSize).array());

            byte[] buffer = new byte[8096];
            int bytes_read = 0;
            while ((bytes_read = skinFile.read(buffer, 0, 8096)) != -1) {
                for(int i = 0; i < bytes_read; i++) {
                    System.out.print(buffer[i]);
                    connection.getOutputStream().write(buffer[i]);
                }
            }

            connection.getOutputStream().flush();
            connection.getOutputStream().close();

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            String res = rd.readLine();

            rd.close();

            return res.equals("ok");
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        } finally {

            if (connection != null)
                connection.disconnect();
        }

    }

    public static boolean uploadCloak(String uuid, String sessionId, InputStream cloakFile) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL("http://" + Globals.API_HOSTNAME + "/mineonline/player/" + uuid + "/cloak.jsp");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.getOutputStream().write(ByteBuffer.allocate(2).putShort((short)sessionId.length()).array());
            connection.getOutputStream().write(sessionId.getBytes(Charset.forName("UTF-8")));

            int cloakSize = cloakFile.available();

            connection.getOutputStream().write(ByteBuffer.allocate(4).putInt(cloakSize).array());

            byte[] buffer = new byte[8096];
            int bytes_read = 0;
            while ((bytes_read = cloakFile.read(buffer, 0, 8096)) != -1) {
                for(int i = 0; i < bytes_read; i++) {
                    System.out.print(buffer[i]);
                    connection.getOutputStream().write(buffer[i]);
                }
            }

            connection.getOutputStream().flush();
            connection.getOutputStream().close();

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            String res = rd.readLine();

            rd.close();

            return res.equals("ok");
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        } finally {

            if (connection != null)
                connection.disconnect();
        }

    }

    public static String login(String username, String password) throws IOException {
        HttpURLConnection connection = null;

        String parameters = "user=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8") + "&version=" + '\f';

        URL url = new URL("http://" + Globals.API_HOSTNAME + "/game/getversion.jsp");
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        connection.setRequestProperty("Content-Length", Integer.toString((parameters.getBytes()).length));
        connection.setRequestProperty("Content-Language", "en-US");

        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);


        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(parameters);
        wr.flush();
        wr.close();


        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();

        if(response.indexOf(":") < 0) {
            throw new IOException(response.toString());
        }

        String[] values = response.toString().split(":");

        if (connection != null)
            connection.disconnect();

        return values[3].trim();
    }

    public static boolean checkSession(String username, String sessionId) {
        HttpURLConnection connection = null;

        try {
            String parameters = "session=" + URLEncoder.encode(sessionId, "UTF-8") + "&name=" + URLEncoder.encode(username, "UTF-8");
            URL url = new URL("http://" + Globals.API_HOSTNAME + "/login/session.jsp?" + parameters);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.connect();

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            String res = rd.readLine();

            rd.close();

            return res.equals("ok");
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        } finally {

            if (connection != null)
                connection.disconnect();
        }
    }

    public static boolean listServer(String ip, String port, int users, int maxUsers, String name, boolean onlineMode, String md5, boolean whitelisted, String[] whitelistUsers, String[] whitelistIPs, String[] whitelistUUIDs, String[] bannedUsers, String[] bannedIPs, String[] bannedUUIDs) {
        HttpURLConnection connection = null;

        try {
            JSONObject jsonObject = new JSONObject();
            if (ip != null)
                jsonObject.put("ip", ip);
            jsonObject.put("port", port);
            if (users > -1)
                jsonObject.put("users", users);
            jsonObject.put("max", maxUsers);
            jsonObject.put("name", name);
            jsonObject.put("onlinemode", onlineMode);
            jsonObject.put("md5", md5);
            jsonObject.put("whitelisted", whitelisted);
            jsonObject.put("whitelistUsers", whitelistUsers);
            jsonObject.put("whitelistIPs", whitelistIPs);
            jsonObject.put("whitelistUUIDs", whitelistUUIDs);
            jsonObject.put("bannedUsers", bannedUsers);
            jsonObject.put("bannedIPs", bannedIPs);
            jsonObject.put("bannedUUIDs", bannedUUIDs);

            String json = jsonObject.toString();

            URL url = new URL("http://" + Globals.API_HOSTNAME + "/mineonline/listserver.jsp");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
            connection.getOutputStream().flush();
            connection.getOutputStream().close();

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            String res = rd.readLine();

            rd.close();

            return res.equals("ok");
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        } finally {

            if (connection != null)
                connection.disconnect();
        }
    }

    public static LinkedList<MineOnlineServer> listServers(String uuid, String sessionId) throws IOException, ParseException {
        HttpURLConnection connection = null;

        String parameters = "sessionId=" + URLEncoder.encode(sessionId, "UTF-8") + "&user=" + URLEncoder.encode(uuid, "UTF-8");
        URL url = new URL("http://" + Globals.API_HOSTNAME + "/mineonline/listservers.jsp?" + parameters);
        connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(false);
        connection.connect();

        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();

        JSONArray jsonArray = new JSONArray(response.toString());

        if (connection != null)
            connection.disconnect();

        return MineOnlineServer.getServers(jsonArray);
    }

    public static JSONArray getVersionsInfo() throws IOException {
        HttpURLConnection connection = null;

        URL url = new URL("http://" + Globals.API_HOSTNAME + "/versions.json");
        connection = (HttpURLConnection) url.openConnection();
        connection.connect();

        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();

        JSONArray jsonArray = new JSONArray(response.toString());

        if (connection != null)
            connection.disconnect();

        return jsonArray;
    }

    public static String getLauncherVersion() throws IOException {
        HttpURLConnection connection = null;

        URL url = new URL("http://" + Globals.API_HOSTNAME + "/launcherversion");
        connection = (HttpURLConnection) url.openConnection();
        connection.connect();

        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();

        return response.toString();
    }
}
