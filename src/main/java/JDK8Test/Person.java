package JDK8Test;

/**
 * Created by YWJ on 2017.4.18.
 * Copyright (c) 2017 NJU PASA Lab All rights reserved.
 */
public class Person {
    String firstName;
    String lastName;

    Person() {}
    Person(String a, String b) {
        this.firstName = a;
        this.lastName = b;
    }

    @Override
    public String toString() {
        return "[ " + this.firstName + "  " + this.lastName +" ]";
    }
}
