package ncj;

import robocode.util.Utils;

public class Vector2D {

	protected final double _x;
	protected final double _y;

	public Vector2D(double x, double y) {
		_x = x;
		_y = y;
	}

	public double getX() {
		return _x;
	}

	public double getY() {
		return _y;
	}

	public double dot(Vector2D v2) {
		return _x * v2.getX() + _y * v2.getY();
	}	

	public Vector2D calculatePerpendicular() {
		return new Vector2D(_y, -1*_x);
	}

	public Vector2D minus(Vector2D v2) {
		return new Vector2D(_x - v2.getX(), _y - v2.getY());
	}

	public Vector2D plus(Vector2D v2) {
		return new Vector2D(_x + v2.getX(), _y + v2.getY());
	}

	public double magnitude() {
		return Math.sqrt(_x*_x +_y*_y);
	}

	public Vector2D unit() {
		double magnitude = magnitude();
		return new Vector2D(_x/magnitude, _y/magnitude);
	}

	public Vector2D times(double scale) {
		return new Vector2D(_x*scale, _y*scale);
	}

	public double bearing() {
		return robocode.util.Utils.normalRelativeAngle(Math.PI/2.0 - Math.atan2(_y, _x));
	}
	
    @Override public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof Vector2D) {
        	Vector2D that = (Vector2D) other;
            result = (Utils.isNear(this.getX(),that.getX()) && Utils.isNear(this.getY(), that.getY())
                    && this.getClass().equals(that.getClass()));
        }
        return result;
    }

    @Override public int hashCode() {
        return (int) (41 * (41 + getX()) + getY());
    }
    
    public String toString()
    {
    	return "<" + _x + "," + _y + ">";
    }

}
