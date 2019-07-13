package thefloydman.moremystcraft.util;

import java.util.Arrays;
import java.util.List;

import com.xcompwiz.mystcraft.api.word.WordData;

public class MystcraftWords {
	private static List<String> mystcraftBaseWords = Arrays.asList(WordData.Balance, WordData.Believe,
			WordData.Celestial, WordData.Chain, WordData.Change, WordData.Chaos, WordData.Civilization,
			WordData.Constraint, WordData.Contradict, WordData.Control, WordData.Convey, WordData.Creativity,
			WordData.Cycle, WordData.Dependence, WordData.Discover, WordData.Dynamic, WordData.Elevate,
			WordData.Encourage, WordData.Energy, WordData.Entropy, WordData.Ethereal, WordData.Exist, WordData.Explore,
			WordData.Flow, WordData.Force, WordData.Form, WordData.Future, WordData.Growth, WordData.Harmony,
			WordData.Honor, WordData.Image, WordData.Infinite, WordData.Inhibit, WordData.Intelligence, WordData.Love,
			WordData.Machine, WordData.Merge, WordData.Momentum, WordData.Motion, WordData.Mutual, WordData.Nature,
			WordData.Nurture, WordData.Order, WordData.Possibility, WordData.Power, WordData.Question, WordData.Rebirth,
			WordData.Remember, WordData.Resilience, WordData.Resurrect, WordData.Sacrifice, WordData.Society,
			WordData.Spur, WordData.Static, WordData.Stimulate, WordData.Survival, WordData.Sustain, WordData.System,
			WordData.Terrain, WordData.Time, WordData.Tradition, WordData.Transform, WordData.Void, WordData.Weave,
			WordData.Wisdom);

	public static List<String> getBaseWords() {
		return mystcraftBaseWords;
	}
}
