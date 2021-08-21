package de.febanhd.fcommand;

import de.febanhd.fcommand.builder.CommandBuilder;

public interface FCommand {

    CommandBuilder create(CommandBuilder builder);
}
