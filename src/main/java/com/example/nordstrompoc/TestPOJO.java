package com.example.nordstrompoc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;

public class TestPOJO {
    @Id
    public String id;

    public String stringField1;
    public String stringField2;
    public String stringField3;
    public Date date;
    public int integer;
    public boolean bool;
    public double dubble;
    public String testField;

    public List<TestPOJONested> nestedStrings;

    public TestPOJO() {
        this.nestedStrings = new ArrayList<TestPOJONested>();
    }
}
