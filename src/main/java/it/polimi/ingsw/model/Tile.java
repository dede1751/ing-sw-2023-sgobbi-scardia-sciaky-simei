package it.polimi.ingsw.model;

public record Tile(Type type, Sprite sprite) {
    
    public static final Tile NOTILE = new Tile(Type.NOTILE, Sprite.NOSPRITE);
    
    public static final int NUM_TILES = 18;
    
    public static final Tile[] ALL_TILES = {
            new Tile(Type.CATS, Sprite.ONE),     new Tile(Type.CATS, Sprite.TWO),     new Tile(Type.CATS, Sprite.THREE),
            new Tile(Type.BOOKS, Sprite.ONE),    new Tile(Type.BOOKS, Sprite.TWO),    new Tile(Type.BOOKS, Sprite.THREE),
            new Tile(Type.GAMES, Sprite.ONE),    new Tile(Type.GAMES, Sprite.TWO),    new Tile(Type.GAMES, Sprite.THREE),
            new Tile(Type.FRAMES, Sprite.ONE),   new Tile(Type.FRAMES, Sprite.TWO),   new Tile(Type.FRAMES, Sprite.THREE),
            new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.TWO), new Tile(Type.TROPHIES, Sprite.THREE),
            new Tile(Type.PLANTS, Sprite.ONE),   new Tile(Type.PLANTS, Sprite.TWO),   new Tile(Type.PLANTS, Sprite.THREE),
    };
    
    public enum Type {
        CATS,
        BOOKS,
        GAMES,
        FRAMES,
        TROPHIES,
        PLANTS,
        NOTILE,
    }
    
    public enum Sprite {
        ONE,
        TWO,
        THREE,
        NOSPRITE,
    }
    
}
