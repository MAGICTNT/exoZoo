package fr.maxime.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Habitat {
    private int idHabitat;
    private String labelHabitat;
}
