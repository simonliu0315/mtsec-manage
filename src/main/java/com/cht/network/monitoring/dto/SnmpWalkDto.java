package com.cht.network.monitoring.dto;

public class SnmpWalkDto {

    private String name;

    private String oid;

    private String variable;

    private String variableSyntax;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public String getVariableSyntax() {
        return variableSyntax;
    }

    public void setVariableSyntax(String variableSyntax) {
        this.variableSyntax = variableSyntax;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SnmpWalkDto{" +
                "name='" + name + '\'' +
                ", oid='" + oid + '\'' +
                ", variable='" + variable + '\'' +
                ", variableSyntax='" + variableSyntax + '\'' +
                '}';
    }
}
