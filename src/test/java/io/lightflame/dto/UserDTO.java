package io.lightflame.dto;

import java.io.Serializable;

/**
 * HelloWorld
 */
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private int age;

    

    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    public UserDTO(String name, int age) {
        this.name = name;
        this.age = age;
    }


}