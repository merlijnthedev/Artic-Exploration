package nl.merlinthedev.game.level.tiles;

import nl.merlinthedev.game.gfx.Colors;
import nl.merlinthedev.game.gfx.Screen;
import nl.merlinthedev.game.level.Level;


public abstract class Tile {
    public static final Tile[] tiles = new Tile[256];
    public static final Tile VOID = new basicSolidTile(0, 0, 0, Colors.get(000, -1, -1, -1), 0xff000000); // alpha channel is the first two digits
    public static final Tile WATER = new basicTile(1, 1, 0, Colors.get(-1, 5, -1, -1), 0xff0000ff); // no red, no green, full blue
    public static final Tile GRASS = new basicTile(2, 2, 0, Colors.get(-1, 555, 141, -1), 0xff00ff00); // no red, full green, no blue
    public static final Tile LOG = new basicSolidTile(3, 3, 0, Colors.get(-1, 432, 420, -1), 0xffff0000); // red
    public static final Tile LEAVE = new basicSolidTile(4, 4, 0, Colors.get(-1, 20, 10, 0), 0xff00ffff); // no red, full green, full blue
    public static final Tile STONE = new basicSolidTile(5, 5, 0, Colors.get(-1, 333, 333, 333), 0xff555555); // grey

    protected byte id;
    protected boolean solid;
    protected boolean emitter;

    private int levelColor;

    public Tile(int id, boolean isSolid, boolean isEmitter, int levelColor) {
        this.id = (byte) id;
        if (tiles[id] != null) throw new RuntimeException("Duplicate tile id on " + id);
        this.solid = isSolid;
        this.emitter = isEmitter;
        this.levelColor = levelColor;
        tiles[id] = this;

    }

    public byte getId() {
        return id;
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean isEmitter() {
        return emitter;
    }

    public int getLevelColor() {
        return levelColor;
    }

    public abstract void tick();

    public abstract void render(Screen screen, Level level, int x, int y);

}
