package end;

import javax.media.opengl.GL;

//daniel
public class NewMain {
	// objetos
	static int Np;
	static int Nt;
	static double[][] pontos;
	static int[][] triangulos;
	static double[][] NormPontos;
	static double[][] NormTriangulos;
	// atributos
	static int[] Ia = new int[3];
	static double Ka;
	static int[] Od = new int[3];
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
	static double d;
	static double hx; // coloquei double p n�o ter surpresa
	static double hy; // coloquei double p n�o ter surpresa

	// auxiliares
	static double[][] matrizMudBase = new double[V.length][V.length];
	static double[][] pontosTrans = new double[Np][3];
	static double[][] pontos2d = new double[Np][2];
	static double[] zero = {0, 0, 0};
	static double[] Pphong = new double[3];
	static double[] Nphong = new double[3];
	static double[] Lphong = new double[3];
	static double[] menosPphong = new double[3];
	static double[] Vphong = new double[3];	

	static void lerObjetos() {
		Arquivo arq = new Arquivo("objeto.txt", "lixoObj.txt");
		Np = arq.readInt();
		Nt = arq.readInt();
		pontos = new double[Np][3];
		triangulos = new int[Nt][3];
		for (int i = 0; i < Np; i++) {
			for (int j = 0; j < 3; j++) {
				pontos[i][j] = arq.readDouble();
			}
		}
		for (int i = 0; i < Nt; i++) {
			for (int j = 0; j < 3; j++) {
				triangulos[i][j] = arq.readInt();
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
		arq = new Arquivo("atributos.txt", "lixoAtr.txt");
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
			v1 = Algbantigo.sub(pontos[pB], pontos[pA]);// calcula os dois vetores
			v2 = Algbantigo.sub(pontos[pC], pontos[pA]);// definidos pelos pontos do
			// triangulo
			n = Algbantigo.prodVetorial(v1, v2);
			for (int j = 0; j <= 3; j++) {
				NormTriangulos[i] = n;// salva a normal no array d normais de
				// triangulo
				// soma essa normal no array de normal de vertices
				NormPontos[pA] = Algbantigo.soma(NormPontos[pA], n);
				NormPontos[pB] = Algbantigo.soma(NormPontos[pA], n);
				NormPontos[pC] = Algbantigo.soma(NormPontos[pA], n);
			}

		}
		for (int i = 0; i < NormPontos.length; i++) {
			NormPontos[i] = Algbantigo.normalize(NormPontos[i]);
		}

	}

	public static double[][] projetar2d(double[][] pontos) {
		double[][] pontos2d = new double[pontos.length][2];
		for (int i = 0; i < pontos.length; i++) {

			pontos2d[i][0] = (pontos[i][0] * d) / (pontos[i][2] * hx);
			pontos2d[i][1] = (pontos[i][1] * d) / (pontos[i][2] * hy);
			pontos2d[i][0] = ((pontos2d[i][0] + 1) / 2) * (800 - 1);
			pontos2d[i][1] = ((1 - pontos2d[i][1]) / 2) * (600 - 1);
		}
		return pontos2d;
	}

	public void varredura(double[][] pontos2d) {
		double px1, px2;
		double varicao;

		px1 = pontos2d[0][0];
		px2 = pontos2d[1][0];

		for (double y = pontos2d[0][1]; y < pontos2d[1][1]; y++) {

		}

	}

	public static void main(String[] args) {
		lerCamera();
		printCamera();
		lerAtributos();
		printAtributos();
		lerObjetos();
		printObjetos();

		// Parte 3 - Mudanca de coor - Mundiais -> Camera
		// eh preciso fazer a inversa dessa matriz para que ela possa ser
		// utilizada para a mudanca de coordenada como requer a terceira parte.
		// porém como as matrizes recebidas serao sempre ortornomais basta
		// apenas
		// transpor ele por sendo ortornomal M^-1 = M^t

		// descobrindo U que é o produto vetorial de N e V
		V = Algbantigo.sub(V, Algbantigo.projec(V, N));
		System.out.println("V = V-Proj(V,N): " + Algbantigo.VectorToString(V));
		U = Algbantigo.prodVetorial(N, V);
		System.out.println("NxV: " + Algbantigo.VectorToString(U));

		// normalizando

		U = Algbantigo.normalize(U);
		V = Algbantigo.normalize(V);
		N = Algbantigo.normalize(N);

		System.out.println("V: " + Algbantigo.VectorToString(V));
		System.out.println("U: " + Algbantigo.VectorToString(U));
		System.out.println("N: " + Algbantigo.VectorToString(N));

		// Matriz de mudança de coordenada de camera

		for (int i = 0; i < V.length; i++) {
			matrizMudBase[0][i] = U[i];
			matrizMudBase[1][i] = V[i];
			matrizMudBase[2][i] = N[i];
		}

		// Mudança de base de todos os pontos e da posicao da fonte de luz Pl
		pontosTrans = Algbantigo.mudancaDeCoordenada(pontos, matrizMudBase, C);
		System.out.println(pontosTrans.length + " "
				+ Algbantigo.VectorToString(pontosTrans[0]));

		pontos2d = projetar2d(pontosTrans);
		System.out.println(Algbantigo.VectorToString(pontos2d[0]));

		Pl = Algbantigo.multMatrizVetor(matrizMudBase, Pl);

		// projetando os pontos na tela


		//PHONG
		//aqui eu preciso da coordenada baricêntrica das normais de um ponto;
		//variavel global Pphong criada para ser o ponto de entrada
		//variavel global Nphong criada para ser resultado da coordenada Baricentrica das normais de um ponto;
		Lphong = Algbantigo.normalize(Algbantigo.sub(Pl, Pphong));
		menosPphong = Algbantigo.sub(zero, Pphong);  
		Vphong = Algbantigo.normalize(menosPphong);

		//int Ilum = Ia + Ie + (Algb.prodEscalar(Lphong, Nphong)*Algb.prodEscalar(Od, Il)* Kd);
		
	}
}
