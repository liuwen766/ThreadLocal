package com.itheima.transfer.service;

import com.itheima.transfer.dao.AccountDao;
import com.itheima.transfer.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;

/*
*   事务：
*       1. 转出和转入必须具备原子性的（不可分割）
*       2. 引入事务
*       Connection 接口
*        a. setAutoCommit(false) 禁止事务自动提交（手动提交）
*        b. commit() : 事务提交
*        c. rollback()： 事务回滚
*
*      注意点：
*       1. 要保证开启事务的connection对象和访问事务的connection对象一致！！
*           最简单的解决方案： 传参
*               问题： service层和dao层的耦合度提高了
*
*       2. 线程并发的场景下， 保证service层和dao层的connection对象不会被其他线程干扰
*           现在的解决方案： synchronized
*               问题 ： 让程序失去并发性，降低了效率
*
*      解决： ThreadLocal
*      1. 传递数据 ： 我们可以通过ThreadLocal在同一线程，不同组件中传递公共变量 -》 解耦
*      2. 线程隔离 ： 每个线程中的各自数据是相互独立 -》 避免同步方式带来性能损失
*
*
*     猜：
*       ThreadLocal -》  Map<Thread,Object>  : 将数据和当前线程进行绑定
*           set(value) :  设置当前线程绑定的数据
*               Key = Thread.currentThread();
*               value
*
*           get(); 取出当前线程绑定的数据
*               Key = Thread.currentThread();
*
*           线程越多，键值对越多

* */
public class AccountService {

    public boolean transfer(String outUser, String inUser, int money) {
        AccountDao ad = new AccountDao();
        Connection conn = null;
        try {
//            synchronized (AccountService.class){
                conn = JdbcUtils.getConnection();

                conn.setAutoCommit(false);

                // 转出
                ad.out(outUser, money);
                //模拟转出成功，转入失败的情况
                int i = 1/0;
                // 转入
                ad.in(inUser, money);

                JdbcUtils.commitAndClose(conn);
//            }

        } catch (Exception e) { //为了捕获算术异常
//            e.printStackTrace();
            JdbcUtils.rollbackAndClose(conn);
            return false;
        }

        return true;
    }

}
