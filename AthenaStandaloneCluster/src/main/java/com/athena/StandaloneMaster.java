package com.athena;

import com.athena.server.Client;
import com.athena.server.ExecutableTask;
import com.athena.server.Server;
import com.athena.worker.Worker;
import lombok.SneakyThrows;

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


    @SneakyThrows
    public static void main(String[] args) {
        List<Worker> workers = new ArrayList<>();
        startServer(workers);
    }

    private static void startServer(List<Worker> workers) {
        AtomicInteger idx = new AtomicInteger(0);
        new Thread(() -> {
            Server server = new Server(6666);
            server.start((object, output) -> {
                if (object instanceof Worker) {
                    workers.add((Worker) object);
                    System.out.println("add worker " + workers);
                } else if (object instanceof ExecutableTask) {
                    ExecutableTask task = (ExecutableTask) object;
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
