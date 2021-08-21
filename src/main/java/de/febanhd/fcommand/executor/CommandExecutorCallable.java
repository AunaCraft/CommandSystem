package de.febanhd.fcommand.executor;

public interface CommandExecutorCallable<T> {
    T call(CommandExecutor executor);
}
