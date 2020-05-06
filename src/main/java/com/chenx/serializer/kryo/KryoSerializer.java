package com.chenx.serializer.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.*;


public class KryoSerializer {

    public static void main(String[] args) throws Exception {
        Kryo kryo = new Kryo();
        Output output = new Output(new FileOutputStream("student.db"));
        Student kirito = new Student("kirito");
        kryo.writeObject(output, kirito);
        output.close();
        Input input = new Input(new FileInputStream("student.db"));
        Student kiritoBak = kryo.readObject(input, Student.class);
        input.close();
        System.out.println("kirito".equals(kiritoBak.getName()));

        /*如果实现类的字节码未知，并且对象可能为 null
        kryo.writeClassAndObject(output, object);
        // ...
        Object object = kryo.readClassAndObject(input);
        if (object instanceof SomeClass) {
            // ...
        }*/
    }


    static class  Student implements Serializable {
        private String name;

        public Student() {
        }

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