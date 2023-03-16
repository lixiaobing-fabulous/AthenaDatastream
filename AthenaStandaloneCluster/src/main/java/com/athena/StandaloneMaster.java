package com.athena;

import com.athena.server.Client;
import com.athena.server.ExecutableTask;
import com.athena.server.Server;
import com.athena.worker.Worker;
import lombok.SneakyThrows;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/14
 * @Version 1.0
 **/
public class StandaloneMaster {

    private static class Dumb implements Serializable {
    }

    @SneakyThrows
    public static void main(String[] args) {
        List<Worker> workers = new ArrayList<>();
        startServer(workers);
    }

    private static void startServer(List<Worker> workers) {
        AtomicInteger idx = new AtomicInteger(0);
        new Thread(() -> {
            Server server = new Server(6666);
            server.start((object, socket) -> {
                if (object instanceof Worker) {
                    workers.add((Worker) object);
                    System.out.println("workers 注册成功" + workers);
                } else if (object instanceof ExecutableTask) {
                    ExecutableTask task = (ExecutableTask) object;
                    System.out.println("master接收任务向worker派发任务:" + task);
                    int size = workers.size();
                    idx.set(idx.incrementAndGet() % size);
                    Worker worker = workers.get(idx.get());
                    Client client = new Client(worker.getHost(), worker.getPort());
                    client.write(task);
                }
            });
        }).start();
    }

}
