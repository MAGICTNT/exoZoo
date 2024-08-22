package fr.maxime.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Constituer {
    private int idConstituer;
    private Nourriture nourriture;
    private Menu menu;
    private float quantite;
}
