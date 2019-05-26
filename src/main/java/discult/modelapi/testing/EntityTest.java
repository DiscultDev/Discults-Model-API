package discult.modelapi.testing;

import discult.modelapi.common.entities.EntitySMDBase;
import net.minecraft.world.World;

public class EntityTest extends EntitySMDBase {
   public EntityTest(World worldIn) {
      super(worldIn);
      this.setModel(new ModelTest());
   }
}
