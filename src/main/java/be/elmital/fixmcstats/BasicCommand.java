package be.elmital.fixmcstats;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.metadata.ModEnvironment;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.StringRepresentableArgument;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.util.CommonColors;
import net.minecraft.util.StringRepresentable;
import org.apache.commons.lang3.function.TriConsumer;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;



public class BasicCommand {
    static void registerArgumentTypes() {
        ArgumentTypeRegistry.registerArgumentType(Identifier.parse("fix-mc-stats:patch"), PatchArgumentType.class, SingletonArgumentInfo.contextAware(access -> PatchArgumentType.patchArgument(null)));
        ArgumentTypeRegistry.registerArgumentType(Identifier.parse("fix-mc-stats:patchaction"), PatchActionArgumentType.class, SingletonArgumentInfo.contextAware(access -> PatchActionArgumentType.pathAction()));
    }

    static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            FixMCStats.LOGGER.info("Registering commands for server side");
            dispatcher.register(BasicCommand.commandNodeBuilder(ModEnvironment.SERVER, (serverCommandSource, text, aBoolean) -> serverCommandSource.sendSuccess(() -> text , aBoolean)));
            FixMCStats.LOGGER.info("Commands registered!");
        });
    }

    public static <S extends SharedSuggestionProvider, E extends ModEnvironment> LiteralArgumentBuilder<S> commandNodeBuilder(E environment, TriConsumer<S, Component, Boolean> sourceNotification) {
        return LiteralArgumentBuilder.<S>literal("fixmcstats-" + environment.name().toLowerCase())
                .requires(source -> !(source instanceof CommandSourceStack stack) || stack.permissions().hasPermission(Permissions.COMMANDS_ADMIN))
                .then(LiteralArgumentBuilder.<S>literal("translation")
                        .executes(commandContext -> {
                            notifySource(commandContext.getSource(), Component.literal("If you find errors in the translations or would like to contribute by adding a new language that you are fluent in, you can open an issue in the Github project :").withStyle(style -> style.withColor(CommonColors.SOFT_YELLOW)).append(Component.literal("[CLICK TO OPEN]").withStyle(style -> style.withClickEvent(new ClickEvent.OpenUrl(URI.create("https://github.com/elmital/FixMCStats/issues"))).withColor(CommonColors.HIGH_CONTRAST_DIAMOND))), false, sourceNotification);
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(LiteralArgumentBuilder.<S>literal("status")
                        .then(RequiredArgumentBuilder.<S,Configs.ConfigEntry>argument("patch", PatchArgumentType.patchArgument(environment))
                                .executes(commandContext -> {
                                    Configs.ConfigEntry patchEntry = PatchArgumentType.getPatch(commandContext, "patch");
                                    notifySource(commandContext.getSource(), (patchEntry.isExperimental() ? Component.translatable("commands.fix-mc-stats.patch.experimental").withColor(CommonColors.RED).append(Component.literal("\n")) : Component.empty()).append(
                                            Component.translatable("commands.fix-mc-stats.patch.config." + (patchEntry.isActive() ? "activated" : "deactivated"), patchEntry.getPatchId())).withColor(CommonColors.LIGHT_GRAY), false, sourceNotification);
                                    return Command.SINGLE_SUCCESS;
                                }))
                ).then(LiteralArgumentBuilder.<S>literal("link")
                        .then(RequiredArgumentBuilder.<S,Configs.ConfigEntry>argument("patch", PatchArgumentType.patchArgument(environment))
                                .executes(commandContext -> {
                                    Configs.ConfigEntry patchEntry = PatchArgumentType.getPatch(commandContext, "patch");
                                    notifySource(commandContext.getSource(), Component.translatable("commands.fix-mc-stats.patch.link", patchEntry.getPatchId()).withColor(CommonColors.HIGH_CONTRAST_DIAMOND).withStyle(style -> patchEntry.getPatchLink() == null ? style : style.withClickEvent(new ClickEvent.OpenUrl(patchEntry.getPatchLink()))), false, sourceNotification);
                                    return Command.SINGLE_SUCCESS;
                                }))
                ).then(RequiredArgumentBuilder.<S,PatchAction>argument("action", PatchActionArgumentType.pathAction())
                        .then(RequiredArgumentBuilder.<S,Configs.ConfigEntry>argument("patch", PatchArgumentType.patchArgument(environment))
                                .executes(context -> {
                                    Configs.ConfigEntry patchEntry = PatchArgumentType.getPatch(context, "patch");
                                    boolean activate = PatchActionArgumentType.getPathAction(context, "action").equals(PatchAction.ACTIVATE);
                                    if (patchEntry.isActive() == activate) {
                                        notifySource(context.getSource(), Component.translatable("commands.fix-mc-stats.already." + (activate ? "activated" : "deactivated")).withColor(CommonColors.RED), false, sourceNotification);
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    try {
                                        patchEntry.updateActive(activate);
                                        notifySource(context.getSource(), Component.translatable("commands.fix-mc-stats.config.update.success." + (activate ? "activated" : "deactivated"), patchEntry.getPatchId()).withColor(CommonColors.GREEN), true, sourceNotification);
                                    } catch (IOException e) {
                                        notifySource(context.getSource(), Component.translatable("commands.fix-mc-stats.config.update.fail"), false, sourceNotification);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })
                        ));
    }

    private static <C extends SharedSuggestionProvider> void notifySource(C source, Component message, boolean broadCastOp, TriConsumer<C,Component,Boolean> consumer) {
        consumer.accept(source, message, broadCastOp);
    }

    public enum PatchAction implements StringRepresentable {
        ACTIVATE,
        DEACTIVATE;

        public static final Codec<PatchAction> CODEC = StringRepresentable.fromEnum(PatchAction::values);

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }


    public static class PatchActionArgumentType extends StringRepresentableArgument<PatchAction> {

        private PatchActionArgumentType() {
            super(PatchAction.CODEC, PatchAction::values);
        }

        public static PatchActionArgumentType pathAction() {
            return new PatchActionArgumentType();
        }

        public static <S> PatchAction getPathAction(CommandContext<S> context, String id) {
            return context.getArgument(id, PatchAction.class);
        }
    }

    public static class PatchArgumentType<M extends ModEnvironment> implements ArgumentType<Configs.ConfigEntry> {
        private final M environment;
        private final Collection<String> EXAMPLES;

        PatchArgumentType(M environment) {
            this.environment = environment;
            EXAMPLES = generateExamples();
        }

        public static <M extends ModEnvironment> PatchArgumentType<M> patchArgument(M modEnvironment) {
            return new PatchArgumentType<>(modEnvironment);
        }

        public static <S> Configs.ConfigEntry getPatch(CommandContext<S> context, String id) {
            return context.getArgument(id, Configs.ConfigEntry.class);
        }

        @Override
        public Configs.ConfigEntry parse(StringReader stringReader) throws CommandSyntaxException {
            var value = stringReader.readUnquotedString();
            for (Configs.ConfigEntry configEntry : Configs.getAllValidPatchConfigEntries(this.environment)) {
                if (Objects.equals(configEntry.getPatchId(), value))
                    return configEntry;
            }
            throw new SimpleCommandExceptionType(Component.nullToEmpty("Invalid patch ID")).createWithContext(stringReader);
        }


        @Override
        public Collection<String> getExamples() {
            return EXAMPLES;
        }

        private Collection<String> generateExamples() {
            return Configs.getAllValidPatchConfigEntries(this.environment).stream().map(Configs.ConfigEntry::getPatchId).filter(Objects::nonNull).sorted(Comparator.naturalOrder()).collect(Collectors.toCollection(ArrayList::new));
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return SharedSuggestionProvider.suggest(EXAMPLES, builder).thenApply(suggestions -> {
                suggestions.getList().sort(Comparator.comparing(suggestion -> Integer.parseInt(suggestion.getText().replace("MC-" , ""))));
                return suggestions;
            });
        }
    }
}
