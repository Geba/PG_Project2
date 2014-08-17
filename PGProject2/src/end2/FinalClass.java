package end2;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL4;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.GLMatrixFunc;

import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.FPSAnimator;



public class FinalClass implements GLEventListener {

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
	static double Od[] = new double[3];
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
	static double hx;
	static double hy;

	// auxiliares
	static double[][] matrizMudBase = new double[V.length][V.length];
	static double[][] pontosTrans, pontos2d;

	// Mapeamento da tela
	static int resX = 800;
	static int resY = 600;
	static double[][] zbuffercamera = new double[resX][resY];
	static double[][] zbufferluz = new double[resX][resY];
	static int[][] matPointsInPixels;// [indice do ponto][{xdo pixel, y do
										// pixel}]//equivalentes ao ponto
	static float[][][] matCor = new float[resX][resY][3];// [x do pixel][y do
															// pixel][{red,
															// blue, green}]

	
	
	
	
	public static double[][] projetar2d(double[][] pontos) {
		double[][] pontos2d = new double[pontos.length][2];
		for (int i = 0; i < pontos.length; i++) {
			pontos2d[i][0] = (pontos[i][0] * d) / (pontos[i][2] * hx);
			pontos2d[i][1] = (pontos[i][1] * d) / (pontos[i][2] * hy);

			// pontos2d[i][0] = ((pontos2d[i][0] + 1) / 2) * (resX - 1);
			// pontos2d[i][1] = ((1 - pontos2d[i][1]) / 2) * (resY - 1);

			matPointsInPixels[i][0] = (int) ((pontos2d[i][0] + 1) / 2 * resX + .5);
			matPointsInPixels[i][1] = (int) (resY - ((pontos2d[i][1] + 1) / 2)
					* resY + .5);

		}
		return pontos2d;
	}

	static void calcularNormais() {
		NormTriangulos = new double[Nt][3];
		NormPontos = new double[Np][3];
		for (int i = 0; i < triangulos.length; i++) {
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
				NormTriangulos[i] = n;// salva a normal no array d normais de
										// triangulo
				// soma essa normal no array de normal de vertices
				NormPontos[pA] = Algb.soma(NormPontos[pA], n);
				NormPontos[pB] = Algb.soma(NormPontos[pA], n);
				NormPontos[pC] = Algb.soma(NormPontos[pA], n);
			}
			// System.out.println(i);
		}
		for (int i = 0; i < NormPontos.length; i++) {
			NormPontos[i] = Algb.normalize(NormPontos[i]);
		}

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
		// printCamera();
		lerAtributos();
		// printAtributos();
		lerObjetos();
		// printObjetos();
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		GLCanvas canvas = new GLCanvas(caps);
		
		V = Algb.sub(V, Algb.projec(V, N));
		System.out.println("V = V-Proj(V,N): " + Algb.VectorToString(V));
		U = Algb.prodVetorial(N, V);
		System.out.println("NxV: " + Algb.VectorToString(U));

		// normalizando

		U = Algb.normalize(U);
		V = Algb.normalize(V);
		N = Algb.normalize(N);

		matrizMudBase = getMudBase(U, V, N);
		calcularNormais();
		pontosTrans = getNewCoordinates(pontos, matrizMudBase, C);
		Pl = getNewCoordinates(Pl, matrizMudBase, C);
		// ok até aqui
		teste();

		pontos2d = projetar2d(pontosTrans);

		Frame frame = new Frame("AWT Window Test");
		frame.setSize(resX, resY);
		frame.add(canvas);
		frame.setVisible(true);

