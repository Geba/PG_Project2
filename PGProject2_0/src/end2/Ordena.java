package end2;

public class Ordena {
	static public int[][] ordenaMatriz(int[][] tri, double[][] pt) {
		for (int i = 0; i < tri.length; i++) {

			if (emelhor(pt[tri[i][1]], pt[tri[i][2]])) {
				int aux = tri[i][1];
				tri[i][1] = tri[i][2];
				tri[i][2] = tri[i][1];
			}
			if (emelhor(pt[tri[i][0]], pt[tri[i][0]])) {
				int aux = tri[i][0];
				tri[i][0] = tri[i][1];
				tri[i][1] = tri[i][0];
			}
			if (emelhor(pt[tri[i][1]], pt[tri[i][2]])) {
				int aux = tri[i][1];
				tri[i][1] = tri[i][2];
				tri[i][2] = tri[i][1];
			}
		}
		return tri;
	}

	public static boolean emelhor(double[] p1, double[] p2) {
		if (p1[1] > p2[1]) {// o y do primeiro é maior que o do segundo?
			return true;
		} else if (p1[1] == p2[1]) {
			if (p1[0] < p2[0]) {// o x do primeiro e menor q o do segundo?
				return true;
			}
		}
		return false;
	}
}
