package com.example.expensetracker.repository.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "user_account")
public class UserAccount {

    @Id
    private UUID id;

    @Column(name = "username")
    private String username;

    @Column(name = "budget")
    private double budget;

    @Column(name = "monthly_salary")
    private double monthlySalary;

    public UserAccount() {
    }

    public UserAccount(String username, double budget, double monthlySalary) {
        id = UUID.randomUUID();
        this.username = username;
        this.budget = budget;
        this.monthlySalary = monthlySalary;
    }

    public UserAccount(UUID id, String username, double budget, double monthlySalary) {
        this.id = id;
        this.username = username;
        this.budget = budget;
        this.monthlySalary = monthlySalary;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
               "id=" + id +
               ", username='" + username + '\'' +
               ", budget=" + budget +
               ", monthlySalary=" + monthlySalary +
               '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UserAccount userAccount = (UserAccount) obj;

        return id.equals(userAccount.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public double getBudget() {
        return budget;
    }

    public double getMonthlySalary() {
        return monthlySalary;
    }
}