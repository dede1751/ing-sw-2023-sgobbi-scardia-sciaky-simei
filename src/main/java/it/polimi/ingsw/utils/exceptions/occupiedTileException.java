package it.polimi.ingsw.utils.exceptions;

public class occupiedTileException extends Exception{
     public occupiedTileException(){
         System.out.println("You are trying to insert a tile in an already occupied coordinate");
     }

}
