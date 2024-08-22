CREATE TABLE race (
                      Id_race SERIAL PRIMARY KEY,
                      label_race VARCHAR(50)
);

CREATE TABLE habitat (
                         Id_habitat SERIAL PRIMARY KEY,
                         label_habitat VARCHAR(50) NOT NULL
);

CREATE TABLE nourriture (
                            Id_nourriture SERIAL PRIMARY KEY,
                            label_nourriture VARCHAR(50) NOT NULL
);

CREATE TABLE menu (
                      Id_menu SERIAL PRIMARY KEY,
                      label_menu VARCHAR(50) NOT NULL,
                      quantite_recommande DECIMAL(13,2)
);

CREATE TABLE animal (
                        Id_animal SERIAL PRIMARY KEY,
                        label_animal VARCHAR(50) NOT NULL,
                        description_animal VARCHAR(50),
                        age SMALLINT,
                        Id_race INT NOT NULL,
                        FOREIGN KEY (Id_race) REFERENCES race(Id_race)
);

CREATE TABLE vivre (
                       Id_animal INT,
                       Id_habitat INT,
                       PRIMARY KEY (Id_animal, Id_habitat),
                       FOREIGN KEY (Id_animal) REFERENCES animal(Id_animal),
                       FOREIGN KEY (Id_habitat) REFERENCES habitat(Id_habitat)
);

CREATE TABLE alimenter (
                           Id_animal INT,
                           Id_menu INT,
                           moment_alimentation TIMESTAMPTZ NOT NULL,
                           PRIMARY KEY (Id_animal, Id_menu),
                           FOREIGN KEY (Id_animal) REFERENCES animal(Id_animal),
                           FOREIGN KEY (Id_menu) REFERENCES menu(Id_menu)
);

CREATE TABLE constituer (
                            Id_nourriture INT,
                            Id_menu INT,
                            quantite DECIMAL(15,2) NOT NULL,
                            PRIMARY KEY (Id_nourriture, Id_menu),
                            FOREIGN KEY (Id_nourriture) REFERENCES nourriture(Id_nourriture),
                            FOREIGN KEY (Id_menu) REFERENCES menu(Id_menu)
);
