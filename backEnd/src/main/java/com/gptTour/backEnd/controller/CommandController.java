package com.gptTour.backEnd.controller;

import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.function.Consumer;


@RestController
public class CommandController {

    private static class StreamGobbler implements Runnable {
        private InputStream inputStream;
        private Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @SneakyThrows()
        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream, "CP949")).lines().forEach(consumer);
        }
    }

    @GetMapping("/pytest")
    public void pytest() throws IOException, InterruptedException {
        System.out.println(":: START :: User ProcessBuilder ");
        String homeDirectory = System.getProperty("user.home");
        System.out.println(":: homeDirectory is " + homeDirectory);

        ProcessBuilder builder = new ProcessBuilder();
        builder.directory(new File("D:/git/gpt-tour-adviser/csv_generator"));

        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        System.out.println(":: OS is " + (isWindows ? "window" : "linux"));

        if(isWindows) {
            builder.command("cmd.exe", "/c", "python travel_adviser.py --start 20230615 --end 20230617 --region 부산");
        } else {
            builder.command("sh", "-c", "ls -l | grep P");
        }

        Process process = builder.start();
        StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);

        int exitCode = process.waitFor();
        assert exitCode == 0;
    }

}
