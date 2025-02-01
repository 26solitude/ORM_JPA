package org.example.orm_jpa.ch05;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Entity
public class Member {

    @Id
    @Column(name = "MEMBER_ID")
    private String id;

    private String username;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public void setTeam(Team team) {

        if (this.team != null) {
            this.team.getMembers().remove(this);
        }

        this.team = team;

        team.getMembers().add(this);
    }

    public Member() {

    }

    public Member(String id, String username) {
        this.id = id;
        this.username = username;
    }
}

