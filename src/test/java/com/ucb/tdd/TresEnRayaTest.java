package com.ucb.tdd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class TresEnRayaTest {

    private TresEnRaya juego;

    @Before
    public void setUp() {
        juego = new TresEnRaya();
    }

    private String jugarSecuencia(int[][] jugadas) throws Exception {
        String resultado = null;
        for (int[] j : jugadas) {
            resultado = juego.jugar(j[0], j[1]);
        }
        return resultado;
    }

    @Test
    public void primerTurnoEsDeX() {
        assertEquals('X', juego.siguienteTurno());
    }

    @Test
    public void losTurnosSeAlternanEntreXyMas() throws Exception {
        juego.jugar(1, 1);
        assertEquals('+', juego.siguienteTurno());
        juego.jugar(2, 2);
        assertEquals('X', juego.siguienteTurno());
    }

    @Test
    public void coordenadaFueraDelTableroLanzaExcepcion() {
        int[][] invalidas = {{0, 1}, {4, 1}, {1, 0}, {1, 4}, {-1, 2}};
        for (int[] c : invalidas) {
            try {
                juego.jugar(c[0], c[1]);
                fail("Debia fallar con " + c[0] + "," + c[1]);
            } catch (Exception e) {
                assertEquals('X', juego.siguienteTurno());
            }
        }
    }

    @Test(expected = Exception.class)
    public void casillaOcupadaLanzaExcepcion() throws Exception {
        juego.jugar(1, 3);
        juego.jugar(1, 3);
    }

    @Test
    public void sinLineaCompletaNoHayGanador() throws Exception {
        assertEquals("No hay ganador", juego.jugar(2, 1));
    }

    @Test
    public void xGanaConUnaFilaCompleta() throws Exception {
        assertEquals("X es el ganador",
                jugarSecuencia(new int[][] {{1, 1}, {2, 1}, {1, 2}, {2, 2}, {1, 3}}));
    }

    @Test
    public void masGanaConUnaColumnaCompleta() throws Exception {
        assertEquals("+ es el ganador",
                jugarSecuencia(new int[][] {{1, 1}, {1, 2}, {2, 1}, {2, 2}, {3, 3}, {3, 2}}));
    }

    @Test
    public void xGanaConLaDiagonalPrincipal() throws Exception {
        assertEquals("X es el ganador",
                jugarSecuencia(new int[][] {{1, 1}, {1, 2}, {2, 2}, {1, 3}, {3, 3}}));
    }

    @Test
    public void masGanaConLaDiagonalOpuesta() throws Exception {
        assertEquals("+ es el ganador",
                jugarSecuencia(new int[][] {{1, 1}, {1, 3}, {1, 2}, {2, 2}, {2, 1}, {3, 1}}));
    }

    @Test
    public void tresFichasIgualesSinAlinearNoGanan() throws Exception {
        assertEquals("No hay ganador",
                jugarSecuencia(new int[][] {{1, 1}, {2, 1}, {1, 3}, {2, 2}, {3, 2}}));
    }

    @Test
    public void ganarEnLaUltimaJugadaDevuelveGanador() throws Exception {
        // la novena jugada de X completa la diagonal opuesta (1,3) (2,2) (3,1) con el tablero lleno
        assertEquals("X es el ganador",
                jugarSecuencia(new int[][] {
                        {1, 1}, {1, 2}, {1, 3}, {2, 1}, {3, 1},
                        {3, 2}, {2, 3}, {3, 3}, {2, 2}}));
    }

    @Test
    public void lasCasillasEmpiezanVacias() throws Exception {
        for (int fila = 1; fila <= 3; fila++) {
            for (int columna = 1; columna <= 3; columna++) {
                assertEquals('0', juego.casillaEn(fila, columna));
            }
        }
    }

    @Test
    public void casillaEnReflejaQuienJugoYEnQueFilaYColumna() throws Exception {
        juego.jugar(2, 3);
        juego.jugar(1, 1);
        assertEquals('X', juego.casillaEn(2, 3));
        assertEquals('+', juego.casillaEn(1, 1));
        assertEquals('0', juego.casillaEn(3, 2));
    }

    @Test(expected = Exception.class)
    public void casillaEnFueraDelTableroLanzaExcepcion() throws Exception {
        juego.casillaEn(4, 1);
    }

    @Test
    public void jugadasRealizadasCuentaSoloLasJugadasValidas() throws Exception {
        assertEquals(0, juego.jugadasRealizadas());
        juego.jugar(1, 1);
        juego.jugar(2, 2);
        assertEquals(2, juego.jugadasRealizadas());
        try {
            juego.jugar(9, 9);
        } catch (Exception e) {
            assertEquals(2, juego.jugadasRealizadas());
        }
    }

    @Test
    public void unaJugadaFueraDelTableroNoOcupaNingunaCasilla() {
        try {
            juego.jugar(1, 7);
            fail("Debia lanzar excepcion");
        } catch (Exception e) {
            assertEquals(0, juego.jugadasRealizadas());
        }
    }

    @Test
    public void reiniciarLimpiaElTableroYDevuelveElTurnoAX() throws Exception {
        juego.jugar(1, 1);
        juego.jugar(2, 2);
        juego.reiniciar();
        assertEquals('X', juego.siguienteTurno());
        assertEquals(0, juego.jugadasRealizadas());
        assertEquals('0', juego.casillaEn(1, 1));
        assertEquals("No hay ganador", juego.jugar(1, 1));
    }

    @Test
    public void reiniciarDespuesDeUnaVictoriaEmpiezaUnaPartidaNueva() throws Exception {
        jugarSecuencia(new int[][] {{1, 1}, {2, 1}, {1, 2}, {2, 2}, {1, 3}});
        juego.reiniciar();
        assertEquals("No hay ganador", juego.jugar(1, 3));
        assertEquals('X', juego.casillaEn(1, 3));
    }

    @Test
    public void dosPartidasSonIndependientes() throws Exception {
        TresEnRaya otro = new TresEnRaya();
        juego.jugar(1, 1);
        assertEquals('0', otro.casillaEn(1, 1));
        assertEquals('X', otro.siguienteTurno());
    }

    @Test
    public void toStringMuestraElTablero() throws Exception {
        assertEquals("0|0|0\n0|0|0\n0|0|0", juego.toString());
        juego.jugar(1, 1);
        juego.jugar(2, 3);
        assertEquals("X|0|0\n0|0|+\n0|0|0", juego.toString());
    }

    @Test
    public void xGanaConCadaUnaDeLasOchoLineas() throws Exception {
        int[][][] lineas = {
                {{1, 1}, {1, 2}, {1, 3}}, {{2, 1}, {2, 2}, {2, 3}}, {{3, 1}, {3, 2}, {3, 3}},
                {{1, 1}, {2, 1}, {3, 1}}, {{1, 2}, {2, 2}, {3, 2}}, {{1, 3}, {2, 3}, {3, 3}},
                {{1, 1}, {2, 2}, {3, 3}}, {{1, 3}, {2, 2}, {3, 1}}};
        for (int[][] linea : lineas) {
            TresEnRaya partida = new TresEnRaya();
            int[][] relleno = celdasFueraDe(linea);
            partida.jugar(linea[0][0], linea[0][1]);
            partida.jugar(relleno[0][0], relleno[0][1]);
            partida.jugar(linea[1][0], linea[1][1]);
            partida.jugar(relleno[1][0], relleno[1][1]);
            assertEquals("X es el ganador", partida.jugar(linea[2][0], linea[2][1]));
        }
    }

    private int[][] celdasFueraDe(int[][] linea) {
        int[][] libres = new int[2][];
        int encontradas = 0;
        for (int fila = 1; fila <= 3 && encontradas < 2; fila++) {
            for (int columna = 1; columna <= 3 && encontradas < 2; columna++) {
                boolean enLinea = false;
                for (int[] c : linea) {
                    enLinea |= c[0] == fila && c[1] == columna;
                }
                if (!enLinea) {
                    libres[encontradas++] = new int[] {fila, columna};
                }
            }
        }
        return libres;
    }
}
