package com.athena.runtime.io.network.partition;

import com.athena.api.util.server.Client;
import com.athena.api.util.server.Server;
import com.athena.rpc.SimpleRpcProxy;
import com.athena.rpc.SimpleRpcServer;
import com.athena.server.ConfigServer;
import com.athena.server.IConfigServer;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/15
 * @Version 1.0
 **/
public class RemoteResultPartitionManager implements ResultPartitionManager, RemoteConsumePartition {
    private ResultPartition resultPartition;
    private Map<Integer, Future<String>> partitionResultFutureMap = new ConcurrentHashMap<>();

    public RemoteResultPartitionManager() {
    }

    @Override
    public void registerResultPartition(ResultPartition partition) {
        this.resultPartition = partition;
        int port = new Random().nextInt(65535);
        System.out.println("partition id 【" + partition.getPartitionId() + "】启动服务器 localhost:" + port);
        IConfigServer configServer = SimpleRpcProxy.create(IConfigServer.class, new InetSocketAddress("localhost", 9999), IConfigServer.class.getClassLoader());
        configServer.put(String.valueOf(partition.getPartitionId()), "localhost_" + port);
        new Thread(() -> {
            SimpleRpcServer simpleRpcServer = new SimpleRpcServer(port);
            simpleRpcServer.registerService(RemoteConsumePartition.class, this);
            simpleRpcServer.start();
        }).start();
    }

    @Override
    public Object consumeFromPartition(Long partitionId, int subTaskIndex) {
        IConfigServer configServer = SimpleRpcProxy.create(IConfigServer.class, new InetSocketAddress("localhost", 9999), IConfigServer.class.getClassLoader());
        String partitionLocation = configServer.get(String.valueOf(partitionId));
        String[] location = partitionLocation.split("_");
        RemoteConsumePartition remoteConsumePartition = SimpleRpcProxy.create(RemoteConsumePartition.class,
                new InetSocketAddress(location[0], Integer.parseInt(location[1])),
                RemoteConsumePartition.class.getClassLoader());
        return remoteConsumePartition.consume(subTaskIndex);
    }


    @Override
    public Object consume(int subIndex) {
        return this.resultPartition.getSubpartitions()[subIndex].consume();
    }
}
