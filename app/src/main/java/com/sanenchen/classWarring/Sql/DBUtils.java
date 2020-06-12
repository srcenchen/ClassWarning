package com.sanenchen.classWarring.Sql;

import android.provider.ContactsContract;

import com.sanenchen.classWarring.ClassWarningSearchActivity;

import java.sql.*;

public class DBUtils {

    // MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://182.92.87.134:3306/classWarring";

    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    //static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    //static final String DB_URL = "jdbc:mysql://localhost:3306/RUNOOB?useSSL=false&serverTimezone=UTC";


    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "classWarring";
    static final String PASS = "classWarring";

    public void DBUtilsGet(String search) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 获取首个字符
            char fir = search.charAt(0);

            // 执行查询
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM WarringA WHERE WarringStudent LIKE '%" + fir + "%'";
            rs = stmt.executeQuery(sql);

            // 展开结果集数据库
            while (rs.next()) {
                System.out.println(rs.getString("WarringT"));
                ClassWarningSearchActivity classWarningSearchActivity = new ClassWarningSearchActivity();
//                classWarningSearchActivity.getT = rs.getString("WarringT");
            }
            //return rs;
            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }// 什么都不做
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        //return rs;
    }

    public void DBUtilsPut(String Title, String WarningGroup, String WarningStudent, String WarningFun, String FunStartTime, String FunEndTime, String BeizhuSS) {
        Connection conn = null;
        Statement stmt = null;
        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM WarningTotal;");

            int WarningTotal = 0;

            // 展开结果集数据库
            while(rs.next()){
                // 通过字段检索
                WarningTotal  = rs.getInt("WarningTotal") + 1;
            }

            String sql3;
            sql3 = "UPDATE WarningTotal SET WarningTotal=" + WarningTotal +";";
            stmt.executeUpdate(sql3);

            // 执行查询
            String sql;
            sql = "INSERT INTO WarringA VALUES(null,'" + Title +"','" + WarningGroup + "','" + WarningStudent +"','" + WarningFun +"','" +
                    FunStartTime +"','" + FunEndTime +"','" + BeizhuSS + "');";
            stmt.executeUpdate(sql);

            // 完成后关闭
            //rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }
}