package de.febanhd.fcommand.wrapper;

import de.febanhd.fcommand.Command;
import de.febanhd.fcommand.FCommandManager;

public interface CommandWrapper {

    void register(Command command, FCommandManager commandManager);
}
