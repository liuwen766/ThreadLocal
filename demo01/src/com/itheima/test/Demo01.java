package com.itheima.test;

/*
*   事情： 在某个线程中存入一个值，那么当这个线程要取出这个值的时候
*           这个值在这个线程内部 必须一致！
*
*           线程隔离： 在线程并发的场景下，各个线程的数据是相互隔离
* */
public class Demo01 {

    ThreadLocal<String> tl = new ThreadLocal<>();

    private String content;

    public String getContent() {
//        return content;
        //取出当前线程绑定的变量
        String s = tl.get();
        return s;
    }

    public void setContent(String content) {
//        this.content = content;
        //将content绑定到当前线程
        tl.set(content);
    }

    public static void main(String[] args) {
        Demo01 demo01 = new Demo01();

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
                    demo01.setContent(Thread.currentThread().getName() + "的数据");
                    System.out.println("-----------------------------------------");
                    String content = demo01.getContent();
                    System.out.println(Thread.currentThread().getName() + ":" + content);
                }
            };
            t.setName("线程" + i);// 线程0~4
            t.start();
        }
    }
}
