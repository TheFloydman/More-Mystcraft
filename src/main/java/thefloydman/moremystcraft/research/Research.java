package thefloydman.moremystcraft.research;

import java.util.ArrayList;
import java.util.List;

public class Research {

	protected static List<Knowledge> knowledgePool = new ArrayList<Knowledge>();

	public static List<Knowledge> getKnowledgePool() {
		return knowledgePool;
	}

	public static boolean addToKnowledgePool(Knowledge knowledge) {
		if (!knowledgePool.contains(knowledge)) {
			knowledgePool.add(knowledge);
			return true;
		}
		return false;
	}

	public static boolean removeFromKnowledgePool(Knowledge knowledge) {
		boolean removed = knowledgePool.contains(knowledge);
		while (knowledgePool.contains(knowledge)) {
			knowledgePool.remove(knowledge);
		}
		return removed;
	}

}
