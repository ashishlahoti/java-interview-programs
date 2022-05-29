package com.example.algo.dynamicprogramming;

import java.util.ArrayList;
import java.util.List;

public class GenerateParenthesis {

    public static void main(String[] args) {
        System.out.println(generateParenthesis(1));
        System.out.println(generateParenthesis(2));
        System.out.println(generateParenthesis(3));
    }

    public static List<String> generateParenthesis(int n) {
        List<String> output = new ArrayList<>();
        // pass the count of open and close parenthesis required
        generate(n, n, output, "");
        return output;
    }

    public static void generate(int open, int close, List<String> output, String temp) {
        // both open and close parenthesis are consumed, add to output
        if (open == 0 && close == 0) {
            output.add(temp);
            return;
        }
        // Should always start with open parenthesis `(`, if available
        if (open != 0) {
            generate(open - 1, close, output, temp + "(");
        }
        // we see open are more then close, balance them by adding close `)`
        if (close > open) {
            generate(open, close - 1, output, temp + ")");
        }
    }
}
