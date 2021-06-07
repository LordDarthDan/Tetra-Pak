package mod.noobulus.tetrapak.druidcraft;

import mod.noobulus.tetrapak.util.ItemHelper;
import mod.noobulus.tetrapak.util.tetra_definitions.IPercentageHoloDescription;
import mod.noobulus.tetrapak.util.tetra_definitions.ITetraEffect;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.IWorld;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.statbar.getter.IStatGetter;
import se.mickelus.tetra.gui.statbar.getter.ITooltipGetter;
import se.mickelus.tetra.items.modular.ModularItem;

import javax.annotation.Nullable;

public class MoonstrikeEffect implements IPercentageHoloDescription {

	private static float getMoonFactor(IWorld world, float efficiency) {
		return 1 + (efficiency * world.getMoonSize() / 100.f);
	}

	@SubscribeEvent
	public void moonstrikeToolsBreakBlocksFaster(PlayerEvent.BreakSpeed event) {
		ItemStack heldItemMainhand = event.getPlayer().getHeldItemMainhand();
		if (!(heldItemMainhand.getItem() instanceof ModularItem))
			return;
		ModularItem item = (ModularItem) heldItemMainhand.getItem();
		if (hasEffect(heldItemMainhand)) {
			IWorld moonPhaseWorld = event.getPlayer().getEntityWorld();
			float efficiency = (float) item.getEffectEfficiency(heldItemMainhand, getEffect());
			event.setNewSpeed(event.getOriginalSpeed() * getMoonFactor(moonPhaseWorld, efficiency));
		}
	}

	@SubscribeEvent
	public void moonstrikeCausesBonusDamage(LivingHurtEvent event) {
		if (shouldMoonstrikeAffect(event.getSource())) {
			Entity source = event.getSource().getImmediateSource();
			if (source == null)
				return;
			IWorld moonPhaseWorld = source.getEntityWorld();
			float efficiency = getEffectEfficiency(event.getSource());
			event.setAmount(event.getAmount() * getMoonFactor(moonPhaseWorld, efficiency));
		}
	}

	private boolean shouldMoonstrikeAffect(@Nullable DamageSource source) {
		if (source == null)
			return false;
		if (source.getTrueSource() instanceof LivingEntity) {
			LivingEntity user = (LivingEntity) source.getTrueSource();

			return hasEffect(user.getHeldItemMainhand()) ||
				hasEffect(ItemHelper.getThrownItemStack(source.getImmediateSource()));
		}
		return false;
	}

	@Override
	public ITooltipGetter getStatTooltipGetter(IStatGetter statGetter) {
		return (player, itemStack) -> I18n.format(getTooltipPath(),
			statGetter.getValue(player, itemStack), statGetter.getValue(player, itemStack));
	}

	@Override
	public ItemEffect getEffect() {
		return ITetraEffect.get("moonstrike");
	}
}
