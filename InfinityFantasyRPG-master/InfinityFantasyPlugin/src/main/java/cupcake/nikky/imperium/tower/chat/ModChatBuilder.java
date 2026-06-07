package cupcake.nikky.imperium.tower.chat;

import lombok.val;
import me.func.mod.ui.MultiChat;
import me.func.protocol.data.chat.ModChat;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ModChatBuilder {

    private String key, name, symbol;

    public static ModChatBuilder create() {
        return new ModChatBuilder();
    }

    public ModChatBuilder setSymbol(@NotNull final String symbol) {
        this.symbol = symbol;
        return this;
    }

    public ModChatBuilder setKey(@NotNull final String key) {
        this.key = key;
        return this;
    }

    public ModChatBuilder setName(@NotNull final String name) {
        this.name = name;
        return this;
    }

    public ModChat build() {
        val chat = new ModChat(UUID.randomUUID(), name, symbol);
        MultiChat.createKey(key, chat);
        return chat;
    }

}
