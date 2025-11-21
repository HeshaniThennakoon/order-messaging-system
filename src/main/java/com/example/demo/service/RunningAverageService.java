package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class RunningAverageService {

    private double total = 0;
    private int count = 0;

    public double add(double price) {
        total += price;
        count++;
        return total / count;
    }
}
