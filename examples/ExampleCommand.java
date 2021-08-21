import de.febanhd.fcommand.FCommand;
import de.febanhd.fcommand.builder.CommandBuilder;
import de.febanhd.fcommand.builder.ParameterBuilder;
import org.bukkit.entity.Player;

public class ExampleCommand implements FCommand {

    //Create heal command

    // /heal [Player]
    // alias: /h [Player]

    @Override
    public CommandBuilder create(CommandBuilder builder) {
        builder.aliases("h");
        builder.permission("example.heal");
        builder.parameter(
                ParameterBuilder.beginParameter("Player")
                        .permission("example.heal.other")
                        .autoCompletionHandler(new ExampleAutoCompletionHandler())
                        .required()
                        .build()
        );
        builder.handler((executor, ctx) -> {
            Player player = ctx.getParameterValue("Player", Player.class);
            player.setHealth(20);
            player.setFoodLevel(20);
            executor.sendMessage("§7You have healed §a" + player.getName());
        });
        return builder;
    }
}
