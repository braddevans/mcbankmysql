package uk.co.breadhub.mysqlecon.listeners;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.co.breadhub.mysqlecon.Main;
import uk.co.breadhub.mysqlecon.database.DatabaseApi;
import uk.co.breadhub.mysqlecon.utils.StringUtils;

public class CommandListener implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.toString().toLowerCase().equals("bank")) {
            if (sender instanceof Player) {
                switch (args.length) {
                    case 0: {
                        sendHelp(sender);
                        break;
                    }
                    case 1: {
                        if (args[0].toLowerCase().equals("balance")) {
                            // get balance from database
                            Main.getDatabase().userBalance(Bukkit.getPlayer(sender.getName()).getUniqueId());
                        } else {
                            sendHelp(sender);
                        }
                        break;
                    }
                    case 2: {
                        switch (args[0]) {
                            case "balance": {
                                if (sender.hasPermission("msbank.balance.others")) {
                                    // get selected user balance from database args[1]
                                    sender.sendMessage(StringUtils.replaceColors("&7[&6Bank&7] &3User&7: &2" + args[1] + " &3has&7: &2" + Main.getDatabase().userBalance(Bukkit.getPlayer(args[1]).getUniqueId())));
                                } else {
                                    sender.sendMessage("&4You do not have permission to use this command");
                                }
                                break;
                            }
                            case "deposit": {
                                if (Integer.parseInt(args[1]) >= 0.0001) {
                                    if(Main.getDatabase().userBalance(Bukkit.getPlayer(args[1]).getUniqueId()) >= 0.0001 && Main.getDatabase().userBalance(Bukkit.getPlayer(args[1]).getUniqueId()) - Integer.parseInt(args[1]) >= 0) {
                                        Main.getDatabase().userDeposit(Bukkit.getPlayer(sender.getName()).getUniqueId(), args[1]);
                                        sender.sendMessage(StringUtils.replaceColors(String.format("&7[&6Bank&7] &3You have Deposited&7: &2%s", args[1])));
                                    } else {
                                        sender.sendMessage("&4that Would take you into the minuses");
                                    }
                                } else {
                                    sender.sendMessage("&4You cannot Deposit Minus numbers or 0");
                                }
                                break;
                            }
                            case "withdraw": {
                                if (Integer.parseInt(args[1]) >= 0.0001) {
                                    Main.getDatabase().userWithdraw(Bukkit.getPlayer(sender.getName()).getUniqueId(), args[1]);
                                    sender.sendMessage(StringUtils.replaceColors(String.format("&7[&6Bank&7] &3You have withdrawn&7: &2%s", args[1])));
                                } else {
                                    sender.sendMessage("&4You cannot Withdraw Minus numbers or 0");
                                }
                                break;
                            }
                            default:
                                sendHelp(sender);
                                break;
                        }
                    }
                    case 3: {
                        if (sender.hasPermission("mcbank.admin")) {
                            switch (args[0]) {
                                case "deposit": {
                                    if (Integer.parseInt(args[1]) >= 0.0001) {
                                        Main.getDatabase().adminDeposit(Bukkit.getPlayer(sender.getName()).getUniqueId(), args[1]);
                                    } else {
                                        sender.sendMessage("&4You cannot Deposit Minus numbers or 0");
                                    }
                                    break;
                                }
                                case "withdraw": {
                                    if (Integer.parseInt(args[1]) >= 0.0001) {
                                        Main.getDatabase().adminWithdraw(Bukkit.getPlayer(sender.getName()).getUniqueId(), args[1]);
                                    } else {
                                        sender.sendMessage("&4You cannot Withdraw Minus numbers or 0");
                                    }
                                    break;
                                }
                                case "set": {
                                    if (Integer.parseInt(args[1]) >= 0.0001) {
                                        Main.getDatabase().adminSet(Bukkit.getPlayer(sender.getName()).getUniqueId(), args[1]);
                                    } else {
                                        sender.sendMessage("&4You cannot Withdraw Minus numbers or 0");
                                    }
                                    break;
                                }
                                default:
                                    sendHelp(sender);
                                    break;
                            }
                            break;
                        } else {
                            sendHelp(sender);
                            break;
                        }
                    }
                    default:
                        sendHelp(sender);
                        break;
                }
            }
        } else {
            sender.sendMessage("Console Cannot use Player commands");
        }
        return false;
    }



    private void sendHelp(CommandSender sender) {
        sender.sendMessage(StringUtils.replaceColors("&6=&5=&6=&5=&6=&5=&6=&7[&9Bank Help&7]=&6=&5=&6=&5=&6=&5=&6="));
        sender.sendMessage("  ");
        sender.sendMessage("  ");
        sender.sendMessage(StringUtils.replaceColors("  &5User Commands&7:"));
        sender.sendMessage(StringUtils.replaceColors("      &7/bank balance"));
        sender.sendMessage(StringUtils.replaceColors("      &7/bank withdraw &8<ammount>"));
        sender.sendMessage(StringUtils.replaceColors("      &7/bank deposit  &8<ammount>"));
        sender.sendMessage("  ");
        if (sender.hasPermission("msbank.admin")) {
            sender.sendMessage(StringUtils.replaceColors("  &4Admin / Staff Commands&7:"));
            sender.sendMessage(StringUtils.replaceColors("      &7/bank balance  &6<User>"));
            sender.sendMessage(StringUtils.replaceColors("      &7/bank withdraw &6<user> &8<ammount>"));
            sender.sendMessage(StringUtils.replaceColors("      &7/bank deposit  &6<user> &8<ammount>"));
            sender.sendMessage(StringUtils.replaceColors("      &7/bank set      &6<user> &8<ammount>"));
            sender.sendMessage("  ");
        }
    }
}
