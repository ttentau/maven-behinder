//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.rebeyond.behinder.core;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Timestamp;
import org.json.JSONArray;
import org.json.JSONObject;

public class ShellManager {
    private static final String Class_Name = "org.sqlite.JDBC";
    private static final String DB_PATH = "data.db";
    private static final String DB_URL = "jdbc:sqlite:data.db";
    private static Connection connection = null;

    public ShellManager() throws Exception {
        if (!(new File("data.db")).exists()) {
            throw new Exception("数据库文件丢失，无法启动。");
        } else {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:data.db");
            connection.setAutoCommit(true);
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public JSONArray listShell() throws Exception {
        JSONArray result = new JSONArray();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from shells");
        ResultSetMetaData rsmd = rs.getMetaData();

        while(rs.next()) {
            int numColumns = rsmd.getColumnCount();
            JSONObject obj = new JSONObject();

            for(int i = 1; i <= numColumns; ++i) {
                String column_name = rsmd.getColumnName(i);
                obj.put(column_name, rs.getObject(column_name));
            }

            result.put(obj);
        }

        return result;
    }

    public JSONObject findShell(int shellID) throws Exception {
        JSONArray result = new JSONArray();
        PreparedStatement statement = connection.prepareStatement("select * from shells where id=?");
        statement.setInt(1, shellID);
        ResultSet rs = statement.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();

        while(rs.next()) {
            int numColumns = rsmd.getColumnCount();
            JSONObject obj = new JSONObject();

            for(int i = 1; i <= numColumns; ++i) {
                String column_name = rsmd.getColumnName(i);
                obj.put(column_name, rs.getObject(column_name));
            }

            result.put(obj);
        }

        return result.getJSONObject(0);
    }

    public int addShell(String url, String password, String type, String comment, String headers) throws Exception {
        PreparedStatement statement = connection.prepareStatement("select count(*) from shells where url=?");
        statement.setString(1, url);
        int num = statement.executeQuery().getInt(1);
        if (num > 0) {
            throw new Exception("该URL已存在");
        } else {
            statement = connection.prepareStatement("insert into shells(url,ip,password,type,os,comment,headers,addtime,updatetime,accesstime) values (?,?,?,?,?,?,?,?,?,?)");
            statement.setString(1, url);
            statement.setString(2, InetAddress.getByName((new URL(url)).getHost()).getHostAddress());
            statement.setString(3, password);
            statement.setString(4, type);
            statement.setString(5, "");
            statement.setString(6, comment);
            statement.setString(7, headers);
            Timestamp now = new Timestamp(System.currentTimeMillis());
            statement.setTimestamp(8, now);
            statement.setTimestamp(9, now);
            statement.setTimestamp(10, now);
            return statement.executeUpdate();
        }
    }

    public int updateShell(int shellID, String url, String password, String type, String comment, String headers) throws Exception {
        PreparedStatement statement = connection.prepareStatement("update shells set url=?,ip=?,password=?,type=?,comment=?,headers=?,updatetime=? where id=?");
        statement.setString(1, url);
        statement.setString(2, InetAddress.getByName((new URL(url)).getHost()).getHostAddress());
        statement.setString(3, password);
        statement.setString(4, type);
        statement.setString(5, comment);
        statement.setString(6, headers);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        statement.setTimestamp(7, now);
        statement.setInt(8, shellID);
        return statement.executeUpdate();
    }

    public int deleteShell(int shellId) throws Exception {
        PreparedStatement statement = connection.prepareStatement("delete from shells where id=?");
        statement.setInt(1, shellId);
        return statement.executeUpdate();
    }

    public int addPlugin(String name, String type, String code) throws Exception {
        PreparedStatement statement = connection.prepareStatement("insert into plugins(name,type,code) values (?,?,?)");
        statement.setString(0, name);
        statement.setString(1, type);
        statement.setString(2, code);
        return statement.executeUpdate();
    }

    public int addProxy(String name, String type, String ip, int port, String username, String password, int status) throws Exception {
        PreparedStatement statement = connection.prepareStatement("insert into proxys(name,type,ip,port,username,password,status) values (?,?,?,?,?,?,?)");
        statement.setString(1, name);
        statement.setString(2, type);
        statement.setString(3, ip);
        statement.setInt(4, port);
        statement.setString(5, username);
        statement.setString(6, password);
        statement.setInt(7, status);
        return statement.executeUpdate();
    }

    public int updateProxy(String name, String type, String ip, int port, String username, String password, int status) throws Exception {
        PreparedStatement statement = connection.prepareStatement("update proxys set type=?,ip=?,port=?,username=?,password=?,status=? where name=?");
        statement.setString(1, type);
        statement.setString(2, ip);
        statement.setInt(3, port);
        statement.setString(4, username);
        statement.setString(5, password);
        statement.setInt(6, status);
        statement.setString(7, name);
        return statement.executeUpdate();
    }

    public JSONObject findProxy(String name) throws Exception {
        JSONArray result = new JSONArray();
        PreparedStatement statement = connection.prepareStatement("select * from  proxys  where name=?");
        statement.setString(1, name);
        ResultSet rs = statement.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();

        while(rs.next()) {
            int numColumns = rsmd.getColumnCount();
            JSONObject obj = new JSONObject();

            for(int i = 1; i <= numColumns; ++i) {
                String column_name = rsmd.getColumnName(i);
                obj.put(column_name, rs.getObject(column_name));
            }

            result.put(obj);
        }

        return result.getJSONObject(0);
    }

    public int updatePlugin(int pluginID, String name, String type, String code) throws Exception {
        PreparedStatement statement = connection.prepareStatement("update plugins set name=?,type=?,code=? where id=?");
        statement.setString(0, name);
        statement.setString(1, type);
        statement.setString(2, code);
        statement.setInt(3, pluginID);
        return statement.executeUpdate();
    }

    public int delPlugin(int pluginID) throws Exception {
        PreparedStatement statement = connection.prepareStatement("delete from plugins where id=?");
        statement.setInt(0, pluginID);
        return statement.executeUpdate();
    }

    public JSONArray listPlugin() throws Exception {
        JSONArray result = new JSONArray();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from plugins");
        ResultSetMetaData rsmd = rs.getMetaData();

        while(rs.next()) {
            int numColumns = rsmd.getColumnCount();
            JSONObject obj = new JSONObject();

            for(int i = 1; i <= numColumns; ++i) {
                String column_name = rsmd.getColumnName(i);
                obj.put(column_name, rs.getObject(column_name));
            }

            result.put(obj);
        }

        return result;
    }

    public int updateOsInfo(int shellID, String osInfo) throws Exception {
        PreparedStatement statement = connection.prepareStatement("update shells set os=? where id=?");
        statement.setString(1, osInfo);
        statement.setInt(2, shellID);
        return statement.executeUpdate();
    }

    public int updateMemo(int shellID, String memo) throws Exception {
        PreparedStatement statement = connection.prepareStatement("update shells set memo=? where id=?");
        statement.setString(1, memo);
        statement.setInt(2, shellID);
        return statement.executeUpdate();
    }
}
