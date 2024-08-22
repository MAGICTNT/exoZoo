package fr.maxime.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Animal {
    private int idAnimal;
    private String labelAnimal;
    private String descriptionAnimal;
    private short  age;
    private Race race;
}
