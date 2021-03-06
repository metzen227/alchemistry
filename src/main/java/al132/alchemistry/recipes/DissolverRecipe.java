package al132.alchemistry.recipes;

import al132.alchemistry.Alchemistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;


public class DissolverRecipe {

    public Ingredient input;
    public boolean reversible = false;
    public ProbabilitySet outputs;
    //private NonNullList<ItemStack> inputs = null;

    public DissolverRecipe(Builder builder) {
        this(builder.input, builder.outputs);
        this.reversible = builder.reversible;
    }

    public DissolverRecipe(Ingredient input, ProbabilitySet outputs) {
        this.input = input;
        this.outputs = outputs;
    }

    public DissolverRecipe copy() {
        return new DissolverRecipe(this.input, this.outputs);
    }
/*
    private void initInputs() {
        inputs = NonNullList.create();
        if (input != null) inputs.addAll(Lists.newArrayList(input.getMatchingStacks().clone()));
    }

    public NonNullList<ItemStack> getInputs() {
        if(this.inputs == null) initInputs();
        return this.inputs;
    }*/

    @Nullable
    public static DissolverRecipe match(ItemStack input, boolean quantitySensitive) {
        for (DissolverRecipe recipe : ModRecipes.dissolverRecipes) {
            for (ItemStack recipeStack : recipe.input.getMatchingStacks().clone()) {
                if (ItemStack.areItemsEqual(recipeStack, input)) {
                    // && (input.itemDamage == recipeStack.itemDamage
                    //|| recipeStack.itemDamage == OreDictionary.WILDCARD_VALUE)) {
                    if (quantitySensitive && input.getCount() >= recipeStack.getCount()) return recipe.copy();
                    else if (!quantitySensitive) return recipe.copy();
                }
            }
        }
        return null;
    }

    public static class Builder {
        private Ingredient input;
        private ProbabilitySet outputs;
        private boolean reversible = false;


        public Builder() {
        }

        public Builder outputs(ProbabilitySet set) {
            this.outputs = set;
            return this;
        }

        public Builder input(ItemStack input) {
            this.input = Ingredient.fromStacks(input);
            return this;
        }

        public Builder input(Item input) {
            this.input = Ingredient.fromItems(input);
            return this;
        }

        public Builder setReversible(boolean value) {
            this.reversible = value;
            return this;
        }

        public Builder input(String tagLocation) {
            this.input = Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation(tagLocation)));
            return this;
        }
/*
        public <T> Builder input(Tag<T> tag) {
            Object[] temp = tag.getAllElements().toArray();
            if (temp.length > 0) {
                if (temp[0] instanceof Item) {
                    this.input = Ingredient.fromTag((Tag<Item>) tag);
                } else {
                    this.input = Ingredient.fromStacks(((Tag<Block>) tag).getAllElements().stream()
                            .map(ItemStack::new)
                            .toArray(ItemStack[]::new));
                }
                return this;

            } else throw new RuntimeException("Invalid tag[" + tag.getId() + "] for dissolver recipe input");
        }
 */

        public Builder input(Ingredient input) {
            this.input = input;
            return this;
        }

        public void build() {
            String ins = input == null ? "null" : input.toString();
            String outs = outputs == null ? "null" : outputs.toString();
            if (this.input != null && this.outputs != null) {
                ModRecipes.dissolverRecipes.add(new DissolverRecipe(this));
            } else Alchemistry.LOGGER.warn("Invalid dissolver recipe - input[" + ins + "], outputs[" + outs + "]");
        }
    }
}