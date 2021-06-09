package nl.merlinthedev.game.level.tiles;

public class basicSolidTile extends basicTile {
    public basicSolidTile(int id, int x, int y, int tileColor, int levelColor) {
        super(id, x, y, tileColor, levelColor);
        this.solid = true;

    }
}
