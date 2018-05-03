package nz.jovial.fopm.command;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import nz.jovial.fopm.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description="Clean all nicknames.", usage="/<command>", source=SourceType.BOTH, rank=Rank.SWING_MANAGER)
public class Command_nickclean {
    private final List<String> FILTER = Arrays.asList("&k", "&n", "&o", "&l", "&m");
    
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        Bukkit.getOnlinePlayers().forEach((Player p) -> {
            Iterator it = FILTER.iterator();
            while (it.hasNext()) {
                String filt = it.toString();
                if (p.getDisplayName().contains(filt)) {
                    p.setDisplayName(p.getDisplayName().replaceAll(filt, ""));
                }
            }
        });
        
        return true;
    }
}
