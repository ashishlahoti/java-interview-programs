package com.example.designpattern;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Immutable class should mark as final so it can not be extended. 
 * Fields should mark as private so direct access is not allowed.
 * Fields should mark as final so value can not be modified once initialized.
 **/
public final class ImmutablePerson {

    // String - immutable
    private final String name;
    
    // Integer - immutable
    private final Integer weight;
    
    // Date - mutable
    private final Date dateOfBirth;
    
    /**
     * All the final fields are initialized through constructor
     * Perform a deep copy of immutable objects
     */
    public ImmutablePerson(String name, Integer weight, Date dateOfBirth){
        this.name = name;
        this.weight = weight;
        this.dateOfBirth = new Date(dateOfBirth.getTime());
    }
    
    /**********************************************
     ***********PROVIDE NO SETTER METHODS *********
     **********************************************/
    
    /**
     * String class is immutable so we can return the instance variable as it is
     **/
    public String getName() {
        return name;
    }
    
    /**
     * Integer class is immutable so we can return the instance variable as it is
     **/
    public Integer getWeight() {
        return weight;
    }
    
    /**
     * Date class is mutable so we need a little care here.
     * We should not return the reference of original instance variable.
     * Instead a new Date object, with content copied to it, should be returned.
     **/
    public Date getDateOfBirth() {
        return new Date(dateOfBirth.getTime());
    }
    
    @Override
    public String toString() {
    	return "Person { name: " + name + ", weight: " + weight + ", dateOfBirth: " + new SimpleDateFormat("dd-MM-yyyy").format(dateOfBirth) + "}";
    }
}