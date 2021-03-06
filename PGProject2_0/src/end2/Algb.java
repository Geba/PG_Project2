package end2;

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

	static double distancia(double[] a, double[] b) {
		double d = 0;
		for (int i = 0; i < a.length; i++) {
			d += (a[i] - b[i]) * (a[i] - b[i]);
		}
		return Math.sqrt(d);

	}

	public static double prodEscalar(double[] a, double[] b) {// produto escalar
																// a.b
		double resp = 0;
		for (int i = 0; i < a.length; i++) {
			resp += a[i] * b[i];
		}
		return resp;
	}

	public static double[] prodByEscalar(double escalar, double[] vetor) {
		double[] k = new double[vetor.length];
		for (int i = 0; i < vetor.length; i++) {
			k[i] = escalar * vetor[i];
		}
		return k;
	}

	public static double[] prodVetorial(double[] a, double[] b) {
		double[] resp = new double[a.length];
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
		int aux;
		double[] retorno = new double[V.length];
		for (int linha = 0; linha < M.length; linha++) {
			for (int coluna = 0; coluna < M[0].length; coluna++) {
				retorno[linha] += M[linha][coluna] * V[coluna];
			}

		}
		return retorno;
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
		double[] v2 = v;
		for (int i = 0; i < v.length; i++) {
			v2[i] = v2[i] / n;
		}
		return v2;
	}

	public static String VectorToString(double[] v) {
		String retorno = "";
		for (int i = 0; i < v.length; i++) {
			retorno += " " + v[i];
		}
		return retorno;
	}

	public static String MatrixToString(double[][] v) {
		String retorno = "";
		for (int linha = 0; linha < v.length; linha++) {
			retorno = "";
			for (int coluna = 0; coluna < v[0].length; coluna++) {
				retorno += retorno + v[linha][coluna] + " ";
			}
			// System.out.println(retorno);
		}

		return retorno;
	}

	// recebe a matriz de mudanca de base(MMB) e a matriz de pontos(MP)
	// faz: [v]a = [M]a ^b * [v]b Multiplica a MMB pela (MP)
	public static double[][] mudancaDeCoordenada(double[][] pontos,
			double[][] MM, double[] C) {
		double[] V = new double[3];
		double[] R = new double[3];
		double[][] MatMud = new double[pontos.length][3];

		for (int i = 0; i < pontos.length; i++) {
			for (int j = 0; j < 3; j++) {
				V[j] = pontos[i][j];
			}
			R = multMatrizVetor(MM, sub(V, C));
			MatMud[i] = R;
		}

		return MatMud;
	}
}
