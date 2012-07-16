package com.aaron;

import org.richfaces.model.TreeNodeImpl;
 
public class OptionTreeNode extends TreeNodeImpl {
    private String name;
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
 
}