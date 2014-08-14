package end;

public class Algb {

	// OPERACOES COM VETORES

	// SOMA
	public static double[] soma(double[] a, double[] b) {// soma dois vetores
		double[] soma = new double[a.length];
		for (int i = 0; i < a.length; i++) {
			soma[i] = a[i] + b[i];
		}
		return soma;
	}

	public static double[] sub(double[] a, double[] b) {// subtrai 2 vetores
		double[] sub = new double[a.length];
		for (int i = 0; i < a.length; i++) {
			sub[i] = a[i] - b[i];
		}
		return sub;
	}

	static double prodEscalar(double[] a, double[] b) {// produto escalar a.b
		double resp = 0;
		for (int i = 0; i < a.length; i++) {
			resp += a[i] * b[i];
		}
		return resp;
	}

	public static double[] prodVetorial(double[] a, double[] b) {
		double[] resp = new double[3];
		resp[0] = a[1] * b[2] - a[2] * b[1];
		resp[1] = a[2] * b[0] - a[0] * b[2];
		resp[2] = a[0] * b[1] - a[1] * b[0];
		return resp;
	}

	public static double[] projec(double[] u, double[] v) {
		double[] proj = new double[u.length];
		double a = 0, b = 0, k;
		double size = u.length;
		// a = <v*u> | b = <u*u>
		for (int i = 0; i < size; i++) {
			a = a + u[i] * v[i];
			b = b + v[i] * v[i];
			proj[i] = v[i];
		}

		k = a / b;

		// proj = v | k*proj = k*v
		for (int i = 0; i < size; i++) {
			proj[i] = k * proj[i];
		}

		return proj;
	}

	public static double[] multMatrizVetor(double[][] M, double[] V) {
		int i, j;
		double[] result = new double[V.length];
		double aux = 0;
		for (i = 0; i < V.length; i++) {
			for (j = 0; j < V.length; j++) {
				aux = aux + M[i][j] * V[j];
			}
			result[i] = aux;
			aux = 0;
		}
		return result;
	}

	public static double getNorma(double[] v) {
		double k = 0;
		for (int i = 0; i < v.length; i++) {
			k = k + v[i] * v[i];
		}
		k = Math.sqrt(k);
		return k;
	}

	public static double[] normalize(double[] v) {
		double n = 0;
		n = getNorma(v);
		for (int i = 0; i < v.length; i++) {
			v[i] = v[i] / n;
		}
		return v;
	}
	
	public static String VectorToString(double[] v){
		String retorno  = "";
		for(int i =0;i<v.length; i++){
			retorno  +=" " +v[i];
		}
		return retorno;
	}
	//recebe a matriz de mudanca de base(MMB) e a matriz de pontos(MP)
	//faz: [v]a = [M]a ^b * [v]b  Multiplica a MMB pela (MP) 
	public static double[][] mudancaDeCoordenada(double[][] pontos, double[][] MM){
		double[] V = new double[3];
		double[] R = new double[3];
		double[][] MatMud = new double[pontos.length][3];
				
		for (int i = 0; i < pontos.length; i++) {
			for (int j = 0; j < 3; j++) {
				V[j] = pontos[i][j];
			} 
		 R = multMatrizVetor(MM, V);
		 MatMud[i][0] = R[0];
		 MatMud[i][1] = R[1];
		 MatMud[i][2] = R[2];
		}
		
		return MatMud;
	}
	
	
	
	/*
	 * public static Vetor sub(Vetor a, Vetor b) {// return a - b; Vetor resp =
	 * new Vetor(a.getSize()); for (int i = 0; i < a.getSize(); i++) { resp[i] =
	 * a[i] - b[i]; } return resp; }
	 * 
	 * public static Vetor soma(Vetor a, Vetor b) {// return a - b; Vetor resp =
	 * new Vetor(a.getSize()); for (int i = 0; i < a.getSize(); i++) { resp[i] =
	 * a[i] + b[i]; } return resp; } static double prodEscalar(Vetor a, Vetor b)
	 * {// produto escalar a.b double resp = 0; for (int i = 0; i < a.getSize();
	 * i++) { resp += a[i] * b[i]; } return resp; } public static Vetor
	 * prodVetorial(Vetor a, Vetor b) { Vetor resp = new Vetor(3); resp[0] =
	 * a[1] * b[2] - a[2] * b[1]; resp[1] = a[2] * b[0] - a[0] * b[2]; resp[2] =
	 * a[0] * b[1] - a[1] * b[0]; return resp; } static Vetor projec(Vetor u,
	 * Vetor v) { Vetor proj = new Vetor(u.size); double a = 0, b = 0, k; double
	 * size = u.getSize(); // a = <v*u> | b = <u*u> for (int i = 0; i < size;
	 * i++) { a = a + u[i] * v[i]; b = b + v[i] * v[i]; proj[i] = v[i]; }
	 * 
	 * k = a / b;
	 * 
	 * // proj = v | k*proj = k*v for (int i = 0; i < size; i++) { proj[i] = k *
	 * proj[i]; }
	 * 
	 * return proj; }
	 */

}
