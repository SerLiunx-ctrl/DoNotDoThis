package me.serliunx.DoNotDoThis.manager;

import me.serliunx.DoNotDoThis.Main;
import me.serliunx.DoNotDoThis.command.Command;
import me.serliunx.DoNotDoThis.command.Commands;
import me.serliunx.DoNotDoThis.utils.StringUtils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Field;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class CommandManager implements CommandExecutor, TabCompleter {

    private final List<Command> commands = new ArrayList<>();

    public CommandManager(String command){
        PluginCommand pluginCommand = Main.getPlugin().getCommand(command);
        if(pluginCommand != null){
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
        }
        registerCommands();
    }

    private void registerCommands(){
        Commands commands = Main.getPlugin().getCommands();
        for (Field field : commands.getClass().getFields()) {
            try {
                Command command = (Command) field.get(commands);
                registerCommand(command);
            } catch (IllegalAccessException exception) {
                exception.printStackTrace();
            }
        }
    }

    public void registerCommand(Command command) {
        if (command.isEnabled()) {
            int index = Collections.binarySearch(commands, command, Comparator.comparing(cmd -> cmd.getAliases().get(0)));
            commands.add(index < 0 ? -(index + 1) : index, command);
        }
    }

    public void unregisterCommand(Command command) {
        commands.remove(command);
    }

    public void reloadCommands(){
        commands.clear();
        registerCommands();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command cmd, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0){
            commandSender.sendMessage("i've no idea what you mean.");
            return false;
        }
        for(Command command : commands){
            if(!(command.getAliases().contains(args[0])))
                continue;

            Command executingCommand = findExecutingCommand(command, args);
            if (executionBlocked(executingCommand, commandSender)) {
                return false;
            }

            boolean success = executingCommand.execute(commandSender, args);
            if (success) executingCommand.getCooldownProvider().applyCooldown(commandSender);
            return true;
        }

        //??????????????????
        commandSender.sendMessage("try again?");
        return false;
    }

    /**
     *
     * ????????????????????????????????????.
     *
     * @param command ??????.
     * @param commandSender ?????????.
     * @return ???????????????????????????, ???????????????.
     */
    private boolean executionBlocked(Command command, @NotNull CommandSender commandSender) {
        if (command.isForPlayer() && !(commandSender instanceof Player)) {
            commandSender.sendMessage("only for player");
            return true;
        }

        if (!hasPermissions(commandSender, command)) {
            commandSender.sendMessage("you do not have permission to do this.");
            return true;
        }

        CooldownProvider<CommandSender> cooldownProvider = command.getCooldownProvider();

        if (commandSender instanceof Player && cooldownProvider.isOnCooldown(commandSender)) {
            Duration remainingTime = cooldownProvider.getRemainingTime(commandSender);
            String formattedTime = StringUtils.formatDuration("{seconds}", remainingTime);
            commandSender.sendMessage("please wait {seconds} second(s).");
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command cmd, @NotNull String label, @NotNull String[] args) {

        //???????????????????????????, ??????????????????.
        if (args.length == 1) {
            ArrayList<String> result = new ArrayList<>();
            for (Command command : commands) {
                for (String alias : command.getAliases()) {
                    if (alias.toLowerCase().startsWith(args[0].toLowerCase()) && hasPermissions(commandSender, command)) {
                        result.add(alias);
                    }
                }
            }

            return result;
        }

        //????????????????????????.
        for (Command command : commands) {
            if (command.getAliases().contains(args[0])) {
                Command executingCommand = findExecutingCommand(command, args);
                if (hasPermissions(commandSender, executingCommand)) {
                    List<String> tabCompletion = new ArrayList<>(executingCommand.onTabComplete(commandSender, cmd, label, args));

                    executingCommand.getChilds().stream()
                            .filter(subCommand -> hasPermissions(commandSender, subCommand))
                            .map(subCommand -> subCommand.getAliases().get(0))
                            .forEach(tabCompletion::add);

                    return filterTabCompletionResults(tabCompletion, args);
                }
            }
        }

        //?????????????????????????????????????????????????????????, ?????????????????????????????????.
        return Collections.emptyList();
    }

    /**
     *
     * ????????????????????????????????????.
     *
     * @param command ??????.
     * @param commandSender ?????????.
     * @return ????????????????????????, ???????????????.
     */
    private boolean hasPermissions(@NotNull CommandSender commandSender, Command command) {
        return commandSender.hasPermission(command.getPermission())
                || command.getPermission().equalsIgnoreCase("")
                || command.getPermission().equalsIgnoreCase("chatmanagement.");
    }

    private Command findExecutingCommand(Command baseCommand, String[] args) {
        Command executingCommand = baseCommand;

        for (int currentArgument = 1; currentArgument < args.length; currentArgument++) {
            Optional<Command> child = executingCommand.getChildByName(args[currentArgument]);
            if (!child.isPresent()) break;

            executingCommand = child.get();
        }

        return executingCommand;
    }

    private List<String> filterTabCompletionResults(List<String> tabCompletion, String[] arguments) {
        return tabCompletion.stream()
                .filter(completion -> completion.toLowerCase().contains(arguments[arguments.length - 1].toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Command> getCommands() {
        return commands;
    }
}
