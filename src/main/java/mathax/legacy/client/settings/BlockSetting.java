package mathax.legacy.client.settings;

import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class BlockSetting extends Setting<Block> {
    public final Predicate<Block> filter;

    public BlockSetting(String name, String description, Block defaultValue, Consumer<Block> onChanged, Consumer<Setting<Block>> onModuleActivated, IVisible visible, Predicate<Block> filter) {
        super(name, description, defaultValue, onChanged, onModuleActivated, visible);

        this.filter = filter;
    }

    @Override
    protected Block parseImpl(String str) {
        return parseId(Registry.BLOCK, str);
    }

    @Override
    protected boolean isValueValid(Block value) {
        return filter == null || filter.test(value);
    }

    @Override
    public Iterable<Identifier> getIdentifierSuggestions() {
        return Registry.BLOCK.getIds();
    }

    @Override
    public NbtCompound toTag() {
        NbtCompound tag = new NbtCompound();

        tag.putString("value", Registry.BLOCK.getId(get()).toString());

        return tag;
    }

    @Override
    public Block fromTag(NbtCompound tag) {
        value = Registry.BLOCK.get(new Identifier(tag.getString("value")));

        if (filter != null && !filter.test(value)) {
            for (Block block : Registry.BLOCK) {
                if (filter.test(block)) {
                    value = block;
                    break;
                }
            }
        }

        changed();
        return get();
    }

    public static class Builder {
        private String name = "undefined", description = "";
        private Block defaultValue;
        private Consumer<Block> onChanged;
        private Consumer<Setting<Block>> onModuleActivated;
        private IVisible visible;
        private Predicate<Block> filter;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder defaultValue(Block defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder onChanged(Consumer<Block> onChanged) {
            this.onChanged = onChanged;
            return this;
        }

        public Builder onModuleActivated(Consumer<Setting<Block>> onModuleActivated) {
            this.onModuleActivated = onModuleActivated;
            return this;
        }

        public Builder filter(Predicate<Block> filter) {
            this.filter = filter;
            return this;
        }

        public Builder visible(IVisible visible) {
            this.visible = visible;
            return this;
        }

        public BlockSetting build() {
            return new BlockSetting(name, description, defaultValue, onChanged, onModuleActivated, visible, filter);
        }
    }
}
