package sample;
import java.util.ArrayList;

public class Frame {

	private static final int MAX_TILES = 7;
	private ArrayList<Tile> tiles;

	Frame() {
		tiles = new ArrayList<Tile>();
	}

	public int size() {
		return(tiles.size());
	}

	public boolean isEmpty() {
		return tiles.isEmpty();
	}

	public boolean isFull() {
		return tiles.size() == MAX_TILES;
	}

	public boolean isAvailable(String letters) {
		boolean found = true;
		if (letters.length() > tiles.size()) {
			found = false;
		}
		else {
			ArrayList<Tile> copyTiles = new ArrayList<Tile>(tiles);
			for (int i=0; i<letters.length() && found; i++) {
				Tile tileSought = new Tile(letters.charAt(i));
				if (copyTiles.contains(tileSought)) {
					copyTiles.remove(tileSought);
				}
				else {
					found = false;
				}
			}
		}
		return found;
	}

	public ArrayList<Tile> getTiles() {
		return tiles;
	}

	// remove precondition: isAvailable(letters) is true
	public void remove(String letters) {
		for (int i=0; i<letters.length(); i++) {
			tiles.remove(new Tile(letters.charAt(i)));
		}
	}

	// getTile precondition: isAvailable(letters) is true
	public Tile getTile(Character letter) {
		int index = tiles.indexOf(new Tile(letter));
		return tiles.get(index);
	}

	// removeTile precondition: isAvailable(letters) is true
	public void remove(Tile tile) {
		tiles.remove(tile);
	}

	public void refill(Pool pool) {
		int numTilesToDraw = MAX_TILES - tiles.size();
		ArrayList<Tile> draw = pool.drawTiles(numTilesToDraw);
		tiles.addAll(draw);
	}

	// test setter
	public void setTiles(String letters) {
		for (int i=0; i<letters.length(); i++) {
			tiles.add(new Tile(letters.charAt(i)));
		}
	}

	@Override
	public String toString() {
		return tiles.toString();
	}

}
