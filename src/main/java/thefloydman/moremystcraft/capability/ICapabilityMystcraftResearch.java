package thefloydman.moremystcraft.capability;

import java.util.List;
import java.util.UUID;

import thefloydman.moremystcraft.research.Knowledge;

public interface ICapabilityMystcraftResearch {

	public void learnKnowledge(Knowledge knowledge);
	
	public void unlearnKnowledge(Knowledge knowledge);
	
	public List<Knowledge> getPlayerKnowledge();
	
	public void setPlayerKnowledge(List<Knowledge> knowledge);

}