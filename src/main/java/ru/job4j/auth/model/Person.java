package ru.job4j.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Table(name = "person")
@Entity
public class Person {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String login;

    private String password;

    @JsonIgnore
    @ManyToMany(mappedBy = "accounts")
    private Set<Employee> employees = new HashSet<>();

    public Person(int id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    @PreRemove
    public void removeAccountFromEmployees() {
        for (Employee employee : employees) {
            employee.getAccounts().remove(this);
        }
    }
}
