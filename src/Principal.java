/*
 * Universidade Federal de Santa Catarina - UFSC
 * Departamento de Informática e Estatística - INE
 * Programa de Pós-Graduação em Ciências da Computação - PROPG
 * Disciplinas: Projeto e Análise de Algoritmos
 * Prof Alexandre Gonçalves da Silva 
 *
 * Baseado nos slides 111 da aula do dia 20/10/2017 
 *
 * Página 459 Thomas H. Cormen 3a Ed
 *
 * Árvore Geradora Mínima(MST) com o Algoritmo de Kruskal
 */

/**
 * @author Osmar de Oliveira Braz Junior
 */

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Principal {        
  
    //Vetor dos pais de um vértice
    static int[] pi;         
    //d[x] armazena o instante de descoberta de x.
    //Cada nó x possui um “posto” rank[x] que ´e um limitante superior para a altura de x.
    //Equivale a d[x]
    static int[] rank;

    /**
     * Encontra a raiz de um conjunto.
     * 
     * Página 415 Thomas H. Cormen 3a Ed
     * 
     * @param x Elemento a ser procurado.
     * @return a raiz do conjunto
     */
    public static int findSet(int x) {        
        int p = pi[x];
        if (x != p) {
            p = findSet(p);
        }
        return p;
    }    

    /**
     * Link realiza a ligação de dois elementos a serem unidos.
     * 
     * Página 415 Thomas H. Cormen 3a Ed
     * 
     * @param x Primeiro elemento
     * @param y Segundo elemento
     */
    public static void link(int x, int y){
        if (rank[x] > rank[y]) {
            pi[y] = x;
        } else  {
            pi[x] = y;
            if (rank[y] == rank[x]) {
                rank[y] = rank[y] + 1;
            }
        }
    }
    
    /**
     * Realiza a união de dois elementos.
     * 
     * Página 415 Thomas H. Cormen 3a Ed
     * 
     * @param x Primeiro elemento
     * @param y Segundo elemento
     */
    public static void union(int x, int y) {        
        link(findSet(x),findSet(y));        
    }
              
    /**
     * Cria o conjunto unitário de x.
     * 
     * Página 415 Thomas H. Cormen 3a Ed
     * 
     * @param x conjunto a ser criado
     */
    public static void makeSet(int x){
        pi[x] = x;
        rank[x] = 0;
    }
 
    /**
     * Troca um número que representa a posição pela vértice do grafo.
     *
     * @param i Posição da letra
     * @return Uma String com a letra da posição i
     */
    public static String trocar(int i) {
        String letras = "abcdefghi";        
        if ((i >=0) && (i<=letras.length())) {
            return letras.charAt(i) + "";
        } else {
            return "-";
        }
    }

    /**
     * Troca a letra pela posição na matriz de adjacência
     *
     * @param v Letra a ser troca pela posição
     * @return Um inteiro com a posição da letra no grafo
     */
    public static int destrocar(char v) {
        String letras = "abcdefghi";
        int pos = -1;
        for (int i = 0; i < letras.length(); i++) {
            if (letras.charAt(i) == v) {
                pos = i;
            }
        }
        return pos;
    }

     /**
     * Exibe a lista de arestas do menor custo
     *
     * @param Lista das arestas a ser exibido o caminho e o custo
     */
    public static void mostrarCaminho(int[][] A, int n) {
        //Guarda o custo do caminho
        int custo = 0;
        //Percorre os as arestas de A
        //O laço começa em 0 e desconta um elemento pois ele se repete
        for (int v = 0; v < n-1; v++) {           
           System.out.println(trocar(A[v][0]) + " -> " + trocar(A[v][1]) + " custo: " + A[v][2]);                              
           custo = custo + A[v][2];        }
        System.out.println("Custo Total:" + custo);
    }

    /**
     * Gera um vetor de arestas e pesos.
     * @param G Matriz de adjacência do grafo
     * @return Um vetor de arestas e pesos.
     */   
    public static List getMatrizVertices(int[][] G){
        int n = G.length;
        List vertices = new LinkedList();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                //Somente para o triângulo superior
                if ((i<j)&&(G[i][j]!=0)){
                    //Cria um vetor de 3 elementos para conter                     
                    //[0]=u(origem), [1]=v(destino), [2]=w(peso)
                    vertices.add(new int[]{i,j,G[i][j]});
                }            
            }            
        }
        return vertices;
    }    
    
    /**
     * Executa o algoritmo de Kruskal para Ãrvore geradora Mínima.
     * 
     * Complexidade do algoritmo é O(E log E) = O(E log V)
     * 
     * @param G Matriz de indicência da árvore      
     * @return Vetor com a lista das arestas de menor custo
     */ 
    public static int[][] algoritmoKruskal(int[][] G) {
        //Vetor das Arestas de retorno
        int[][] A;
         
        //Converte a matriz em uma lista de vértices
        List arestas = getMatrizVertices(G); 
        
        //Quantidade de arestas da lista
        int n = arestas.size();
                                     
        //Inicializa os vetores
        A = new int[n][]; //Vetor de retorno
        pi = new int[n];
        rank = new int[n];
        //Cria a árvore para cada vértice
        for (int v = 0; v < n; v++) {
            makeSet(v);                
        }
        //Ordene as arestas em ordem não-decrescente de peso
        Collections.sort(arestas, (int[] e1, int[] e2) -> {
            if (e1[2] < e2[2]) return -1;
            if (e1[2] > e2[2]) return 1;
            return 0;
        });                                                 // O(E lg E)
               
        int linha = 0;
        //Para cada aresta na ordem anterior
        for(int i=0; i<arestas.size();i++){            
            int[] vertice = (int[])arestas.get(i);
            int u = findSet(vertice[0]);
            int v = findSet(vertice[1]);
            if (u != v) { //Estão em árvores diferentes
                A[linha] = vertice;
                linha = linha + 1;                
                union(u,v);//Unir as duas árvores
            }
        }
        return A;        
     }    

    public static void main(String args[]) {
        //Matriz de incidência para um grafo direcionado     
        //Grafo do slide 56 de 20/10/2017
               
        int G[][] =
               //a  b  c  d  e  f  g  h  i 
               {{0, 4, 0, 0, 0, 0, 0, 8, 0}, //a
                {4, 0, 8, 0, 0, 0, 0, 11,0}, //b
                {0, 8, 0, 7, 0, 4, 0, 0, 2}, //c
                {0, 0, 7, 0, 9, 14,0, 0, 0}, //d
                {0, 0, 0, 9, 0, 10,0, 0, 0}, //e
                {0, 0, 4, 14,10,0, 2, 0, 0}, //f
                {0, 0, 0, 0, 0, 2, 0, 1, 6}, //g
                {8, 11,0, 0, 0, 0, 1, 0, 7}, //h
                {0, 0, 2, 0, 0, 0, 6, 7, 0}};//i

        //Quantidade de vértices
        int n = G.length;
        
        System.out.println(">>> Árvore Geradora Mínima - Algoritmo de Kruskal <<<");

        //Executa o algoritmo
        int[][] g = algoritmoKruskal(G);

        //Mostra o menor custo
        mostrarCaminho(g, n);
    }
}