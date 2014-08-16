package end;

import org.la4j.LinearAlgebra;
import org.la4j.factory.Factory;
import org.la4j.linear.GaussianSolver;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.Vector;
import org.la4j.vector.dense.BasicVector;

public class Phong {

	public static double[] getCoeficients(double[] P, double P1[], double P2[],
			double P3[]) {
		double[][] data = new double[3][3];
		for (int i = 0; i < 2; i++) {
			data[i][0] = P1[0];
			data[i][1] = P2[0];
			data[i][2] = P3[0];
			data[2][i] = 1;
		}
		Basic2DMatrix m = new Basic2DMatrix(data);
		BasicVector v = new BasicVector(P);
		double[] resp = new double[3];
		GaussianSolver solvers = new GaussianSolver(m);
		Vector n = solvers.solve(v,
				(Factory) LinearAlgebra.JACOBI);
		for (int i = 0; i < 3; i++) {
			resp[i] = n.get(i);
		}
		return resp;
	}
}
