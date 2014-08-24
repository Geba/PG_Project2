package finalend;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.GLMatrixFunc;

import com.jogamp.opengl.util.FPSAnimator;

import end2.Algb;

public class EsseFunciona implements GLEventListener {
	static void calcularNormais() {

		NormTriangulos = new double[Nt][3];
		NormPontos = new double[Np][3];
		for (int t = 0; t < triangulos.length; t++) {
			double[] v1, v2, n;
			int pA, pB, pC;
			pA = triangulos[t][0];
			pB = triangulos[t][1];
			pC = triangulos[t][2];
			int[] indicepontos = { pA, pB, pC };
			// descobrir quais vetores pegar aqui
			v1 = Algeb.sub(pontosInCamera[pB], pontosInCamera[pA]);// calcula os
																	// dois
																	// vetores
			v2 = Algeb.sub(pontosInCamera[pC], pontosInCamera[pA]);// definidos
																	// pelos
																	// pontos do
			// Triangle
			n = Algeb.prodVetorial(v1, v2);// calcula a normal
			double[] baricentro = new double[3];// calcula o baricentro do
												// triangulo
			for (int v = 0; v < 3; v++) {// v de vertice
				baricentro = Algeb.soma(baricentro, Algeb.prodByEscalar(
						(1.0 / 3.0), pontosInCamera[indicepontos[v]]));
			}
			double[] toObserv = Algeb.sub(zero, baricentro);// vetor do
															// baricentro para
															// observadr
			toObserv = Algeb.normalize(toObserv);

			// System.out.println(Algb.VectorToString(n));
			double prodescalar = Algeb.prodEscalar(toObserv, n);
			if (prodescalar < 0) {
				n = Algeb.sub(zero, n);
			}
			// soma essa normal no array de normal de vertices
			NormPontos[pA] = Algeb.soma(NormPontos[pA], n);
			NormPontos[pB] = Algeb.soma(NormPontos[pB], n);
			NormPontos[pC] = Algeb.soma(NormPontos[pC], n);

			n = Algeb.normalize(n);
			NormTriangulos[t] = n;// salva a normal no array d normais de
			// triangulo

		}
		for (int i = 0; i < NormPontos.length; i++) {
			NormPontos[i] = Algeb.normalize(NormPontos[i]);
		}

	}

	public static double[] coefsBaricentricos(double[] P, double[] P1,
			double[] P2, double[] P3) {
		double[] PP1 = Algeb.sub(P, P1);
		double[] P2P1 = Algeb.sub(P2, P1);
		double[] P2P3 = Algeb.sub(P2, P3);
		double[] PP3 = Algeb.sub(P, P3);
		double[] P3P1 = Algeb.sub(P3, P1);
		double[] vPP1 = new double[] { PP1[0], PP1[1], 0 };
		double[] vP2P1 = new double[] { P2P1[0], P2P1[1], 0 };
		double[] vP2P3 = new double[] { P2P3[0], P2P3[1], 0 };
		double[] vPP3 = new double[] { PP3[0], PP3[1], 0 };
		double[] vP3P1 = new double[] { P3P1[0], P3P1[1], 0 };

		double A1 = Algeb.getNorma(Algeb.prodVetorial(vPP1, vP2P1));
		double A2 = Algeb.getNorma(Algeb.prodVetorial(vPP3, vP2P3));
		double A3 = Algeb.getNorma(Algeb.prodVetorial(vPP1, vP3P1));
		double total = A1 + A2 + A3;
		return new double[] { (A2 / total), (A3 / total), (A1 / total) };
	}

	private static void drawLine(double[] px1, double[] px2, double[] px3, int t) {
		double xmin = resX;
		double xmax = 0;
		double[][] pixels = { px1, px2, px3 };
		for (int p = 0; p < 3; p++) {
			if (pixels[p][0] < xmin)
				xmin = pixels[p][0];
			if (pixels[p][0] < xmax)
				xmax = pixels[p][0];
		}
		for (double x = xmin; x <= xmax; x++) {
			phong(x, px1[1], t);
			matCor[(int) Math.round(x)][(int) Math.round(px1[1])] = new float[] {
					1, 1, 0 };
		}

	}

