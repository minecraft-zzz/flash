package com.google.mlkit.vision.demo.dao;


import com.google.mlkit.vision.demo.entity.User;


import com.google.mlkit.vision.demo.utils.JDBCUtils;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {


    public boolean login(String name,String password){

        String sql = "select * from users where name = ? and password = ?";

        Connection  con = JDBCUtils.getConn();

        try {
            PreparedStatement pst=con.prepareStatement(sql);

            pst.setString(1,name);
            pst.setString(2,password);

            if(pst.executeQuery().next()){

                return true;

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.close(con);
        }

        return false;
    }

    public boolean register(User user){

        String sql = "insert into users(name,username,password,age,phone) values (?,?,?,?,?)";

        Connection  con = JDBCUtils.getConn();

        try {
            PreparedStatement pst=con.prepareStatement(sql);

            pst.setString(1,user.getName());
            pst.setString(2,user.getUsername());
            pst.setString(3,user.getPassword());
            pst.setInt(4,user.getAge());
            pst.setString(5,user.getPhone());

            int value = pst.executeUpdate();

            if(value>0){
                return true;
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.close(con);
        }
        return false;
    }
    public boolean isAccountExists(String accountName) {
        String sql = "SELECT COUNT(*) FROM users WHERE name = ?";

        Connection con = JDBCUtils.getConn();
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, accountName);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // 如果计数大于0，则表示存在同名账号
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(con);
        }
        return false;
    }


    public User findUser(String name){
        String sql = "select * from users where name = ?";
        Connection con = null;
        User user = null;

        try {
            con = JDBCUtils.getConn();
            if (con == null) {
                System.err.println("Failed to establish database connection");
                return null;
            }

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, name);
            ResultSet rs = pst.executeQuery();

            while (rs.next()){
                int uid = rs.getInt("uid");
                String namedb = rs.getString("name");
                String username = rs.getString("username");
                String password = rs.getString("password");
                int age = rs.getInt("age");
                byte[] phoneBytes = rs.getBytes("phone");
                String phone = new String(phoneBytes, StandardCharsets.UTF_8);

                user = new User(uid, namedb, username, password, age, phone);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return user;
    }

    public boolean isUsernameExists(String username) {
        String sql = "select count(*) from users where username = ?";

        Connection con = JDBCUtils.getConn();
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(con);
        }
        return false;
    }

}

