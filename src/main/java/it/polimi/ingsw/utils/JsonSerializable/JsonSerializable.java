package it.polimi.ingsw.utils.JsonSerializable;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

public interface JsonSerializable<T>{
    
    JsonSerializer<T> getSerilizerInstance();
    JsonDeserializer<T> getDeserializerInstance();
    
}
