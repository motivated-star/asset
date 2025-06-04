package com.example.demo.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    private Long id;  

    @Column(nullable = false)
    private String fullName;

    private String designation;
}
