package uk.co.breadhub.mysqlecon.listeners;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import uk.co.breadhub.mysqlecon.Main;
import uk.co.breadhub.mysqlecon.utils.StringUtils;

import java.math.BigDecimal;

public class CommandListener implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            switch (args.length) {
                case 0: {
                    sendHelp(sender);
                    break;
                }
                case 1: {
                    if (args[0].toLowerCase().equals("balance")) {
                        // get balance from database
                        sender.sendMessage(StringUtils.replaceColors("&7[&6Bank&7] &3You Have&7: &2$" + Main.getDatabase().userBalance(Bukkit.getOfflinePlayer(sender.getName()).getUniqueId())));
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
                                sender.sendMessage(StringUtils.replaceColors("&7[&6Bank&7] &3User&7: &2" + args[1] + " &3has&7: &2$" + Main.getDatabase().userBalance(Bukkit.getOfflinePlayer(args[1]).getUniqueId())));
                            } else {
                                sender.sendMessage(StringUtils.replaceColors("&4You do not have permission to use this command"));
                            }
                            break;
                        }
                        case "deposit": {
                            if (Integer.parseInt(args[1]) >= 0.0001) {
                                if (Economy.getMoney(args[1]) >= Integer.parseInt(args[1])) {
                                    Main.getDatabase().userDeposit(Bukkit.getOfflinePlayer(sender.getName()).getUniqueId(), Integer.parseInt(args[1]));
                                    sender.sendMessage(StringUtils.replaceColors(String.format("&7[&6Bank&7] &3You have Deposited&7: &2$%s", Integer.parseInt(args[1]))));
                                    Economy.substract(sender.getName(), BigDecimal.valueOf(Integer.parseInt(args[1])));
                                    break;
                                } else {
                                    sender.sendMessage(StringUtils.replaceColors("&4that Would take you into the minuses"));
                                    break;
                                }
                            } else {
                                sender.sendMessage(StringUtils.replaceColors("&4You cannot Deposit Minus numbers or 0"));
                            }
                            break;
                        }
                        case "withdraw": {
                            if (Integer.parseInt(args[1]) >= 0.0001) {
                                Main.getDatabase().userWithdraw(Bukkit.getOfflinePlayer(sender.getName()).getUniqueId(), Integer.parseInt(args[1]));
                                sender.sendMessage(StringUtils.replaceColors(String.format("&7[&6Bank&7] &3You have withdrawn&7: &4 $%s", Integer.parseInt(args[1]))));
                                Economy.add(sender.getName(), BigDecimal.valueOf(Integer.parseInt(args[1])));

                            } else {
                                sender.sendMessage(StringUtils.replaceColors("&4You cannot Withdraw Minus numbers or 0"));
                            }
                            break;
                        }
                        default:
                            sendHelp(sender);
                            break;
                    }
                    break;
                }
                case 3: {
                    if (sender.hasPermission("mcbank.admin")) {
                        switch (args[0]) {
                            case "deposit": {
                                if (Integer.parseInt(args[2]) >= 0.0001) {
                                    Main.getDatabase().adminDeposit(Bukkit.getOfflinePlayer(args[1]).getUniqueId(), Integer.parseInt(args[2]));
                                    sender.sendMessage(StringUtils.replaceColors(String.format("&7[&6Bank&7] &3You have Deposited&7: &4$%s&2 into &3" + args[1] + "&2's bank", Integer.parseInt(args[2]))));
                                    Economy.substract(args[1], BigDecimal.valueOf(Integer.parseInt(args[2])));
                                } else {
                                    sender.sendMessage(StringUtils.replaceColors("&4You cannot Deposit Minus numbers or 0"));
                                }
                                break;
                            }
                            case "withdraw": {
                                if (Integer.parseInt(args[2]) >= 0.0001) {
                                    sender.sendMessage(StringUtils.replaceColors(String.format("&7[&6Bank&7] &3You have Withdrawn&7: &4$%s&2 from &3" + args[1] + "&2's bank", Integer.parseInt(args[2]))));
                                    Main.getDatabase().adminWithdraw(Bukkit.getOfflinePlayer(args[1]).getUniqueId(), Integer.parseInt(args[2]));
                                    Economy.add(args[1], BigDecimal.valueOf(Integer.parseInt(args[2])));
                                } else {
                                    sender.sendMessage(StringUtils.replaceColors("&4You cannot Withdraw Minus numbers or 0"));
                                }
                                break;
                            }
                            case "set": {
                                if (Integer.parseInt(args[2]) >= 0.0001) {
                                    sender.sendMessage(StringUtils.replaceColors(String.format("&7[&6Bank&7] &3You have Set&7: &4$%s&2 into &3" + args[1] + "&2's bank", Integer.parseInt(args[2]))));
                                    Main.getDatabase().adminSet(Bukkit.getOfflinePlayer(args[1]).getUniqueId(), Integer.parseInt(args[2]));
                                    Economy.setMoney(args[1], BigDecimal.valueOf(Integer.parseInt(args[2])));
                                } else {
                                    sender.sendMessage(StringUtils.replaceColors("&4You cannot Set Minus numbers or 0"));
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
        } catch (UserDoesNotExistException | NoLoanPermittedException e) {
            e.printStackTrace();
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
            sender.sendMessage(StringUtils.replaceColors("      &7/bank set       &6<user> &8<ammount>"));
            sender.sendMessage("  ");
        }
    }
}
