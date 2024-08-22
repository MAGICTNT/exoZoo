package fr.maxime.entity;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class Alimenter {
    private int idAlimenter;
    private Animal animal;
    private Menu menu;
    private Date mommentAlimentation;
}
