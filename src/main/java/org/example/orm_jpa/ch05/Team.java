package org.example.orm_jpa.ch05;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
//@Entity
public class Team {

    @Id
    @Column(name = "TEAM_ID")
    private String id;
    private String name;


    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();


    public Team() {

    }

    public Team(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
