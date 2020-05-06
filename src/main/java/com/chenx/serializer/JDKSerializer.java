package com.chenx.serializer;

import java.io.*;

public class JDKSerializer {

    public static void main(String[] args) throws Exception {
        // create a Student
        Student st = new Student("kirito");
        // serialize the st to student.db file
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("student.db"));
        oos.writeObject(st);
        oos.close();
        // deserialize the object from student.db
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("student.db"));
        Student kirito = (Student) ois.readObject();
        ois.close();
        // assert
        System.out.println("kirito".equals(kirito.getName()));
    }

    static class  Student implements Serializable {
        private String name;

        public Student(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}