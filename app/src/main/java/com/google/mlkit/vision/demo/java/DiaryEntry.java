package com.google.mlkit.vision.demo.java;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DiaryEntry {
    private String title;
    private String content;
    private ArrayList<Integer> videoResourceIds;
    private ArrayList<String> videoNames;
    private ArrayList<Integer> groupNumbers;
    private ArrayList<Integer> numbers;
    private int year;
    private int month;
    private int dayOfMonth;
    private List<Date> dateArray; // 修改为List<Date>

    // 构造函数
    public DiaryEntry(String title, String content, ArrayList<Integer> videoResourceIds, ArrayList<String> videoNames, ArrayList<Integer> groupNumbers, ArrayList<Integer> numbers, int year, int month, int dayOfMonth) {
        this.title = title;
        this.content = content;
        this.videoResourceIds = videoResourceIds;
        this.videoNames = videoNames;
        this.groupNumbers = groupNumbers;
        this.numbers = numbers;
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.dateArray = new ArrayList<>(); // 初始化日期数组
    }

    // 获取方法
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public ArrayList<Integer> getVideoResourceIds() {
        return videoResourceIds;
    }

    public ArrayList<String> getVideoNames() {
        return videoNames;
    }

    public ArrayList<Integer> getGroupNumbers() {
        return groupNumbers;
    }

    public ArrayList<Integer> getNumbers() {
        return numbers;
    }

    public List<Date> getDateArray() {
        return dateArray;
    }

    public void setDateArray(List<Date> dateArray) {
        this.dateArray = dateArray;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }
}
