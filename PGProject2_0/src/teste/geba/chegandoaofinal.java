package teste.geba;

import end.Algbantigo;
import end2.Arquivo;
import end2.Algb;
import end2.Varredura;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL4;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.GLMatrixFunc;

import org.la4j.LinearAlgebra;
import org.la4j.linear.LinearSystemSolver;
import org.la4j.matrix.sparse.CRSMatrix;
import org.la4j.vector.Vector;
import org.la4j.vector.dense.BasicVector;

import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.FPSAnimator;

public class chegandoaofinal implements GLEventListener {

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
	static double[][] matrizMudBase = new double[V.length][V.length];
	static double[][] pontosInCamera, pontosInPlanoNormalizado;

	// Mapeamento da tela
	static int resX = 1280;
	static int resY = 720;
	static double[][] zbuffercamera = new double[resX][resY];
	static double[][] zbufferluz = new double[resX][resY];
	static double[][] pontosInPixels;// [indice do ponto][{xdo pixel, y do
	// pixel}]//equivalentes ao ponto
	static float[][][] matCor = new float[resX][resY][3];// [x do pixel][y do

	// pixel][{red,
	// blue, green}]

	public static double[][] projetar2d(double[][] pontosTrans) {
		double[][] pontos2d = new double[pontosTrans.length][2];
		for (int i = 0; i < pontosTrans.length; i++) {
			pontos2d[i][0] = (d / hx) * (pontosTrans[i][0] / pontosTrans[i][2]);
			pontos2d[i][1] = (d / hy) * (pontosTrans[i][1] / pontosTrans[i][2]);

			pontosInPixels[i][0] = (pontos2d[i][0] + 1) / 2 * (resX - 1);
			pontosInPixels[i][1] = (1 - pontos2d[i][1]) / 2 * (resY - 1);
			// pontos2d[i][0] = (pontos2d[i][0] + 1) / 2 * (resX - 1);
			// pontos2d[i][1] = (1 - pontos2d[i][1]) / 2 * (resY - 1);
		}
		return pontos2d;
	}

