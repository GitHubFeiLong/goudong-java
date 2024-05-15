package com.goudong.modules.simple.ten.one;

/**
 * 类描述：
 *
 * @author chenf
 */
public interface MyService {

    void get();

    class MyServiceImpl implements MyService{

        @Override
        public void get() {
            System.out.println("get method");
        }

        public static void main(String[] args) {
            new MyServiceImpl().get();
        }
    }
}
