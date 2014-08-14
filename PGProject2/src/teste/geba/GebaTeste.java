package teste.geba;

import end.Algb;
import end.Arquivo;

//oi
public class GebaTeste {
	// objetos
	static int Np;
	static int Nt;
	static double[][] pontos;
	static int[][] triangulos;
	// atributos
	static int[] Ia = new int[3];
	static double Ka;
	static int Od[] = new int[3];
	static double Kd;
	static double Ks;
	static double n;
	static double[] Il = new double[3];
	static double[] Pl = new double[3];
	static Arquivo arq;
	// camera
	static double[] C = new double[3];
	static double[] N = new double[3];
	static double[] V = new double[3];
	static double[] U = new double[3];
	static double[] V1 = new double[3];
	static double[][] NormPontos;
	static double[][] NormTriangulos;
	static double d;
	static double hx; // coloquei double p n�o ter surpresa
	static double hy; // coloquei double p n�o ter surpresa

	// auxiliares

	static void lerObjetos() {
		Arquivo arq = new Arquivo("objeto.txt", "lixoObj.txt");
		Np = arq.readInt();
		Nt = arq.readInt();
		pontos = new double[Np][3];
		triangulos = new int[Nt][3];
		NormPontos = new double[Np][3];
		NormTriangulos = new double[Nt][3];
		for (int i = 0; i < Np; i++) {
			for (int j = 0; j < 3; j++) {
				pontos[i][j] = arq.readDouble();
				}
		}
		for (int i = 0; i < Nt; i++) {
			for (int j = 0; j < 3; j++) {
				triangulos[i][j] = arq.readInt() - 1;
			}
		}
	}

	static void printObjetos() {
		System.out.println("Np: " + Np + " Nt: " + Np);
		for (int i = 0; i < Np; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(pontos[i][j] + " ");
			}
		}
		for (int i = 0; i < Nt; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.println(triangulos[i][j] + " ");
			}
		}
	}

	static void printAtributos() {
		System.out.println("Ia: " + Ia[0] + " " + Ia[1] + " " + Ia[2]);
		System.out.println("Ka: " + Ka);
		System.out.println("Od: " + Od[0] + " " + Od[1] + " " + Od[2]);
		System.out.println("Kd: " + Kd);
		System.out.println("Ks: " + Ks);
		System.out.println("n: " + n);
		System.out.println("Il :" + Il[0] + " " + Il[1] + " " + Il[2]);
		System.out.println("Pl :" + Pl[0] + " " + Pl[1] + " " + Pl[2]);
	}

	static void lerAtributos() {
		arq = new Arquivo("atributo.txt", "lixoAtr.txt");
		Ia[0] = arq.readInt();
		Ia[1] = arq.readInt();
		Ia[2] = arq.readInt();
		Ka = arq.readDouble();
		Od[0] = arq.readInt();
		Od[1] = arq.readInt();
		Od[2] = arq.readInt();
		Kd = arq.readInt();
		Ks = arq.readInt();
		n = arq.readInt();
		Il[0] = arq.readDouble();
		Il[1] = arq.readDouble();
		Il[2] = arq.readDouble();
		Pl[0] = arq.readDouble();
		Pl[1] = arq.readDouble();
		Pl[2] = arq.readDouble();

	}

	static void lerCamera() {
		arq = new Arquivo("camera.txt", "lixoCam.txt");
		C[0] = arq.readDouble();
		C[1] = arq.readDouble();
		C[2] = arq.readDouble();
		N[0] = arq.readDouble();
		N[1] = arq.readDouble();
		N[2] = arq.readDouble();
		V[0] = arq.readDouble();
		V[1] = arq.readDouble();
		V[2] = arq.readDouble();
		d = arq.readDouble();
		hx = arq.readDouble();
		hy = arq.readDouble();
	}

	static void printCamera() {
		System.out.println("C: " + C[0] + " " + C[1] + " " + C[2]);
		System.out.println("N: " + N[0] + " " + N[1] + " " + N[2]);
		System.out.println("V: " + V[0] + " " + V[1] + " " + V[2]);
		System.out.println("d: " + d + "\nhx: " + hx + "\nhy: " + hy);
	}

	static void calcularNormais() {
		for (int i = 0; i <= triangulos.length; i++) {
			double[] v1, v2, n;
			int pA, pB, pC;
			pA = triangulos[i][0];
			pB = triangulos[i][1];
			pC = triangulos[i][2];

			// descobrir quais vetores pegar aqui
			v1 = Algb.sub(pontos[pB], pontos[pA]);// calcula os dois vetores
			v2 = Algb.sub(pontos[pC], pontos[pA]);// definidos pelos pontos do
													// triangulo
			n = Algb.prodVetorial(v1, v2);
			for (int j = 0; j <= 3; j++) {
				NormTriangulos[i] = n;//salva a normal no array d normais de triangulo
				//soma essa normal no array de normal de vertices
				NormPontos[pA] = Algb.soma(NormPontos[pA], n);
				NormPontos[pB] = Algb.soma(NormPontos[pA], n);
				NormPontos[pC] = Algb.soma(NormPontos[pA], n);
			}

		}
		for(int i=0;i<NormPontos.length;i++){
			NormPontos[i] = Algb.normalize(NormPontos[i]);
		}

	}

	public static void main(String[] args) {
		lerCamera();
		printCamera();

		// Parte 3 - Mudan�a de coor - Mundiais -> C�mera
		// � preciso fazer a inversa dessa matriz para que ela possa ser
		// utilizada para a mudan�a de coor como requer a terceira parte.
		V = Algb.sub(V, Algb.projec(V, N));
		System.out.println("V = V-Proj(V,N): " + Algb.VectorToString(V));
		U = Algb.prodVetorial(N, V);
		System.out.println("NxV: " + Algb.VectorToString(U));
		U = Algb.normalize(U);
		V = Algb.normalize(V);
		N = Algb.normalize(N);

		System.out.println("V: " + Algb.VectorToString(V));
		System.out.println("U: " + Algb.VectorToString(U));
		System.out.println("N: " + Algb.VectorToString(N));

		double[] P = new double[3];
		P[0] = 5;
		P[1] = -1;
		P[2] = 2;

		double[][] I = new double[V.length][V.length];
		for (int i = 0; i < V.length; i++) {
			I[0][i] = U[i];
			I[1][i] = V[i];
			I[2][i] = N[i];

		}
		System.out.println();

		for (int i = 0; i < 3; i++) {
			System.out.println(+I[i][0] + " " + I[i][1] + " " + I[i][2]);
		}
		double[] Z = new double[3];

		double[] teste = Algb.sub(P, C);
		System.out.println(Algb.VectorToString(teste));

		Z = Algb.multMatrizVetor(I, teste);

		System.out.println(Z[0] + " " + Z[1] + " " + Z[2]);

		// PARA QUANDO ESTIVERMOS LENDO O ARQUIVO
		/*
		 * lerAtributos(); printAtributos(); lerObjetos(); printObjetos();
		 * 
		 * double [][] matrizRotInv = new double[V.size][V.size]; for (int i =
		 * 0; i < V.size; i++) { matrizRotInv[i][0] = U[i]; matrizRotInv[i][1] =
		 * V[i]; matrizRotInv[i][2] = N[i]; }
		 */

		// double[] A = new double[](1, 2, 3);
		// double[] B = new double[](4, 2, 1);
		//
		// double[] C = Algb.prodEscalar(A, B);
		// C[0] + " " + C[1] + " " + C[2]
		// System.out.println(Algb.prodEscalar(A, B));

	}
}
