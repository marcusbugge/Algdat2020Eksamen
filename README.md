# Algdat2020Eksamen
Eksamen i Algoritmer og Datastrukturer - 2020 Høst
Marcus Karlsen Bugge - s341898

Oppgave 1:
---------

Kode hentet fra kompendium:

- Funksjon som skal legge inn en node med en gitt verdi på riktig plass i treet. Det gjøres ved å sammenligne verdien til foreldrenoden og verdien som kommer inn. - - Hvis verdien er større eller lik vil den legges på første ledige plass på høyre bein. Hvis den er mindre så legges den på venstre trebein.
- En loop som går så lenge det er flere noder igjen i treet. Returnerer en bolskvariabel - true hvis noden er lagt inn, ellers false. Antall noder økes med en.

Oppgave 2:
---------

- Oppretter en tellevariabel av typen int
- Setter node til å være øverste node i det binære treet.
- while-løkke som går så lenge det er flere noder igjen
- Hvis den verdien til noden er lik verdien som ønskes returnerer compare 0. Går så videre mot høyere og øker teller med 1
- Compare retunerer -1 hvis node sin verdi er større enn verdi. Da vet man også at verdien man søker etter må ligge i høyre trebein

Oppgave 3:
---------

Første postorden
- En løkke som iterer så lenge noden ikke er null
- På hver iterasjon vil nodene prioritere å bevege seg så langt til venstre før den forsøker seg på å gå mot høyre barn.
- Når det er gjort står den på første postorden og returnerer dermed den noden

Neste postorden
- Hvis p ikke har en forelder er denne noden roten av treet. Returnerer derfor null
- Hvis noden er høyre barn vil forelder til denne noden være den neste noden
- Hvis den ikke er høyre barn så er den venstre
- Sjekker da det finnes et høyre barn - hvis ikke returneres foreldrenoden
- Hvis det finnes både høyre og venstre barn så settes noden til å være høyre node
- En loop som kjører så lenge det finnes barn på venstre eller høyre
- Sjekker først om det finnes et venstre barn, hvis det gjør det så beveger den seg nedover til siste node i venstre "trebein"
- Hvis det ikke er noen venstre node må den bevege seg helt til den treffer den siste høyre node

Oppgave 4:
---------

Iterativ
- Funksjon som skal skrive ut alle verdiene til nodene.
- Itererer seg igjennom treet ved hjelp av førstePostOrden() og nestePostOrden() så lenge det er flere noder igjen

Rekursiv
- Hvis venstre barn er tilgjengelig vil funksjonen kalle på seg selv igjen, men med barne til venstre, helt til det ikke er noe venstre barn
- Når det ikke er noe venstre barn vil den kjøre funksjonen med høyre barn
- Skriver ut noden sin verdi hvert funksjonskall

Oppgave 5:
---------

Serialisering
- Funksjon som legger nodene i en arraylist
- En løkke som itererer så lenge det er noder i køen
- Det som skjer i løkken er at rot-noden blir kastet ut av køen, og p blir nye noden.
- Adder så verdien til p i listen

Deserialisering
- Funksjon som skal gjøre om en arraylist til et binært tre
- En løkke som går så mange elementer det er i listen
- Kjører leggInn() hver iterasjon, og returnerer så treet

Oppgave 6:
---------

Nullstill
- Funksjon som skal nullstille hele treet
- Det vil si å fjerne barnene til alle nodene - hvis de har
- Bruker førstePostorden() og nestePostOrden() til å flytte meg igjennom treet

Fjern - første verdi
- Kode tatt fra kompendiet (som oppgitt i oppgaven). La inn tre if-sjekker i tilfelle 1) og 2) som gjorde at nodene fikk riktig pekere
- Funksjon som skal fjerne en den første noden med en gitt verdi
- Det gjøres ved å loope igjennom hele treet ved å bruke comparefunksjonen. Dette er en effektiv og rask måte
- Sammenligner ved hver iterasjon veriden til noden og den gitte søkeverdien som kommer inn som parameter
- Hvis veriden er funnet endres pekere slik at noden kommer "ut" av treet

Fjern - alle verdier
- Funksjon som skal fjerne alle noder med ønsket verdier.
- Bruker samme søkealgoritme som i antall(T verdi) - oppgaven.
- I stedetfor å øke antall-telleren  med en når verdien er funnet så kjøres fjern(verdi).
- Returnerer antallet av noder som er blitt fjernet

