package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.utility.JsonConverter;

import java.lang.reflect.Type;

public abstract class Event<T> {
    protected String jsonContent;

    public Event(Object object){
        this.jsonContent = JsonConverter.toJson(object);
    }

    public String getJsonContent(){
        return jsonContent;
    }

    // use these two
    public Object getEventPayload(Class clazz){
        return JsonConverter.fromEventToObject(this, clazz);
    }

    public Object getEventPayload(Type type){
        return JsonConverter.fromEventToObject(this, type);
    }
}
