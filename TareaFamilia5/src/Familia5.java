import java.util.ArrayList; // Importa la clase ArrayList para manejar listas dinámicas
import java.util.List; // Importa la interfaz List
import java.util.concurrent.*; // Importa las clases necesarias para la programación concurrente

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

    // CLASE PARA COMPROBAR SI EL NÚMERO ES MÚLTIPLO DE 5
    static class esMultiplo implements Callable<Integer> {

        private int a_Numero;

        // Constructor que inicializa el número
        public esMultiplo(int p_Numero) {
            this.a_Numero = p_Numero;
        }

        @Override
        public Integer call() throws Exception {
            // Declaración de variables.
            int l_NumeroMultiplo = 0; // Variable para almacenar el número múltiplo

            // Comprobar que el número es múltiplo de 5.
            if (a_Numero % NUMERO5 == 0) {
                l_NumeroMultiplo = a_Numero; // Asignar el número si es múltiplo de 5
            }

            return l_NumeroMultiplo; // Retornar 0 si no es múltiplo
        }
    }

    // CLASE PARA CALCULAR LA SUMA DE LOS DÍGITOS DE UN NÚMERO
    static class MultiplosSuma implements Callable<Integer> {

        private int a_Numero;

        // Constructor que inicializa el número
        public MultiplosSuma(int p_Numero) {
            this.a_Numero = p_Numero;
        }

        public Integer call() throws Exception {

            // Declaración de variables.
            int l_Numero = 0;
            int l_Digito1;
            int l_DigitosRestantes = a_Numero;
            int l_Suma = 0;

            // Calcular la suma de los dígitos
            while (l_DigitosRestantes != 0) {
                l_Digito1 = l_DigitosRestantes % 10; // Obtener el último dígito
                l_Suma += l_Digito1; // Sumar el dígito actual a la suma total
                l_DigitosRestantes /= 10; // Quitar el último dígito del número
            }

            // Verificar si la suma es múltiplo de 5
            if (l_Suma % 5 == 0) {
                l_Numero = a_Numero; // Asignar el número si la suma de sus dígitos es múltiplo de 5
            }

            return l_Numero; // Retornar 0 si no es múltiplo
        }
    }

    public static void main(String[] args) {
        // Crear un ejecutor con un número fijo de hilos
        ThreadPoolExecutor l_Executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(NUMERO_HILOS);
        List<esMultiplo> l_ListaTareas = new ArrayList<>(); // Lista para almacenar tareas de múltiplos
        List<MultiplosSuma> l_ListaTareasSuma = new ArrayList<>(); // Lista para almacenar tareas de suma
        List<Future<Integer>> l_ListaResultados; // Lista para almacenar los resultados de las tareas de múltiplos
        List<Future<Integer>> l_ListaResultados2; // Lista para almacenar los resultados de las tareas de suma
        List<Integer> l_Resultado = new ArrayList<>(); // Lista para almacenar los resultados de múltiplos
        List<Integer> l_Resultado2 = new ArrayList<>(); // Lista para almacenar los resultados de suma

        // Mensaje de inicio
        System.out.println("\n ----[ Múltiplos de 5 / Suma de dígitos múltiplo de 5 ]----");

        // Crear tareas para evaluar los números en el rango
        for (int l_Contador = 0; l_Contador <= TOPE_RANGO; l_Contador++) {

            if (l_Contador % 5 == 0) l_ListaTareas.add(new esMultiplo(l_Contador)); // Agregar tarea para múltiplos de 5

            l_ListaTareasSuma.add(new MultiplosSuma(l_Contador)); // Agregar tarea para la suma de dígitos
        }

        try {
            // Ejecutar todas las tareas de múltiplos y obtener los resultados
            l_ListaResultados = l_Executor.invokeAll(l_ListaTareas);

            // Ejecutar todas las tareas de suma y obtener los resultados
            l_ListaResultados2 = l_Executor.invokeAll(l_ListaTareasSuma);
            l_Executor.shutdown(); // Apagar el ejecutor

            // Recuperar los resultados de las tareas de múltiplos
            for (Future<Integer> l_Future : l_ListaResultados) {
                l_Resultado.add(l_Future.get()); // Agregar el resultado a la lista de resultados
            }

            // Recuperar los resultados de las tareas de suma
            for (Future<Integer> l_Future : l_ListaResultados2) {
                l_Resultado2.add(l_Future.get()); // Agregar el resultado a la lista de resultados
            }

            // Comparar las listas de resultados y mostrar los números que coinciden
            for (Integer l_Numero : l_Resultado) {
                if (l_Resultado2.contains(l_Numero)) { // Si el número está en ambas listas
                    System.out.println("⫸ " + l_Numero); // Imprimir el número
                }
            }

        } catch (InterruptedException | ExecutionException l_Excepcion) {
            // Manejo de excepciones si ocurre un error al recuperar resultados
            System.out.println("ERROR: Ha fallado un get() con error: " + l_Excepcion);
        }
    }
}
