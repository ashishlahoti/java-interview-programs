package com.help4j.core.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class CallableWithoutExecutor {

	public static void main(String[] args) {
		try {
            Callable<String> c = new Callable<String>() {
				@Override
				public String call() throws Exception {
					return "Ashish";
				}
            };
            FutureTask<String> ft = new FutureTask<String>(c);
            Thread t = new Thread(ft);
            t.start();

            String result = ft.get();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
