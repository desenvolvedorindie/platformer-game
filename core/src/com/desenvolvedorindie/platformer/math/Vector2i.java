package com.desenvolvedorindie.platformer.math;

public class Vector2i {

	public static final Vector2i NULL_VECTOR = new Vector2i(0, 0);

	private final int x;
	private final int y;

	public Vector2i() {
		x = 0;
		y = 0;
	}

	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public double distance(int x, int y)
	{
		double d0 = (double)(this.getX() - x);
		double d1 = (double)(this.getY() - y);
		return Math.sqrt(d0 * d0 + d1 * d1);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Vector2i vector2i = (Vector2i) o;

		if (x != vector2i.x) return false;
		return y == vector2i.y;

	}

	@Override
	public int hashCode() {
		int result = x;
		result = 31 * result + y;
		return result;
	}
}
