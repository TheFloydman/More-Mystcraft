package thefloydman.moremystcraft.capability;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import thefloydman.moremystcraft.network.MoreMystcraftPacketHandler;
import thefloydman.moremystcraft.research.Knowledge;
import thefloydman.moremystcraft.util.Reference;

public class CapabilityMystcraftResearch implements ICapabilityMystcraftResearch {

	protected List<Knowledge> playerKnowledge = new ArrayList<Knowledge>();

	@Override
	public void learnKnowledge(Knowledge knowledge, @Nullable EntityPlayerMP player) {
		if (!this.hasKnowledge(knowledge)) {
			this.playerKnowledge.add(knowledge);
			if (player != null) {
				if (knowledge.asBiome() != null) {
					MoreMystcraftPacketHandler.sendTranslatedMessage(player, Reference.Message.LEARN_BIOME.key,
							Reference.MessageType.STATUS.ordinal(), "none");
				}
			}
		} else {
			if (player != null) {
				if (knowledge.asBiome() != null) {
					MoreMystcraftPacketHandler.sendTranslatedMessage(player, Reference.Message.KNOWN_BIOME.key,
							Reference.MessageType.STATUS.ordinal(), "none");
				}
			}
		}
	}

	@Override
	public void forgetKnowledge(Knowledge knowledge) {
		if (this.hasKnowledge(knowledge)) {
			this.playerKnowledge.remove(knowledge);
		}
	}

	@Override
	public List<Knowledge> getPlayerKnowledge() {
		return this.playerKnowledge;
	}

	@Override
	public void setPlayerKnowledge(List<Knowledge> knowledge) {
		this.playerKnowledge = knowledge;
	}

	@Override
	public boolean hasKnowledge(Knowledge knowledgeIn) {
		boolean knows = false;
		for (Knowledge knowledgePiece : this.getPlayerKnowledge()) {
			if (knowledgePiece.toString().equals(knowledgeIn.toString())) {
				knows = true;
				break;
			}
		}
		return knows;
	}

}
