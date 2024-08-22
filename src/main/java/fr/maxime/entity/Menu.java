package fr.maxime.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Menu {
    private int idMenu;
    private String labelMenu;
    private float quantiteRecommande;
}
