import de.febanhd.fcommand.FCommandManager;
import de.febanhd.fcommand.wrapper.bukkit.BukkitCommandWrapper;
import org.bukkit.plugin.java.JavaPlugin;

public class ExampleMain extends JavaPlugin {

    private FCommandManager commandManager;

    @Override
    public void onEnable() {
        commandManager = FCommandManager.create(new BukkitCommandWrapper(this)); //create command manager
        commandManager.registerCommand(new ExampleCommand(), "heal"); //register command
    }
}
