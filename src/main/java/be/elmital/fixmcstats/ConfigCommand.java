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
import net.minecraft.command.CommandSource;
import net.minecraft.command.PermissionLevelSource;
import net.minecraft.command.argument.EnumArgumentType;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import org.apache.commons.lang3.function.TriConsumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;



public class ConfigCommand {
    static <M extends ModEnvironment> void registerArgumentTypes() {
        ArgumentTypeRegistry.registerArgumentType(Identifier.of("fix-mc-stats:patch"), PatchArgumentType.class, ConstantArgumentSerializer.of(access -> PatchArgumentType.patchArgument(ModEnvironment.SERVER)));
        ArgumentTypeRegistry.registerArgumentType(Identifier.of("fix-mc-stats:patchaction"), PatchActionArgumentType.class, ConstantArgumentSerializer.of(access -> PatchActionArgumentType.pathAction()));
    }

    static void registerConfigCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            FixMCStats.LOGGER.info("Registering commands for server side");
            dispatcher.register(ConfigCommand.commandNodeBuilder(ModEnvironment.SERVER, (serverCommandSource, text, aBoolean) -> serverCommandSource.sendFeedback(() -> text , aBoolean)));
            FixMCStats.LOGGER.info("Commands registered!");
        });
    }

    public static <S extends CommandSource, E extends ModEnvironment> LiteralArgumentBuilder<S> commandNodeBuilder(E environment, TriConsumer<S, Text, Boolean> sourceNotification) {
        return LiteralArgumentBuilder.<S>literal("fixmcstats-" + environment.name().toLowerCase())
                .requires(source -> !(source instanceof PermissionLevelSource perm) || perm.hasPermissionLevel(4))
                .then(LiteralArgumentBuilder.<S>literal("status")
                        .then(RequiredArgumentBuilder.<S,Configs.ConfigEntry>argument("patch", PatchArgumentType.patchArgument(environment))
                        .executes(commandContext -> {
                            Configs.ConfigEntry patchEntry = PatchArgumentType.getPatch(commandContext, "patch");
                            notifySource(commandContext.getSource(), (patchEntry.isExperimental() ? Text.translatable("commands.fix-mc-stats.patch.experimental").withColor(Colors.RED).append(Text.literal("\n")) : Text.empty()).append(
                                    Text.translatable("commands.fix-mc-stats.patch.config." + (patchEntry.isActive() ? "activated" : "deactivated"))).withColor(Colors.LIGHT_GRAY), false, sourceNotification);
                            return Command.SINGLE_SUCCESS;
                        }))
                ).then(LiteralArgumentBuilder.<S>literal("link")
                        .then(RequiredArgumentBuilder.<S,Configs.ConfigEntry>argument("patch", PatchArgumentType.patchArgument(environment))
                        .executes(commandContext -> {
                            Configs.ConfigEntry patchEntry = PatchArgumentType.getPatch(commandContext, "patch");
                            notifySource(commandContext.getSource(), Text.translatable("commands.fix-mc-stats.patch.link", patchEntry.getPatchId()).withColor(Colors.CYAN).styled(style -> patchEntry.getPatchLink() == null ? style : style.withClickEvent(new ClickEvent.OpenUrl(patchEntry.getPatchLink()))), false, sourceNotification);
                            return Command.SINGLE_SUCCESS;
                        }))
                ).then(RequiredArgumentBuilder.<S,PatchAction>argument("action", PatchActionArgumentType.pathAction())
                        .then(RequiredArgumentBuilder.<S,Configs.ConfigEntry>argument("patch", PatchArgumentType.patchArgument(environment))
                                .executes(context -> {
                                    Configs.ConfigEntry patchEntry = PatchArgumentType.getPatch(context, "patch");
                                    boolean activate = PatchActionArgumentType.getPathAction(context, "action").equals(PatchAction.ACTIVATE);
                                    if (patchEntry.isActive() == activate) {
                                        notifySource(context.getSource(), Text.translatable("commands.fix-mc-stats.already." + (activate ? "activated" : "deactivated")).withColor(Colors.RED), false, sourceNotification);
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    try {
                                        patchEntry.updateActive(activate);
                                        notifySource(context.getSource(), Text.translatable("commands.fix-mc-stats.config.update.success." + (activate ? "activated" : "deactivated"), patchEntry.getPatchId()).withColor(Colors.GREEN), true, sourceNotification);
                                    } catch (IOException e) {
                                        notifySource(context.getSource(), Text.translatable("commands.fix-mc-stats.config.update.fail"), false, sourceNotification);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })
                        ));
    }

    private static <C extends CommandSource> void notifySource(C source, Text message, boolean broadCastOp, TriConsumer<C,Text,Boolean> consumer) {
        consumer.accept(source, message, broadCastOp);
    }

    public enum PatchAction implements StringIdentifiable {
        ACTIVATE,
        DEACTIVATE;

        public static final Codec<PatchAction> CODEC = StringIdentifiable.createCodec(PatchAction::values);

        @Override
        public String asString() {
            return name().toLowerCase();
        }
    }


    public static class PatchActionArgumentType extends EnumArgumentType<PatchAction> {

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
            for (Configs.ConfigEntry configEntry : Configs.getAllValidPatchConfigEntries(this.environment)) {
                if (Objects.equals(configEntry.getPatchId(), stringReader.readUnquotedString()))
                    return configEntry;
            }
            throw new SimpleCommandExceptionType(Text.of("Invalid patch ID")).createWithContext(stringReader);
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
            return CommandSource.suggestMatching(EXAMPLES, builder);
        }
    }
}
