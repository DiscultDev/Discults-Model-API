package discult.modelapi.common.util;

import org.joml.Vector3f;
import static org.lwjgl.opengl.GL11.*;

public class ModleUtils
{
    public static void drawLine(Vector3f start, Vector3f end)
    {
        glBegin(GL_LINE_STRIP);
        glVertex3f(start.x, start.y, start.z);
        glVertex3f(end.x, end.y, end.z);
        glEnd();
    }

    public static void drawCube(Vector3f point, float size) {
        glBegin(GL_LINE_STRIP);
        glVertex3d(point.x + size, point.y + size, point.z + size);
        glVertex3d(point.x - size, point.y + size, point.z + size);
        glVertex3d(point.x - size, point.y + size, point.z - size);
        glVertex3d(point.x + size, point.y + size, point.z - size);
        glVertex3d(point.x + size, point.y + size, point.z + size);

        glVertex3d(point.x + size, point.y - size, point.z + size);
        glVertex3d(point.x - size, point.y - size, point.z + size);
        glVertex3d(point.x - size, point.y - size, point.z - size);
        glVertex3d(point.x + size, point.y - size, point.z - size);
        glVertex3d(point.x + size, point.y - size, point.z + size);
        glEnd();

        glBegin(GL_LINE_STRIP);
        glVertex3d(point.x - size, point.y - size, point.z + size);
        glVertex3d(point.x - size, point.y + size, point.z + size);
        glEnd();

        glBegin(GL_LINE_STRIP);
        glVertex3d(point.x - size, point.y - size, point.z - size);
        glVertex3d(point.x - size, point.y + size, point.z - size);
        glEnd();

        glBegin(GL_LINE_STRIP);
        glVertex3d(point.x + size, point.y - size, point.z - size);
        glVertex3d(point.x + size, point.y + size, point.z - size);
        glEnd();

    }

}
