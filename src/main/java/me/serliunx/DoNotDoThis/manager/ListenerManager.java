package me.serliunx.DoNotDoThis.manager;

import me.serliunx.DoNotDoThis.Main;
import me.serliunx.DoNotDoThis.listener.Listener;
import me.serliunx.DoNotDoThis.listener.Listeners;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ListenerManager implements org.bukkit.event.Listener{
    protected final Main instance;
    private final List<Listener> listeners = new ArrayList<>();

    public ListenerManager(){
        this.instance = Main.getPlugin();
    }

    private void registerListeners(){
        Listeners listeners = instance.getListeners();

        for(Field field:listeners.getClass().getFields()){
            try{
                Listener listener = (Listener) field.get(listeners);
                registerListener(listener);
            }catch (IllegalAccessException exception){
                exception.printStackTrace();
            }
        }
    }

    public void registerListener(Listener listener){
        if(!listeners.contains(listener))
            listeners.add(listener);
    }
}
