package discult.modelapi.client.loaders.oldSMD;

import discult.modelapi.client.models.ModelSMDBase;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

public class Socket
{
    private String name;
    private int boneID;
    private Vector4f location;
    private ModelSMDBase owner;
    private float boxSpace = 0.2f;

    public Socket(String name, int boneID, ModelSMDBase owner)
    {
        this.name = name;
        this.boneID = boneID;
        this.owner = owner;
    }


    public void drawSocket()
    {

        if(location != null)
        {
           // GlStateManager.scale(1,-1,-1);
            GlStateManager.rotate(90,1,0,0);
            GlStateManager.disableDepth();

            GL11. glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDisable(GL11.GL_LIGHTING);


            GlStateManager.color(0,0,1,0.5f);
            GL11.glPointSize(5F);
            GL11.glBegin(GL11.GL_POINTS);
            GL11.glVertex3d(getLocation().getX() /16,getLocation().getY() /16,getLocation().getZ() /16);
            GL11.glEnd();
            GlStateManager.color(1,1,1,1);
            //TOP
            GlStateManager.color(0,1,0,0.5f);
            drawLine(
                    getLocation().getX() /16 - boxSpace,getLocation().getY() /16 + boxSpace,getLocation().getZ() /16 + boxSpace,//1
                    getLocation().getX() /16 + boxSpace,getLocation().getY() /16 + boxSpace,getLocation().getZ() /16+ boxSpace);//2
            drawLine(
                    getLocation().getX() /16 - boxSpace,getLocation().getY() /16 + boxSpace,getLocation().getZ() /16 - boxSpace,//3
                    getLocation().getX() /16 + boxSpace,getLocation().getY() /16 + boxSpace,getLocation().getZ() /16 - boxSpace);//4
            drawLine(
                    getLocation().getX() /16 - boxSpace,getLocation().getY() /16 + boxSpace,getLocation().getZ() /16 + boxSpace,//1
                    getLocation().getX() /16 - boxSpace,getLocation().getY() /16 + boxSpace,getLocation().getZ() /16 - boxSpace);//3
            drawLine(
                    getLocation().getX() /16 + boxSpace,getLocation().getY() /16 + boxSpace,getLocation().getZ() /16+ boxSpace,//2
                    getLocation().getX() /16 + boxSpace,getLocation().getY() /16 + boxSpace,getLocation().getZ() /16 - boxSpace);//4
            GlStateManager.color(1,1,1,1);
            //TopEND
            //Bottom
            GlStateManager.color(1,0,0,0.5f);
            drawLine(
                    getLocation().getX() /16 -boxSpace,getLocation().getY() /16 - boxSpace,getLocation().getZ() /16 + boxSpace,//5
                    getLocation().getX() /16 + boxSpace,getLocation().getY() /16 - boxSpace,getLocation().getZ() /16+ boxSpace);//6
            drawLine(
                    getLocation().getX() /16 -boxSpace,getLocation().getY() /16 - boxSpace,getLocation().getZ() /16 - boxSpace,//7
                    getLocation().getX() /16 + boxSpace,getLocation().getY() /16 - boxSpace,getLocation().getZ() /16 - boxSpace);//8
            drawLine(
                    getLocation().getX() /16 -boxSpace,getLocation().getY() /16 - boxSpace,getLocation().getZ() /16 + boxSpace,//5
                    getLocation().getX() /16 -boxSpace,getLocation().getY() /16 - boxSpace,getLocation().getZ() /16 - boxSpace);//7
            drawLine(
                    getLocation().getX() /16 + boxSpace,getLocation().getY() /16 - boxSpace,getLocation().getZ() /16+ boxSpace,//6
                    getLocation().getX() /16 + boxSpace,getLocation().getY() /16 - boxSpace,getLocation().getZ() /16 - boxSpace);//8
            //BottomEnd
            GlStateManager.color(1,1,1,1);

            //Connecting Lines
            GlStateManager.color(0.2f,0.7f,0.1f,0.5f);
            drawLine(
                    getLocation().getX() /16 -boxSpace,getLocation().getY() /16 + boxSpace,getLocation().getZ() /16 + boxSpace,//1
                    getLocation().getX() /16 -boxSpace,getLocation().getY() /16 - boxSpace,getLocation().getZ() /16 + boxSpace);//5
            drawLine(
                    getLocation().getX() /16 -boxSpace,getLocation().getY() /16 + boxSpace,getLocation().getZ() /16 - boxSpace,//3
                    getLocation().getX() /16 -boxSpace,getLocation().getY() /16 - boxSpace,getLocation().getZ() /16 - boxSpace);//7
            drawLine(
                    getLocation().getX() /16 + boxSpace,getLocation().getY() /16 + boxSpace,getLocation().getZ() /16 - boxSpace,//4
                    getLocation().getX() /16 + boxSpace,getLocation().getY() /16 - boxSpace,getLocation().getZ() /16 - boxSpace);//8
            drawLine(
                    getLocation().getX() /16 + boxSpace,getLocation().getY() /16 + boxSpace,getLocation().getZ() /16 + boxSpace,//2
                        getLocation().getX() /16 + boxSpace,getLocation().getY() /16 - boxSpace,getLocation().getZ() /16 + boxSpace);//6
            GlStateManager.color(1,1,1,1);
            //Connecting Lines END



            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_ALPHA_TEST);

            GL11.glEnable(GL11.GL_LIGHTING);

            GlStateManager.enableDepth();
            GlStateManager.disableCull();
        }
    }

    private void drawLine(float x, float y, float z, float cx, float cy, float cz)
    {
        GL11.glLineWidth(5F);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex3d(x,y,z);
        GL11.glVertex3d(cx,cy,cz);
        GL11.glEnd();
    }

    public String getName()
    {
        return name;
    }

    public int getBoneID()
    {
        return boneID;
    }

    public Vector4f getLocation()
    {
        return location;
    }

    public ModelSMDBase getOwner()
    {
        return owner;
    }

    public void setLocation(Vector4f location)
    {
        this.location = location;
    }
}
