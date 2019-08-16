package thefloydman.moremystcraft.util;

import com.xcompwiz.mystcraft.api.word.WordData;

public enum MystcraftWords {

	BALANCE(WordData.Balance),
	BELIEVE(WordData.Believe),
	CELESTIAL(WordData.Celestial),
	CHAIN(WordData.Chain),
	CHANGE(WordData.Change),
	CHAOS(WordData.Chaos),
	CIVILIZATION(WordData.Civilization),
	CONSTRAINT(WordData.Constraint),
	CONTRADICT(WordData.Contradict),
	CONTROL(WordData.Control),
	CONVEY(WordData.Convey),
	CREATIVITY(WordData.Creativity),
	CYCLE(WordData.Cycle),
	DEPENDENCE(WordData.Dependence),
	DISCOVER(WordData.Discover),
	DYNAMIC(WordData.Dynamic),
	ELEVATE(WordData.Elevate),
	ENCOURAGE(WordData.Encourage),
	ENERGY(WordData.Energy),
	ENTROPY(WordData.Entropy),
	ETHEREAL(WordData.Ethereal),
	EXIST(WordData.Exist),
	EXPLORE(WordData.Explore),
	FLOW(WordData.Flow),
	FORCE(WordData.Force),
	FORM(WordData.Form),
	FUTURE(WordData.Future),
	GROWTH(WordData.Growth),
	HARMONY(WordData.Harmony),
	HONOR(WordData.Honor),
	IMAGE(WordData.Image),
	INFINITE(WordData.Infinite),
	INHIBIT(WordData.Inhibit),
	INTELLIGENCE(WordData.Intelligence),
	LOVE(WordData.Love),
	MACHINE(WordData.Machine),
	MERGE(WordData.Merge),
	MOMENTUM(WordData.Momentum),
	MOTION(WordData.Motion),
	MUTUAL(WordData.Mutual),
	NATURE(WordData.Nature),
	NURTURE(WordData.Nurture),
	ORDER(WordData.Order),
	POSSIBILITY(WordData.Possibility),
	POWER(WordData.Power),
	QUESTION(WordData.Question),
	REBIRTH(WordData.Rebirth),
	REMEMBER(WordData.Remember),
	RESILIENCE(WordData.Resilience),
	RESURRECT(WordData.Resurrect),
	SACRIFICE(WordData.Sacrifice),
	SOCIETY(WordData.Society),
	SPUR(WordData.Spur),
	STATIC(WordData.Static),
	STIMULATE(WordData.Stimulate),
	SURVIVAL(WordData.Survival),
	SUSTAIN(WordData.Sustain),
	SYSTEM(WordData.System),
	TERRAIN(WordData.Terrain),
	TIME(WordData.Time),
	TRADITION(WordData.Tradition),
	TRANSFORM(WordData.Transform),
	VOID(WordData.Void),
	WEAVE(WordData.Weave),
	WISDOM(WordData.Wisdom);

	public String word;

	MystcraftWords(String str) {
		this.word = str;
	}
	
	@Override
	public String toString() {
		return this.word;
	}

}
