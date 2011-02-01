package ncj;

public class Solver {

	public static Vector2D findPointInVectorAtDistance(Vector2D pTail, Vector2D v, Vector2D pEnemy, double r) {

		//put the 'enemy' point at (0,0)
		Vector2D pTailShifted = new Vector2D(pTail.getX()-pEnemy.getX(), pTail.getY() - pEnemy.getY());

		//Find intersection between the line defined by pTail+k*v and the circle with radius r around the origin,
		//r^2 = x^2 + y^2
		//y = a + km
		//x = b + kn
		//So,
		//0 = (a^2+b^2-r^2) + 2*(a*m+b*n)*k + (m^2 +n^2)*k^2
		
		double a = pTailShifted.getY();
		double b = pTailShifted.getX();
		double m = v.getY();
		double n = v.getX();
		
		//coefficients
		double c0 = a*a+b*b-r*r;
		double c1 = 2*(a*m+b*n);
		double c2 = (m*m +n*n);
		
		double discriminant = c1*c1 - 4*c0*c2;
		double k1 = (-c1 - Math.sqrt(discriminant))/(2*c2);
		double k2 = (-c1 + Math.sqrt(discriminant))/(2*c2);
			
		double k;
		if((0 < k1) && (k1 <= 1))
			k = k1;
		else if ((0 < k2) && (k2 <= 1))
			k = k2;
		else
			return null;
		
		return new Vector2D(pTail.getX() + k* v.getX(), pTail.getY() + k* v.getY());
	}

	public static Vector2D[] findFrontAndBackPointsInRange(Vector2D pTail, Vector2D v, Vector2D pEnemy, double minR, double maxR) {
		Vector2D p1 = findPointInVectorAtDistance(pTail, v, pEnemy, maxR);
		if(p1 == null)
			p1 = pTail;
		
		Vector2D p2 = findPointInVectorAtDistance(pTail, v, pEnemy, minR);
		if(p2 == null)
			p2 = pTail.plus(v);
		
		return new Vector2D[] {p1,p2};
	}
	
}
