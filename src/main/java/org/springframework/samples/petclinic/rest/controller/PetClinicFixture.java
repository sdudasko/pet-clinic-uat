package org.springframework.samples.petclinic.rest.controller;

public class PetClinicFixture {

    String name;

    String type;
    public PetClinicFixture()
    {
        System.out.println("called");
    }
    public String setName(String name) {
        System.out.println(name);

        return name;
    }

    public String setName() {
        System.out.println("name");

        return "asd";
    }
    public void setPet()
    {
        System.out.println("settingpet");
    }
    public boolean setPet(String name, String type) {
        System.out.println("Adding a pet");
        return true;
        // Logic to call your API and add a pet
        // Return true if successful, false otherwise
    }

    public void setPet(String name) {
        System.out.println("Adding a pet");
        System.out.println(name);
        this.name = name;
        this.type = "testtype";
        // Logic to call your API and add a pet
        // Return true if successful, false otherwise
    }

    public boolean type() {
        return type.equals("");
    }
}
