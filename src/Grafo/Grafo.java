package Grafo;

import java.util.ArrayList;
import java.util.LinkedList;

public class Grafo<E> {

    ArrayList<No<E>> nos;

    public class CaminhoDijkstra {

        No<E> no;
        No<E> caminho;
        double valor;
        boolean completo;

        public CaminhoDijkstra(No<E> no, No<E> caminho, double valor, boolean completo) {
            this.no = no;
            this.caminho = caminho;
            this.valor = valor;
            this.completo = completo;
        }

    }

    public Grafo() {
        nos = new ArrayList<>();
    }

    public No<E> inserirNo(No<E> no) {
        nos.add(no);
        return no;
    }

    public void conectar(No<E> noA, No<E> noB) {
        noA.conectarA(noB);
    }

    public void conectarEspelho(No<E> noA, No<E> noB) {
        noA.conectarEspelho(noB);
    }

    public void conectar(No<E> noA, No<E> noB, double valor) {
        noA.conectarA(noB, valor);
    }

    public void conectarEspelho(No<E> noA, No<E> noB, double valor) {
        noA.conectarEspelho(noB, valor);
    }

    public void remover(No<E> no) {
        nos.remove(no);
        for (No<E> n : nos) {
            n.remover(no);
        }
    }

    public int regular() {
        int t = -1;
        if (nos.size() == 0) {
            return t;
        } else {
            t = nos.get(0).ligadoA.size();
        }

        for (No<E> no : nos) {
            if (no.ligadoA.size() != t) {
                return -1;
            }
        }
        return t;
    }

    public boolean completo() {
        int t = nos.size() - 1;
        for (No<E> no : nos) {
            if (no.ligadoA.size() < t) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<No<E>> buscaLargura(No<E> no) {
        ArrayList<No<E>> retorno = new ArrayList<>();
        LinkedList<No<E>> fila = new LinkedList<>();

        for (No<E> n : nos) {
            n.visitado = false;
        }
        fila.add(no);
        retorno.add(no);

        no.visitado = true;

        while (fila.size() > 0) {
            No<E> novo = fila.getLast();
            fila.removeLast();
            for (Conexao<E> n : novo.ligadoA) {
                if (!n.no.visitado) {
                    n.no.visitado = true;
                    fila.addFirst(n.no);
                    retorno.add(n.no);
                }
            }
        }

        return retorno;
    }

    public boolean conexo(No<E> no) {
        if (nos.size() > 0) {
            return buscaLargura(no).size() == nos.size();
        }
        return false;
    }

    public ArrayList<CaminhoDijkstra> dijkstraCompleto() {
        ArrayList<CaminhoDijkstra> tabela = new ArrayList();
        if (nos.size() > 0) {
            tabela.add(new CaminhoDijkstra(nos.get(0), nos.get(0), 0, false));
            for (int i = 1; i < nos.size(); i++) {
                tabela.add(new CaminhoDijkstra(nos.get(i), null, Double.MAX_VALUE, false));
            }

            boolean completo = false;

            while (!completo) {
                completo = true;
                double menorValor = Double.MAX_VALUE;
                int posicaoMenor = 0;
                for (int i = 0; i < tabela.size(); i++) {
                    if (!tabela.get(i).completo && tabela.get(i).valor < menorValor) {
                        posicaoMenor = i;
                        menorValor = tabela.get(i).valor;
                        completo = false;
                    }
                }

                tabela.get(posicaoMenor).completo = true;

                for (Conexao<E> conexao : tabela.get(posicaoMenor).no.ligadoA) {
                    for (CaminhoDijkstra cam : tabela) {
                        if (cam.no == conexao.no) {
                            if (tabela.get(posicaoMenor).valor + conexao.valor < cam.valor) {
                                cam.valor = tabela.get(posicaoMenor).valor + conexao.valor;
                                cam.caminho = tabela.get(posicaoMenor).no;
                            }
                        }
                    }
                }
            }
        }
        return tabela;
    }

    public ArrayList<No> ordenacaoTopologica() {
        ArrayList<No> lista = new ArrayList<>();

        boolean existeNaoVisitado = true;
        while (existeNaoVisitado) {
            existeNaoVisitado = false;
            for (No<E> no : nos) {
                if (no.grauDeEntrada == 0) {
                    existeNaoVisitado = true;
                    lista.add(no);
                    for (Conexao<E> conexao : no.ligadoA) {
                        conexao.no.grauDeEntrada--;
                    }
                }
                break;
            }
        }

        for (No<E> no : nos) {
            no.resetarGrau();
        }
        
        return lista;
    }
}