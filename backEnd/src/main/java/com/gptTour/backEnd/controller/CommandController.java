package com.gptTour.backEnd.controller;

import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.function.Consumer;



@RestController
public class CommandController {

    private static class StreamGobbler implements Runnable {
        private List<String> result;
        private InputStream inputStream;

        public StreamGobbler(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @SneakyThrows()
        @Override
        public void run() {
            result = new BufferedReader(new InputStreamReader(inputStream, "CP949")).lines().collect(Collectors.toList());
//            getResult();
        }

//        synchronized void getResult() {
//            result = temp;
////            System.out.println(result);
//        }
    }

    @GetMapping("/pytest")
    public void pytest(@RequestParam("strtDt") String start_date, @RequestParam("endDt") String end_date, @RequestParam("region") String region) throws IOException, InterruptedException {
        System.out.println(":: START :: User ProcessBuilder ");
        String homeDirectory = System.getProperty("user.home");
        System.out.println(":: homeDirectory is " + homeDirectory);

        ProcessBuilder builder = new ProcessBuilder();
        builder.directory(new File("F:/git/gpt-tour-adviser/csv_generator"));


        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        System.out.println(":: OS is " + (isWindows ? "window" : "linux"));

        if(isWindows) {
            builder.command("cmd.exe", "/c", "python travel_adviser.py --start " + start_date +  " --end " +  end_date + " --region " + region);

        } else {
            builder.command("sh", "-c", "ls -l | grep P");
        }

        Process process = builder.start();
        StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream());

        Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = process.waitFor();
        assert exitCode == 0;
        System.out.println(streamGobbler.result);

    }

}