	static void ordenarTriangulos() {
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
                            v1 = Algb.sub(pontosInCamera[pB], pontosInCamera[pA]);// calcula os
                                                                                                                                            // dois
                                                                                                                                            // vetores
                            v2 = Algb.sub(pontosInCamera[pC], pontosInCamera[pA]);// definidos
                                                                                                                                            // pelos
                                                                                                                                            // pontos do
                            // triangulo
                            n = Algb.prodVetorial(v1, v2);// calcula a normal
                            double[] baricentro = new double[3];// calcula o baricentro do
                                                                                                    // triangulo
                            for (int i = 0; i < 3; i++) {
                                    baricentro = Algb.soma(baricentro, Algb.prodByEscalar(
                                                    (1.0 / 3.0), pontosInCamera[indicepontos[i]]));
                            }
                            double[] toObserv = Algb.sub(zero, baricentro);// vetor do
                                                                                                                            // baricentro para
                                                                                                                            // observadr
                            toObserv = Algb.normalize(toObserv);
                            n = Algb.normalize(n);
     
                            // System.out.println(Algb.VectorToString(n));
                            double prodescalar = Algb.prodEscalar(toObserv, n);
                            if (prodescalar < 0) {
                                    n = Algb.sub(zero, n);
                            }
                            NormTriangulos[t] = n;// salva a normal no array d normais de
                            // triangulo
                            // soma essa normal no array de normal de vertices
                            NormPontos[pA] = Algb.soma(NormPontos[pA], n);
                            NormPontos[pB] = Algb.soma(NormPontos[pB], n);
                            NormPontos[pC] = Algb.soma(NormPontos[pC], n);
     
                            // System.out.println(i);
                    }
                    for (int i = 0; i < NormPontos.length; i++) {
                            NormPontos[i] = Algb.normalize(NormPontos[i]);
                    }
     
            }



	public static void main(String[] args) {

		lerCamera();
		lerAtributos();
		lerObjetos();
		printAtributos();
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		GLCanvas canvas = new GLCanvas(caps);
		V = Algb.sub(V, Algb.projec(V, N));
		U = Algb.prodVetorial(N, V);
		// normalizando
		U = Algb.normalize(U);
		V = Algb.normalize(V);
		N = Algb.normalize(N);

		matrizMudBase = getMudBase(U, V, N);
		pontosInCamera = getNewCoordinates(pontos, matrizMudBase, C);
		Plight = getNewCoordinates(Plight, matrizMudBase, C);
		// ok até aqui
		for (int x = 0; x < resX; x++) {
			for (int y = 0; y < resY; y++) {
				zbuffercamera[x][y] = Double.MAX_VALUE - 1;
			}
		}
		calcularNormais();
		pontosInPlanoNormalizado = projetar2d(pontosInCamera);
		ordenarTriangulos();
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
		canvas.addGLEventListener(new chegandoaofinal());
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
		// scanline();

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
		scanline();
		for (float x = 0; x < resX; x++) {
			for (float y = 0; y < resY; y++) {
				gl.glPointSize(6);
				// float red = 1;
				// float green = (230.0f / 255.0f);
				// float blue = 0;
				// matCor[(int) x][(int) y] = new float[] { red, green, blue };
				// gl.glColor3f(red, green, blue);
				gl.glVertex2i((int) x, (int) y);
			}
		}
		gl.glEnd();

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

		// gl.glBegin(GL.GL_POINTS);
		// for (int i = 0; i < pontosInPlanoNormalizado.length; i++) {
		// gl.glPointSize(20);
		// gl.glColor3f(1.f, (72.0f) / 255.f, 149.f / 255.f);
		// gl.glVertex2d(pontosInPixels[i][0], pontosInPixels[i][1]);
		//
		// }
		// gl.glEnd();

		// for (int t = 0; t < Nt - 1; t++) {
		// gl.glBegin(GL.GL_POINTS);
		// for (int k = 0; k < 3; k++) {
		// double[] a = matPointsInPixels[triangulos[t][k]];
		// gl.glColor3f(1, 1, 1);
		// double zoom = 1;
		// gl.glVertex2d(a[0] / zoom, a[1] / zoom);
		// // System.out.println(a[0]);
		//
		// gl.glEnd();
		//
		// }
		// }

	}

	private static void scanline() {
		double deltaXMax, deltaYMax, deltaYMin, deltaXMin, posicaoRelativa, deltaXMed, deltaYMed, coefAng31, b, x;
		double[] pxNovo = new double[2];// ponto de corte dos triangulo em 2;
		double[] aux;
		double[] px1, px2, px3;

		for (int t = 0; t < triangulos.length; t++) {// para cada triangulo

			// colocando cada triangulo em uma variavel
			px1 = pontosInPixels[triangulos[t][0]];
			px2 = pontosInPixels[triangulos[t][1]];
			px3 = pontosInPixels[triangulos[t][2]];

			// descobrindo as posicoes relativas do triangulos
			deltaXMax = Math.round(px2[0] - px1[0]);
			deltaYMax = Math.round(px2[1] - px1[1]);
			deltaYMin = Math.round(px3[1] - px2[1]);
			deltaXMin = Math.round(px3[0] - px2[0]);
			deltaXMed = Math.round(px3[0] - px1[0]);
			deltaYMed = Math.round(px3[1] - px1[1]);

			if (deltaYMax == 0) {

				if (deltaXMax < 0) {
					aux = px1;
					px1 = px2;
					px2 = aux;
				}
				scanDOWN(px1, px2, px3, t);
			} else {

				if (deltaYMin == 0) {
					if (deltaXMin < 0) {
						aux = px2;
						px2 = px3;
						px3 = aux;
					}
					scanUP(px1, px2, px3, t);
				} else {
					posicaoRelativa = px2[0] - px3[0];
					if (posicaoRelativa > 0) {
						
						if (deltaXMed == 0) {
							coefAng31 = 0;
						} else {
							coefAng31 = deltaYMed / deltaXMed;
						} // descobre o
							// coefAngular
						b = px1[1] - coefAng31 * px1[0]; // descobre o b da reta
															// de px3 e px1
						if (coefAng31 == 0) {
							x = px1[0];
						} else {
							x = (px2[1] - b) / coefAng31;
						}
						pxNovo[0] = x;
						pxNovo[1] = px2[1];

						scanUP(px1, pxNovo, px2, t);
						scanDOWN(pxNovo, px2, px3, t);

					} else {
						if (deltaXMed == 0) {
							coefAng31 = 0;
						} else {
							coefAng31 = deltaYMed / deltaXMed;
						}// descobre o
							// coefAngular
						b = px1[1] - coefAng31 * px1[0]; // descobre o b da reta
															// de px3 e px1
						if(coefAng31==0){
							x = px1[0];
						}
						else
						x = (px2[1] - b) / coefAng31;
						
						System.out.println(x);
						pxNovo[0] = x;
						pxNovo[1] = px2[1];
						scanUP(px1, px2, pxNovo, t);
						scanDOWN(px2, pxNovo, px3, t);

					}
				}
			}
		}
	}

	public static void scanUP(double[] px1, double[] pxNovo, double[] px2, int t) {
		double invslope1 = (pxNovo[0] - px1[0]) / (pxNovo[1] - px1[1]);
		double invslope2 = (px2[0] - px1[0]) / (px2[1] - px1[1]);
		double curx1 = px1[0];
		double curx2 = px1[0];
		if (curx1 < 0)
			curx1 = 0;
		if (curx2 > resX)
			curx2 = resX;

		for (int y = (int) px1[1]; y <= pxNovo[1]; y++) {
			for (int x = (int) curx1; x <= curx2; x++) {
				if (x > 0 && x < resX && y > 0 && y < resY) {
					phong(x, y, t);
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

	public static void scanDOWN(double[] px1, double[] px2, double[] px3, int t) {

		double invslope1 = (px3[0] - px1[0]) / (px3[1] - px1[1]);
		double invslope2 = (px3[0] - px2[0]) / (px3[1] - px2[1]);
		double curx1 = px3[0];
		double curx2 = px3[0];
		int ymax = (int) px3[1];
		int ymin = (int) px1[1];

		if (ymax >= resY)
			ymax = resY;
		if (ymin < 0)
			ymin = 0;

		for (int y = ymax; y > ymin; y--) {

			if (curx1 < 0)
				curx1 = 0;
			if (curx2 > resX)
				curx2 = resX;

			for (double x = curx1; x < curx2; x++) {
				if (x > 0 && x < resX && y > 0 && y < resY) {

					phong((int) x, y, t);
				}
			}
			curx1 -= invslope1;
			curx2 -= invslope2;
			if (curx1 < 0)
				curx1 = 0;
			if (curx2 > resX)
				curx2 = resX;
		}
	}

	

    private static void phong(int x, int y, int t) {
                    double[] teste;
                    double[] px1, px2, px3, P1, P2, P3, px = { x, y }, P, coef, normal;
                    int[] triangulo = triangulos[t];
                    float[] cor;
                    px1 = pontosInPixels[triangulo[0]];
                    px2 = pontosInPixels[triangulo[1]];
                    px3 = pontosInPixels[triangulo[2]];
                    coef = coefsBaricentricos(px, px1, px2, px3);
                     //System.out.println("coefs" + coef[0]+ " "+coef[1]+ " "+coef[2]+ " ");
                    P = new double[3];
                    for (int i = 0; i < 3; i++) {
                            P = Algb.soma(P,
                                            Algb.prodByEscalar(coef[i], pontosInCamera[triangulo[i]]));
     
                    }
                    if (zbuffercamera[x][y] > P[2]) {// deve-se pintar
                            zbuffercamera[x][y] = P[2];
     
                            normal = new double[3];
                            for (int i = 0; i < 3; i++) {
                                    normal = Algb.soma(normal,
                                                    Algb.prodByEscalar(coef[i], NormPontos[triangulo[i]]));
     
                            }
                            cor = new float[3];
     
                            for (int i = 0; i < 3; i++) {
                                    cor[i] = 0;
                            }
                            normal = Algb.normalize(normal);// tenho a normal do ponto
     
                            double[] light = Algb.normalize(Algb.sub(Plight, P));// tenho o vetor
                                                                                                                                    // direcionado
                                                                                                                                    // para a luz
     
                            double[] VObservador = Algb.sub(zero, P);
                            // teste = P;
                            // System.out.println(teste[0]+" "+teste[1]+" "+teste[2]);
                            //
                            VObservador = Algb.normalize(VObservador);
                            if (Algb.prodEscalar(normal, VObservador) < 0)
                                    normal = Algb.sub(zero, normal);
                            double N_L = Algb.prodEscalar(normal, light);
                            double[] R = new double[3];
                            R = Algb.sub(Algb.prodByEscalar(2*Algb.prodEscalar(light,normal), normal), light);
                            //R = 2*(n.l)n - l
    //                     
    //                      R[0] = 2
    //                       * (Algb.prodEscalar(light, N) / (Algb.getNorma(light) * Algb
    //                       .getNorma(N))) * (N[0] - light[0]);
    //                       R[1] = 2
    //                       * (Algb.prodEscalar(light, N) / (Algb.getNorma(light) * Algb
    //                       .getNorma(N))) * (N[1] - light[1]);
    //                       R[2] = 2
    //                       * (Algb.prodEscalar(light, N) / (Algb.getNorma(light) * Algb
    //                       .getNorma(N))) * (N[2] - light[2]);
    //                       R = Algb.normalize(R);
     
                            double V_R = Algb.prodEscalar(R, VObservador);
     
                            for (int i = 0; i < 3; i++) {
                                    float cor1 = (float) (Ka * Ia[i] + Il[i]);
                                    float ambiente = (float) (Ka * Ia[i]);
                                    float difusa = (float) Math.max((Il[i] * Kd * N_L), 0);
                                    // System.out.println(R[i]);
                                    float especular = (float) (Il[i] * Ks * V_R);
                                    // System.out.println("ambiente: "+ambiente+" difusa: "+difusa+
                                    // " especular :" +especular);
                                    cor[i] = (float) (ambiente+difusa+especular);
                            }
                            matCor[x][y] = cor;
                            // matCor[x][y] = new float[]{1.f, 1.f, 1.f,};
                    }
     
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
		CRSMatrix a = new CRSMatrix(new double[][] { { x1, x2, x3 },
				{ y1, y2, y3 }, { 1.0, 1.0, 1.0 } });
		double[] b2 = { x, y, 1.0 };
		BasicVector b = new BasicVector(b2);
		LinearSystemSolver solver = a
				.withSolver(LinearAlgebra.FORWARD_BACK_SUBSTITUTION);
		Vector resp = solver.solve(b, LinearAlgebra.SPARSE_FACTORY);
		return new double[] { resp.get(0), resp.get(1), resp.get(2) };

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
		System.out.println("Pl :" + Plight[0] + " " + Plight[1] + " "
				+ Plight[2]);
	}

	//
	// static void lerAtributos() {
	// arq = new Arquivo("atributos.txt", "lixoAtr.txt");
	// Ia[0] = arq.readInt();
	// Ia[1] = arq.readInt();
	// Ia[2] = arq.readInt();
	// Ka = arq.readDouble();
	// Od[0] = arq.readDouble() / 255.0f;
	// Od[1] = arq.readDouble() / 255.0f;
	// Od[2] = arq.readDouble() / 255.0f;
	// Kd = arq.readDouble();
	// Ks = arq.readDouble();
	// n = arq.readDouble();
	// Il[0] = arq.readDouble() / 255.0f;
	// Il[1] = arq.readDouble() / 255.0f;
	// Il[2] = arq.readDouble() / 255.0f;
	// Plight[0] = arq.readDouble();
	// Plight[1] = arq.readDouble();
	// Plight[2] = arq.readDouble();
	//
	// }
	static void lerAtributos() {
		arq = new Arquivo("atributos.txt", "lixoAtr.txt");
		Plight[0] = arq.readDouble();
		Plight[1] = arq.readDouble();
		Plight[2] = arq.readDouble();
		Ka = arq.readDouble();
		Ia[0] = arq.readDouble() / 255.0f;
		Ia[1] = arq.readDouble() / 255.0f;
		Ia[2] = arq.readDouble() / 255.0f;
		Kd = arq.readDouble();
		Od[0] = arq.readDouble() / 255.0f;
		Od[1] = arq.readDouble() / 255.0f;
		Od[2] = arq.readDouble() / 255.0f;
		Ks = arq.readDouble();
		Il[0] = arq.readDouble() / 255.0f;
		Il[1] = arq.readDouble() / 255.0f;
		Il[2] = arq.readDouble() / 255.0f;
		n = arq.readDouble();
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