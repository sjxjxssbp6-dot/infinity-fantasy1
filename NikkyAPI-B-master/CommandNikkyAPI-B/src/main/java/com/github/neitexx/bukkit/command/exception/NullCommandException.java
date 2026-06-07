package com.github.neitexx.bukkit.command.exception;

/**
 * Created by NeitExx on 09.08.2020
 */

public class NullCommandException extends RuntimeException {

    public NullCommandException(){
        super("The command was not found. There may be a problem in plugin.yml or JavaPlugin link.");
    }

}
