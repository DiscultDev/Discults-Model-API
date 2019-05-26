package discult.modelapi.common.entities;

import discult.modelapi.client.models.ModelSMDBase;
import discult.modelapi.common.entities.animation.AnimationVariables;
import net.minecraft.entity.EntityCreature;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntitySMDBase extends EntityCreature {
   private ModelSMDBase model;
   private AnimationVariables animationVariables;
   public boolean animationCounting = false;
   public boolean animationSwap = false;
   private int animationDelayCounter = 0;
   public boolean isBlenderExport = false;

   public EntitySMDBase(World worldIn) {
      super(worldIn);
   }

   @SideOnly(Side.CLIENT)
   public ModelSMDBase getModel() {
      return this.model;
   }

   public void setModel(ModelSMDBase modelIn) {
      this.model = modelIn;
   }

   public AnimationVariables getAnimationVariables() {
      if (this.animationVariables == null) {
         this.animationVariables = new AnimationVariables();
      }

      return this.animationVariables;
   }

   public void onUpdate() {
      super.onUpdate();
      int animationDelayLimit = 3;
      if (this.world.isRemote) {
         if (this.animationVariables != null) {
            this.animationVariables.tick();
         }

         if (this.animationCounting) {
            if (this.animationDelayCounter < 3) {
               ++this.animationDelayCounter;
               this.animationSwap = false;
            }

            if (this.animationDelayCounter >= 3) {
               this.animationSwap = true;
               this.animationDelayCounter = 0;
            }
         } else {
            this.animationDelayCounter = 0;
            this.animationSwap = false;
         }

         if (this.getModel() instanceof ModelSMDBase && this instanceof EntitySMDBase) {
            this.getModel().doAnimation(this);
         }
      }

   }
}
