package com.zlgspace.singleinstancehelper.build;

public class BuildClass {

    private String packageStr;

    private String name;

    private String clzModifiers;

    private StringBuilder importBuilder = new StringBuilder();

    private StringBuilder fieldBuilder = new StringBuilder();

    private StringBuilder methodBuilder = new StringBuilder();

    public BuildClass(){
    }

    public BuildClass(String name){
        this.name = name;
    }

    public String getPackage() {
        return packageStr;
    }

    public void setPackage(String packageStr) {
        this.packageStr = packageStr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClzModifiers() {
        return clzModifiers;
    }

    public void setClzModifiers(String clzModifiers) {
        this.clzModifiers = clzModifiers;
    }

    public void appendImport(String importStr){
        importBuilder.append(importStr);
    }

    public void appendField(String field){
        fieldBuilder.append(field);
    }

    public void appendMethod(String method){
        methodBuilder.append(method);
    }

    @Override
    public String toString() {
        StringBuilder clzBuilder = new StringBuilder();
        clzBuilder.append(importBuilder);
        clzBuilder.append("\n"+clzModifiers+" "+name+"{\n\n");
        clzBuilder.append(fieldBuilder);
        clzBuilder.append("\n");
        clzBuilder.append(methodBuilder);
        clzBuilder.append("\n\n");
        clzBuilder.append("}");
        return clzBuilder.toString();
    }
}
