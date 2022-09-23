package com.example.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class CallableWithoutExecutor {

	public static void main(String[] args) {
		try {
            Callable<String> c = () -> "Ashish";
            FutureTask<String> ft = new FutureTask<>(c);
            Thread t = new Thread(ft);
            t.start();

            String result = ft.get();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
