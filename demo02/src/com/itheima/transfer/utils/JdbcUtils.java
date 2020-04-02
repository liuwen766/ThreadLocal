package com.itheima.transfer.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JdbcUtils {

    static ThreadLocal<Connection> tl = new ThreadLocal<>();

    // c3p0 数据库连接池对象属性
    private static final ComboPooledDataSource ds = new ComboPooledDataSource();
    /*
        获取连接
        1. 原来： 是直接从连接池中取出连接
        2. 现在：
            1. 先从threadLocal对象中获取连接 （是跟当前线程绑定）
            2. 如果没有这个连接，再从连接池中获取，然后绑定到当前线程中

     */
    public static Connection getConnection() throws SQLException {
        Connection conn = tl.get();
        if(conn == null){
            conn = ds.getConnection();

            tl.set(conn);
        }
        return conn;
    }
    //释放资源
    public static void release(AutoCloseable... ios){
        for (AutoCloseable io : ios) {
            if(io != null){
                try {
                    io.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void commitAndClose(Connection conn) {
        try {
            if(conn != null){
                //将当前线程绑定的事务进行解绑
                tl.remove();
                //提交事务
                conn.commit();
                //释放连接
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rollbackAndClose(Connection conn) {
        try {
            if(conn != null){
                //将当前线程绑定的事务进行解绑
                tl.remove();
                //回滚事务
                conn.rollback();
                //释放连接
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
