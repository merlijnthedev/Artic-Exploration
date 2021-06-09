package nl.merlinthedev.game.level.tiles;

import nl.merlinthedev.game.gfx.Screen;
import nl.merlinthedev.game.level.Level;

public class basicTile extends Tile {

    protected int tileId;
    protected int tileColor;

    public basicTile(int id, int x, int y, int tileColor, int levelColor) {
        super(id, false, false, levelColor);
        this.tileId = x + y * 32;
        this.tileColor = tileColor;
    }


    public void tick() {

    }

    public void render(Screen screen, Level level, int x, int y) {
        screen.render(x, y, tileId, tileColor, 0x00, 1);

    }
}
