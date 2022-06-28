import DataClasses.GraphicsData;

public class Game {

    public void updateAndRender(GraphicsData graphicsData)
    {
        for(int index = 0; index < graphicsData.pixels.length; index++)
        {
            graphicsData.pixels[index] = 0;
        }

        for(int y = 0; y < graphicsData.height; y++)
        {
            for(int x = 0; x < graphicsData.width; x++)
            {
                graphicsData.pixels[x+y*graphicsData.width] = 0xff00ff;
            }
        }

    }
}
