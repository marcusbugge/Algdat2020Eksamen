package no.oslomet.cs.algdat.Eksamen;


import java.util.*;

public class EksamenSBinTre<T> {
    private static final class Node<T>   // en indre nodeklasse
    {
        private T verdi;                   // nodens verdi
        private Node<T> venstre, høyre;    // venstre og høyre barn
        private Node<T> forelder;          // forelder

        // konstruktør
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder) {
            this.verdi = verdi;
            venstre = v;
            høyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder)  // konstruktør
        {
            this(verdi, null, null, forelder);
        }

        @Override
        public String toString() {
            return "" + verdi;
        }

    } // class Node

    private Node<T> rot;                            // peker til rotnoden
    private int antall;                             // antall noder
    private int endringer;                          // antall endringer

    private final Comparator<? super T> comp;       // komparator

    public EksamenSBinTre(Comparator<? super T> c)    // konstruktør
    {
        rot = null;
        antall = 0;
        comp = c;
    }

    public boolean inneholder(T verdi) {
        if (verdi == null) return false;

        Node<T> p = rot;

        while (p != null) {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else return true;
        }

        return false;
    }

    public int antall() {
        return antall;
    }

    public String toStringPostOrder() {
        if (tom()) return "[]";

        StringJoiner s = new StringJoiner(", ", "[", "]");

        Node<T> p = førstePostorden(rot); // går til den første i postorden
        while (p != null) {
            s.add(p.verdi.toString());
            p = nestePostorden(p);
        }

        return s.toString();
    }

    public boolean tom() {
        return antall == 0;
    }

    public boolean leggInn(T verdi) {
        Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

        Node<T> p = rot, q = null;               // p starter i roten
        int cmp = 0;                             // hjelpevariabel

        while (p != null)       // fortsetter til p er ute av treet
        {
            q = p;                                 // q er forelder til p
            cmp = comp.compare(verdi,p.verdi);     // bruker komparatoren
            p = cmp < 0 ? p.venstre : p.høyre;     // flytter p
        }

        // p er nå null, dvs. ute av treet, q er den siste vi passerte

        p = new Node<>(verdi, q);                   // oppretter en ny node

        if (q == null) rot = p;                  // p blir rotnode
        else if (cmp < 0) q.venstre = p;         // venstre barn til q
        else q.høyre = p;                        // høyre barn til q

        antall++;                                // én verdi mer i treet
        return true;                             // vellykket innlegging
    }

    public boolean fjern(T verdi) {
        if (verdi == null) return false;  // treet har ingen nullverdier

        Node<T> p = rot, q = null;   // q skal være forelder til p

        while (p != null)            // leter etter verdi
        {
            int cmp = comp.compare(verdi,p.verdi);      // sammenligner
            if (cmp < 0) { q = p; p = p.venstre; }      // går til venstre
            else if (cmp > 0) { q = p; p = p.høyre; }   // går til høyre
            else break;    // den søkte verdien ligger i p
        }
        if (p == null) return false;   // finner ikke verdi

        if (p.venstre == null || p.høyre == null)  // Tilfelle 1) og 2)
        {
            Node<T> b = p.venstre != null ? p.venstre : p.høyre;  // b for barn
            if (p == rot) {
                rot = b;
                if (rot != null) {
                    rot.forelder = null;
                }
            }
            else if (p == q.venstre) {
                q.venstre = b;
                if (b != null) {
                    b.forelder = q;
                }
            }
            else {
                q.høyre = b;
                if (b != null) {
                    b.forelder = q;
                }
            }
        }
        else  // Tilfelle 3)
        {
            Node<T> s = p, r = p.høyre;   // finner neste i inorden
            while (r.venstre != null)
            {
                s = r;    // s er forelder til r
                r = r.venstre;
            }

            p.verdi = r.verdi;   // kopierer verdien i r til p

            if (s != p) s.venstre = r.høyre;
            else s.høyre = r.høyre;
        }

        antall--;   // det er nå én node mindre i treet
        return true;
    }

    public int fjernAlle(T verdi) {
        int antallNoderFjernet = 0;

        Node<T> node = førstePostorden(rot);
        while (node != null) {
            System.out.println("Verdi: "+node.verdi);
            if (node.verdi.equals(verdi)) {
                fjern(verdi);
            }

            antallNoderFjernet++;
            node = nestePostorden(node);
        }
        return antallNoderFjernet;
    }

    public int antall(T verdi) {

        //Oppretter en tellevariabel av typen int

        int teller = 0;

        //Setter node til å være øverste node i det binære treet.
        Node<T> node = rot;

        // while-løkke som går så lenge det er flere noder igjen

        while(node != null) {

           if (comp.compare(node.verdi, verdi) == 0) {
               node = node.høyre;
               teller++;
           }
           else if (comp.compare(node.verdi, verdi) < 0) {
               node = node.høyre;
           }
           else {
               node = node.venstre;
           }
        }
        return teller;
    }

    public void nullstill() {

        // Looper igjennom hele treet og fjerner begge barn - hvis de har.
        Node<T> node = førstePostorden(rot);
        while (node != null) {

            if (node == rot) {
                rot = null;
            }
            else if (node.venstre != null) {
                node.venstre = null;
            }
            else if (node.høyre != null) {
                node.høyre = null;
            }

            node = nestePostorden(node);
            antall--;
        }
    }

    private static <T> Node<T> førstePostorden(Node<T> p) {

        while (p != null) {
            if (p.venstre != null) {
                p = p.venstre;
            }
            else if (p.høyre != null) {
                p = p.høyre;
            }
            else {
                return p;
            }
        }
        return null;
    }

    private static <T> Node<T> nestePostorden(Node<T> p) {
        Node<T> node = p;
        // Hvis p ikke har en forelder er denne noden roten av treet. Returnerer derfor null
        if (node.forelder == null) {
            return null;
        }
        //Hvis noden er høyre barn vil forelder til denne noden være den neste noden
        else if (node == node.forelder.høyre) {
            return node.forelder;
        }
        // Hvis den ikke er høyre barn så er den venstre
        else {
            // Sjekker da det finnes et høyre barn - hvis ikke returneres foreldrenoden
            if (node.forelder.høyre == null) {
                return node.forelder;
            }
            //Hvis det finnes både høyre og venstre barn så settes noden til å være høyre node
            else {
                node = node.forelder.høyre;

                // En loop som kjører så lenge det finnes barn på venstre eller høyre
                while (node.venstre != null || node.høyre != null) {

                    // Sjekker først om det finnes et venstre barn, hvis det gjør det så beveger den seg nedover til siste node i venstre "trebein"
                    if (node.venstre != null) {
                        node = node.venstre;
                    }
                    // Hvis det ikke er noen venstre node må den bevege seg helt til den treffer den siste høyre node
                    else {
                        node = node.høyre;
                    }
                }
                return node;
            }
        }
    }

    public void postorden(Oppgave<? super T> oppgave) {
        Node<T> p = førstePostorden(rot);
        while (p != null) {
            oppgave.utførOppgave(p.verdi);
            p = nestePostorden(p);
        }
    }

    public void postordenRecursive(Oppgave<? super T> oppgave) {
        postordenRecursive(rot, oppgave);
    }

    private void postordenRecursive(Node<T> p, Oppgave<? super T> oppgave) {
        if (p.venstre != null) {
            postordenRecursive(p.venstre, oppgave);
        }
        if (p.høyre != null) {
            postordenRecursive(p.høyre, oppgave);
        }
        oppgave.utførOppgave(p.verdi);
    }

    public ArrayList<T> serialize() {

        Node<T> p = rot;
        ArrayList<T> arrayList = new ArrayList<>();
        Queue<Node<T>> queue = new ArrayDeque<>();
        queue.add(p);

        while (!queue.isEmpty()) {
            p = queue.poll();
            arrayList.add(p.verdi);
            if (p.venstre != null) {
                queue.add(p.venstre);
            }
            if (p.høyre != null) {
                queue.add(p.høyre);
            }
        }
        return arrayList;
    }

    static <K> EksamenSBinTre<K> deserialize(ArrayList<K> data, Comparator<? super K> c) {
        EksamenSBinTre<K> nodeTre = new EksamenSBinTre<>(c);

        for (K nodeVerdi : data) {
            nodeTre.leggInn(nodeVerdi);
        }

        return nodeTre;
        }


    public static void main(String[] args) {

    }


} // ObligSBinTre
