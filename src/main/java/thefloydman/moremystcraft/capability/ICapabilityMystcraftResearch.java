package thefloydman.moremystcraft.capability;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import thefloydman.moremystcraft.research.Knowledge;

public interface ICapabilityMystcraftResearch {

	public void learnKnowledge(Knowledge knowledge, @Nullable EntityPlayerMP player);

	public void forgetKnowledge(Knowledge knowledge);

	public List<Knowledge> getPlayerKnowledge();

	public void setPlayerKnowledge(List<Knowledge> knowledge);

	public boolean hasKnowledge(Knowledge knowledge);

}