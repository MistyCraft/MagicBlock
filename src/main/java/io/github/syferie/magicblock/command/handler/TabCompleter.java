package io.github.syferie.magicblock.command.handler;

import io.github.syferie.magicblock.MagicBlockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    private final MagicBlockPlugin plugin;
    private final List<String> commands = Arrays.asList("get", "reload", "settimes", "addtimes", "getfood", "help", "give", "list", "performance", "perf");

    public TabCompleter(MagicBlockPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (command.getName().equalsIgnoreCase("magicblock")) {
            if (args.length == 1) {
                // 基础命令
                List<String> availableCommands = new ArrayList<>();
                availableCommands.add("help");
                
                // 根据权限添加命令
                if (sender.hasPermission("magicblock.get")) {
                    availableCommands.add("get");
                }
                if (sender.hasPermission("magicblock.give")) {
                    availableCommands.add("give");
                }
                if (sender.hasPermission("magicblock.getfood")) {
                    availableCommands.add("getfood");
                }
                if (sender.hasPermission("magicblock.settimes")) {
                    availableCommands.add("settimes");
                }
                if (sender.hasPermission("magicblock.addtimes")) {
                    availableCommands.add("addtimes");
                }
                if (sender.hasPermission("magicblock.list")) {
                    availableCommands.add("list");
                }
                if (sender.hasPermission("magicblock.reload")) {
                    availableCommands.add("reload");
                }
                if (sender.hasPermission("magicblock.performance")) {
                    availableCommands.add("performance");
                    availableCommands.add("perf");
                }

                // 过滤并返回匹配的命令
                String input = args[0].toLowerCase();
                completions.addAll(availableCommands.stream()
                    .filter(cmd -> cmd.startsWith(input))
                    .collect(Collectors.toList()));
            } else if (args.length == 2) {
                // 针对特定命令的第二个参数提供补全
                switch (args[0].toLowerCase()) {
                    case "give":
                        if (sender.hasPermission("magicblock.give")) {
                            // 返回在线玩家列表
                            String input = args[1].toLowerCase();
                            completions.addAll(Bukkit.getOnlinePlayers().stream()
                                .map(Player::getName)
                                .filter(name -> name.toLowerCase().startsWith(input))
                                .collect(Collectors.toList()));
                        }
                        break;
                    case "getfood":
                        if (sender.hasPermission("magicblock.getfood")) {
                            // 从配置文件中获取可用食物列表
                            String input = args[1].toLowerCase();
                            if (plugin.getFoodConfig().contains("foods")) {
                                completions.addAll(plugin.getFoodConfig().getConfigurationSection("foods").getKeys(false).stream()
                                    .filter(food -> food.toLowerCase().startsWith(input))
                                    .collect(Collectors.toList()));
                            }
                        }
                        break;
                    case "addtimes":
                        if (sender.hasPermission("magicblock.addtimes")) {
                            // 如果是控制台执行，第二个参数是玩家名
                            if (!(sender instanceof Player)) {
                                String input = args[1].toLowerCase();
                                completions.addAll(Bukkit.getOnlinePlayers().stream()
                                    .map(Player::getName)
                                    .filter(name -> name.toLowerCase().startsWith(input))
                                    .collect(Collectors.toList()));
                            } else {
                                // 如果是玩家执行，第二个参数是次数，提供一些常用数值
                                String input = args[1].toLowerCase();
                                List<String> commonValues = Arrays.asList("100", "500", "1000", "5000", "10000");
                                completions.addAll(commonValues.stream()
                                    .filter(value -> value.startsWith(input))
                                    .collect(Collectors.toList()));
                            }
                        }
                        break;
                }
            } else if (args.length == 3) {
                // 针对特定命令的第三个参数提供补全
                switch (args[0].toLowerCase()) {
                    case "give":
                        if (sender.hasPermission("magicblock.give")) {
                            // 第三个参数是次数，提供一些常用数值
                            String input = args[2].toLowerCase();
                            List<String> commonValues = Arrays.asList("100", "500", "1000", "5000", "10000", "-1");
                            completions.addAll(commonValues.stream()
                                .filter(value -> value.startsWith(input))
                                .collect(Collectors.toList()));
                        }
                        break;
                    case "addtimes":
                        if (sender.hasPermission("magicblock.addtimes") && !(sender instanceof Player)) {
                            // 控制台执行时，第三个参数是次数
                            String input = args[2].toLowerCase();
                            List<String> commonValues = Arrays.asList("100", "500", "1000", "5000", "10000");
                            completions.addAll(commonValues.stream()
                                .filter(value -> value.startsWith(input))
                                .collect(Collectors.toList()));
                        }
                        break;
                    case "getfood":
                        if (sender.hasPermission("magicblock.getfood")) {
                            // 第三个参数是次数，提供一些常用数值
                            String input = args[2].toLowerCase();
                            List<String> commonValues = Arrays.asList("64", "100", "500", "1000", "-1");
                            completions.addAll(commonValues.stream()
                                .filter(value -> value.startsWith(input))
                                .collect(Collectors.toList()));
                        }
                        break;
                }
            }
        }

        return completions;
    }
}





