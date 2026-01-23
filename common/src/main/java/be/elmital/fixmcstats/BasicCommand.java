package be.elmital.fixmcstats;

import be.elmital.fixmcstats.platform.Services;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.serialization.Codec;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.StringRepresentableArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.util.CommonColors;
import net.minecraft.util.StringRepresentable;
import org.apache.commons.lang3.function.TriConsumer;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;



public abstract class BasicCommand<C extends SharedSuggestionProvider> {
    @SuppressWarnings("unchecked,rawtypes")
    public final static ArgumentType<Configs.ConfigEntry, PatchArgumentType<?>, SingletonArgumentInfo<PatchArgumentType<?>>.Template> PATCH_ARGUMENT = new ArgumentType<>(Identifier.parse("fix-mc-stats:patch"), PatchArgumentType.class, SingletonArgumentInfo.contextAware(access -> PatchArgumentType.patchArgument(null)));
    public final static ArgumentType<BasicCommand.PatchAction, PatchActionArgumentType, SingletonArgumentInfo<PatchActionArgumentType>.Template> PATCH_ACTION_ARGUMENT = new ArgumentType<>(Identifier.parse("fix-mc-stats:patchaction"), PatchActionArgumentType.class, SingletonArgumentInfo.contextAware(access -> PatchActionArgumentType.pathAction()));

    public static void notifyArgumentRegisteringStarting() {
        Constants.LOGGER.info("Registering commands argument types...");
    }
    public static void notifyArgumentRegisteringEnding() {
        Constants.LOGGER.info("Commands argument types registered");
    }

    static void registerServerSide(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        Constants.LOGGER.info("Registering commands server side...");
        commandDispatcher.register(BasicCommand.commandNodeBuilder(ModEnvironment.SERVER, (serverCommandSource, text, aBoolean) -> serverCommandSource.sendSuccess(() -> text , aBoolean)));
        Constants.LOGGER.info("Server commands registered!");
    }

    static <S extends SharedSuggestionProvider> void registerClientSide(CommandDispatcher<S> commandDispatcher, BasicCommand<S> basicCommand) {
        Constants.LOGGER.info("Registering commands client side...");
        commandDispatcher.register(BasicCommand.commandNodeBuilder(ModEnvironment.CLIENT, (source, text, aBoolean) -> basicCommand.sendClientFeedBack(source, text)));
        Constants.LOGGER.info("Client commands registered!");
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
                                    if (patchEntry.isActive() == activate && !patchEntry.requireRestart()) {
                                        notifySource(context.getSource(), Component.translatable("commands.fix-mc-stats.already." + (activate ? "activated" : "deactivated")).withColor(CommonColors.RED), false, sourceNotification);
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    try {
                                        patchEntry.updateActive(activate);
                                        if (patchEntry.requireRestart() && Services.PLATFORM.isDedicatedServer())
                                            notifySource(context.getSource(), Component.translatable("commands.fix-mc-stats.config.update.restart.needed").withColor(CommonColors.YELLOW), true, sourceNotification);
                                        else
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

    public abstract void sendClientFeedBack(C clientCommandSource, Component text);

    public static class ArgumentType<B,A extends com.mojang.brigadier.arguments.ArgumentType<B>, T extends net.minecraft.commands.synchronization.ArgumentTypeInfo.Template<A>> {
        final Identifier id;
        final Class<? extends A> clazz;
        final ArgumentTypeInfo<A,T> serializer;

        ArgumentType(Identifier id, Class<? extends A> clazz, ArgumentTypeInfo<A,T> serializer) {
            this.id = id;
            this.clazz = clazz;
            this.serializer = serializer;
        }
    }

    public enum PatchAction implements StringRepresentable {
        ACTIVATE,
        DEACTIVATE;

        public static final Codec<PatchAction> CODEC = StringRepresentable.fromEnum(PatchAction::values);

        @Override
        public @NonNull String getSerializedName() {
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

    public static class PatchArgumentType<M extends ModEnvironment> implements com.mojang.brigadier.arguments.ArgumentType<Configs.ConfigEntry> {
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
