package ca.cmaisonneuve.pokedex.service;

public class PokemonNonTrouveException extends Exception {
    public PokemonNonTrouveException(String message) {
        super(message);
    }
}