	private static double[][] getMudBase(double[] u, double[] v, double[] n) {
		double mudBase[][] = new double[v.length][v.length];
		for (int i = 0; i < v.length; i++) {
			mudBase[0][i] = u[i];
			mudBase[1][i] = v[i];
			mudBase[2][i] = n[i];
		}
		return mudBase;
	}

	private static double[] getNewCoordinates(double[] pl2,
			double[][] matrizMudBase2, double[] c2) {
		pl2 = Algeb.sub(pl2, c2);
		pl2 = Algeb.multMatrizVetor(matrizMudBase2, pl2);
		return pl2;
	}

	public static double[][] getNewCoordinates(double[][] pontos,
			double[][] mudBase, double[] camera) {
		double[][] retorno = new double[pontos.length][3];
		for (int i = 0; i < pontos.length; i++) {
			double[] ponto = pontos[i];
			ponto = Algeb.sub(ponto, camera);
			// System.out.println(Algeb.VectorToString(ponto));
			ponto = Algeb.multMatrizVetor(mudBase, ponto);
			// System.out.println(ponto[0]);
			retorno[i] = ponto;
		}
		return retorno;
	}

	private static void initi() {
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		GLCanvas canvas = new GLCanvas(caps);
		Frame frame = new Frame("AWT Window Test");
		frame.setSize(resX, resY);
		frame.add(canvas);
		frame.setVisible(true);

		// by default, an AWT Frame doesn't do anything when you click
		// the close button; this bit of code will terminate the program when
		// the window is asked to close
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		canvas.addGLEventListener(new EsseFunciona());
		FPSAnimator animator = new FPSAnimator(canvas, 60);
		animator.add(canvas);
		animator.start();

	}

	static void lerAtributos() {// ok
		arq = new Arquivo("atributos.txt", "lixoAtr.txt");
		Plight[0] = arq.readDouble();
		Plight[1] = arq.readDouble();
		Plight[2] = arq.readDouble();
		Ka = arq.readDouble();
		Ia[0] = arq.readDouble() / 255.0f;
		Ia[1] = arq.readDouble() / 255.0f;
		Ia[2] = arq.readDouble() / 255.0f;
		Kd = arq.readDouble();
		Od[0] = arq.readDouble();
		Od[1] = arq.readDouble();
		Od[2] = arq.readDouble();
		Ks = arq.readDouble();
		Il[0] = arq.readDouble() / 255.0f;
		Il[1] = arq.readDouble() / 255.0f;
		Il[2] = arq.readDouble() / 255.0f;
		n = arq.readDouble();
	}

