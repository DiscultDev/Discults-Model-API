package discult.modelapi.common.entities.animation;

import java.util.HashMap;

public class AnimationVariables {
   private HashMap<Integer, Integer> ints = new HashMap();
   private HashMap<Integer, IncrementingVariable> incs = new HashMap();

   public int getInt(int id) {
      return (Integer)this.ints.get(id);
   }

   public void setInt(int id, int value) {
      this.ints.put(id, value);
   }

   public void setCounter(int id, float limit, float increment) {
      this.incs.put(id, new IncrementingVariable(increment, limit));
   }

   public IncrementingVariable getCounter(int id) {
      return (IncrementingVariable)this.incs.get(id);
   }

   public void tick() {
      this.incs.values().forEach(IncrementingVariable::tick);
   }

   public boolean hasInt(int id) {
      return this.ints.get(id) != null;
   }
}
