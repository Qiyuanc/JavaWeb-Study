package com.qiyuan.pojo;

/**
 * @ClassName person
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/7/27 22:18
 * @Version 1.0
 **/
public class Person {
    // alt + inser 快速构造
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
