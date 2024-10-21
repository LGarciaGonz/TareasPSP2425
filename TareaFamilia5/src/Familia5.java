import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**********************************************************************************************************************************************
 *   APLICACIÓN: "Familia 5"                                                                                                            *
 **********************************************************************************************************************************************
 *   PROGRAMACIÓN DE SERVICIOS Y PROCESOS 2DAM                                                                                                *
 **********************************************************************************************************************************************
 *   @author Laia García                                                                                                                      *
 *   @version 1.0 - Versión inicial del algoritmo.                                                                                            *
 *   @since 21OCT2024                                                                                                                         *
 **********************************************************************************************************************************************
 *   COMENTARIOS:                                                                                                                             *
 *        - Uso de Executors y Callable().                                                                                                    *
 *        - Ejercicio Tema 1 - TAREA 1 - Familia 5                                                                                            *
 **********************************************************************************************************************************************/


class Familia5 {

    // DECLARACIÓN DE CONSTANTES
    public static final int TOPE_RANGO = 100;
    public static final int NUMERO_HILOS = 5;
    public static final int NUMERO5 = 5;

    // COMPROBAR SI EL NÚMERO ES MÚLTIPLO DE 5.
    static class esMultiplo implements Callable<Integer> {

        private int a_Numero;

        public esMultiplo(int p_Numero) {
            this.a_Numero = p_Numero;
        }

        @Override
        public Integer call() throws Exception {

            // Declaración de variables.
            int l_NumeroMultiplo = 0;

            // Comprobar que el número es múltiplo de 5.
            if (a_Numero % NUMERO5 == 0) l_NumeroMultiplo = a_Numero;

            return l_NumeroMultiplo;
        }

    }

    static class MultiplosSuma implements Callable<Integer> {

        private int a_Numero;

        public MultiplosSuma(int p_Numero) {
            this.a_Numero = p_Numero;
        }

        public Integer call() throws Exception {

            // DECLARACIÓN DE VARIABLES.
            int l_Numero = 0;
            int l_Digito1 = 0;
            int l_DigitosRestantes = a_Numero;
            int l_Suma = 0;

            while (l_DigitosRestantes != 0) {
                l_Digito1 = l_DigitosRestantes % 10;
                l_DigitosRestantes /= 10;
                l_Suma += (l_Digito1 + l_DigitosRestantes);
            }

            if (l_Suma % 5 == 0) l_Numero = a_Numero;

            return l_Numero;
        }
    }

    public static void main(String[] args) {

        ThreadPoolExecutor l_Executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(NUMERO_HILOS);
        esMultiplo l_Tarea1 = null;
        MultiplosSuma l_Tarea2 = null;
        List<esMultiplo> l_ListaTareas = new ArrayList<esMultiplo>();
        List<MultiplosSuma> l_ListaTareasSuma = new ArrayList<MultiplosSuma>();
        List<Future<Integer>> l_ListaResultados = null;
        List<Future<Integer>> l_ListaResultados2 = null;
        List<Integer> l_Resultado = new ArrayList<>();
        List<Integer> l_Resultado2 = new ArrayList<>();
        int l_Contador = 0;

        System.out.println("\n ----[ Lista de números múltiplos de 5, cuya suma de dígitos es además múltiplo de 5 ]----\n");

        // Creamos las tareas (objetos de clase Multiplos) y las añadimos a la colección.
        for (l_Contador = 0; l_Contador <= TOPE_RANGO; l_Contador++) {

            l_Tarea1 = new esMultiplo(l_Contador);

            // Comprobar que el número es múltiplo antes de añadirlo a la lista.
            if (l_Tarea1.a_Numero % 5 == 0) {
                l_ListaTareas.add(l_Tarea1);
            }

            l_Tarea2 = new MultiplosSuma(l_Contador);
            l_ListaTareasSuma.add(l_Tarea2);

        } // for()

        try {
            // Le decimos al gestor de tareas que las tareas de la colección ya son elegibles, recogemos los resultados en otra colección, y apagamos el ejecutor.
            l_ListaResultados = l_Executor.invokeAll(l_ListaTareas);
            l_ListaResultados2 = l_Executor.invokeAll(l_ListaTareasSuma);
            l_Executor.shutdown();

            // Recuperamos uno a uno los resultados de cada tarea acabada correctamente y los mostramos.
            for (l_Contador = 0; l_Contador < l_ListaResultados.size(); l_Contador++) {
                l_Resultado.add(l_ListaResultados.get(l_Contador).get());
            } // for()


            // Recuperamos uno a uno los resultados de cada tarea acabada correctamente y los mostramos.
            for (l_Contador = 0; l_Contador < l_ListaResultados2.size(); l_Contador++) {
                l_Resultado2.add(l_ListaResultados2.get(l_Contador).get());
            } // for()


            // Mostrar los números que coinciden en ambas listas.
            for (l_Contador = 0; l_Contador < l_Resultado.size(); l_Contador++) {

                int l_Numero = l_Resultado.get(l_Contador);
                if (l_Resultado2.contains(l_Numero)) {
                    System.out.println(l_Numero);
                } // if()

            } // for()

        } catch (InterruptedException | ExecutionException l_Excepcion) {
            System.out.println("ERROR: Ha fallado un get() con error: " + l_Excepcion);
        } // try() catch()

    }

}