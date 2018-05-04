package nz.jovial.fopm.command;

import nz.jovial.fopm.rank.Rank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(description = "Vanish.", aliases = "vanish", source = SourceType.IN_GAME, rank = Rank.SWING_MANAGER)
public class Command_v
{

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        //todo
        sender.sendMessage("This command is not yet implemented.");
        return true;
    }
}   