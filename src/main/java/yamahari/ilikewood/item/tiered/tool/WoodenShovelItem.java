package yamahari.ilikewood.item.tiered.tool;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraftforge.common.ToolType;
import yamahari.ilikewood.item.tiered.IWoodenTieredItem;
import yamahari.ilikewood.util.IWooden;
import yamahari.ilikewood.util.WoodType;
import yamahari.ilikewood.util.WoodenItemTier;
import yamahari.ilikewood.util.WoodenTieredObjectType;

import javax.annotation.Nullable;

public final class WoodenShovelItem extends ShovelItem implements IWooden, IWoodenTieredItem {
    private final WoodType woodType;
    private final WoodenItemTier woodenItemTier;

    public WoodenShovelItem(final WoodType woodType, final WoodenItemTier woodenItemTier) {
        super(ItemTier.WOOD, 0.f, 0.f, (new Item.Properties().group(ItemGroup.TOOLS)));
        this.woodType = woodType;
        this.woodenItemTier = woodenItemTier;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public IItemTier getTier() {
        return this.getWoodenItemTier();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public int getHarvestLevel(final ItemStack stack, final ToolType tool, @Nullable final PlayerEntity player, @Nullable final BlockState blockState) {
        return this.getWoodenItemTier().getHarvestLevel();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public float getDestroySpeed(final ItemStack stack, final BlockState state) {
        return this.getToolTypes(stack).stream().anyMatch(state::isToolEffective) ? this.getWoodenItemTier().getEfficiency() : 1.f;
    }

    @Override
    public int getItemEnchantability() {
        return this.getWoodenItemTier().getEnchantability();
    }

    @Override
    public boolean isDamageable() {
        return this.getMaxDamage(null) > 0;
    }

    @Override
    public int getMaxDamage(final ItemStack stack) {
        return this.getWoodenItemTier().getMaxUses();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean getIsRepairable(final ItemStack toRepair, final ItemStack repair) {
        return this.getWoodenItemTier().getRepairMaterial().test(repair);
    }

    @Override
    public int getBurnTime(ItemStack itemStack) {
        return this.getWoodenItemTier().getProperties(this.getWoodenTieredObjectType()).getBurnTime();
    }

    public float getAttackDamage() {
        return this.getWoodenItemTier().getAttackDamage() + this.getWoodenItemTier().getProperties(this.getWoodenTieredObjectType()).getAttackDamage();
    }

    public float getAttackSpeed() {
        return this.getWoodenItemTier().getProperties(this.getWoodenTieredObjectType()).getAttackSpeed();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(final EquipmentSlotType equipmentSlot) {
        final Multimap<Attribute, AttributeModifier> attributeModifiers = HashMultimap.create();
        if (equipmentSlot == EquipmentSlotType.MAINHAND) {
            attributeModifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", this.getAttackDamage(), AttributeModifier.Operation.ADDITION));
            attributeModifiers.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", this.getAttackSpeed(), AttributeModifier.Operation.ADDITION));
        }
        return attributeModifiers;
    }

    @Override
    public WoodenItemTier getWoodenItemTier() {
        return this.woodenItemTier;
    }

    @Override
    public WoodenTieredObjectType getWoodenTieredObjectType() {
        return WoodenTieredObjectType.SHOVEL;
    }

    @Override
    public WoodType getWoodType() {
        return this.woodType;
    }
}
