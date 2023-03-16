package com.athena.worker;

import com.athena.StandaloneMaster;
import com.athena.server.Client;
import com.athena.server.ExecutableTask;
import com.athena.server.JavaSerializer;
import com.athena.server.Server;

import java.net.URLClassLoader;


/**
 * @Description TODO
 * @Author lixiaobing
 * @Date 2023/3/14
 * @Version 1.0
 **/
public class StandaloneWorker {

    public void startServer(int port) {
        new Thread(() -> {
            Server server = new Server(port);
            server.start((object, output) -> {
                if (object instanceof ExecutableTask) {
                    ExecutableTask task = (ExecutableTask) object;
                    System.out.println(task);
                    JavaSerializer javaSerializer = new JavaSerializer();
                    URLClassLoader urlClassLoader = new URLClassLoader(task.getJars(), StandaloneMaster.class.getClassLoader());
                    Runnable executable = (Runnable) javaSerializer.deserialize(task.getExecutable(), urlClassLoader);
                    System.out.println("worker接收任务开始执行: " + executable);
                    new Thread(executable).start();
                }
            });
        }).start();
    }

    public static void start(int port) {
        StandaloneWorker standaloneWorker = new StandaloneWorker();
        standaloneWorker.startServer(port);
        registerWorker(port);

    }

    private static void registerWorker(int port) {
        Client client = new Client("localhost", 6666);
        client.write(new Worker("localhost", port));
    }
}

