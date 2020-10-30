package no.oslomet.cs.algdat.Eksamen;


import java.util.*;

public class EksamenSBinTre<T> {
    private static final class Node<T>                          // en indre nodeklasse
    {
        private T verdi;                                        // nodens verdi
        private Node<T> venstre, høyre;                         // venstre og høyre barn
        private Node<T> forelder;                               // forelder

        // konstruktør
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder) {
            this.verdi = verdi;
            venstre = v;
            høyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder)                 // konstruktør
        {
            this(verdi, null, null, forelder);
        }

        @Override
        public String toString() {
            return "" + verdi;
        }

    } // class Node

    private Node<T> rot;                                         // peker til rotnoden
    private int antall;                                          // antall noder
    private int endringer;                                       // antall endringer

    private final Comparator<? super T> comp;                    // komparator

    public EksamenSBinTre(Comparator<? super T> c)               // konstruktør
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

        Node<T> p = førstePostorden(rot);                   // går til den første i postorden
        while (p != null) {
            s.add(p.verdi.toString());
            p = nestePostorden(p);
        }

        return s.toString();
    }

    public boolean tom() {
        return antall == 0;
    }

    // Kode tatt fra kompendiet
    public final boolean leggInn(T verdi)                       // skal ligge i class SBinTre
    {
        Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

        Node<T> p = rot, q = null;                              // p starter i roten
        int cmp = 0;                                            // hjelpevariabel

        while (p != null)                                       // fortsetter til p er ute av treet
        {
            q = p;                                              // q er forelder til p
            cmp = comp.compare(verdi, p.verdi);                 // bruker komparatoren
            p = cmp < 0 ? p.venstre : p.høyre;                  // flytter p
        }

        // p er nå null, dvs. ute av treet, q er den siste vi passerte

        p = new Node<>(verdi, q);                               // oppretter en ny node

        if (q == null) rot = p;                                 // p blir rotnode
        else if (cmp < 0) q.venstre = p;                        // venstre barn til q
        else q.høyre = p;                                       // høyre barn til q

        antall++;                                               // én verdi mer i treet
        return true;                                            // vellykket innlegging
    }

    // Kode tatt fra kompendiet
    public boolean fjern(T verdi)                               // hører til klassen SBinTre
    {
        if (verdi == null) return false;                        // treet har ingen nullverdier

        Node<T> p = rot, q = null;                              // q skal være forelder til p

        while (p != null)                                       // leter etter verdi
        {
            int cmp = comp.compare(verdi, p.verdi);             // sammenligner
            if (cmp < 0) {
                q = p;
                p = p.venstre;
            }                                                   // går til venstre
            else if (cmp > 0) {
                q = p;
                p = p.høyre;
            }                                                   // går til høyre
            else break;                                         // den søkte verdien ligger i p
        }
        if (p == null) return false;                            // finner ikke verdi

        if (p.venstre == null || p.høyre == null)               // Tilfelle 1) og 2)
        {
            Node<T> b = p.venstre != null ? p.venstre : p.høyre;  // b for barn
            if (p == rot) {
                rot = b;
                if (rot != null) {
                    rot.forelder = null;
                }
            } else if (p == q.venstre) {
                q.venstre = b;
                if (b != null) {
                    b.forelder = q;
                }
            } else {
                q.høyre = b;
                if (b != null) {
                    b.forelder = q;
                }
            }
        } else                                                // Tilfelle 3)
        {
            Node<T> s = p, r = p.høyre;                         // finner neste i inorden
            while (r.venstre != null) {
                s = r;                                          // s er forelder til r
                r = r.venstre;
            }

            p.verdi = r.verdi;                                  // kopierer verdien i r til p

            if (s != p) s.venstre = r.høyre;
            else s.høyre = r.høyre;
        }

        antall--;                                               // det er nå én node mindre i treet
        return true;
    }

    public int fjernAlle(T verdi) {
        int antallNoderFjernet = 0;                             // teller
        Node<T> node = rot;                                     // starter i rot

        while (node != null) {                                  // loop som går så lenge det er node er tilgjenelig
            if (comp.compare(node.verdi, verdi) == 0) {         // sammenligner
                node = node.høyre;                              // går mot høyre
                fjern(verdi);                                   // fjerner verdi
                antallNoderFjernet++;                           // øker antall noder som er fjernet med en
            } else if (comp.compare(node.verdi, verdi) < 0) {   // sammenligner
                node = node.høyre;                              // går mot høyre
            } else {
                node = node.venstre;                            // hvis ikke den går mot høyre - går den mot venstre
            }
        }
        return antallNoderFjernet;                              // returnerer antall noder som er blitt fjernet
    }

    public int antall(T verdi) {

        int teller = 0;                                         // teller av typen int
        Node<T> node = rot;                                     // starter i roten

        while (node != null) {                                  // loop
            if (comp.compare(node.verdi, verdi) == 0) {         // sammenligner
                node = node.høyre;                              // går mot høyre
                teller++;                                       // øker teller med 1
            } else if (comp.compare(node.verdi, verdi) < 0) {   // sammenligner
                node = node.høyre;                              // går møt høyre
            } else {
                node = node.venstre;                            // går mot venstre
            }
        }
        return teller;                                          // returnerer antall noder i listen
    }

    public void nullstill() {

        Node<T> node = førstePostorden(rot);                    // starter i første postorden
        while (node != null) {                                  // loop

            if (node == rot) {                                  // når søkealgoritmen er på roten er den på det siste elementet
                rot = null;                                     // og derfor setter den til null
            } else if (node.venstre != null) {
                node.venstre = null;                            // går mot venstre
            } else if (node.høyre != null) {
                node.høyre = null;                              // går mot høyre
            }

            node = nestePostorden(node);                        // hopper til neste node i postorden rekkefølge
            antall--;                                           // reduserer antall med 1
        }
    }

    private static <T> Node<T> førstePostorden(Node<T> p) {

        while (p != null) {                                     // loop
            if (p.venstre != null) {
                p = p.venstre;                                  // går mot venstre hvis venstre node er tilgjengelig
            } else if (p.høyre != null) {                       // hvis det ikke er flere venstre noder sjekker den høyre
                p = p.høyre;                                    // går mot høyre
            } else {
                return p;                                       // returnerer noden hvis det ikke er flere venstre eller høyre noder
            }
        }
        return null;
    }

    private static <T> Node<T> nestePostorden(Node<T> p) {
        Node<T> node = p;                                       // starter i p

        if (node.forelder == null) {                            // hvis node sin forelder er lik null
            return null;                                        // returner null
        } else if (node == node.forelder.høyre) {               // sjekker om node er høyre barn
            return node.forelder;                               // returnerer node sin forelder
        } else {
            if (node.forelder.høyre == null) {                  // sjekker om venstre barn er enebarn
                return node.forelder;                           // returnerer forelder som neste
            } else {                                            // hvis begge barn er tilgjenelig
                node = node.forelder.høyre;                     // hopper over til høyre barn
                while (node.venstre != null || node.høyre != null) {    // loop
                    if (node.venstre != null) {                 // beveger seg mot venstre
                        node = node.venstre;
                    } else {
                        node = node.høyre;                      // hvis ikke - mot høyre
                    }
                }
                return node;                                    // returnerer node
            }
        }
    }

    public void postorden(Oppgave<? super T> oppgave) {
        Node<T> p = førstePostorden(rot);                       // starter i første postorden
        while (p != null) {                                     // loop
            oppgave.utførOppgave(p.verdi);                      // "skriver" ut verdien til p
            p = nestePostorden(p);                              // hopper til neste node
        }
    }

    public void postordenRecursive(Oppgave<? super T> oppgave) {
        postordenRecursive(rot, oppgave);
    }

    private void postordenRecursive(Node<T> p, Oppgave<? super T> oppgave) {
        if (p.venstre != null) {                                // hvis p.venstre ikke er null
            postordenRecursive(p.venstre, oppgave);             // kjører metoden på nytt og hopper til venstre
        }
        if (p.høyre != null) {                                  // hvis p sin høyre ikke er null
            postordenRecursive(p.høyre, oppgave);               // kjører metoden på nytt og hopper til høyre
        }
        oppgave.utførOppgave(p.verdi);                          // "skriver" ut verdien til p
    }

    public ArrayList<T> serialize() {

        Node<T> p = rot;                                        // starter i roten
        ArrayList<T> arrayList = new ArrayList<>();             // oppretter en arraylist
        Queue<Node<T>> queue = new ArrayDeque<>();              // oppretter et queue
        queue.add(p);                                           // adder første node i køen

        while (!queue.isEmpty()) {                              // loop som går lenge det er elementer i køen
            p = queue.poll();                                   // dytter ut første verdi og legger inn en ny
            arrayList.add(p.verdi);                             // adder node sin verdi i listen
            if (p.venstre != null) {
                queue.add(p.venstre);                           // går mot venstre og adder noden i køen
            }
            if (p.høyre != null) {
                queue.add(p.høyre);                             // går mot høyre og adder noden i køen
            }
        }
        return arrayList;
    }

    static <K> EksamenSBinTre<K> deserialize(ArrayList<K> data, Comparator<? super K> c) {
        EksamenSBinTre<K> nodeTre = new EksamenSBinTre<>(c);

        for (K nodeVerdi : data) {                              // loop som går itererer så mange elementer det er i listen
            nodeTre.leggInn(nodeVerdi);                         // legger noden i treet
        }
        return nodeTre;
    }
}
