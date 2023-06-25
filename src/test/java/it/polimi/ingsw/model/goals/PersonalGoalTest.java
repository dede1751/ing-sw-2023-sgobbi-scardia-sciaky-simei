package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.goals.personal.PersonalGoal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonalGoalTest {
    Shelf shelf;
    
    @BeforeEach
    public void initShelf() {
        this.shelf = new Shelf();
    }
    
    @Test
    public void personaGoalTestTwelvePoints() {
        
        
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE)), 0);
        
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE)), 1);
        
        shelf.addTiles(List.of(
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.FRAMES, Tile.Sprite.ONE)), 2);
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE)), 3);
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE)), 4);
        PersonalGoal personalGoal = new PersonalGoal(0);
        assertEquals(12, personalGoal.checkGoal(shelf));
        
        
    }
    
    @Test
    public void personaGoalTestNinePoints() {
        
        
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE)), 0);
        
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE)), 1);
        
        shelf.addTiles(List.of(
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.FRAMES, Tile.Sprite.ONE)), 2);
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE)), 3);
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE)), 4);
        PersonalGoal personalGoal = new PersonalGoal(0);
        assertEquals(9, personalGoal.checkGoal(shelf));
        
        
    }
    
    
    @Test
    public void personaGoalTestSixPoints() {
        
        
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE)), 0);
        
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE)), 1);
        
        shelf.addTiles(List.of(
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.FRAMES, Tile.Sprite.ONE)), 2);
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE)), 3);
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE)), 4);
        PersonalGoal personalGoal = new PersonalGoal(0);
        assertEquals(6, personalGoal.checkGoal(shelf));
        
    }
    
    
    @Test
    public void personaGoalTestFourPoints() {
        
        
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE)), 0);
        
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE)), 1);
        
        shelf.addTiles(List.of(
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.FRAMES, Tile.Sprite.ONE)), 2);
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE)), 3);
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE)), 4);
        PersonalGoal personalGoal = new PersonalGoal(0);
        assertEquals(4, personalGoal.checkGoal(shelf));
        
    }
    
    
    @Test
    public void personaGoalTestTwoPoints() {
        
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE)), 0);
        
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE)), 1);
        
        shelf.addTiles(List.of(
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.FRAMES, Tile.Sprite.ONE)), 2);
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE)), 3);
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE)), 4);
        PersonalGoal personalGoal = new PersonalGoal(0);
        assertEquals(2, personalGoal.checkGoal(shelf));
        
    }
    
    
    @Test
    public void personaGoalTestOnePoints() {
        
        
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE)), 0);
        
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE)), 1);
        
        shelf.addTiles(List.of(
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE)), 2);
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE)), 3);
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE)), 4);
        PersonalGoal personalGoal = new PersonalGoal(0);
        assertEquals(1, personalGoal.checkGoal(shelf));
        
    }
    
    @Test
    public void personaGoalTestNoPoints() {
        
        
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE)), 0);
        
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE)), 1);
        
        shelf.addTiles(List.of(
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE)), 2);
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE)), 3);
        shelf.addTiles(List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE)), 4);
        PersonalGoal personalGoal = new PersonalGoal(0);
        assertEquals(0, personalGoal.checkGoal(shelf));
        
    }
    
    
}

