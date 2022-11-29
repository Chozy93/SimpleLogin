package com.example.simplelogin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@SequenceGenerator(
        name = "SQ_ROLE_GENERATOR"
        ,sequenceName = "SQ_ROLE"
        ,initialValue = 1
        ,allocationSize = 1
)
@Table(name="TB_ROLE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE
                    ,generator="SQ_ROLE_GENERATOR"
    )
    @Column
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column
    private ERole name;

    public Role(ERole name) {
        this.name = name;
    }
}
