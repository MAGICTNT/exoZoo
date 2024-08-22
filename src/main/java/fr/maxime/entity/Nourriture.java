package fr.maxime.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Nourriture {
    private int idNourriture;
    private String labelNourriture;
}
