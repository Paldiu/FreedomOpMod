package nz.jovial.fopm.command;

import nz.jovial.fopm.rank.Rank;
import nz.jovial.fopm.util.FUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description="Toggle your tp.", usage="/<command>", source=SourceType.IN_GAME, rank=Rank.OP)
public class Command_tptoggle {
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        Player p = (Player) sender;
        FUtil.setTpToggled(p);
        
        return true;
    }
}
