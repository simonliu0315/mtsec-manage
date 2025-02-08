package com.cht.network.monitoring.domain;


import jakarta.persistence.*;

@Entity
@Table(name = "role_functional")
@IdClass(RoleFunctionalPK.class)
public class RoleFunctional {

    @Id
    @Column(name = "role", nullable = false, length = 30)
    private String role;

    @Id
    @Column(name = "role_functional", nullable = false, length = 45)
    private String roleFunctional;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRoleFunctional() {
        return roleFunctional;
    }

    public void setRoleFunctional(String roleFunctional) {
        this.roleFunctional = roleFunctional;
    }
}