		// by default, an AWT Frame doesn't do anything when you click
		// the close button; this bit of code will terminate the program when
		// the window is asked to close
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		canvas.addGLEventListener(new FinalClass());
		FPSAnimator animator = new FPSAnimator(canvas, 60);
		animator.add(canvas);
		animator.start();
	}

	private static double[] getNewCoordinates(double[] pl2,
			double[][] matrizMudBase2, double[] c2) {
		pl2 = Algb.sub(pl2, c2);
		pl2 = Algb.multMatrizVetor(matrizMudBase2, pl2);
		return pl2;
	}

	private static void teste() {
		printNormals();

		double[][] pontosTrans = getNewCoordinates(pontos, matrizMudBase, C);
		// System.out.println(pontosTrans.length);
		for (int i = 0; i < pontosTrans.length; i++) {
			// System.out.println(" "+Algeb.VectorToString(pontosTrans[i]));
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

	public static double[][] getNewCoordinates(double[][] pontos,
			double[][] mudBase, double[] camera) {
		double[][] retorno = new double[pontos.length][3];
		for (int i = 0; i < pontos.length; i++) {
			double[] ponto = pontos[i];
			ponto = Algb.sub(ponto, camera);
			// System.out.println(Algeb.VectorToString(ponto));
			ponto = Algb.multMatrizVetor(mudBase, ponto);
			// System.out.println(ponto[0]);
			retorno[i] = ponto;
		}
		return retorno;
	}

	// tamanho ideal camaro original zoom 10 - 8
	// tamanho ideal camaro 2d zoom ideal .7 - 1
	// tamanho ideal camaro 2d zoom ideal 1
	private void render(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glMatrixMode(gl.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0, resX, resY, 0, -1, 1);
		gl.glMatrixMode(gl.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glBegin(GL.GL_POINTS);
		gl.glPointSize(40);
		
		for( float x =0;x<resX;x++){
			for (float y = 0;y<resY;y++){
				gl.glPointSize(6);
				float red  =x/resX;
				float green = y/resY;
				float blue= red*green;
				gl.glColor3f(red, green, blue);
				gl.glVertex2i((int) x, (int) y);
				}
		}
		gl.glEnd();
		
		gl.glBegin(GL.GL_POINTS);
		for(int x = 0;x<resX;x++){
			for(int y = 0; y<resY;y++){
				gl.glColor3f(matCor[x][y][0],matCor[x][y][1],matCor[x][y][2]);
				gl.glVertex2i(x, y);
			}
		}
		gl.glEnd();
		
//		for (int t = 0; t < Nt - 1; t++) {
//			gl.glBegin(GL.GL_POINTS);
//			for (int k = 0; k < 3; k++) {
//				int[] a = matPointsInPixels[triangulos[t][k]];
//				gl.glColor3f(1, 1, 1);
//				double zoom = 10;
//				gl.glVertex2d(a[0] / zoom, a[1] / zoom);
//				//System.out.println(a[0]);
//
//			}
//			gl.glEnd();
//
//		}
//		
		
		

	}

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

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
	}

	private void update() {

	}

	public static double[] coefsBaricentricos(double[] P, double[] P1,
			double[] P2, double[] P3) {
		double x = P[0], y = P[1], x1 = P1[0], y1 = P1[1], x2 = P2[0], y2 = P2[1], x3 = P[0], y3 = P[1];
		double u, v, w;
		double denom = ((x1 - x3) * (y2 - y3) - (y1 - y3) * (x2 - x3));
		u = ((y2 - y3) * (x - x3) - ((x3 - x2) * (y3 - y))) / denom;
		v = (((y3 - y1) * (x - x3)) - ((x1 - x3) * (y3 - y))) / denom;
		w = 1.0 - u - v;
		return new double[] { u, v, w };
	}

	public void Varrer(int[][] ponto) {
		// yMedia: Valor do y que tem o valor intermediário dos três pontos

		// tratar casos de divisao por zero
		int inclinacaoP1P2, inclinacaoP1P3, inclinacaoP2P3;
		if (ponto[0][0] - ponto[1][0] == 0) {
			inclinacaoP1P2 = 0;
		} else {
			inclinacaoP1P2 = (ponto[0][1] - ponto[1][1])
					/ (ponto[0][0] - ponto[1][0]);
		}

		if (ponto[0][0] - ponto[2][0] == 0) {
			inclinacaoP1P3 = 0;
		} else {
			inclinacaoP1P3 = (ponto[0][1] - ponto[2][1])
					/ (ponto[0][0] - ponto[2][0]);
		}
		if (ponto[1][0] - ponto[2][0] == 0) {
			inclinacaoP2P3 = 0;
		} else {
			inclinacaoP2P3 = (ponto[1][1] - ponto[2][1])
					/ (ponto[1][0] - ponto[2][0]);
		}
		// dividir o triangulo em dois triangulos
		int posicaoInicio = ponto[0][0], posicaoAtualY = ponto[0][1], posicaoFim;
		posicaoInicio = ponto[0][0];
		posicaoFim = ponto[0][0];
		// varre o primeiro triangulo
		for (int i = ponto[0][1]; i <= ponto[1][1]; i++) {
			posicaoInicio = posicaoInicio + inclinacaoP1P2;
			posicaoFim = posicaoFim + inclinacaoP1P3;
			int incremento = 1;
			if (posicaoFim < posicaoInicio)
				incremento *= -1;
			for (int j = posicaoInicio; j <= posicaoFim; j += incremento) {
				matCor[i][j][0] = 1;matCor[i][j][1] = 1;matCor[i][j][2] = 1;
				
				
				
				
				
				
				
			}
		}
		// varre no segundo triangulo
		for (int i = ponto[1][1]; i < ponto[2][1]; i++) {
			posicaoInicio = posicaoInicio + inclinacaoP2P3;
			posicaoFim = posicaoFim + inclinacaoP1P3;
			int incremento = 1;
			if (posicaoFim < posicaoInicio)
				incremento *= -1;
			for (int j = posicaoInicio; j <= posicaoFim; j += incremento) {

			}
		}
		// acha o ponto que intersecta as retas com maior e menor y

	}
	static void lerObjetos() {
		Arquivo arq = new Arquivo("objeto.txt", "lixoObj.txt");
		Np = arq.readInt();
		Nt = arq.readInt();
		pontos = new double[Np][3];
		triangulos = new int[Nt][3];
		pontosTrans = new double[Np][3];
		pontos2d = new double[Np][2];
		matPointsInPixels = new int[Np][2];
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

	static void printNormals() {

		for (int i = 0; i < NormTriangulos.length; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(NormTriangulos[i][j] + " ");
			}
			System.out.println();
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

}
