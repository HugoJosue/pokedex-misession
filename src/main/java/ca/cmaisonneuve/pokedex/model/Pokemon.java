package ca.cmaisonneuve.pokedex.model;

public record Pokemon(
        int id,
        String nom,
        String type1,
        String type2,
        int hp,
        int attaque,
        int defense,
        int attaqueSpe,
        int defenseSpe,
        int vitesse,
        String spriteUrl
) {
}