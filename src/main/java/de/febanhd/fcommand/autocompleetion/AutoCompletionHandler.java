package de.febanhd.fcommand.autocompleetion;

import com.google.common.collect.Lists;
import de.febanhd.fcommand.executor.CommandExecutor;

import java.util.List;

public interface AutoCompletionHandler<T> {

    List<String> getCompletions(CommandExecutor executor);
    boolean isValid(String in, CommandExecutor executor);
    T convertString(String string);
}
