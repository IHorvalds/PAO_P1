# Proiect 1 Programare Avansata pe Obiecte

I really really __really__ ~hate~ love Java...

## Lista de actiuni
1. Get
2. GetAll
3. Update
4. Delete
5. Insert
6. Print
7. Read

## Tipuri de obiecte
1. Queryable
2. Client
3. Medic
4. Programare
5. Store
6. Service

Pentru fiecare clasa din model (Medic, Client, Programare)

Toate modelele mostenesc Queryable. Queryable tine evidenta tuturor Store-urilor pentru clasele de Modele.
Atunci cand o clasas Model noua este adaugata (in cod), un store va fi creat pentru ea cand va fi instantiata prima data.
Persistenta nu e implementata. 
Sortarea se face dupa id-ul obiectelor de tip Queryable.

Clasa Service expune cateva functii simple: de adaugare si de afisare. In etapele urmatoare voi adauga si stergere si updatare.
Clasa Main evidentiaza functiile din Service.

![Working Penguin](https://media.giphy.com/media/KEkCtOMhkdze5azTMa/giphy.gif)
