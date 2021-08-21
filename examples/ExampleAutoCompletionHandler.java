

import com.google.common.collect.Lists;
import de.febanhd.fcommand.autocompleetion.AutoCompletionHandler;
import de.febanhd.fcommand.executor.CommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

//Task: autocompletion of Bukkit Players
public class ExampleAutoCompletionHandler implements AutoCompletionHandler<Player> {

    @Override
    public List<String> getCompletions(CommandExecutor executor) {
        //Create a list where all completions come in
        List<String> matches = Lists.newArrayList();

        //Get bukkit commandsender from fcommand commandexecutor
        CommandSender bukkitCommandSender = executor.toBukkitCommandSender();

        //check if commandsender is player
        if(bukkitCommandSender instanceof Player) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

                //check if the player can see the other player. So that he only gets the players as completion that he can see
                if(((Player) bukkitCommandSender).canSee(onlinePlayer))
                    matches.add(onlinePlayer.getName());

            }
        }else { //not a player -> console

            //add all players because the console "sees" all players
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                matches.add(onlinePlayer.getName());
            }
        }
        return matches;
    }

    //Check if the entered player name matches a player the executor can see
    @Override
    public boolean isValid(String in, CommandExecutor executor) {
        Player player = Bukkit.getPlayer(in);
        if(player == null || !player.isOnline()) return false;

        //Get bukkit commandsender from fcommand commandexecutor
        CommandSender bukkitCommandSender = executor.toBukkitCommandSender();

        //check if commandsender is player
        if(bukkitCommandSender instanceof Player) {
            if(!((Player) bukkitCommandSender).canSee(player)) return false;
        }
        return true;
    }

    //convert playername to Player object
    @Override
    public Player convertString(String string) {
        return Bukkit.getPlayer(string);
    }
}
