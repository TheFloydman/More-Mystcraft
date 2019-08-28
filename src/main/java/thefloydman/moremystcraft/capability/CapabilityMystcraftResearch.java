package thefloydman.moremystcraft.capability;

import java.util.ArrayList;
import java.util.List;

import thefloydman.moremystcraft.research.Knowledge;

public class CapabilityMystcraftResearch implements ICapabilityMystcraftResearch {

	protected List<Knowledge> playerKnowledge = new ArrayList<Knowledge>();

	@Override
	public void learnKnowledge(Knowledge knowledge) {
		if (!this.playerKnowledge.contains(knowledge)) {
			this.playerKnowledge.add(knowledge);
		}
	}

	@Override
	public void unlearnKnowledge(Knowledge knowledge) {
		if (this.playerKnowledge.contains(knowledge)) {
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

}
