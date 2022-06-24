package me.serliunx.DoNotDoThis.manager;

import me.serliunx.DoNotDoThis.Main;
import me.serliunx.DoNotDoThis.listener.Listener;
import me.serliunx.DoNotDoThis.listener.Listeners;
import org.bukkit.Bukkit;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ListenerManager{
    protected final Main instance;
    private final List<Listener> listenerList = new ArrayList<>();

    public ListenerManager(){
        this.instance = Main.getPlugin();
        registerListeners();

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

        for(Listener listener:listenerList){
            Bukkit.getPluginManager().registerEvents(listener, instance);
        }
    }

    public void registerListener(Listener listener){
        if(!listenerList.contains(listener))
            listenerList.add(listener);
    }
}
