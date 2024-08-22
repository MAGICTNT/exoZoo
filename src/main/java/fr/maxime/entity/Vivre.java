package fr.maxime.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Vivre {
    private int idVivre;
    private Animal animal;
    private Habitat habitat;
}
