package sereneseasons.effect;

import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ColdEffect extends MobEffect {
    public ColdEffect(MobEffectCategory $$0, int $$1) {
        super($$0, $$1);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.withDefaultNamespace("effect.slowness"), -0.15000000596046448, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
    public boolean applyEffectTick(LivingEntity player, int $$1) {
        if (player.getHealth() > 1.0F) {
            player.hurt(player.damageSources().magic(), 1.0F);;
        }
        return true;
    }

    public boolean shouldApplyEffectTickThisTick(int $$0, int $$1) {
        int $$2 = 25 >> $$1;
        if ($$2 > 0) {
            return $$0 % $$2 == 0;
        } else {
            return true;
        }
    }
}
