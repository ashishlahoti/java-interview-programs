package com.example.thread;

import java.util.concurrent.*;

public class ExceptionHandlingInThread {

    public static void main(String[] args) {
        ExceptionHandlingInThread exceptionHandlingInThread = new ExceptionHandlingInThread();
        //exceptionHandlingInThread.submitThenThrowUncheckedThenGet();
        //exceptionHandlingInThread.executeThenThrowUnchecked();
        exceptionHandlingInThread.executeWithCustomThreadPoolExecutorThenThrowUnchecked();
    }

    public void submitThenThrowUncheckedThenGet() {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        final Future<Object> future = executorService.submit(() -> {
            throw new RuntimeException("Uncaught exception in callable task");
        });

        try {
            future.get();
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("Caught the exception ( " + e.getMessage() + " )");
        }

        executorService.shutdown();
    }

    public static class AppExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println("Caught the exception ( " + e.getMessage() + " ) thrown by thread [" + t.getName() + "]");
        }
    }

    public static class AppThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            final Thread thread = new Thread(r);
            thread.setUncaughtExceptionHandler(new AppExceptionHandler());
            return thread;
        }
    }

    public void executeThenThrowUnchecked() {
        final ExecutorService executorService = Executors.newFixedThreadPool(1, new AppThreadFactory());

        executorService.execute(() -> {
            throw new RuntimeException("Uncaught exception in runnable task");
        });

        executorService.shutdown();
    }

    public static class MonitoringThreadPoolExecutor extends ThreadPoolExecutor {

        public MonitoringThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);
            if(t != null){
                System.out.println("Caught the exception ( " + t.getMessage() + " )");
            }
        }
    }
    public void executeWithCustomThreadPoolExecutorThenThrowUnchecked() {
        final ExecutorService executorService = new MonitoringThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>());
        executorService.execute(() -> {
            throw new RuntimeException("Uncaught exception in runnable task");
        });

        executorService.shutdown();
    }

}
