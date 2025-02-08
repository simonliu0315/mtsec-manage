package com.cht.network.monitoring.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

public class RoleFunctionalPK implements Serializable {

    @Id
    @Column(name = "role", nullable = false, length = 30)
    private String role;

    @Id
    @Column(name = "role_functional", nullable = false, length = 45)
    private String roleFunctional;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RoleFunctionalPK that = (RoleFunctionalPK) o;
        return Objects.equals(role, that.role) && Objects.equals(roleFunctional, that.roleFunctional);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role, roleFunctional);
    }
}
