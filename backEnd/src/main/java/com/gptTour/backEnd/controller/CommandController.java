package com.gptTour.backEnd.controller;

import org.python.util.PythonInterpreter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.StringWriter;

@RestController
public class CommandController {

    private static PythonInterpreter interpreter;

    @GetMapping("/pytest")
    public void pytest() {
        interpreter = new PythonInterpreter();
        StringWriter out = new StringWriter();
        interpreter.setOut(out);
        interpreter.execfile("D:/git/gpt-tour-adviser/csv_generator/openai_test.py");

        String result = out.toString();
        System.out.println(result);
        interpreter.close();
    }

}