	static void lerCamera() {// ok
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

	static void lerObjetos() {
		Arquivo arq = new Arquivo("objeto.txt", "lixoObj.txt");
		Np = arq.readInt();
		Nt = arq.readInt();
		pontos = new double[Np][3];
		triangulos = new int[Nt][3];
		pontosInCamera = new double[Np][3];
		pontosInPlanoNormalizado = new double[Np][2];
		pontosInPixels = new double[Np][2];
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

	public static void main(String[] args) {

		lerCamera();
		lerAtributos();
		lerObjetos();
		printAtributos();
		initi();
		refresh();
		// essa funcoes devem vir em ordem
		V = Algeb.sub(V, Algeb.projec(V, N));
		U = Algeb.prodVetorial(N, V);
		U = Algeb.normalize(U);
		V = Algeb.normalize(V);
		N = Algeb.normalize(N);
		matrizMudBase = getMudBase(U, V, N);
		pontosInCamera = getNewCoordinates(pontos, matrizMudBase, C);
		Plight = getNewCoordinates(Plight, matrizMudBase, C);
		pontosInPlanoNormalizado = projetar2d(pontosInCamera);
		pontosInPixels = projetarEmTela(pontosInPlanoNormalizado);
		// essa funcoes devem vir em ordem
		// essas nao

		calcularNormais();
		ordenarTriangulos();
		// essas nao
	}

	static void ordenarTriangulos() {// ok
		for (int t = 0; t < triangulos.length; t++) {
			if (pontosInCamera[triangulos[t][1]][1] < pontosInCamera[triangulos[t][2]][1]) {
				int aux = triangulos[t][2];
				triangulos[t][2] = triangulos[t][1];
				triangulos[t][1] = aux;
			}
			if (pontosInCamera[triangulos[t][0]][1] < pontosInCamera[triangulos[t][1]][1]) {
				int aux = triangulos[t][1];
				triangulos[t][1] = triangulos[t][0];
				triangulos[t][0] = aux;
			}
			if (pontosInCamera[triangulos[t][1]][1] < pontosInCamera[triangulos[t][2]][1]) {
				int aux = triangulos[t][2];
				triangulos[t][2] = triangulos[t][1];
				triangulos[t][1] = aux;
			}
		}
	}

	private static void phong(double x1, double y2, int t) {// ok
		int x = (int) Math.round(x1);
		int y = (int) Math.round(y2);
		double[] px1, px2, px3, px = { x1, y2 }, P, coef, normal;
		int[] triangulo = triangulos[t];
		double[] cor;
		px1 = pontosInPixels[triangulo[0]];
		px2 = pontosInPixels[triangulo[1]];
		px3 = pontosInPixels[triangulo[2]];
		coef = coefsBaricentricos(px, px1, px2, px3);
		P = new double[3];
		for (int i = 0; i < 3; i++) {
			P = Algeb.soma(P,
					Algeb.prodByEscalar(coef[i], pontosInCamera[triangulo[i]]));
		}

		if (zbuffercamera[x][y] > P[2]) {// deve-se pintar
			zbuffercamera[x][y] = P[2];
			normal = new double[3];
			for (int i = 0; i < 3; i++) {
				normal = Algeb.soma(normal,
						Algeb.prodByEscalar(coef[i], NormPontos[triangulo[i]]));
			}
			cor = new double[3];
			for (int i = 0; i < 3; i++) {
				cor[i] = 0;
			}
			normal = Algeb.normalize(normal);// tenho a normal do ponto
			double[] light = Algeb.normalize(Algeb.sub(Plight, P));// tenho o
																	// vetor
																	// direcionado
																	// para a
																	// luz
			double[] VObservador = Algeb.sub(zero, P);
			VObservador = Algeb.normalize(VObservador);
			if (Algeb.prodEscalar(normal, VObservador) < 0)
				normal = Algeb.sub(zero, normal);
			double N_L = Algeb.prodEscalar(normal, light);
			double[] R = new double[3];
			R = Algeb.sub(Algeb.prodByEscalar(
					2 * Algeb.prodEscalar(light, normal), normal), light);
			double V_R = Algeb.prodEscalar(R, VObservador);
			for (int i = 0; i < 3; i++) {
				double ambiente = (Ka * Ia[i]);
				double difusa = (float) Math.max((Od[i] * Il[i] * Kd * N_L), 0);
				double especular = (float) Math.max(
						(Il[i] * Ks * Math.pow(V_R, n)), 0);
				cor[i] = (ambiente + difusa + especular);
				matCor[x][y][i] = (float) cor[i];
			}
		}
	}

	static void printAtributos() {// ok
		System.out.println("Ia: " + Algeb.VectorToString(Ia));
		System.out.println("Ka: " + Ka);
		System.out.println("Od: " + Algeb.VectorToString(Od));
		System.out.println("Kd: " + Kd);
		System.out.println("Ks: " + Ks);
		System.out.println("n: " + n);
		System.out.println("Il :" + Algeb.VectorToString(Il));
		System.out.println("Pl :" + Algeb.VectorToString(Plight));
	}

	static void printCamera() {// ok
		System.out.println("C: " + Algeb.VectorToString(C));
		System.out.println("N: " + Algeb.VectorToString(N));
		System.out.println("V: " + Algeb.VectorToString(V));
		System.out.println("d: " + d + "\nhx: " + hx + "\nhy: " + hy);
	}

	static void printNormals() {// ok

		for (int i = 0; i < NormTriangulos.length; i++) {

			System.out.print("NormTriangulos " + i + " "
					+ Algb.VectorToString(NormTriangulos[i]));

		}
	}

	static void printObjetos() {// /ok
		System.out.println("Np: " + Np + " Nt: " + Np);
		for (int i = 0; i < Np; i++) {
			System.out.println("Ponto " + i + " "
					+ Algeb.VectorToString(pontos[i]));
		}
		for (int i = 0; i < Nt; i++) {
			System.out.println("Triangulo " + i + " "
					+ Algeb.VectorToString(triangulos[i]));

		}
	}

	public static double[][] projetar2d(double[][] pontosEspaco) {
		double[][] pontos2d = new double[pontosEspaco.length][2];
		for (int i = 0; i < pontosEspaco.length; i++) {
			pontos2d[i][0] = (d / hx)
					* (pontosEspaco[i][0] / pontosEspaco[i][2]);
			pontos2d[i][1] = (d / hy)
					* (pontosEspaco[i][1] / pontosEspaco[i][2]);
		}
		return pontos2d;
	}

	public static double[][] projetarEmTela(double[][] pontos2d) {// ok
		double[][] pontosInPixels = new double[pontos2d.length][2];
		for (int i = 0; i < pontos2d.length; i++) {
			pontosInPixels[i][0] = (pontos2d[i][0] + 1) / 2 * (resX - 1);
			pontosInPixels[i][1] = (1 - pontos2d[i][1]) / 2 * (resY - 1);
		}
		return pontosInPixels;

	}

	private static void refresh() {
		zbuffercamera = new double[resX][resY];
		pontosInPixels = projetarEmTela(pontosInPlanoNormalizado);
		for (int x = 0; x < resX; x++) {
			for (int y = 0; y < resY; y++) {
				zbuffercamera[x][y] = Double.MAX_VALUE - 1;
			}
		}
		matCor = new float[resX][resY][3];// [x do pixel][y do

	}

	public static void scanDOWN(double[] px1, double[] px2, double[] px3, int t) {
		// reta 31
		if (px2[0] < px1[0]) {
			double[] aux = px1;
			px1 = px2;
			px2 = aux;
		}
		double deltay31 = px3[1] - px1[1];
		double deltax31 = px3[0] - px1[0];
		double r1 = deltax31 / deltay31;
		double deltay32 = px3[1] - px2[1];
		double deltax32 = px3[0] - px2[0];
		double r2 = deltax32 / deltay32;
		double ymax = px3[1];
		double ymin = px1[1];
		double xmin = px1[0];
		double xmax = px2[0];
		for (double y = ymin; y <= ymax; y++) {
			xmax += r2;
			xmin += r1;
			for (double x = xmin; x < xmax; x++) {
				int i = 0;
				if (x >= 0 && x < resX-1 && y >= 0 && y < resY-1) {
					System.out.println(resX);
					phong(x, y, t);
					i++;
				}
				System.out.println(i);
			}
		}

		// double deltay31 = px1[1] - px3[1];
		// double deltax31 = px1[0] - px3[0];
		// double r1 = deltax31 / deltay31;
		// double deltay32 = px2[1] - px3[1];
		// double deltax32 = px2[0] - px3[0];
		// double r2 = deltax32 / deltay32;
		// double ymax = px3[1];
		// double ymin = px1[1];
		// double xmin = px3[0];
		// double xmax = xmin;
		// for (double y = ymax; y > ymin; y--) {
		// for (double x = xmin; x <= xmax; x++) {
		// int i =0;
		// if (x > 0 && x < resX && y > 0 && y < resY) {
		// phong(x, y, t);
		// i++;
		// }
		// System.out.println(i);
		// }
		// xmax-=r2;
		// xmin-=r1;
		// }

	}

	private static void scanline() {
		double deltay12, deltay23;
		double[] px1, px2, px3;

		for (int t = 0; t < triangulos.length; t++) {
			px1 = pontosInPixels[triangulos[t][0]];
			px2 = pontosInPixels[triangulos[t][1]];
			px3 = pontosInPixels[triangulos[t][2]];

			// verifica se é um triangulo virado para cima:
			deltay23 = Math.abs(px2[1] - px3[1]);
			if (Math.abs(deltay23) <= intervalo) {
				//scanUP(px1, px2, px3, t);
			} else {
				deltay12 = Math.abs(px2[1] - px3[1]);
				if (Math.abs(deltay12) <= intervalo) {// triangulo virado para
														// baixo
					scanDOWN(px1, px2, px3, t);
				} else {// we have to split the triangle
						// como os vertices do triangulo estao ordenados do
						// maior para o menor y, em coordenadas de plano de
						// vista, o y do ponto de intecessao é igual a px2.y
					double x, deltax31;

					double deltay31 = px3[1] - px1[1];

					if (Math.abs(deltay31) < intervalo) {// linha horizontal
						drawLine(px1, px2, px3, t);
					} else {
						deltax31 = px3[0] - px1[0];
						double a = deltax31 / deltay31;
						double deltay21 = px2[1] - px1[1];
						x = px1[0] + a * deltay21;
						double[] pxNew = new double[] { x, px2[1] };
					//	scanUP(px1, px2, pxNew, t);
						scanDOWN(px1, pxNew, px3, t);
					}

				}
			}

		}

	}

	public static void scanUP(double[] px1, double[] px2, double[] px3, int t) {
		double invslope1 = (px2[0] - px1[0]) / (px2[1] - px1[1]);
		double invslope2 = (px3[0] - px1[0]) / (px3[1] - px1[1]);
		double curx1 = px1[0];
		double curx2 = px1[0];
		if (curx1 < 0)
			curx1 = 0;
		if (curx2 > resX)
			curx2 = resX;

		for (int y = (int) px1[1]; y <= px2[1]; y++) {
			for (int x = (int) curx1; x <= curx2; x++) {
				if (x > 0 && x < resX && y > 0 && y < resY) {
					phong(x, y, t);
					matCor[Math.round(x)][Math.round(y)] = new float[] { 0, 1,
							1 };
				}
			}
			curx1 += invslope1;
			curx2 += invslope2;
			if (curx1 < 0)
				curx1 = 0;
			if (curx2 > resX)
				curx2 = resX;
		}
	}

	// objetos
	static int Np;
	static int Nt;
	static double[][] pontos;

	static int[][] triangulos;

	static double[][] NormPontos;

	static double[][] NormTriangulos;

	// atributos
	static double[] Ia = new double[3];

	static double Ka;

	static double Od[] = new double[3];

	static double Kd;

	static double Ks;

	static double n;

	static double[] Il = new double[3];

	static double[] Plight = new double[3];

	static Arquivo arq;

	// camera
	static double[] C = new double[3];

	static double[] N = new double[3];

	static double[] V = new double[3];

	static double[] U = new double[3];

	static double[] V1 = new double[3];

	static double d;

	static double hx;

	static double hy;

	// auxiliares
	static double[] zero = { 0.0, 0.0, 0.0 };

	static final double intervalo = 0.05;

	static double[][] matrizMudBase = new double[V.length][V.length];

	static double[][] pontosInCamera, pontosInPlanoNormalizado;

	// Mapeamento da tela
	static int resX = 1280;

	static int resY = 720;

	static double[][] zbuffercamera;

	static double[][] pontosInPixels;// [indice do ponto][{xdo pixel, y do

	// private static void teste() {
	// // scanline();
	//
	// }

	// pixel}]//equivalentes ao ponto
	static float[][][] matCor;// [x do pixel][y do

	@Override
	public void display(GLAutoDrawable drawable) {
		update();
		render(drawable);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	@Override
	public void init(GLAutoDrawable drawable) {
	}

	// tamanho ideal camaro original zoom 10 - 8
	// tamanho ideal camaro 2d zoom ideal .7 - 1
	// tamanho ideal camaro 2d zoom ideal 1
	private void render(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0, resX, resY, 0, -1, 1);
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glBegin(GL.GL_POINTS);
		gl.glPointSize(40);
		scanline();
		// /pinta os pontos
		gl.glBegin(GL.GL_POINTS);
		for (int x = 0; x < resX; x++) {
			for (int y = 0; y < resY; y++) {
				gl.glColor3f(matCor[x][y][0], matCor[x][y][1], matCor[x][y][2]);
				gl.glVertex2i(x, y);
			}
		}
		gl.glEnd();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
		System.out.println("NEW: X" + x + " y: " + y + " w:" + w + " h:" + h);
		resX = w;
		resY = h;
		refresh();
	}

	private void update() {

	}

}