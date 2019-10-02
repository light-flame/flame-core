package io.lightflame.bootstrap;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.concurrent.*;

public class ListenerSync {

    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    ExecutorService executorService = Executors.newFixedThreadPool(10);
    ConcurrentLinkedQueue<Future> futures = new ConcurrentLinkedQueue<>();
    ConcurrentMap<Integer,Listener> listenerMap = new ConcurrentHashMap<>();


    void addListener(Listener listener){
        try {
            listener.bind(bossGroup, workerGroup);
        }catch (InterruptedException e){
            exit(e);
        }
        futures.add(executorService.submit(new ListenerSyncCallable(listener)));
        listenerMap.put(listener.port(),listener);
    }

    Boolean close(int port){
        Listener listener =  listenerMap.get(port);
        if (listener == null){
            return false;
        }
        listener.close();
        listenerMap.remove(listener);
        return true;
    }

    void startSync(){
        try {
            loop();
        }catch(Exception e){
            throw new RuntimeException(e);
        }finally {
            exit(null);
        }
    }

    void exit(Exception e){
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    private void loop()throws InterruptedException{
        while (!executorService.awaitTermination(2, TimeUnit.SECONDS)){
            futures.removeIf(x -> x.isCancelled() || x.isDone());
            if (futures.isEmpty()){
                executorService.shutdown();
            }
        }
    }

    public class ListenerSyncCallable implements Callable<Boolean> {

        private Listener listener;

        ListenerSyncCallable(Listener ls){
            this.listener = ls;
        }

        public Boolean call()  throws InterruptedException{
            listener.sync();
            return true;
        }
    }
}
