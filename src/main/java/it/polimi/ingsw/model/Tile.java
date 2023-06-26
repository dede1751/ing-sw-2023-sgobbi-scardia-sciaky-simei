package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.exceptions.InvalidStringException;

import java.io.Serializable;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Model's internal shelf representation. <br>
 * Tiles are composed of the Tile Type and the specific Sprite on the physical tile. <br>
 * The Sprite is only used for cosmetic purposes.
 *
 * @param type  the type of the tile
 * @param sprite the sprite of the tile
 */
public record Tile(Type type, Sprite sprite) implements Serializable {
    
    /**
     * Unique tile representing an empty space. <br>
     * Within a single machine, as long as all empty tiles are obtained from Tile.NOTILE, tiles can be compared with
     * tile == Tile.NOTILE. If receiving tiles from a diffrent machine, this becomes impossible.
     */
    public static final Tile NOTILE = new Tile(Type.NOTILE, Sprite.NOSPRITE);
    
    /**
     * Create a new tile of the given type, with the default sprite.
     * @param type the type of the tile
     */
    public Tile(Type type) {
        this(type, Sprite.ONE);
    }
    
    /**
     * Total number of unique tiles.
     */
    public static final int NUM_TILES = 18;
    
    /**
     * List of all unique tiles.
     */
    public static final Tile[] ALL_TILES = {
            new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.TWO), new Tile(Type.CATS, Sprite.THREE),
            new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.TWO), new Tile(Type.BOOKS, Sprite.THREE),
            new Tile(Type.GAMES, Sprite.ONE), new Tile(Type.GAMES, Sprite.TWO), new Tile(Type.GAMES, Sprite.THREE),
            new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.TWO), new Tile(Type.FRAMES, Sprite.THREE),
            new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.TWO), new Tile(Type.TROPHIES,
                                                                                               Sprite.THREE),
            new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.PLANTS, Sprite.TWO), new Tile(Type.PLANTS, Sprite.THREE),
    };
    
    /**
     * Enum for the different types of tiles in the game
     */
    public enum Type {
        /**
         * Tile of type CATS
         */
        CATS,
        /**
         * Tile of type BOOKS
         */
        BOOKS,
        /**
         * Tile of type GAMES
         */
        GAMES,
        /**
         * Tile of type FRAMES
         */
        FRAMES,
        /**
         * Tile of type TROPHIES
         */
        TROPHIES,
        /**
         * Tile of type PLANTS
         */
        PLANTS,
        /**
         * Empty tile
         */
        NOTILE,
    }
    
    /**
     * Enum for the different sprites of tiles in the game. <br>
     * Each tile comes in 3 different sprites.
     */
    public enum Sprite {
        /**
         * Sprite ONE
         */
        ONE,
        /**
         * Sprite TWO
         */
        TWO,
        /**
         * Sprite THREE
         */
        THREE,
        /**
         * Empty sprite
         */
        NOSPRITE,
    }
    
    /**
     * Return the textual representation of the Tile, as in (X,Y) where : <br>
     * - X is the first letter of the Type enum value<br>
     * - Y is the digit of the corrisponding Sprite number, where NOSPRITE is 0
     *
     * @return a valid string object
     */
    @Override
    public String toString() {
        var sp = switch( sprite() ) {
            case ONE -> 1;
            case TWO -> 2;
            case THREE -> 3;
            default -> 0;
        };
        return "(" + type().name().charAt(0) + "," + sp + ")";
    }
    
    
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    private static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    private static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    private static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    private static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    private static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    
    /**
     * Return a string representation of the Tile used by the view.
     *
     * @return a pretty printed tile
     */
    public String toTile() {
        return switch( type() ) {
            case CATS -> ANSI_GREEN_BACKGROUND + ANSI_BLACK + " C " + ANSI_RESET;
            case BOOKS -> ANSI_WHITE_BACKGROUND + ANSI_BLACK + " B " + ANSI_RESET;
            case GAMES -> ANSI_YELLOW_BACKGROUND + ANSI_BLACK + " G " + ANSI_RESET;
            case FRAMES -> ANSI_BLUE_BACKGROUND + ANSI_BLACK + " F " + ANSI_RESET;
            case TROPHIES -> ANSI_CYAN_BACKGROUND + ANSI_BLACK + " T " + ANSI_RESET;
            case PLANTS -> ANSI_PURPLE_BACKGROUND + ANSI_BLACK + " P " + ANSI_RESET;
            case NOTILE -> ANSI_BLACK_BACKGROUND + "   " + ANSI_RESET;
        };
    }
    
    /**
     * Return a tile from a valid string representation.
     *
     * @param string string that represent the Tile
     * @return Tile object
     *
     * @throws InvalidStringException if the string is invald
     */
    public static Tile fromString(String string) throws InvalidStringException {
        Pattern pattern = Pattern.compile("([CBGFTPN],[0123])");
        Type type;
        Sprite sprite;
        if( pattern.matcher(string).find() ) {
            type = Arrays.stream(Type.values()).filter((x) -> x.name().charAt(0) == string.charAt(1)).toList().get(0);
            sprite = switch( string.charAt(3) ) {
                case '1' -> Sprite.ONE;
                case '2' -> Sprite.TWO;
                case '3' -> Sprite.THREE;
                default -> Sprite.NOSPRITE;
            };
            return new Tile(type, sprite);
        }else {
            throw new InvalidStringException();
        }
        
    }
}
