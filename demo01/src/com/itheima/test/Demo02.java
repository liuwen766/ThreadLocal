package com.itheima.test;

/*
*   事情： 在某个线程中存入一个值，那么当这个线程要取出这个值的时候
*           这个值在这个线程内部 必须一致！
*
*           线程隔离： 在线程并发的场景下，各个线程的数据是相互隔离
*
*   卖票：
*       总共100张票，有多个窗口进行售票
*       线程模拟窗口， 多线程并发的场景
*       卖票： 可能第99张比第100张先卖出
*       原因： thread A 正在卖第100张的时候，i-- 但是还没来得及打印
*             thread B 抢到执行权， 99张， 先打印
*             问题： 线程同步问题
*
*             java程序 执行权 抢占式调度（随机性特点）
*       加锁： synchronized
*
*   ThreadLocal ：
*           thread A ： 值1
*           thread B ： 值2
*
*           保证： thread A 存了值1， 以后thread A取出 值1
*           将值绑定到当前线程
* */
public class Demo02 {

    ThreadLocal<String> tl = new ThreadLocal<>();

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static void main(String[] args) {
        Demo02 demo01 = new Demo02();

        //开启5个线程
        for (int i = 0; i < 5; i++) {
            Thread t = new Thread(){
                @Override
                public void run() {
                    /*
                    * 在该线程中存入一个值
                    * 然后取出并打印
                    *
                    * 线程0 存入的数据 ： 线程0的数据
                    * 线程0 取出的数据 ： 线程0的数据
                    *
                    * 问题： 在某个线程中，存入的数据和取出的数据并不一致！
                    * */
                    synchronized (Demo02.class){
                        demo01.setContent(Thread.currentThread().getName() + "的数据");
                        System.out.println("-----------------------------------------");
                        String content = demo01.getContent();
                        System.out.println(Thread.currentThread().getName() + ":" + content);
                    }

                }
            };
            t.setName("线程" + i);// 线程0~4
            t.start();
        }
    }
}
