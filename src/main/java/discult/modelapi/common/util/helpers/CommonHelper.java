package discult.modelapi.common.util.helpers;

import java.util.ArrayList;

public class CommonHelper {
   public static void ensureIndex(ArrayList list, int i) {
      while(list.size() <= i) {
         list.add((Object)null);
      }

   }
}
